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
    }

    public void showOnBoarding(View v){
        Intent intent = new Intent(WelcomeScreen.this,OnBoarding.class);
        startActivity(intent);
        finish();
    }
}
