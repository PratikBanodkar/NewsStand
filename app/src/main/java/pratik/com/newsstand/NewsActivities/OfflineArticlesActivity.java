package pratik.com.newsstand.NewsActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pratik.com.newsstand.ExclusionStrategy_Bitmap_Drawable;
import pratik.com.newsstand.NewsFetching.ArticleImageFetching.ImageLoader;
import pratik.com.newsstand.NewsFetching.NewsItemObject;
import pratik.com.newsstand.R;

public class OfflineArticlesActivity extends AppCompatActivity {

    private String urlResponse = null ;
    private String responseString = null ;
    SharedPreferences savedArticlesPref;
    private ArrayList<String> bookmarkedArticles_String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_articles);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        SharedPreferences savedArticlesPref = getApplicationContext().getSharedPreferences("All Saved Articles", Context.MODE_PRIVATE);
        String json_string_saved_articles = savedArticlesPref.getString("Bookmarked",null);
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy_Bitmap_Drawable())
                .serializeNulls() //<-- uncomment to serialize NULL fields as well
                .create();
        Type type = new TypeToken<List<NewsItemObject>>(){}.getType();
        List<NewsItemObject> savedArticles_Retrieved = gson.fromJson(json_string_saved_articles, type);
        if(savedArticles_Retrieved == null || savedArticles_Retrieved.size() == 0){
            recyclerView.setVisibility(View.GONE);
            TextView noArticlesTextview = findViewById(R.id.noArticlesYet_textview);
            noArticlesTextview.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            TextView noArticlesTextview = findViewById(R.id.noArticlesYet_textview);
            noArticlesTextview.setVisibility(View.GONE);
            RecyclerView.Adapter adapter = new OfflineRecyclerAdapter((ArrayList<NewsItemObject>) savedArticles_Retrieved);
            recyclerView.setAdapter(adapter);
        }

    }

    public void updateOfflineArticlesList(ArrayList newsItemObjects){
        SharedPreferences savedArticlesPref = getApplicationContext().getSharedPreferences("All Saved Articles", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = savedArticlesPref.edit();
        //String json_string_saved_articles = savedArticlesPref.getString("Bookmarked",null);
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy_Bitmap_Drawable())
                .serializeNulls() //<-- uncomment to serialize NULL fields as well
                .create();
        String json_string_saved_articles = gson.toJson(newsItemObjects);
        prefsEditor.putString("Bookmarked",json_string_saved_articles);
        prefsEditor.commit();
    }

    public class OfflineRecyclerAdapter extends RecyclerView.Adapter<OfflineRecyclerAdapter.ViewHolder> {
        private ArrayList<NewsItemObject> newsItemObjects;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        OfflineRecyclerAdapter(ArrayList<NewsItemObject> newsItems){
            this.newsItemObjects = newsItems;
        }

        @Override
        public OfflineRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_short_offline, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final OfflineRecyclerAdapter.ViewHolder holder, int position) {
            viewBinderHelper.bind(holder.swipeRevealLayout, newsItemObjects.get(holder.getAdapterPosition()).getId());
            holder.textView_Headline.setText(newsItemObjects.get(holder.getAdapterPosition()).getTitle());
            holder.textView_Description.setText(newsItemObjects.get(holder.getAdapterPosition()).getDescription());
            holder.textView_Source.setText(newsItemObjects.get(holder.getAdapterPosition()).getSource());
            Resources resources = getApplicationContext().getResources();
            int resourceId = resources.getIdentifier(newsItemObjects.get(holder.getAdapterPosition()).getArticleSourceID(), "drawable",getApplicationContext().getPackageName());
            holder.imageView_articleSourceLogo.setImageDrawable(resources.getDrawable(resourceId));
            String dateString = newsItemObjects.get(holder.getAdapterPosition()).getArticleDate();
            holder.textView_timeStamp.setText(dateString);
            holder.article_options_layout.setBackgroundColor(Color.rgb(156,39,176));

            holder.article_contents_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open article for reading
                    String selectedArticleContent  = newsItemObjects.get(holder.getAdapterPosition()).getArticleContent();
                    Intent viewArticleIntent = new Intent(OfflineArticlesActivity.this,ReadArticleActivity.class);
                    //viewArticleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    viewArticleIntent.putExtra("CONTENT",selectedArticleContent);
                    try{
                        startActivity(viewArticleIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsItemObjects.remove(newsItemObjects.get(holder.getAdapterPosition()));
                    updateOfflineArticlesList(newsItemObjects);
                    notifyItemRemoved(holder.getAdapterPosition());

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
            protected TextView textView_timeStamp;
            protected ImageView imageView_articleSourceLogo;
            protected LinearLayout article_options_layout;
            protected LinearLayout article_contents_layout;
            protected ImageButton deleteImageButton;
            protected SwipeRevealLayout swipeRevealLayout;
            public ViewHolder(View itemView) {
                super(itemView);
                textView_Headline =  (TextView) itemView.findViewById(R.id.text_view_news_head);
                textView_Description =  (TextView) itemView.findViewById(R.id.text_view_news_desc);
                textView_Source =  (TextView) itemView.findViewById(R.id.text_view_news_source);
                imageView_articleSourceLogo = (ImageView) itemView.findViewById(R.id.imageview_source_logo);
                textView_timeStamp = (TextView) itemView.findViewById(R.id.text_view_news_datetime);
                article_options_layout = (LinearLayout) itemView.findViewById(R.id.article_options);
                article_contents_layout = (LinearLayout) itemView.findViewById(R.id.article_content);
                deleteImageButton = (ImageButton) itemView.findViewById(R.id.delete_imagebutton);
                swipeRevealLayout = (SwipeRevealLayout)itemView.findViewById(R.id.swipeRevealLayout);
            }

            @Override
            public void onClick(View view) {
                Log.d("RECYCLER-CLICK-EVENTS","Item Clicked at position "+getLayoutPosition());
            }

            @Override
            public boolean onLongClick(View view) {
                Log.d("RECYCLER-CLICK-EVENTS","Item Long-Clicked at position "+getLayoutPosition());
                return true;
            }
        }

    }


    public class SaveArticleTask extends AsyncTask<Void,Void,String> {

        ProgressDialog dialog = new ProgressDialog(OfflineArticlesActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            URL url = null;
            String responseString = null;

            try {
                url = new URL("https://developer.android.com/reference/java/net/HttpURLConnection.html");
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
            urlResponse = responseString;
            dialog.dismiss();
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


    }


}
