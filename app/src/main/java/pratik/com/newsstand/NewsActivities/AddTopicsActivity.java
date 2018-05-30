package pratik.com.newsstand.NewsActivities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import pratik.com.newsstand.R;

public class AddTopicsActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    ArrayList<String> old_sources = new ArrayList<>();
    ArrayList techSources = new ArrayList<Pair>();
    ArrayList sportsSources = new ArrayList<Pair>();
    ArrayList businessSources = new ArrayList<Pair>();
    ArrayList healthSources = new ArrayList<Pair>();
    ArrayList generalSources = new ArrayList<Pair>();
    ArrayList entertainmentSources = new ArrayList<Pair>();
    ArrayList scienceSources = new ArrayList<Pair>();
    CheckBox ars_technica,crypto_coin,engadget,hacker_news,recode,techcrunch,techradar,the_next_web,the_verge,wired;
    CheckBox bbc_sport,bleacher_report,espn,espn_cricinfo,football_italia,four_four_two,fox_sports,nfl,nhl,talksport,the_sport_bible;
    CheckBox afr,bloomberg,business_insider,cnbc,economist,financial_post,financial_times,fortune,wsj;
    CheckBox mnt;
    CheckBox abc_news,abc_news_au,al_jazeera_english,associated_press,axios,bbc_news,breitbart_news,cbc_news,cbs_news,cnn;
    CheckBox fox_news,google_news,independent,metro,mirror,msnbc,nbc_news,new_york_magazine,new_com_au,news24;
    CheckBox newsweek,politico,reddit_r_all,reuters,rte_ie,the_globe_and_mail,the_guardian_uk,the_hill,the_hindu;
    CheckBox the_huffington_post,the_new_york_times,the_telegraph,the_times_of_india,the_washington_post,time,usa_today,vice_news;
    CheckBox buzzfeed,daily_mail,e_weekly,ign,mashable,mtv_news,mtv_news_uk,polygon,the_lad_bible;
    CheckBox nat_geo,new_scientist,next_big_future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topics);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("bundle");
        old_sources = (ArrayList<String>) b.getStringArrayList("sources list");

        Animation in = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.left_out);

        flipper  = findViewById(R.id.viewFlipper);
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);

        initializeAllCheckBoxes();
        setTechCheckboxes(old_sources);
        setSportsCheckboxes(old_sources);
        setBusinessCheckboxes(old_sources);
        setHealthCheckboxes(old_sources);
        setGeneralCheckboxes(old_sources);
        setEntertainmentCheckboxes(old_sources);
        setScienceCheckboxes(old_sources);

    }

    private void initializeAllCheckBoxes() {
        //TECHNOLOGY
        ars_technica = (CheckBox)findViewById(R.id.ars_technica_checkbox);
        crypto_coin = (CheckBox)findViewById(R.id.crypto_coins_checkbox);
        engadget = (CheckBox)findViewById(R.id.engadget_checkbox);
        hacker_news = (CheckBox)findViewById(R.id.hacker_news_checkbox);
        recode = (CheckBox)findViewById(R.id.recode_checkbox);
        techcrunch = (CheckBox)findViewById(R.id.techcrunch_checkbox);
        techradar = (CheckBox)findViewById(R.id.techradar_checkbox);
        the_next_web = (CheckBox)findViewById(R.id.the_next_web_checkbox);
        the_verge = (CheckBox)findViewById(R.id.the_verge_checkbox);
        wired = (CheckBox)findViewById(R.id.wired_checkbox);

        //SPORTS
        bbc_sport = (CheckBox)findViewById(R.id.bbc_sport_checkbox);
        bleacher_report = (CheckBox)findViewById(R.id.bleacher_report_checkbox);
        espn = (CheckBox)findViewById(R.id.espn_checkbox);
        espn_cricinfo= (CheckBox)findViewById(R.id.espn_cricinfo_checkbox);
        football_italia = (CheckBox)findViewById(R.id.football_italia_checkbox);
        four_four_two = (CheckBox)findViewById(R.id.four_four_two_checkbox);
        fox_sports = (CheckBox)findViewById(R.id.fox_sports_checkbox);
        nfl = (CheckBox)findViewById(R.id.nfl_checkbox);
        nhl= (CheckBox)findViewById(R.id.nhl_checkbox);
        talksport = (CheckBox)findViewById(R.id.talksport_checkbox);
        the_sport_bible = (CheckBox)findViewById(R.id.the_sport_bible_checkbox);

        //BUSINESS
        afr = (CheckBox)findViewById(R.id.afr_checkbox);
        bloomberg = (CheckBox)findViewById(R.id.bloomberg_checkbox);
        business_insider = (CheckBox)findViewById(R.id.business_insider_checkbox);
        cnbc = (CheckBox)findViewById(R.id.cnbc_checkbox);
        economist = (CheckBox)findViewById(R.id.economist_checkbox);
        financial_post = (CheckBox)findViewById(R.id.financial_post_checkbox);
        financial_times = (CheckBox)findViewById(R.id.financial_times_checkbox);
        fortune = (CheckBox)findViewById(R.id.fortune_checkbox);
        wsj = (CheckBox)findViewById(R.id.wsj_checkbox);

        //HEALTH
        mnt = (CheckBox)findViewById(R.id.mnt_checkbox);

        //GENERAL
        abc_news = (CheckBox)findViewById(R.id.abc_news_checkbox);
        abc_news_au = (CheckBox)findViewById(R.id.abc_news_au_checkbox);
        al_jazeera_english = (CheckBox)findViewById(R.id.al_jazeera_english_checkbox);
        associated_press = (CheckBox)findViewById(R.id.associated_press_checkbox);
        axios = (CheckBox)findViewById(R.id.axios_checkbox);
        bbc_news = (CheckBox)findViewById(R.id.bbc_news_checkbox);
        breitbart_news = (CheckBox)findViewById(R.id.breitbart_news_checkbox);
        cbc_news = (CheckBox)findViewById(R.id.cbc_news_checkbox);
        cbs_news = (CheckBox)findViewById(R.id.cbs_news_checkbox);
        cnn = (CheckBox)findViewById(R.id.cnn_checkbox);
        fox_news = (CheckBox)findViewById(R.id.fox_news_checkbox);
        google_news = (CheckBox)findViewById(R.id.google_news_checkbox);
        independent = (CheckBox)findViewById(R.id.independent_checkbox);
        metro = (CheckBox)findViewById(R.id.metro_checkbox);
        mirror = (CheckBox)findViewById(R.id.mirror_checkbox);
        msnbc = (CheckBox)findViewById(R.id.msnbc_checkbox);
        nbc_news = (CheckBox)findViewById(R.id.nbc_news_checkbox);
        new_york_magazine = (CheckBox)findViewById(R.id.new_york_magazine_checkbox);
        new_com_au = (CheckBox)findViewById(R.id.news_com_au_checkbox);
        news24 = (CheckBox)findViewById(R.id.news24_checkbox);
        newsweek = (CheckBox)findViewById(R.id.newsweek_checkbox);
        politico = (CheckBox)findViewById(R.id.politico_checkbox);
        reddit_r_all = (CheckBox)findViewById(R.id.reddit_checkbox);
        reuters = (CheckBox)findViewById(R.id.reuters_checkbox);
        rte_ie = (CheckBox)findViewById(R.id.rte_checkbox);
        the_globe_and_mail = (CheckBox)findViewById(R.id.the_globe_and_mail_checkbox);
        the_guardian_uk = (CheckBox)findViewById(R.id.the_guardian_uk_checkbox);
        the_hill = (CheckBox)findViewById(R.id.the_hill_checkbox);
        the_hindu = (CheckBox)findViewById(R.id.the_hindu_checkbox);
        the_huffington_post = (CheckBox)findViewById(R.id.the_huffington_post_checkbox);
        the_new_york_times = (CheckBox)findViewById(R.id.the_new_york_times_checkbox);
        the_telegraph = (CheckBox)findViewById(R.id.the_telegraph_checkbox);
        the_times_of_india = (CheckBox)findViewById(R.id.the_toi_checkbox);
        the_washington_post = (CheckBox)findViewById(R.id.the_washington_post_checkbox);
        time = (CheckBox)findViewById(R.id.time_checkbox);
        usa_today = (CheckBox)findViewById(R.id.usa_today_checkbox);
        vice_news = (CheckBox)findViewById(R.id.vice_news_checkbox);

        //ENTERTAINMENT
        buzzfeed = (CheckBox)findViewById(R.id.buzzfeed_checkbox);
        daily_mail = (CheckBox)findViewById(R.id.daily_mail_checkbox);
        e_weekly = (CheckBox)findViewById(R.id.entertainment_weekly_checkbox);
        ign = (CheckBox)findViewById(R.id.ign_checkbox);
        mashable = (CheckBox)findViewById(R.id.mashable_checkbox);
        mtv_news = (CheckBox)findViewById(R.id.mtv_news_checkbox);
        mtv_news_uk = (CheckBox)findViewById(R.id.mtv_news_uk_checkbox);
        polygon = (CheckBox)findViewById(R.id.polygon_checkbox);
        the_lad_bible = (CheckBox)findViewById(R.id.the_lad_bible_checkbox);

        //SCIENCE
        nat_geo = (CheckBox)findViewById(R.id.nat_geo_checkbox);
        new_scientist = (CheckBox)findViewById(R.id.new_scientist_checkbox);
        next_big_future = (CheckBox)findViewById(R.id.next_big_future_checkbox);

    }

    public void changeViewToShow(View v){
        switch (v.getId()){
            case R.id.save_tech_sources_button:
                flipper.setDisplayedChild(0);
                //getTechSources();
                break;
            case R.id.save_sports_sources_button:
                flipper.setDisplayedChild(0);
                //getSportsSources();
                break;
            case R.id.save_business_sources_button:
                flipper.setDisplayedChild(0);
                //getBusinessSources();
                break;
            case R.id.save_health_sources_button:
                flipper.setDisplayedChild(0);
                //getHealthSources();
                break;
            case R.id.save_general_sources_button:
                flipper.setDisplayedChild(0);
                //getGeneralSources();
                break;
            case R.id.save_entertainment_sources_button:
                flipper.setDisplayedChild(0);
                //getEntertainmentSources();
                break;
            case R.id.save_science_sources_button:
                flipper.setDisplayedChild(0);
                //getScienceSources();
                break;
            case R.id.add_tech_imagebutton:
                flipper.setDisplayedChild(1);
                //setTechCheckboxes(old_sources);
                break;
            case R.id.add_sports_imagebutton:
                flipper.setDisplayedChild(2);
                //setSportsCheckboxes(old_sources);
                break;
            case R.id.add_business_imagebutton:
                flipper.setDisplayedChild(3);
                //setBusinessCheckboxes(old_sources);
                break;
            case R.id.add_health_imagebutton:
                flipper.setDisplayedChild(4);
                //setHealthCheckboxes(old_sources);
                break;
            case R.id.add_general_imagebutton:
                flipper.setDisplayedChild(5);
                //setGeneralCheckboxes(old_sources);
                break;
            case R.id.add_entertainment_imagebutton:
                flipper.setDisplayedChild(6);
                //setEntertainmentCheckboxes(old_sources);
                break;
            case R.id.add_science_imagebutton:
                flipper.setDisplayedChild(7);
                //setScienceCheckboxes(old_sources);
                break;
        }
    }

    //METHODS TO SHOW CHECKED/UNCHECKED
    private void setTechCheckboxes(ArrayList<String> sources){
        for(int i=0;i<sources.size();i++) {
            String s = sources.get(i);
            if (s.equals("Ars Technica")) {
                ars_technica.setChecked(true);
                continue;
            }
            if (s.equals("Crypto Coins News")) {
                crypto_coin.setChecked(true);
                continue;
            }
            if (s.equals("Engadget")) {
                engadget.setChecked(true);
                continue;
            }
            if (s.equals("Hacker News")) {
                hacker_news.setChecked(true);
                continue;
            }
            if (s.equals("Recode")) {
                recode.setChecked(true);
                continue;
            }
            if (s.equals("Techcrunch")) {
                techcrunch.setChecked(true);
                continue;
            }
            if (s.equals("Techradar")) {
                techradar.setChecked(true);
                continue;
            }
            if (s.equals("The Next Web")){
                the_next_web.setChecked(true);
                continue;
            }
            if(s.equals("The Verge")) {
                the_verge.setChecked(true);
                continue;
            }
            if(s.equals("Wired")){
                wired.setChecked(true);
            }
        }

    }

    private void setSportsCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("BBC Sport"))
                bbc_sport.setChecked(true);
            if(s.equals("Bleacher Report"))
                bleacher_report.setChecked(true);
            if(s.equals("ESPN"))
                espn.setChecked(true);
            if(s.equals("ESPN Cricinfo"))
                espn_cricinfo.setChecked(true);
            if(s.equals("Football Italia"))
                football_italia.setChecked(true);
            if(s.equals("Four Four Two"))
                four_four_two.setChecked(true);
            if(s.equals("Fox Sports"))
                fox_sports.setChecked(true);
            if(s.equals("NFL"))
                nfl.setChecked(true);
            if(s.equals("NHL"))
                nhl.setChecked(true);
            if(s.equals("Talksport"))
                talksport.setChecked(true);
            if(s.equals("The Sport Bible"))
                the_sport_bible.setChecked(true);
        }
    }

    private void setBusinessCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("AFR"))
                afr.setChecked(true);
            if(s.equals("Bloomberg"))
                bloomberg.setChecked(true);
            if(s.equals("Business Insider"))
                business_insider.setChecked(true);
            if(s.equals("CNBC"))
                cnbc.setChecked(true);
            if(s.equals("Economist"))
                economist.setChecked(true);
            if(s.equals("Financial Post"))
                financial_post.setChecked(true);
            if(s.equals("Financial Times"))
                financial_times.setChecked(true);
            if(s.equals("Fortune"))
                fortune.setChecked(true);
            if(s.equals("Wall Street Journal"))
                wsj.setChecked(true);
        }
    }

    private void setHealthCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("Medical News Today"))
                mnt.setChecked(true);
        }
    }

    private void setGeneralCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("ABC News"))
                abc_news.setChecked(true);
            if(s.equals("ABC News AU"))
                abc_news_au.setChecked(true);
            if(s.equals("Al Jazeera English"))
                al_jazeera_english.setChecked(true);
            if(s.equals("Associated Press"))
                associated_press.setChecked(true);
            if(s.equals("Axios"))
                axios.setChecked(true);
            if(s.equals("BBC News"))
                bbc_news.setChecked(true);
            if(s.equals("Breitbart News"))
                breitbart_news.setChecked(true);
            if(s.equals("CBC News"))
                cbc_news.setChecked(true);
            if(s.equals("CBS News"))
                cbs_news.setChecked(true);
            if(s.equals("CNN"))
                cnn.setChecked(true);
            if(s.equals("Fox News"))
                fox_news.setChecked(true);
            if(s.equals("Google News"))
                google_news.setChecked(true);
            if(s.equals("Independent"))
                independent.setChecked(true);
            if(s.equals("Metro"))
                metro.setChecked(true);
            if(s.equals("Mirror"))
                mirror.setChecked(true);
            if(s.equals("MSNBC"))
                msnbc.setChecked(true);
            if(s.equals("NBC News"))
                nbc_news.setChecked(true);
            if(s.equals("New York Magazine"))
                new_york_magazine.setChecked(true);
            if(s.equals("News.com.au"))
                new_com_au.setChecked(true);
            if(s.equals("News 24"))
                news24.setChecked(true);
            if(s.equals("Newsweek"))
                newsweek.setChecked(true);
            if(s.equals("Politico"))
                politico.setChecked(true);
            if(s.equals("Reddit/r/all"))
                reddit_r_all.setChecked(true);
            if(s.equals("Reuters"))
                reuters.setChecked(true);
            if(s.equals("RTE"))
                rte_ie.setChecked(true);
            if(s.equals("The Globe and Mail"))
                the_globe_and_mail.setChecked(true);
            if(s.equals("The Guardian UK"))
                the_guardian_uk.setChecked(true);
            if(s.equals("The Hill"))
                the_hill.setChecked(true);
            if(s.equals("The Hindu"))
                the_hindu.setChecked(true);
            if(s.equals("The Huffington Post"))
                the_huffington_post.setChecked(true);
            if(s.equals("The New York Times"))
                the_new_york_times.setChecked(true);
            if(s.equals("The Telegraph"))
                the_telegraph.setChecked(true);
            if(s.equals("The Times of India"))
                the_times_of_india.setChecked(true);
            if(s.equals("The Washington Post"))
                the_washington_post.setChecked(true);
            if(s.equals("Time"))
                time.setChecked(true);
            if(s.equals("USA Today"))
                usa_today.setChecked(true);
            if(s.equals("Vice News"))
                vice_news.setChecked(true);
        }
    }

    private void setEntertainmentCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("Buzzfeed"))
                buzzfeed.setChecked(true);
            if(s.equals("Daily Mail"))
                daily_mail.setChecked(true);
            if(s.equals("Entertainment Weekly"))
                e_weekly.setChecked(true);
            if(s.equals("IGN"))
                ign.setChecked(true);
            if(s.equals("Mashable"))
                mashable.setChecked(true);
            if(s.equals("MTV News"))
                mtv_news.setChecked(true);
            if(s.equals("MTV News UK"))
                mtv_news_uk.setChecked(true);
            if(s.equals("Polygon"))
                polygon.setChecked(true);
            if(s.equals("The Lad Bible"))
                the_lad_bible.setChecked(true);

        }
    }

    private void setScienceCheckboxes(ArrayList<String> sources) {
        for(int i=0;i<sources.size();i++){
            String s = sources.get(i);
            if(s.equals("National Geographic"))
                nat_geo.setChecked(true);
            if(s.equals("New Scientist"))
                new_scientist.setChecked(true);
            if(s.equals("Next Big Future"))
                next_big_future.setChecked(true);
        }
    }

    //METHODS TO GET WHICH CHECKBOX HAS BEEN CHECKED
    public void getTechSources(){
        if(ars_technica.isChecked()) {techSources.add(new Pair<>("ars-technica", "Ars Technica"));}
        if(crypto_coin.isChecked()) {techSources.add(new Pair<>("crypto-coins-news", "Crypto Coins News"));}
        if(engadget.isChecked()) {techSources.add(new Pair<>("engadget","Engadget"));}
        if(hacker_news.isChecked()) {techSources.add(new Pair<>("hacker-news","Hacker News"));}
        if(recode.isChecked()) {techSources.add(new Pair<>("recode","Recode"));}
        if(techcrunch.isChecked()) {techSources.add(new Pair<>("techcrunch","Techcrunch"));}
        if(techradar.isChecked()) {techSources.add(new Pair<>("techradar","Techradar"));}
        if(the_next_web.isChecked()) {techSources.add(new Pair<>("the-next-web","The Next Web"));}
        if(the_verge.isChecked()) {techSources.add(new Pair<>("the-verge","The Verge"));}
        if(wired.isChecked()) {techSources.add(new Pair<>("wired","Wired"));}
    }

    public void getSportsSources(){
        if(bbc_sport.isChecked()) {sportsSources.add(new Pair<>("bbc-sport","BBC Sport"));}
        if(bleacher_report.isChecked()) {sportsSources.add(new Pair<>("bleacher-report","Bleacher Report"));}
        if(espn.isChecked()) {sportsSources.add(new Pair<>("espn","ESPN"));}
        if(espn_cricinfo.isChecked()) {sportsSources.add(new Pair<>("espn-cric-info","ESPN Cric Info"));}
        if(football_italia.isChecked()) {sportsSources.add(new Pair<>("football-italia","Football Italia"));}
        if(four_four_two.isChecked()) {sportsSources.add(new Pair<>("four-four-two","Four Four Two"));}
        if(fox_sports.isChecked()) {sportsSources.add(new Pair<>("fox-sports","Fox Sports"));}
        if(nfl.isChecked()) {sportsSources.add(new Pair<>("nfl-news","NFL News"));}
        if(nhl.isChecked()) {sportsSources.add(new Pair<>("nhl-news","NHL News"));}
        if(talksport.isChecked()) {sportsSources.add(new Pair<>("talksport","Talksport"));}
        if(the_sport_bible.isChecked()) {sportsSources.add(new Pair<>("the-sport-bible","The Sport Bible"));}
    }

    public void getBusinessSources(){
        if(afr.isChecked()) {businessSources.add(new Pair<>("australian-financial-review","Australian Financial Review"));}
        if(bloomberg.isChecked()) {businessSources.add(new Pair<>("bloomberg","Bloomberg"));}
        if(business_insider.isChecked()) {businessSources.add(new Pair<>("business-insider","Business Insider"));}
        if(cnbc.isChecked()) {businessSources.add(new Pair<>("cnbc","CNBC"));}
        if(economist.isChecked()) {businessSources.add(new Pair<>("the-economist","The Economist"));}
        if(financial_post.isChecked()) {businessSources.add(new Pair<>("financial-post","Financial Post"));}
        if(financial_times.isChecked()) {businessSources.add(new Pair<>("financial-times","Financial Times"));}
        if(fortune.isChecked()) {businessSources.add(new Pair<>("fortune","Fortune"));}
        if(wsj.isChecked()) {businessSources.add(new Pair<>("wall-street-journal","Wall Street Journal"));}
    }

    public void getHealthSources(){
        if(mnt.isChecked()) {healthSources.add(new Pair<>("medical-news-today","Medical News Today"));}
    }

    public void getGeneralSources(){
        if(abc_news.isChecked()) {generalSources.add(new Pair<>("abc-news","ABC News"));}
        if(abc_news_au.isChecked()) {generalSources.add(new Pair<>("abc-news-au","ABC News AU"));}
        if(al_jazeera_english.isChecked()) {generalSources.add(new Pair<>("al-jazeera-english","Al Jazeera English"));}
        if(associated_press.isChecked()) {generalSources.add(new Pair<>("associated-press","Associated Press"));}
        if(axios.isChecked()) {generalSources.add(new Pair<>("axios","Axios"));}
        if(bbc_news.isChecked()) {generalSources.add(new Pair<>("bbc-news","BBC News"));}
        if(breitbart_news.isChecked()) {generalSources.add(new Pair<>("breitbart-news","Breitbart News"));}
        if(cbc_news.isChecked()) {generalSources.add(new Pair<>("cbc-news","CBC News"));}
        if(cnn.isChecked()) {generalSources.add(new Pair<>("cnn","CNN"));}
        if(fox_news.isChecked()) {generalSources.add(new Pair<>("fox-news","Fox News"));}
        if(google_news.isChecked()) {generalSources.add(new Pair<>("google-news","Google News"));}
        if(independent.isChecked()) {generalSources.add(new Pair<>("independent","Independent"));}
        if(metro.isChecked()) {generalSources.add(new Pair<>("metro","Metro"));}
        if(mirror.isChecked()) {generalSources.add(new Pair<>("mirror","Mirror"));}
        if(msnbc.isChecked()) {generalSources.add(new Pair<>("msnbc","MSNBC"));}
        if(nbc_news.isChecked()) {generalSources.add(new Pair<>("nbc-news","NBC News"));}
        if(new_york_magazine.isChecked()) {generalSources.add(new Pair<>("new-york-magazine","New York Magazine"));}
        if(new_com_au.isChecked()) {generalSources.add(new Pair<>("news-com-au","News.com.au"));}
        if(news24.isChecked()) {generalSources.add(new Pair<>("news24","News 24"));}
        if(newsweek.isChecked()) {generalSources.add(new Pair<>("newsweek","Newsweek"));}
        if(politico.isChecked()) {generalSources.add(new Pair<>("politoco","Politico"));}
        if(reddit_r_all.isChecked()) {generalSources.add(new Pair<>("reddit-r-all","Reddit/r/all"));}
        if(reuters.isChecked()) {generalSources.add(new Pair<>("reuters","Reuters"));}
        if(rte_ie.isChecked()) {generalSources.add(new Pair<>("rte","RTE"));}
        if(the_globe_and_mail.isChecked()) {generalSources.add(new Pair<>("the-globe-and-mail","The Globe and Mail"));}
        if(the_guardian_uk.isChecked()) {generalSources.add(new Pair<>("the-guardian-uk","The Guardian UK"));}
        if(the_hill.isChecked()) {generalSources.add(new Pair<>("the-hill","The Hill"));}
        if(the_hindu.isChecked()) {generalSources.add(new Pair<>("the-hindu","The Hindu"));}
        if(the_huffington_post.isChecked()) {generalSources.add(new Pair<>("the-huffington-post","The Huffington Post"));}
        if(the_new_york_times.isChecked()) {generalSources.add(new Pair<>("the-new-york-times","The New York Times"));}
        if(the_telegraph.isChecked()) {generalSources.add(new Pair<>("the-telegraph","The Telegraph"));}
        if(the_times_of_india.isChecked()) {generalSources.add(new Pair<>("the-times-of-india","The Times of India"));}
        if(the_washington_post.isChecked()) {generalSources.add(new Pair<>("the-washington-post","The Washington Post"));}
    }

    public void getEntertainmentSources(){
        if(buzzfeed.isChecked()) {entertainmentSources.add(new Pair<>("buzzfeed","Buzzfeed"));}
        if(daily_mail.isChecked()) {entertainmentSources.add(new Pair<>("daily-mail","Daily Mail"));}
        if(e_weekly.isChecked()) {entertainmentSources.add(new Pair<>("entertainment-weekly","Entertainment Weekly"));}
        if(ign.isChecked()) {entertainmentSources.add(new Pair<>("ign","IGN"));}
        if(mashable.isChecked()) {entertainmentSources.add(new Pair<>("mashable","Mashable"));}
        if(mtv_news.isChecked()) {entertainmentSources.add(new Pair<>("mtv-news","MTV News"));}
        if(mtv_news_uk.isChecked()) {entertainmentSources.add(new Pair<>("mtv-news-uk","MTV News UK"));}
        if(polygon.isChecked()) {entertainmentSources.add(new Pair<>("polygon","Polygon"));}
        if(the_lad_bible.isChecked()) {entertainmentSources.add(new Pair<>("the-lad-bible","The Lad Bible"));}
    }

    public void getScienceSources(){
        if(nat_geo.isChecked()) {scienceSources.add(new Pair<>("national-geographic","National Geographic"));}
        if(new_scientist.isChecked()) {scienceSources.add(new Pair<>("new-scientist","New Scientist"));}
        if(next_big_future.isChecked()) {scienceSources.add(new Pair<>("next-big-future","Next Big Future"));}
    }

    public void goBackToFeedSettings(View view) {
        getTechSources();getSportsSources();getBusinessSources();
        getHealthSources();getGeneralSources();getEntertainmentSources();
        getScienceSources();
        Intent intent=new Intent();
        HashMap feedmap = new HashMap();
        Gson gson = new Gson();
        feedmap.put("Technology", techSources);
        feedmap.put("Sports", sportsSources);
        feedmap.put("Business", businessSources);
        feedmap.put("Health", healthSources);
        feedmap.put("General", generalSources);
        feedmap.put("Entertainment", entertainmentSources);
        feedmap.put("Science", scienceSources);
        String hashmap_to_string = gson.toJson(feedmap);
        intent.putExtra("Feed Map",hashmap_to_string);
        setResult(5003,intent);
        finish();
    }
}
