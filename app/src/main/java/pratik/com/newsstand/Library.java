package pratik.com.newsstand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pratik.com.newsstand.Connectivity.ConnectivityReceiver;
import pratik.com.newsstand.Connectivity.MyApplication;
import pratik.com.newsstand.NewsActivities.BusinessNewsActivity;
import pratik.com.newsstand.NewsActivities.CustomFeed;
import pratik.com.newsstand.NewsActivities.EntertainmentNewsActivity;
import pratik.com.newsstand.NewsActivities.GeneralNewsActivity;
import pratik.com.newsstand.NewsActivities.HealthNewsActivity;
import pratik.com.newsstand.NewsActivities.OfflineArticlesActivity;
import pratik.com.newsstand.NewsActivities.ScienceNewsActivity;
import pratik.com.newsstand.NewsActivities.SportsNewsActivity;
import pratik.com.newsstand.NewsActivities.TechNewsActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Library extends AppCompatActivity {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_library);
        setContentView(R.layout.activity_library);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        Boolean set = pref.getBoolean("PREFERENCES SET",false);
        //MobileAds.initialize(this, getResources().getString(R.string.appid_admob));
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        tabLayout = findViewById(R.id.library_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Top Headlines"));
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        Pager pagerAdapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        LibraryOnTabSelectedListener listener = new LibraryOnTabSelectedListener(viewPager);
        tabLayout.addOnTabSelectedListener(listener);
        TextView tv_head = (TextView)LayoutInflater.from(this).inflate(R.layout.library_tab_custom_header,null);
        tv_head.setText("Top Headlines");
        tabLayout.getTabAt(0).setCustomView(tv_head);
        TextView tv_articles = (TextView)LayoutInflater.from(this).inflate(R.layout.library_tab_custom_header,null);
        tv_articles.setText("Articles");
        tabLayout.getTabAt(1).setCustomView(tv_articles);

    }


    public void showTechNews(View view) {
        ImageView iv_tech = findViewById(R.id.iv_tech);
        Intent techNewsIntent = new Intent(Library.this, TechNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,iv_tech,"libraryToTech");
        startActivity(techNewsIntent,options.toBundle());
    }

    public void showSportsNews(View view){
        ImageView iv_sports = findViewById(R.id.iv_sports);
        Intent sportsNewsIntent = new Intent(Library.this, SportsNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_sports, "libraryToSport");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showBusinessNews(View view){
        ImageView iv_business = findViewById(R.id.iv_business);
        Intent sportsNewsIntent = new Intent(Library.this, BusinessNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_business, "libraryToBusiness");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showHealthNews(View view){
        ImageView iv_health = findViewById(R.id.iv_health);
        Intent sportsNewsIntent = new Intent(Library.this, HealthNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_health, "libraryToHealth");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showGeneralNews(View view){
        ImageView iv_general= findViewById(R.id.iv_general);
        Intent sportsNewsIntent = new Intent(Library.this, GeneralNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_general, "libraryToGeneral");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showEntertainmentNews(View view){
        ImageView iv_entertainment= findViewById(R.id.iv_entertainment);
        Intent sportsNewsIntent = new Intent(Library.this, EntertainmentNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_entertainment, "libraryToEntertainment");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showScienceNews(View view){
        ImageView iv_science = findViewById(R.id.iv_science);
        Intent sportsNewsIntent = new Intent(Library.this, ScienceNewsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_science, "libraryToScience");
        startActivity(sportsNewsIntent,options.toBundle());
    }

    public void showOfflineArticles(View view) {
        ImageView iv_offline = findViewById(R.id.iv_bookmarked);
        Intent offlineNewsIntent = new Intent(Library.this, OfflineArticlesActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_offline, "libraryToOffline");
        startActivity(offlineNewsIntent,options.toBundle());
    }

    public void showCustomFeed(View view) {
        ImageView iv_custom = findViewById(R.id.iv_custom);
        Intent customNewsIntent = new Intent(Library.this, CustomFeed.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)iv_custom, "libraryToCustom");
        startActivity(customNewsIntent,options.toBundle());
    }
}
