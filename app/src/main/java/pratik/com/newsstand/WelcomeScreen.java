package pratik.com.newsstand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        Boolean set = pref.getBoolean("PREFERENCES SET",false);
        //System.out.println("Welcome-Pref Set? "+set);
    }

    public void showLibrary(View v){
        //Intent intent = new Intent(WelcomeScreen.this,Library.class);
        Intent intent = new Intent(WelcomeScreen.this,Library.class);
        startActivity(intent);
        finish();
    }
}
