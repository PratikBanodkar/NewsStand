package pratik.com.newsstand;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Pratz on 29-01-2018.
 */

public class ArticlesTab extends Fragment {

    private ArrayList allCategories = new ArrayList<Category>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.library_articles_layout, container, false);
        populateCategoryArraylist();
        RecyclerView articles_recyclerview = (RecyclerView) rootView.findViewById(R.id.articles_root_recyclerview);
        articles_recyclerview.setHasFixedSize(true);
        LibraryRecyclerAdapter recyclerAdapter = new LibraryRecyclerAdapter(getContext(),allCategories);
        articles_recyclerview.setAdapter(recyclerAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        articles_recyclerview.setLayoutManager(llm);

        return rootView;
    }

    private void populateCategoryArraylist() {

        //Add category source IDs !!////
        //Configure and add the TECHNOLOGY Category
        Category technology = new Category();
        ArrayList techSources = new ArrayList();
        techSources.add("Ars Technica");techSources.add("Crypto Coins");techSources.add("Engadget");techSources.add("Hacker News");techSources.add("Recode");
        techSources.add("TechCrunch");techSources.add("TechRadar");techSources.add("The Next Web");techSources.add("The Verge");techSources.add("Wired");

        ArrayList techSourceLogos = new ArrayList();
        techSourceLogos.add(R.drawable.ars_technica);techSourceLogos.add(R.drawable.crypto_coins_news);techSourceLogos.add(R.drawable.engadget);
        techSourceLogos.add(R.drawable.hacker_news);techSourceLogos.add(R.drawable.recode);techSourceLogos.add(R.drawable.techcrunch);
        techSourceLogos.add(R.drawable.techradar);techSourceLogos.add(R.drawable.the_next_web);techSourceLogos.add(R.drawable.the_verge);
        techSourceLogos.add(R.drawable.wired);

        ArrayList techSourceIDs = new ArrayList();
        techSourceIDs.add("ars-technica");techSourceIDs.add("crypto-coins-news");techSourceIDs.add("engadget");techSourceIDs.add("hacker-news");techSourceIDs.add("recode");
        techSourceIDs.add("techcrunch");techSourceIDs.add("techradar");techSourceIDs.add("the-next-web");techSourceIDs.add("the-verge");techSourceIDs.add("wired");

        technology.setCategoryName("Technology");
        technology.setCategorySourceNames(techSources);
        technology.setCategorySourceLogos(techSourceLogos);
        technology.setCategorySourceIDs(techSourceIDs);

        allCategories.add(technology);

        //Configure and add the SPORTS Category
        Category sports = new Category();
        ArrayList sportsSources = new ArrayList();
        sportsSources.add("BBC Sport");sportsSources.add("Bleacher Report");sportsSources.add("ESPN");sportsSources.add("ESPN Cricinfo");sportsSources.add("Football Italia");
        sportsSources.add("Four Four Two");sportsSources.add("Fox Sports");sportsSources.add("NFL");sportsSources.add("NHL");sportsSources.add("Talksport");
        sportsSources.add("The Sport Bible");

        ArrayList sportsSourceLogos = new ArrayList();
        sportsSourceLogos.add(R.drawable.bbc_sport);sportsSourceLogos.add(R.drawable.bleacher_report);sportsSourceLogos.add(R.drawable.espn);
        sportsSourceLogos.add(R.drawable.espn_cric_info);sportsSourceLogos.add(R.drawable.football_italia);sportsSourceLogos.add(R.drawable.four_four_two);
        sportsSourceLogos.add(R.drawable.fox_sports);sportsSourceLogos.add(R.drawable.nfl_news);sportsSourceLogos.add(R.drawable.nhl_news);
        sportsSourceLogos.add(R.drawable.talksport);sportsSourceLogos.add(R.drawable.the_sport_bible);

        ArrayList sportsSourceIDs = new ArrayList();
        sportsSourceIDs.add("bbc-sport");sportsSourceIDs.add("bleacher-report");sportsSourceIDs.add("espn");sportsSourceIDs.add("espn-cric-info");sportsSourceIDs.add("football-italia");
        sportsSourceIDs.add("four-four-two");sportsSourceIDs.add("fox-sports");sportsSourceIDs.add("nfl");sportsSourceIDs.add("nhl");sportsSourceIDs.add("talksport");
        sportsSourceIDs.add("the-sport-bible");

        sports.setCategoryName("Sports");
        sports.setCategorySourceNames(sportsSources);
        sports.setCategorySourceLogos(sportsSourceLogos);
        sports.setCategorySourceIDs(sportsSourceIDs);

        allCategories.add(sports);

        //Configure and add the BUSINESS category
        Category business = new Category();
        ArrayList bizSources = new ArrayList();
        bizSources.add("AFR");bizSources.add("Bloomberg");bizSources.add("Business Insider");bizSources.add("CNBC");bizSources.add("Economist");
        bizSources.add("Financial Post");bizSources.add("Financial Times");bizSources.add("Fortune");bizSources.add("Wall Street Journal");

        ArrayList bizSourceLogos = new ArrayList();
        bizSourceLogos.add(R.drawable.australian_financial_review);bizSourceLogos.add(R.drawable.bloomberg);bizSourceLogos.add(R.drawable.business_insider);bizSourceLogos.add(R.drawable.cnbc);bizSourceLogos.add(R.drawable.the_economist);
        bizSourceLogos.add(R.drawable.financial_post);bizSourceLogos.add(R.drawable.financial_times);bizSourceLogos.add(R.drawable.fortune);bizSourceLogos.add(R.drawable.the_wall_street_journal);

        ArrayList bizSourceIDs = new ArrayList();
        bizSourceIDs.add("australian-financial-review");bizSourceIDs.add("bloomberg");bizSourceIDs.add("business-insider");bizSourceIDs.add("cnbc");bizSourceIDs.add("economist");
        bizSourceIDs.add("financial-post");bizSourceIDs.add("financial-times");bizSourceIDs.add("fortune");bizSourceIDs.add("the-wall-street-journal");

        business.setCategoryName("Business");
        business.setCategorySourceNames(bizSources);
        business.setCategorySourceLogos(bizSourceLogos);
        business.setCategorySourceIDs(bizSourceIDs);

        allCategories.add(business);

        //Configure and add the HEALTH category
        Category health = new Category();
        ArrayList healthSources = new ArrayList();
        healthSources.add("Medical News Today");

        ArrayList healthSourceLogos = new ArrayList();
        healthSourceLogos.add(R.drawable.medical_news_today);

        ArrayList healthSourcesIDs = new ArrayList();
        healthSourcesIDs.add("medical-news-today");

        health.setCategoryName("Health");
        health.setCategorySourceNames(healthSources);
        health.setCategorySourceLogos(healthSourceLogos);
        health.setCategorySourceIDs(healthSourcesIDs);

        allCategories.add(health);

        //Configure and add the GENERAL category
        Category general = new Category();
        ArrayList generalSources = new ArrayList();
        generalSources.add("ABC News");generalSources.add("ABC News AU");generalSources.add("Al Jazeera English");generalSources.add("Associated Press");generalSources.add("Axios");
        generalSources.add("BBC News");generalSources.add("Breitbart News");generalSources.add("CBC News");generalSources.add("CBS News");generalSources.add("CNN");
        generalSources.add("Fox News");generalSources.add("Google News");generalSources.add("Independent");generalSources.add("Metro");generalSources.add("Mirror");
        generalSources.add("MSNBC");generalSources.add("NBC News");generalSources.add("New York Magazine");generalSources.add("News.com.au");generalSources.add("News24");
        generalSources.add("Newsweek");generalSources.add("Politico");generalSources.add("Reddit /r/all");generalSources.add("Reuters");generalSources.add("Rte.ie");
        generalSources.add("The Globe and Mail");generalSources.add("The Guardian UK");generalSources.add("The Hill");generalSources.add("The Hindu");generalSources.add("The Huffington Post");
        generalSources.add("The New York Times");generalSources.add("The Telegraph");generalSources.add("The Times of India");generalSources.add("The Washington Post");generalSources.add("Time");
        generalSources.add("USA Today");generalSources.add("Vice News");

        ArrayList generalSourceLogos = new ArrayList();
        generalSourceLogos.add(R.drawable.abc_news);generalSourceLogos.add(R.drawable.abc_news_au);generalSourceLogos.add(R.drawable.al_jazeera_english);generalSourceLogos.add(R.drawable.associated_press);generalSourceLogos.add(R.drawable.axios);
        generalSourceLogos.add(R.drawable.bbc_news);generalSourceLogos.add(R.drawable.breitbart_news);generalSourceLogos.add(R.drawable.cbc_news);generalSourceLogos.add(R.drawable.cbs_news);generalSourceLogos.add(R.drawable.cnn);
        generalSourceLogos.add(R.drawable.fox_news);generalSourceLogos.add(R.drawable.google_news);generalSourceLogos.add(R.drawable.independent);generalSourceLogos.add(R.drawable.metro);generalSourceLogos.add(R.drawable.mirror);
        generalSourceLogos.add(R.drawable.msnbc);generalSourceLogos.add(R.drawable.nbc_news);generalSourceLogos.add(R.drawable.new_york_magazine);generalSourceLogos.add(R.drawable.news_com_au);generalSourceLogos.add(R.drawable.news24);
        generalSourceLogos.add(R.drawable.newsweek);generalSourceLogos.add(R.drawable.politico);generalSourceLogos.add(R.drawable.reddit_r_all);generalSourceLogos.add(R.drawable.reuters);generalSourceLogos.add(R.drawable.rte);
        generalSourceLogos.add(R.drawable.the_globe_and_mail);generalSourceLogos.add(R.drawable.the_guardian_uk);generalSourceLogos.add(R.drawable.the_hill);generalSourceLogos.add(R.drawable.the_hindu);generalSourceLogos.add(R.drawable.the_huffington_post);
        generalSourceLogos.add(R.drawable.the_new_york_times);generalSourceLogos.add(R.drawable.the_telegraph);generalSourceLogos.add(R.drawable.the_times_of_india);generalSourceLogos.add(R.drawable.the_washington_post);generalSourceLogos.add(R.drawable.time);
        generalSourceLogos.add(R.drawable.usa_today);generalSourceLogos.add(R.drawable.vice_news);

        ArrayList generalSourceIDs = new ArrayList();
        generalSourceIDs.add("abc-news");generalSourceIDs.add("abc-news-au");generalSourceIDs.add("al-jazeera-english");generalSourceIDs.add("associated-press");generalSourceIDs.add("axios");
        generalSourceIDs.add("bbc-news");generalSourceIDs.add("breitbart-news");generalSourceIDs.add("cbc-news");generalSourceIDs.add("cbs-news");generalSourceIDs.add("cnn");
        generalSourceIDs.add("fox-news");generalSourceIDs.add("google-news");generalSourceIDs.add("independent");generalSourceIDs.add("metro");generalSourceIDs.add("mirror");
        generalSourceIDs.add("msnbc");generalSourceIDs.add("nbc-news");generalSourceIDs.add("new-york-magazine");generalSourceIDs.add("news-com-au");generalSourceIDs.add("news24");
        generalSourceIDs.add("newsweek");generalSourceIDs.add("politico");generalSourceIDs.add("reddit-r-all");generalSourceIDs.add("reuters");generalSourceIDs.add("rte");
        generalSourceIDs.add("the-globe-and-mail");generalSourceIDs.add("the-guardian-uk");generalSourceIDs.add("the-hill");generalSourceIDs.add("the-hindu");generalSourceIDs.add("the-huffington-post");
        generalSourceIDs.add("the-new-york-times");generalSourceIDs.add("the-telegraph");generalSourceIDs.add("the-times-of-india");generalSourceIDs.add("the-washington-post");generalSourceIDs.add("time");
        generalSourceIDs.add("usa-today");generalSourceIDs.add("vice-news");

        general.setCategoryName("General");
        general.setCategorySourceNames(generalSources);
        general.setCategorySourceLogos(generalSourceLogos);
        general.setCategorySourceIDs(generalSourceIDs);

        allCategories.add(general);

        //Configure and add the ENTERTAINMENT category
        Category entertainment = new Category();
        ArrayList entertainmentSources = new ArrayList();
        entertainmentSources.add("Buzzfeed");entertainmentSources.add("Daily Mail");entertainmentSources.add("Entertainment Weekly");entertainmentSources.add("IGN");
        entertainmentSources.add("Mashable");entertainmentSources.add("MTV News");entertainmentSources.add("MTV News UK");entertainmentSources.add("Polygon");entertainmentSources.add("The Lad Bible");

        ArrayList entertainmentSourceLogos = new ArrayList();
        entertainmentSourceLogos.add(R.drawable.buzzfeed);entertainmentSourceLogos.add(R.drawable.daily_mail);entertainmentSourceLogos.add(R.drawable.entertainment_weekly);
        entertainmentSourceLogos.add(R.drawable.ign);entertainmentSourceLogos.add(R.drawable.mashable);entertainmentSourceLogos.add(R.drawable.mtv_news);
        entertainmentSourceLogos.add(R.drawable.mtv_news_uk);entertainmentSourceLogos.add(R.drawable.polygon);entertainmentSourceLogos.add(R.drawable.the_lad_bible);

        ArrayList entertainmentSourceIDs = new ArrayList();
        entertainmentSourceIDs.add("buzzfeed");entertainmentSourceIDs.add("daily-mail");entertainmentSourceIDs.add("entertainment-weekly");entertainmentSourceIDs.add("ign");
        entertainmentSourceIDs.add("mashable");entertainmentSourceIDs.add("mtv-news");entertainmentSourceIDs.add("mtv-news-uk");entertainmentSourceIDs.add("polygon");entertainmentSourceIDs.add("the-lad-bible");

        entertainment.setCategoryName("Entertainment");
        entertainment.setCategorySourceNames(entertainmentSources);
        entertainment.setCategorySourceLogos(entertainmentSourceLogos);
        entertainment.setCategorySourceIDs(entertainmentSourceIDs);

        allCategories.add(entertainment);

        //Configure and add the SCIENCE category
        Category science = new Category();
        ArrayList scienceSources = new ArrayList();
        scienceSources.add("National Geographic");scienceSources.add("New Scientist");scienceSources.add("Next Big Future");

        ArrayList scienceSourceLogos = new ArrayList();
        scienceSourceLogos.add(R.drawable.national_geographic);scienceSourceLogos.add(R.drawable.new_scientist);
        scienceSourceLogos.add(R.drawable.next_big_future);

        ArrayList scienceSourceIDs = new ArrayList();
        scienceSourceIDs.add("national-geographic");scienceSourceIDs.add("new-scientist");scienceSourceIDs.add("next-big-future");

        science.setCategoryName("Science");
        science.setCategorySourceNames(scienceSources);
        science.setCategorySourceLogos(scienceSourceLogos);
        science.setCategorySourceIDs(scienceSourceIDs);

        allCategories.add(science);
    }



}
