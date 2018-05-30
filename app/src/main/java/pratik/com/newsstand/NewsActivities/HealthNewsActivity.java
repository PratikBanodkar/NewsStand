package pratik.com.newsstand.NewsActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pratik.com.newsstand.Connectivity.ConnectivityReceiver;
import pratik.com.newsstand.Connectivity.MyApplication;
import pratik.com.newsstand.ExclusionStrategy_Bitmap_Drawable;
import pratik.com.newsstand.NewsFetching.ArticleImageFetching.ImageLoader;
import pratik.com.newsstand.NewsFetching.AsyncTaskCompleteListener;
import pratik.com.newsstand.NewsFetching.NewsItemObject;
import pratik.com.newsstand.PermissionUtils;
import pratik.com.newsstand.R;

public class HealthNewsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private boolean updateFeedRequested = false;
    private static String healthURL_mnt;
    private ArrayList allSourceURLs = new ArrayList();
    public ArrayList<NewsItemObject> bookmarkedArticles = new ArrayList<>();
    private boolean filter=false;
    private String news_source = new String();
    private String rationalMessage;
    private String[] permissions = new String[1];
    private static final int REQUEST_CODE = 1000;

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MyApplication.getInstance().removeConnectivityListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setDefaultNewsSource();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        final RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        healthURL_mnt = getResources().getString(R.string.mnt_url);
        allSourceURLs.add(healthURL_mnt);

        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        rationalMessage = getString(R.string.permission_android_permission_WRITE_EXTERNAL_STORAGE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            checkPermission();
        }
        else
        {
            if (checkConnection())
                fetchNews();
            else {
                doOnNoInternet();
            }
        }

    }

    public void doOnNoInternet(){
        RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setVisibility(View.GONE);
        ProgressBar pBar = findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
        TextView fNL = findViewById(R.id.fetching_news_label);
        fNL.setVisibility(View.GONE);
        HorizontalScrollView fSV = findViewById(R.id.filter_scrollview);
        fSV.setVisibility(View.GONE);
        TextView fBV = findViewById(R.id.filterByValue_textview);
        fBV.setVisibility(View.GONE);

        LinearLayout noInternetLayout = findViewById(R.id.noInternetLayout);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            fetchNews();
        } else {
            message = "No internet connection";
            color = Color.rgb(244,67,54);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.parent_relative_layout), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    private void showNoInternetSnackBar(){
        String message;
        int color;
        message = "No internet connection";
        color = Color.rgb(244,67,54);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.parent_relative_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    public void fetchNews(){
        fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>(),this);
        RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        if(recyclerView.getVisibility() == View.GONE)
            recyclerView.setVisibility(View.VISIBLE);
        ProgressBar pBar = findViewById(R.id.progressBar);
        if(pBar.getVisibility() == View.GONE)
            pBar.setVisibility(View.VISIBLE);
        TextView fNL = findViewById(R.id.fetching_news_label);
        if(fNL.getVisibility() == View.GONE)
            fNL.setVisibility(View.VISIBLE);
        HorizontalScrollView fSV = findViewById(R.id.filter_scrollview);
        if(fSV.getVisibility() == View.GONE)
            fSV.setVisibility(View.VISIBLE);
        TextView fBV = findViewById(R.id.filterByValue_textview);
        if(fBV.getVisibility() == View.GONE)
            fBV.setVisibility(View.VISIBLE);
        new fetch(listener).execute(news_source);
    }

    private void checkPermission() {
        int deniedIndex = checkSelfPermissions(permissions);
        if (deniedIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[deniedIndex])) {
                if (TextUtils.isEmpty(rationalMessage)) {
                    requestPermission();
                } else {
                    showRationalDialog();
                }

            } else {
                requestPermission();
            }
        } else {
            if (checkConnection())
                fetchNews();
            else {
                doOnNoInternet();
            }
        }
    }

    private int checkSelfPermissions(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                int index = PermissionUtils.verifyPermissions(grantResults);
                if (index == -1) {
                    if (checkConnection())
                        fetchNews();
                    else {
                        doOnNoInternet();
                    }
                } else {
                    if (checkConnection())
                        fetchNews();
                    else {
                        doOnNoInternet();
                    }
                }
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void showRationalDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.RationalDialogStyle))
                .setTitle("Storage")
                .setMessage(rationalMessage)
                .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show()
                .setCancelable(false);
    }

    private void setDefaultNewsSource() {
        news_source = getResources().getString(R.string.mnt_url);  // When loaded, set news source to only 1 URL - Medical News Today//
        //  DULL THE OTHER NEWS SOURCES  //
    }

    public void refreshFeed(View view) {
        if(!checkConnection())
            showNoInternetSnackBar();
        else{
            updateFeedRequested = true;
            RelativeLayout root_relative_layout = findViewById(R.id.root_relative_layout);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
            TextView fetchingNewsLabel = findViewById(R.id.fetching_news_label);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            if (fetchingNewsLabel.getVisibility()==View.GONE){
                fetchingNewsLabel.setVisibility(View.VISIBLE);
            }
            if(progressBar.getVisibility()==View.GONE)
                progressBar.setVisibility(View.VISIBLE);

            fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>(),this);
            new fetch(listener).execute(news_source);
        }
    }

    public void  addToBookmarkedArticles(NewsItemObject newsObj){
        SharedPreferences savedArticlesPref = getApplicationContext().getSharedPreferences("All Saved Articles", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = savedArticlesPref.edit();
        //Fetch already existing saved articles
        String json_string_saved_articles = savedArticlesPref.getString("Bookmarked",null);
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy_Bitmap_Drawable())
                .serializeNulls() //<-- uncomment to serialize NULL fields as well
                .create();
        Type type = new TypeToken<List<NewsItemObject>>(){}.getType();
        List<NewsItemObject> savedArticles_Retrieved;
        if(json_string_saved_articles == null)
            savedArticles_Retrieved = new ArrayList<NewsItemObject>();
        else
            savedArticles_Retrieved = gson.fromJson(json_string_saved_articles, type);


        //Add the newly saved article to the already saved articles
        savedArticles_Retrieved.add(newsObj);
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy_Bitmap_Drawable())
                .serializeNulls() //<-- uncomment to serialize NULL fields as well
                .create();
        json_string_saved_articles = gson.toJson(savedArticles_Retrieved);
        prefsEditor.putString("Bookmarked",json_string_saved_articles);
        prefsEditor.commit();

    }

    public void  removeFromBookmarkedArticles(NewsItemObject newsObj){
        SharedPreferences savedArticlesPref = getApplicationContext().getSharedPreferences("All Saved Articles", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = savedArticlesPref.edit();
        String json_string_saved_articles = savedArticlesPref.getString("Bookmarked",null);
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy_Bitmap_Drawable())
                .serializeNulls() //<-- uncomment to serialize NULL fields as well
                .create();
        Type type = new TypeToken<List<NewsItemObject>>(){}.getType();
        List<NewsItemObject> savedArticles_Retrieved = gson.fromJson(json_string_saved_articles, type);
        savedArticles_Retrieved.remove(newsObj);
        json_string_saved_articles = gson.toJson(savedArticles_Retrieved);
        prefsEditor.putString("Bookmarked",json_string_saved_articles);
        prefsEditor.commit();

    }

    public class fetch extends AsyncTask<String,Integer,ArrayList> {

        private ArrayList<NewsItemObject> newsItemObjectArrayList;
        private fetch_completeListener listener;
        private ProgressBar progressBar;
        private TextView fetchingNewsLabel;
        private HorizontalScrollView hsv;
        private TextView filterBySourceLabel;

        public fetch(fetch_completeListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchingNewsLabel = findViewById(R.id.fetching_news_label);
            fetchingNewsLabel.setVisibility(View.VISIBLE);
            if(filter && !updateFeedRequested)
                fetchingNewsLabel.setText("Filtering News...");
            else if(updateFeedRequested)
                fetchingNewsLabel.setText("Updating Feed...");
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            hsv = findViewById(R.id.filter_scrollview);
            filterBySourceLabel = findViewById(R.id.filterByValue_textview);
            filterBySourceLabel.setVisibility(View.GONE);
            hsv.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList doInBackground(String... urls) {
            ArrayList <NewsItemObject> allNewsArticles = new ArrayList<>();
            String endpoint = urls[0];
            try
            {
                URL url = new URL(endpoint);
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                myConnection.setRequestProperty("x-api-key", getApplicationContext().getResources().getString(R.string.newsapikey));
                myConnection.setUseCaches(true);
                myConnection.setDefaultUseCaches(true);
                if (myConnection.getResponseCode() == 200)
                {
                    InputStream responseBody = myConnection.getInputStream();
                    myConnection.disconnect();
                    String responseString = convertStreamToString(responseBody);
                    JSONObject responseObject = new JSONObject(responseString);
                    JSONArray articlesArray  = responseObject.getJSONArray("articles");
                    for(int f=0; f<articlesArray.length(); f++)
                    {
                        NewsItemObject news = new NewsItemObject();
                        JSONObject articleObject = articlesArray.getJSONObject(f);
                        String articleAuthor = articleObject.getString("author");
                        String articleTitle = articleObject.getString("title");
                        String articleDesc = articleObject.getString("description");
                        String articleUrl = articleObject.getString("url");
                        String articleImgUrl = articleObject.getString("urlToImage");

                        String articleDateTimeStamp = articleObject.getString("publishedAt");
                        Date articleDate = new Date();
                        String articleDateString = null;
                        if(!articleDateTimeStamp.equals("null")){
                            String [] parts = articleDateTimeStamp.split("T");
                            parts[1] = parts[1].replace("Z","");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            articleDate = sdf.parse(parts[0]);
                            articleDateString = String.format(Locale.getDefault(),"%1$tb %1$td %1$tY ", articleDate);
                        }
                        JSONObject sourceObject = articleObject.getJSONObject("source");
                        String articleSource = sourceObject.getString("name");
                        String articleSourceID = sourceObject.getString("id");

                        if(articleTitle.equals(null))
                            articleTitle = " ";
                        if(articleDesc.equals(null) || articleDesc.equals("null"))
                            articleDesc = " ";
                        if(articleSource.equals(null))
                            articleSource = " ";
                        if(articleSourceID.equals(null))
                            articleSourceID = " ";
                        else{
                            if(articleSourceID.contains("-")){
                                articleSourceID=articleSourceID.replace("-","_");
                            }
                        }
                        if(articleDateTimeStamp.equals(null))
                            articleDateString = " ";

                        news.setId("Health_"+f);
                        news.setTitle(articleTitle);
                        news.setDescription(articleDesc);
                        news.setSource(articleSource);
                        news.setArticleSourceID(articleSourceID);
                        news.setArticleDate(articleDateString);
                        news.setOptionsColor(Color.rgb(244,67,54));
                        Resources resources = getApplicationContext().getResources();
                        int resourceId = resources.getIdentifier(articleSourceID, "drawable",getApplicationContext().getPackageName());
                        news.setArticleSourceLogo(resources.getDrawable(resourceId));
                        news.setUrl(articleUrl);
                        news.setImgurl(articleImgUrl);
                        news.setAuthor(articleAuthor);
                        news.setBookmarked(false);

                        allNewsArticles.add(news);
                    }

                }

            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return allNewsArticles;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);
            fetchingNewsLabel.setVisibility(View.GONE);
            progressBar.setProgress(10);
            progressBar.setVisibility(View.GONE);
            filterBySourceLabel.setVisibility(View.VISIBLE);
            hsv.setVisibility(View.VISIBLE);
            newsItemObjectArrayList = result;

            if(updateFeedRequested){
                showUpdateFeedSnackBar();
                updateFeedRequested = false;
            }

            listener.onTaskComplete(newsItemObjectArrayList,HealthNewsActivity.this);
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private void showUpdateFeedSnackBar() {
        RelativeLayout root_relative_layout = findViewById(R.id.root_relative_layout);
        Snackbar snackbar = Snackbar.make(root_relative_layout, "Feed Updated", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.rgb(244,67,54));
        snackbar.show();
    }

    public class fetch_completeListener implements AsyncTaskCompleteListener<ArrayList,Activity> {

        ArrayList <NewsItemObject> newsItemObjectArrayList;

        public fetch_completeListener(ArrayList a , Activity ca){
            this.newsItemObjectArrayList = a;
        }

        @Override
        public void onTaskComplete(ArrayList newsItems, Activity ca) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ca.getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new HealthRecyclerAdapter(newsItems);
            recyclerView.setAdapter(adapter);
        }

    }

    public class HealthRecyclerAdapter extends RecyclerView.Adapter<HealthRecyclerAdapter.ViewHolder> {
        private ArrayList<NewsItemObject> newsItemObjects;
        ImageLoader imageLoader = new ImageLoader(HealthNewsActivity.this);
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public HealthRecyclerAdapter(ArrayList<NewsItemObject> newsItems){
            this.newsItemObjects = newsItems;
        }

        @Override
        public HealthRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_short, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final HealthRecyclerAdapter.ViewHolder holder, final int position) {
            viewBinderHelper.bind(holder.swipeRevealLayout, newsItemObjects.get(position).getId());
            holder.textView_Headline.setText(newsItemObjects.get(position).getTitle());
            holder.textView_Description.setText(newsItemObjects.get(position).getDescription());
            holder.textView_Source.setText(newsItemObjects.get(position).getSource());
            imageLoader.DisplayImage(newsItemObjects.get(position).getImgurl(),holder.imageView_articleImage);
            holder.imageView_articleSourceLogo.setImageDrawable(newsItemObjects.get(position).getArticleSourceLogo());
            String dateString = newsItemObjects.get(position).getArticleDate();
            holder.textView_timeStamp.setText(dateString);
            holder.article_options_layout.setBackgroundColor(newsItemObjects.get(position).getOptionsColor());

            holder.article_contents_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open article for reading
                    String selectedArticleURL  = newsItemObjects.get(position).getUrl();
                    Intent viewArticleIntent = new Intent(HealthNewsActivity.this,ReadArticleActivity.class);
                    viewArticleIntent.putExtra("URL",selectedArticleURL);
                    try{
                        startActivity(viewArticleIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            holder.shareImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(android.content.Intent.ACTION_SEND);
                    mIntent.setType("text/plain");
                    String shareString = newsItemObjects.get(position).getUrl() + "\n\n" +
                            newsItemObjects.get(position).getTitle() + "\n\n" +
                            "Shared from News Stand App";
                    mIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                    Intent i = Intent.createChooser(mIntent, "Share this article");
                    startActivity(i);
                    holder.swipeRevealLayout.close(true);
                }
            });

            holder.bookmarkImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(newsItemObjects.get(position).isBookmarked()){
                        newsItemObjects.get(position).setBookmarked(false);
                        removeFromBookmarkedArticles(newsItemObjects.get(position));
                        Toast.makeText(HealthNewsActivity.this, "Article removed from offline reading", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!checkConnection())
                            showNoInternetSnackBar();
                        else{
                            newsItemObjects.get(position).setBookmarked(true);
                            new SaveArticleTask(holder).execute(newsItemObjects.get(position));
                        }
                    }

                    holder.swipeRevealLayout.close(true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return newsItemObjects.size();
        }

        public ArrayList getNewsItemObjects(){
            return this.newsItemObjects;
        }

        public void setNewsItemObjects(ArrayList newsItemObjects){
            this.newsItemObjects = newsItemObjects;
        }

        public void clear() {
            int size = this.newsItemObjects.size();
            this.newsItemObjects.clear();
            notifyItemRangeRemoved(0, size);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
            protected TextView textView_Headline;
            protected TextView textView_Description;
            protected TextView textView_Source;
            protected ImageView imageView_articleImage;
            protected TextView textView_timeStamp;
            protected ImageView imageView_articleSourceLogo;
            protected LinearLayout article_options_layout;
            protected LinearLayout article_contents_layout;
            protected ImageButton shareImageButton;
            protected ImageButton bookmarkImageButton;
            protected SwipeRevealLayout swipeRevealLayout;
            protected ProgressBar progressBar;
            public ViewHolder(View itemView) {
                super(itemView);
                textView_Headline =  (TextView) itemView.findViewById(R.id.text_view_news_head);
                textView_Description =  (TextView) itemView.findViewById(R.id.text_view_news_desc);
                textView_Source =  (TextView) itemView.findViewById(R.id.text_view_news_source);
                imageView_articleImage = (ImageView) itemView.findViewById(R.id.image_view_news_img);
                imageView_articleSourceLogo = (ImageView) itemView.findViewById(R.id.imageview_source_logo);
                textView_timeStamp = (TextView) itemView.findViewById(R.id.text_view_news_datetime);
                article_options_layout = (LinearLayout) itemView.findViewById(R.id.article_options);
                article_contents_layout = (LinearLayout) itemView.findViewById(R.id.article_content);
                shareImageButton = (ImageButton) itemView.findViewById(R.id.share_imagebutton);
                bookmarkImageButton = (ImageButton) itemView.findViewById(R.id.bookmark_article_imagebutton);
                swipeRevealLayout = (SwipeRevealLayout)itemView.findViewById(R.id.swipeRevealLayout);
                progressBar = (ProgressBar)itemView.findViewById(R.id.progressBarNewsItem);
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        }

    }

    public class SaveArticleTask extends AsyncTask<NewsItemObject,Void,String> {

        NewsItemObject object;
        HealthRecyclerAdapter.ViewHolder holder;

        public SaveArticleTask(HealthRecyclerAdapter.ViewHolder viewHolder){
            this.holder = viewHolder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.holder.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(NewsItemObject... objects) {
            String responseString = null;
            NewsItemObject object = objects[0];
            this.object = object;
            try {
                URL url = new URL(object.getUrl());
                responseString = null;
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = convertStreamToString(in);
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            object.setArticleContent(responseString);
            addToBookmarkedArticles(object);
            this.holder.progressBar.setVisibility(View.GONE);
            Toast.makeText(HealthNewsActivity.this, "Article saved for offline reading", Toast.LENGTH_SHORT).show();
        }

    }

}
