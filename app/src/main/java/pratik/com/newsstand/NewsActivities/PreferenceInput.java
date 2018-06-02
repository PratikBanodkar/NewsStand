package pratik.com.newsstand.NewsActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pratik.com.newsstand.CustomAlertDialog;
import pratik.com.newsstand.R;

public class PreferenceInput extends AppCompatActivity {

    HashMap new_feedmap = new HashMap();
    HashMap old_feedmap = new HashMap();
    ArrayList<String> latest_sources = new ArrayList<String>();
    ArrayList<String> latest_sourceIDs = new ArrayList<String>();
    ArrayList<String> old_sources = new ArrayList<String>();

    boolean savePressed;
    boolean feedChanged;
    SharedPreferences userFeed_sharedPreferences;
    RecyclerView sources_recyclerview;
    CustomAlertDialog cd = new CustomAlertDialog();
    Switch showOnLoadSwitch;
    boolean switchStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_input);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        savePressed = false;

        Gson gson = new Gson();
        userFeed_sharedPreferences = getApplicationContext().getSharedPreferences("pratik.com.newsstand.ufsa", Context.MODE_PRIVATE);
        String storedArrayString = userFeed_sharedPreferences.getString("Source Array", null);
        boolean showFeedOnStart = userFeed_sharedPreferences.getBoolean("Show on load",false);
        Type type = new Type() {};
        if(storedArrayString != null){
            type = new TypeToken<ArrayList<String>>(){}.getType();
            old_sources = (ArrayList<String>) gson.fromJson(storedArrayString, type);
        }
        sources_recyclerview = findViewById(R.id.sources_reyclerview);
        SourceRecyclerAdapter adapter = new SourceRecyclerAdapter(old_sources);
        old_sources = adapter.sourceNames;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        sources_recyclerview.setLayoutManager(layoutManager);
        sources_recyclerview.setNestedScrollingEnabled(false);
        sources_recyclerview.setAdapter(adapter);

        showOnLoadSwitch = findViewById(R.id.showCustomOnLoad_switch);
        showOnLoadSwitch.setChecked(showFeedOnStart);

    }

    public String constructFeedURL(ArrayList<String> sourceIDs){
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        for(int i = 0; i<sourceIDs.size(); i++){
            if(i != sourceIDs.size() - 1)
                urlBuilder.append(sourceIDs.get(i)).append(",");
            else
                urlBuilder.append(sourceIDs.get(i));
        }
        urlBuilder.append("&language=en&sortBy=publishedAt&pageSize=100");
        return urlBuilder.toString();
    }


    public ArrayList<String> getUserDefinedSourceList(HashMap map){
        ArrayList<String> sources = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList arrayList = (ArrayList) pair.getValue();
            for (int i=0;i<arrayList.size();i++)
                sources.add(((LinkedTreeMap) arrayList.get(i)).get("second").toString());
            it.remove();
        }
        return sources;
    }

    public ArrayList<String> getSourceIdsFromSourceNames(ArrayList<String> sources){
        ArrayList<String> source_ids = new ArrayList<>();
        for(int i=0;i<sources.size();i++){
            String sourceName = sources.get(i);
            String sourceName_LC = sourceName.toLowerCase();
            String sourceID = sourceName_LC.replace(" ","-");
            source_ids.add(sourceID);
        }
        return source_ids;
    }

    public void savePreferences(View v){
        savePressed = true;
        switchStatus = showOnLoadSwitch.isChecked();
        SourceRecyclerAdapter adapter = (SourceRecyclerAdapter) sources_recyclerview.getAdapter();
        latest_sources = adapter.sourceNames;
        latest_sourceIDs = getSourceIdsFromSourceNames(latest_sources);
        old_sources = latest_sources;
        userFeed_sharedPreferences = getApplicationContext().getSharedPreferences("pratik.com.newsstand.ufsa", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userFeed_sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonString_of_latest_sources = gson.toJson(latest_sources);
        editor.putString("Source Array", jsonString_of_latest_sources);
        editor.putBoolean("Show on load",switchStatus);
        if(latest_sourceIDs.size() !=0)
            editor.putString("Feed URL",constructFeedURL(latest_sourceIDs));
        else
            editor.putString("Feed URL",null);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Feed settings saved successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("Feed Changed",feedChanged);
        intent.putExtras(b);
        setResult(0,intent);
        finish();
    }

    public void goToAddSourcesScreen(View view) {
        try{
            Intent intent = new Intent(PreferenceInput.this,AddTopicsActivity.class);
            Bundle b = new Bundle();
            SourceRecyclerAdapter adapter = (SourceRecyclerAdapter) sources_recyclerview.getAdapter();
            latest_sources = adapter.sourceNames;
            old_sources = latest_sources;
            b.putStringArrayList("sources list",old_sources);
            intent.putExtra("bundle",b);
            //old_feedmap = new_feedmap;
            this.startActivityForResult(intent,2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2){
            if(resultCode == 5003){
                Bundle b = data.getExtras();
                Type type = new Type() {};
                Gson gson = new Gson();
                String hashmap_to_string = "";
                type = new TypeToken<HashMap<String, ArrayList>>(){}.getType();
                hashmap_to_string = (String) b.getString("Feed Map");
                new_feedmap  = (HashMap) gson.fromJson(hashmap_to_string, type);
                latest_sources = getUserDefinedSourceList(new_feedmap);
                latest_sourceIDs = getSourceIdsFromSourceNames(latest_sources);
                if(latest_sources.equals(old_sources))
                    feedChanged = false;
                else
                    feedChanged = true;
                SourceRecyclerAdapter adapter = new SourceRecyclerAdapter(latest_sources);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                sources_recyclerview.setLayoutManager(layoutManager);
                sources_recyclerview.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onBackPressed() {
        switchStatus = showOnLoadSwitch.isChecked();
        if( !savePressed && feedChanged){
            //User trying to ge to previous screen but didnt save his feed.
            try{
                final Dialog dialog = cd.createDialog("Feed Changed !", "Seems like your feed settings have changed. Do you want to save them ?", "Yes", "No", PreferenceInput.this, R.layout.custom_alert_feed_changed);
                Button yesButton = dialog.findViewById(R.id.alert_yes);
                Button noButton = dialog.findViewById(R.id.alert_no);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SourceRecyclerAdapter adapter = (SourceRecyclerAdapter) sources_recyclerview.getAdapter();
                        latest_sources = adapter.sourceNames;
                        latest_sourceIDs = getSourceIdsFromSourceNames(latest_sources);
                        old_sources = latest_sources;
                        userFeed_sharedPreferences = getApplicationContext().getSharedPreferences("pratik.com.newsstand.ufsa", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userFeed_sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonString_of_latest_sources = gson.toJson(latest_sources);
                        editor.putString("Source Array", jsonString_of_latest_sources);
                        editor.putBoolean("Show on load", switchStatus);
                        if(latest_sourceIDs.size()!=0)
                            editor.putString("Feed URL",constructFeedURL(latest_sourceIDs));
                        else
                            editor.putString("Feed URL",null);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Feed settings saved successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putBoolean("Feed Changed",feedChanged);
                        intent.putExtras(b);
                        setResult(0,intent);
                        finish();
                        PreferenceInput.super.onBackPressed();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putBoolean("Feed Changed",feedChanged);
                        intent.putExtras(b);
                        setResult(0,intent);
                        finish();
                        PreferenceInput.super.onBackPressed();
                    }
                });
                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putBoolean("Feed Changed",feedChanged);
            intent.putExtras(b);
            setResult(0,intent);
            finish();
        }
    }


    public class SourceRecyclerAdapter extends RecyclerView.Adapter<SourceRecyclerAdapter.ViewHolder>{

        private ArrayList<String> sourceNames;
        private ArrayList<String> sourceNames_partialDelete = new ArrayList<>();

        public SourceRecyclerAdapter(ArrayList<String> sourceNames){
            this.sourceNames = sourceNames;
        }

        @Override
        public SourceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_source_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SourceRecyclerAdapter.ViewHolder holder, int position) {
            holder.textView_SourceName.setText(sourceNames.get(holder.getAdapterPosition()));
            holder.imageButton_deleteSource.setImageResource(R.drawable.ic_delete_black);

            holder.imageButton_deleteSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourceNames_partialDelete.add(sourceNames.get(holder.getAdapterPosition()));
                    final int index_of_removed_source = holder.getAdapterPosition();
                    final String removed_source = sourceNames.remove(holder.getAdapterPosition());
                    feedChanged = true;
                    Snackbar snackbar = Snackbar
                            .make(sources_recyclerview, removed_source+" removed", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    feedChanged = false;
                                    sourceNames.add(index_of_removed_source, removed_source);
                                    notifyItemInserted(index_of_removed_source);
                                    sources_recyclerview.scrollToPosition(index_of_removed_source);
                                    sourceNames_partialDelete.remove(removed_source);
                                }
                            });
                    snackbar.show();
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return sourceNames.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{
            protected TextView textView_SourceName;
            protected ImageButton imageButton_deleteSource;

            public ViewHolder(View itemView) {
                super(itemView);
                textView_SourceName = itemView.findViewById(R.id.sourceName_textview);
                imageButton_deleteSource = itemView.findViewById(R.id.deleteSource_imagebutton);
            }
        }
    }


}
