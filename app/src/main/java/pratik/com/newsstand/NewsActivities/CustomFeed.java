package pratik.com.newsstand.NewsActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pratik.com.newsstand.R;

public class CustomFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_feed);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        Boolean set = pref.getBoolean("PREFERENCES SET",false);
        //System.out.println("Before Reset-Pref Set? "+set);
    }


    public void resetPreferences(View v){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("PREFERENCES SET",false);
        editor.commit();
        //System.out.println("After Reset-Pref Set? "+ pref.getBoolean("PREFERENCES SET",false));
    }

    public void showPreferenceScreen(View v){
        Intent intent = new Intent(CustomFeed.this,PreferenceInput.class);
        startActivity(intent);
    }


}
