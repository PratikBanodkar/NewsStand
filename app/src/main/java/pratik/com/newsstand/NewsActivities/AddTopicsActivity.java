package pratik.com.newsstand.NewsActivities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;

import pratik.com.newsstand.R;

public class AddTopicsActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    ArrayList techSources = new ArrayList();
    ArrayList sportsSources = new ArrayList();
    ArrayList businessSources = new ArrayList();
    ArrayList healthSources = new ArrayList();
    ArrayList generalSources = new ArrayList();
    ArrayList entertainmentSources = new ArrayList();
    ArrayList scienceSources = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topics);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Animation in = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.left_out);

        flipper  = findViewById(R.id.viewFlipper);
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);

    }

    public void changeViewToShow(View v){
        switch (v.getId()){
            case R.id.save_tech_sources_button:
                flipper.setDisplayedChild(0);
                getTechSources();
                break;
            case R.id.save_sports_sources_button:
                flipper.setDisplayedChild(0);
                getSportsSources();
                break;
            case R.id.save_business_sources_button:
                flipper.setDisplayedChild(0);
                getBusinessSources();
                break;
            case R.id.save_health_sources_button:
                flipper.setDisplayedChild(0);
                getHealthSources();
                break;
            case R.id.save_general_sources_button:
                flipper.setDisplayedChild(0);
                break;
            case R.id.save_entertainment_sources_button:
                flipper.setDisplayedChild(0);
                getEntertainmentSources();
                break;
            case R.id.save_science_sources_button:
                flipper.setDisplayedChild(0);
                getScienceSources();
                break;
            case R.id.add_tech_imagebutton:
                flipper.setDisplayedChild(1);
                break;
            case R.id.add_sports_imagebutton:
                flipper.setDisplayedChild(2);
                break;
            case R.id.add_business_imagebutton:
                flipper.setDisplayedChild(3);
                break;
            case R.id.add_health_imagebutton:
                flipper.setDisplayedChild(4);
                break;
            case R.id.add_general_imagebutton:
                flipper.setDisplayedChild(5);
                break;
            case R.id.add_entertainment_imagebutton:
                flipper.setDisplayedChild(6);
                break;
            case R.id.add_science_imagebutton:
                flipper.setDisplayedChild(7);
                break;
        }


    }

    public void getTechSources(){
        CheckBox ars_technica = (CheckBox)findViewById(R.id.ars_technica_checkbox); if(ars_technica.isChecked()) {techSources.add("Ars Technica");}
        CheckBox crypto_coin = (CheckBox)findViewById(R.id.crypto_coins_checkbox);  if(crypto_coin.isChecked()) {techSources.add("Crypto Coin News");}
        CheckBox engadget = (CheckBox)findViewById(R.id.engadget_checkbox);         if(engadget.isChecked()) {techSources.add("Engadget");}
        CheckBox hacker_news = (CheckBox)findViewById(R.id.hacker_news_checkbox);   if(hacker_news.isChecked()) {techSources.add("Hacker News");}
        CheckBox recode = (CheckBox)findViewById(R.id.recode_checkbox);             if(recode.isChecked()) {techSources.add("Recode");}
        CheckBox techcrunch = (CheckBox)findViewById(R.id.techcrunch_checkbox);     if(techcrunch.isChecked()) {techSources.add("Techcrunch");}
        CheckBox techradar = (CheckBox)findViewById(R.id.techradar_checkbox);       if(techradar.isChecked()) {techSources.add("Techradar");}
        CheckBox the_next_web = (CheckBox)findViewById(R.id.the_next_web_checkbox); if(the_next_web.isChecked()) {techSources.add("The Next Web");}
        CheckBox the_verge = (CheckBox)findViewById(R.id.the_verge_checkbox);       if(the_verge.isChecked()) {techSources.add("The Verge");}
        CheckBox wired = (CheckBox)findViewById(R.id.wired_checkbox);               if(wired.isChecked()) {techSources.add("Wired");}
    }

    public void getSportsSources(){
        CheckBox bbc_sport = (CheckBox)findViewById(R.id.bbc_sport_checkbox);               if(bbc_sport.isChecked()) {sportsSources.add("BBC Sport");}
        CheckBox bleacher_report = (CheckBox)findViewById(R.id.bleacher_report_checkbox);   if(bleacher_report.isChecked()) {sportsSources.add("Bleacher Report");}
        CheckBox espn = (CheckBox)findViewById(R.id.espn_checkbox);                         if(espn.isChecked()) {sportsSources.add("ESPN");}
        CheckBox espn_cricinfo= (CheckBox)findViewById(R.id.espn_cricinfo_checkbox);        if(espn_cricinfo.isChecked()) {sportsSources.add("ESPN Cricinfo");}
        CheckBox football_italia = (CheckBox)findViewById(R.id.football_italia_checkbox);   if(football_italia.isChecked()) {sportsSources.add("Football Italia");}
        CheckBox four_four_two = (CheckBox)findViewById(R.id.four_four_two_checkbox);       if(four_four_two.isChecked()) {sportsSources.add("Four Four Two");}
        CheckBox fox_sports = (CheckBox)findViewById(R.id.fox_sports_checkbox);             if(fox_sports.isChecked()) {sportsSources.add("Fox Sports");}
        CheckBox nfl = (CheckBox)findViewById(R.id.nfl_checkbox);                           if(nfl.isChecked()) {sportsSources.add("NFL");}
        CheckBox nhl= (CheckBox)findViewById(R.id.nhl_checkbox);                            if(nhl.isChecked()) {sportsSources.add("NHL");}
        CheckBox talksport = (CheckBox)findViewById(R.id.talksport_checkbox);               if(talksport.isChecked()) {sportsSources.add("Talksport");}
        CheckBox the_sport_bible = (CheckBox)findViewById(R.id.the_sport_bible_checkbox);   if(the_sport_bible.isChecked()) {sportsSources.add("The Sport Bible");}
    }

    public void getBusinessSources(){
        CheckBox afr = (CheckBox)findViewById(R.id.afr_checkbox);                           if(afr.isChecked()) {businessSources.add("AFR");}
        CheckBox bloomberg = (CheckBox)findViewById(R.id.bloomberg_checkbox);               if(bloomberg.isChecked()) {businessSources.add("Bloomberg");}
        CheckBox business_insider = (CheckBox)findViewById(R.id.business_insider_checkbox); if(business_insider.isChecked()) {businessSources.add("Business Insider");}
        CheckBox cnbc = (CheckBox)findViewById(R.id.cnbc_checkbox);                         if(cnbc.isChecked()) {businessSources.add("CNBC");}
        CheckBox economist = (CheckBox)findViewById(R.id.economist_checkbox);               if(economist.isChecked()) {businessSources.add("Economist");}
        CheckBox financial_post = (CheckBox)findViewById(R.id.financial_post_checkbox);     if(financial_post.isChecked()) {businessSources.add("Financial Post");}
        CheckBox financial_times = (CheckBox)findViewById(R.id.financial_times_checkbox);   if(financial_times.isChecked()) {businessSources.add("Financial Times");}
        CheckBox fortune = (CheckBox)findViewById(R.id.fortune_checkbox);                   if(fortune.isChecked()) {businessSources.add("Fortune");}
        CheckBox wsj = (CheckBox)findViewById(R.id.wsj_checkbox);                           if(wsj.isChecked()) {businessSources.add("Wall Street Journal");}
    }

    public void getHealthSources(){
        CheckBox mnt = (CheckBox)findViewById(R.id.mnt_checkbox); if(mnt.isChecked()) {healthSources.add("Medical News Today");}
    }

    public void getGeneralSources(){}

    public void getEntertainmentSources(){
        CheckBox buzzfeed = (CheckBox)findViewById(R.id.buzzfeed_checkbox);             if(buzzfeed.isChecked()) {entertainmentSources.add("Buzzfeed");}
        CheckBox daily_mail = (CheckBox)findViewById(R.id.daily_mail_checkbox);         if(daily_mail.isChecked()) {entertainmentSources.add("Daily Mail");}
        CheckBox e_weekly = (CheckBox)findViewById(R.id.entertainment_weekly_checkbox); if(e_weekly.isChecked()) {entertainmentSources.add("Entertainment Weekly");}
        CheckBox ign = (CheckBox)findViewById(R.id.ign_checkbox);                       if(ign.isChecked()) {entertainmentSources.add("IGN");}
        CheckBox mashable = (CheckBox)findViewById(R.id.mashable_checkbox);             if(mashable.isChecked()) {entertainmentSources.add("Mashable");}
        CheckBox mtv_news = (CheckBox)findViewById(R.id.mtv_news_checkbox);             if(mtv_news.isChecked()) {entertainmentSources.add("MTV News");}
        CheckBox mtv_news_uk = (CheckBox)findViewById(R.id.mtv_news_uk_checkbox);       if(mtv_news_uk.isChecked()) {entertainmentSources.add("MTV News UK");}
        CheckBox polygon = (CheckBox)findViewById(R.id.polygon_checkbox);               if(polygon.isChecked()) {entertainmentSources.add("Polygon");}
        CheckBox the_lad_bible = (CheckBox)findViewById(R.id.the_lad_bible_checkbox);   if(the_lad_bible.isChecked()) {entertainmentSources.add("The Lad Bible");}
    }

    public void getScienceSources(){
        CheckBox nat_geo = (CheckBox)findViewById(R.id.nat_geo_checkbox);
        if(nat_geo.isChecked()) {scienceSources.add("National Geographic");}
        CheckBox new_scientist = (CheckBox)findViewById(R.id.new_scientist_checkbox);
        if(new_scientist.isChecked()) {scienceSources.add("New Scientist");}
        CheckBox next_big_future = (CheckBox)findViewById(R.id.next_big_future_checkbox);
        if(next_big_future.isChecked()) {scienceSources.add("Next Big Future");}
    }

    public void goBackToFeedSettings(View view) {
        Intent intent=new Intent();
        HashMap feedmap = new HashMap();
        feedmap.put("Technology", techSources);
        feedmap.put("Sports", sportsSources);
        feedmap.put("Business", businessSources);
        feedmap.put("Health", healthSources);
        feedmap.put("General", generalSources);
        feedmap.put("Entertainment", entertainmentSources);
        feedmap.put("Science", scienceSources);
        intent.putExtra("Feed Map",feedmap);
        setResult(2,intent);
        finish();
    }
}
