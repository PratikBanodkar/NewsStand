package pratik.com.newsstand.NewsActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
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
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pratik.com.newsstand.Connectivity.ConnectivityReceiver;
import pratik.com.newsstand.Connectivity.MyApplication;
import pratik.com.newsstand.ExclusionStrategy_Bitmap_Drawable;
import pratik.com.newsstand.NewsFetching.ArticleImageFetching.ImageLoader;
import pratik.com.newsstand.NewsFetching.AsyncTaskCompleteListener;
import pratik.com.newsstand.NewsFetching.NewsItemObject;
import pratik.com.newsstand.PermissionUtils;
import pratik.com.newsstand.R;


public class TechNewsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private boolean updateFeedRequested = false;
    private RecyclerView recyclerView;
    private TechRecyclerAdapter recyclerAdapter;
    public List<NewsItemObject> techBookmarked = new ArrayList<NewsItemObject>();
    public ArrayList<NewsItemObject> bookmarkedArticles = new ArrayList<>();
    private boolean filter = false;
    private String news_source = new String();
    private String rationalMessage;
    private String[] permissions = new String[1];
    private static final int REQUEST_CODE = 1000;
    private String bookmarked_urlResponse;


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_news);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setDefaultNewsSource();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        final RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        rationalMessage = getString(R.string.permission_android_permission_WRITE_EXTERNAL_STORAGE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            checkPermission();
        }
        else
        {
            checkConnection();
            //fetchNews();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            fetchNews();
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.parent_relative_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void fetchNews(){
        fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>(),this);
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
            checkConnection();
            //fetchNews();
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
                    fetchNews();
                } else {
                    fetchNews();
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
        news_source = getResources().getString(R.string.ars_technica_url);  // When loaded, set news source to only 1 URL - Ars Technica //
        //  DULL THE OTHER NEWS SOURCES  //
        View view = findViewById(R.id.crypto_coin_news_logo);   view.setAlpha((float)0.2);
        view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
        view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
        view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
        view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
        view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
        view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
        view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
        view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
    }

    public void filterChanged(View v){
        filter = true;
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        recyclerAdapter = (TechRecyclerAdapter) recyclerView.getAdapter();
        recyclerAdapter.clear();
        switch (v.getId()){
            case R.id.ars_technica_logo:
                news_source = getResources().getString(R.string.ars_technica_url);
                View view = findViewById(R.id.crypto_coin_news_logo);   view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                v.setAlpha((float)1.0);
                break;
            case R.id.crypto_coin_news_logo:
                news_source = getResources().getString(R.string.crypto_coins_news_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.engadget_logo:
                news_source = getResources().getString(R.string.engadget_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.hacker_news_logo:
                news_source = getResources().getString(R.string.hacker_news_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.recode_logo:
                news_source = getResources().getString(R.string.recode_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.techcrunch_logo:
                news_source = getResources().getString(R.string.techcrunch_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.techradar_logo:
                news_source = getResources().getString(R.string.techradar_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.the_next_web_logo:
                news_source = getResources().getString(R.string.the_next_web_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.the_verge_logo:
                news_source = getResources().getString(R.string.the_verge_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.wired_logo);                   view.setAlpha((float)0.2);
                break;
            case R.id.wired_logo:
                news_source = getResources().getString(R.string.wired_url);
                v.setAlpha((float)1.0);
                view = findViewById(R.id.ars_technica_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.crypto_coin_news_logo);        view.setAlpha((float)0.2);
                view = findViewById(R.id.engadget_logo);                view.setAlpha((float)0.2);
                view = findViewById(R.id.hacker_news_logo);             view.setAlpha((float)0.2);
                view = findViewById(R.id.recode_logo);                  view.setAlpha((float)0.2);
                view = findViewById(R.id.techcrunch_logo);              view.setAlpha((float)0.2);
                view = findViewById(R.id.techradar_logo);               view.setAlpha((float)0.2);
                view = findViewById(R.id.the_next_web_logo);            view.setAlpha((float)0.2);
                view = findViewById(R.id.the_verge_logo);               view.setAlpha((float)0.2);
                break;
        }

        fetch_completeListener listener = new fetch_completeListener(new ArrayList<NewsItemObject>(),this);
        new fetch(listener).execute(news_source);
    }

    public void refreshFeed(View view) {
        updateFeedRequested = true;
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        recyclerAdapter = (TechRecyclerAdapter) recyclerView.getAdapter();
        recyclerAdapter.clear();
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

    public class TechRecyclerAdapter extends RecyclerView.Adapter<TechRecyclerAdapter.ViewHolder> {
        private ArrayList<NewsItemObject> newsItemObjects;
        ImageLoader imageLoader = new ImageLoader(TechNewsActivity.this);
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public TechRecyclerAdapter(ArrayList<NewsItemObject> newsItems){
            this.newsItemObjects = newsItems;
            //viewBinderHelper.setOpenOnlyOne(true);
        }

        @Override
        public TechRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_short, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final TechRecyclerAdapter.ViewHolder holder, final int position) {
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
                    Intent viewArticleIntent = new Intent(TechNewsActivity.this,ReadArticleActivity.class);
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
                        Toast.makeText(TechNewsActivity.this, "Article removed from offline reading", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        newsItemObjects.get(position).setBookmarked(true);
                        new SaveArticleTask(holder).execute(newsItemObjects.get(position));
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

    public class fetch extends AsyncTask<String,Void,ArrayList> {
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

                        news.setId("Tech_"+f);
                        news.setTitle(articleTitle);
                        news.setDescription(articleDesc);
                        news.setSource(articleSource);
                        news.setArticleSourceID(articleSourceID);
                        news.setArticleDate(articleDateString);
                        news.setOptionsColor(Color.rgb(255,152,0));
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
            System.out.println("POST-EXEC:is this refresh request? "+updateFeedRequested);

            if(updateFeedRequested){
                showUpdateFeedSnackBar();
                updateFeedRequested = false;
            }

            listener.onTaskComplete(newsItemObjectArrayList,TechNewsActivity.this);
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
        Snackbar snackbar = Snackbar.make(root_relative_layout, "FEED UPDATED", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.rgb(255,152,0));
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
            RecyclerView.Adapter adapter = new TechRecyclerAdapter(newsItems);
            recyclerView.setAdapter(adapter);
        }

    }

    public class SaveArticleTask extends AsyncTask<NewsItemObject,Void,String> {

        NewsItemObject object;
        TechRecyclerAdapter.ViewHolder holder;

        public SaveArticleTask(TechRecyclerAdapter.ViewHolder viewHolder){
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
            Toast.makeText(TechNewsActivity.this, "Article saved for offline reading", Toast.LENGTH_SHORT).show();
        }

    }



}
