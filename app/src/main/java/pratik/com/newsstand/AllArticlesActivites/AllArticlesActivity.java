package pratik.com.newsstand.AllArticlesActivites;

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
import android.os.Parcelable;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pratik.com.newsstand.Connectivity.ConnectivityReceiver;
import pratik.com.newsstand.Connectivity.MyApplication;
import pratik.com.newsstand.ExclusionStrategy_Bitmap_Drawable;
import pratik.com.newsstand.NewsActivities.BusinessNewsActivity;
import pratik.com.newsstand.NewsActivities.ReadArticleActivity;
import pratik.com.newsstand.NewsActivities.TechNewsActivity;
import pratik.com.newsstand.NewsFetching.ArticleImageFetching.ImageLoader;
import pratik.com.newsstand.NewsFetching.AsyncTaskCompleteListener;
import pratik.com.newsstand.NewsFetching.NewsItemObject;
import pratik.com.newsstand.PermissionUtils;
import pratik.com.newsstand.R;

public class AllArticlesActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private LinearLayoutManager layoutManager;
    private int totalArticles;
    private boolean updateFeedRequested = false;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private ArrayList<NewsItemObject> newsItemObjectArrayList;
    String source_id;
    String fDate,tDate;
    int noOfPages;
    ArrayList <NewsItemObject> allNewsArticles;
    int articlesFetched = 0;
    private Parcelable recyclerViewState;
    RelativeLayout filterLayout;
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
        setContentView(R.layout.activity_all_articles);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String source = extras.getString("Source");
        source_id = extras.getString("Source ID");

        Date cDate = new Date();
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekOldDate = calendar.getTime();
        tDate = new SimpleDateFormat("yyyy-MM-dd").format(oneWeekOldDate);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        ImageView source_image = findViewById(R.id.source_image);
        collapsingToolbar.setTitle(extras.getString("Source"));
        source_image.setImageDrawable(getDrawable(extras.getInt("Drawable ID")));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        filterLayout = findViewById(R.id.filter_layout1);

        recyclerView = findViewById(R.id.news_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);


        final RadioButton week_radiobutton = findViewById(R.id.radio_week);
        RadioButton month_radiobutton = findViewById(R.id.radio_month);
        RadioGroup radioGroup = findViewById(R.id.filter_layout_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRb = (RadioButton) findViewById(checkedId);
                if (selectedRb == week_radiobutton){
                    //Week Old News Articles
                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    Date oneWeekOldDate = calendar.getTime();
                    String tDate = new SimpleDateFormat("yyyy-MM-dd").format(oneWeekOldDate);
                    String endpoint = "https://newsapi.org/v2/everything?sources="+source_id+"&from="+fDate+"&to="+tDate+"&language=en&sortBy=publishedAt";
                    if(checkConnection()){
                        fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>());
                        new fetch(listener).execute(endpoint);
                    }
                    else{
                        showNoInternetSnackBar();
                    }

                }
                else{
                    //Month Old News Articles
                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MONTH, -1);
                    Date oneMonthOldDate = calendar.getTime();
                    String tDate = new SimpleDateFormat("yyyy-MM-dd").format(oneMonthOldDate);
                    String endpoint = "https://newsapi.org/v2/everything?sources="+source_id+"&from="+fDate+"&to="+tDate+"&language=en&sortBy=publishedAt";
                    if (checkConnection()) {
                        fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>());
                        new fetch(listener).execute(endpoint);
                    }
                    else{
                        showNoInternetSnackBar();
                    }
                }

            }
        });


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
        fNL.setVisibility(View.GONE);;
        TextView fLT = findViewById(R.id.filter_label_textview);
        fLT.setVisibility(View.GONE);
        RadioGroup fLRG = findViewById(R.id.filter_layout_radio_group);
        fLRG.setVisibility(View.GONE);

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
            color = Color.rgb(255,255,255);
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
        color = Color.rgb(255,255,255);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.parent_relative_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void fetchNews(){
        String endpoint = "https://newsapi.org/v2/everything?sources="+source_id+"&from="+fDate+"&to="+tDate;
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
        TextView fLT = findViewById(R.id.filter_label_textview);
        if(fLT.getVisibility() == View.GONE)
            fLT.setVisibility(View.VISIBLE);
        RadioGroup fLRG = findViewById(R.id.filter_layout_radio_group);
        if(fLRG.getVisibility() == View.GONE)
            fLRG.setVisibility(View.VISIBLE);
        new fetch(listener).execute(endpoint);
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

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        String endpoint = "https://newsapi.org/v2/everything?sources="+source_id+"&from="+fDate+"&to="+tDate+"&language=en&page="+offset;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        if(checkConnection()){
            append_completeListener listener = new append_completeListener(new ArrayList<NewsItemObject>(),this);
            new append(listener).execute(endpoint);
        }
        else{
            showNoInternetSnackBar();
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

    public void refreshFeed(View view) {
        if(!checkConnection())
            showNoInternetSnackBar();
        else{
            updateFeedRequested = true;
            recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
            AllArticlesRecyclerAdapter recyclerAdapter = (AllArticlesRecyclerAdapter) recyclerView.getAdapter();
            recyclerAdapter.clear();
            TextView fetchingNewsLabel = findViewById(R.id.fetching_news_label);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            if (fetchingNewsLabel.getVisibility()==View.GONE){
                fetchingNewsLabel.setVisibility(View.VISIBLE);
            }
            if(progressBar.getVisibility()==View.GONE)
                progressBar.setVisibility(View.VISIBLE);
            String endpoint = "https://newsapi.org/v2/everything?sources="+source_id+"&from="+fDate+"&to="+tDate+"&language=en&sortBy=publishedAt";
            fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>(),this);
            new fetch(listener).execute(endpoint);
        }
    }

    private void showUpdateFeedSnackBar() {
        RelativeLayout root_relative_layout = findViewById(R.id.root_relative_layout);
        Snackbar snackbar = Snackbar.make(root_relative_layout, "FEED UPDATED", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.rgb(255,255,255));
        snackbar.show();
    }

    public class fetch extends AsyncTask<String,Void,ArrayList> {

        private fetch_completeListener listener;
        private TextView fetchingNewsLabel;
        private ProgressBar progressBar;

        public fetch(fetch_completeListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchingNewsLabel = findViewById(R.id.fetching_news_label);
            fetchingNewsLabel.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.GONE);
            if(updateFeedRequested)
                fetchingNewsLabel.setText("Updating Feed...");
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList doInBackground(String... urls) {
            allNewsArticles = new ArrayList<>();
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
                    String responseString = convertStreamToString(responseBody);
                    JSONObject responseObject = new JSONObject(responseString);
                    myConnection.disconnect();
                    totalArticles = responseObject.getInt("totalResults");
                    if(totalArticles % 100 != 0)
                        noOfPages = totalArticles/100 + 1;
                    else
                        noOfPages = totalArticles/100;
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
                        if(!articleSourceID.equals(source_id))
                            return allNewsArticles;
                        else{

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

                            news.setId("Id_"+allNewsArticles.size()+1);
                            news.setTitle(articleTitle);
                            news.setDescription(articleDesc);
                            news.setSource(articleSource);
                            news.setArticleSourceID(articleSourceID);
                            news.setArticleDate(articleDateString);
                            news.setOptionsColor(Color.rgb(33,33,33));
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
                articlesFetched = articlesArray.length();
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
            progressBar.setVisibility(View.GONE);
            filterLayout.setVisibility(View.VISIBLE);
            newsItemObjectArrayList = result;

            if(updateFeedRequested){
                showUpdateFeedSnackBar();
                updateFeedRequested = false;
            }

            listener.onTaskComplete(newsItemObjectArrayList,AllArticlesActivity.this);
        }


    }

    public class append extends AsyncTask<String,Void,ArrayList> {
        private append_completeListener listener;
        private TextView fetchingNewsLabel;
        private ProgressBar progressBar;

        public append(append_completeListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchingNewsLabel = findViewById(R.id.fetching_news_label);
            fetchingNewsLabel.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.GONE);
            if(updateFeedRequested)
                fetchingNewsLabel.setText("Updating Feed...");
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList doInBackground(String... urls) {
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
                    String responseString = convertStreamToString(responseBody);
                    JSONObject responseObject = new JSONObject(responseString);
                    myConnection.disconnect();
                    totalArticles = responseObject.getInt("totalResults");
                    if(totalArticles % 100 != 0)
                        noOfPages = totalArticles/100 + 1;
                    else
                        noOfPages = totalArticles/100;
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

                        news.setId("Id_"+allNewsArticles.size()+1);
                        news.setTitle(articleTitle);
                        news.setDescription(articleDesc);
                        news.setSource(articleSource);
                        news.setArticleSourceID(articleSourceID);
                        news.setArticleDate(articleDateString);
                        news.setOptionsColor(Color.rgb(33,33,33));
                        Resources resources = getApplicationContext().getResources();
                        int resourceId = resources.getIdentifier(articleSourceID, "drawable",getApplicationContext().getPackageName());
                        news.setArticleSourceLogo(resources.getDrawable(resourceId));
                        news.setUrl(articleUrl);
                        news.setImgurl(articleImgUrl);
                        news.setAuthor(articleAuthor);
                        news.setBookmarked(false);

                        allNewsArticles.add(news);
                    }
                    articlesFetched = articlesArray.length();
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
            progressBar.setVisibility(View.GONE);
            filterLayout.setVisibility(View.VISIBLE);
            newsItemObjectArrayList = result;

            if(updateFeedRequested){
                showUpdateFeedSnackBar();
                updateFeedRequested = false;
            }

            listener.onTaskComplete(newsItemObjectArrayList,AllArticlesActivity.this);
        }


    }

    public class fetch_completeListener implements AsyncTaskCompleteListener<ArrayList,Activity> {

        ArrayList <NewsItemObject> newsItemObjectArrayList;

        public fetch_completeListener(ArrayList a , Activity ca){
            this.newsItemObjectArrayList = a;
        }

        public fetch_completeListener(ArrayList a){
            this.newsItemObjectArrayList = a;
        }

        @Override
        public void onTaskComplete(ArrayList newsItems, Activity ca) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
            recyclerView.setHasFixedSize(true);
            //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ca.getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new AllArticlesRecyclerAdapter(newsItems);
            recyclerView.setAdapter(adapter);
        }

    }

    public class append_completeListener implements AsyncTaskCompleteListener<ArrayList,Activity> {

        ArrayList <NewsItemObject> newsItemObjectArrayList;

        public append_completeListener(ArrayList a , Activity ca){
            this.newsItemObjectArrayList = a;
        }

        @Override
        public void onTaskComplete(ArrayList newsItems, Activity ca) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
            recyclerView.setHasFixedSize(true);
            //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ca.getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new AllArticlesRecyclerAdapter(newsItems);
            //adapter.notifyItemRangeInserted(newsItems.size(),articlesFetched);
            recyclerView.setAdapter(adapter);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        }

    }

    public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 1;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 1;

        RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                }
                else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        // Call this method whenever performing new searches
        public void resetState() {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            this.loading = true;
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }

    public class AllArticlesRecyclerAdapter extends RecyclerView.Adapter<AllArticlesRecyclerAdapter.ViewHolder> {
        private ArrayList<NewsItemObject> newsItemObjects;
        ImageLoader imageLoader = new ImageLoader(AllArticlesActivity.this);
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public AllArticlesRecyclerAdapter(ArrayList<NewsItemObject> newsItems){
            this.newsItemObjects = newsItems;
        }

        @Override
        public AllArticlesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_short, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final AllArticlesRecyclerAdapter.ViewHolder holder, final int position) {
            viewBinderHelper.bind(holder.swipeRevealLayout, newsItemObjects.get(position).getId());
            holder.textView_Headline.setText(newsItemObjects.get(position).getTitle());
            holder.textView_Description.setText(newsItemObjects.get(position).getDescription());
            holder.textView_Source.setText(newsItemObjects.get(position).getSource());
            imageLoader.DisplayImage(newsItemObjects.get(position).getImgurl(),holder.imageView_articleImage);
            holder.imageView_articleSourceLogo.setImageDrawable(newsItemObjects.get(position).getArticleSourceLogo());
            //holder.imageView_articleImage.setImageBitmap(newsItemObjects.get(position).getArticleImage());
            String dateString = newsItemObjects.get(position).getArticleDate();
            holder.textView_timeStamp.setText(dateString);
            holder.article_options_layout.setBackgroundColor(newsItemObjects.get(position).getOptionsColor());

            holder.article_contents_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open article for reading
                    String selectedArticleURL  = newsItemObjects.get(position).getUrl();
                    Intent viewArticleIntent = new Intent(AllArticlesActivity.this,ReadArticleActivity.class);
                    //viewArticleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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
                    //mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent i = Intent.createChooser(mIntent, "Share this article");
                    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    holder.swipeRevealLayout.close(true);
                }
            });

            holder.bookmarkImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(newsItemObjects.get(position).isBookmarked()){
                        //holder.bookmarkImageButton.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
                        newsItemObjects.get(position).setBookmarked(false);
                        removeFromBookmarkedArticles(newsItemObjects.get(position));
                        Toast.makeText(AllArticlesActivity.this, "Article removed from offline reading", Toast.LENGTH_SHORT).show();
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
                //Log.d("RECYCLER-CLICK-EVENTS","Item Clicked at position "+getLayoutPosition());
            }

            @Override
            public boolean onLongClick(View view) {
                //Log.d("RECYCLER-CLICK-EVENTS","Item Long-Clicked at position "+getLayoutPosition());
                return true;
            }
        }

    }

    public class SaveArticleTask extends AsyncTask<NewsItemObject,Void,String> {

        NewsItemObject object;
        AllArticlesRecyclerAdapter.ViewHolder holder;

        public SaveArticleTask(AllArticlesRecyclerAdapter.ViewHolder viewHolder){
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
            Toast.makeText(AllArticlesActivity.this, "Article saved for offline reading", Toast.LENGTH_SHORT).show();
        }

    }
}
