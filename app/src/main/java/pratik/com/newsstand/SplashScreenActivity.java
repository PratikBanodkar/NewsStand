package pratik.com.newsstand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pratik.com.newsstand.NewsActivities.CustomFeed;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
                Boolean set = pref.getBoolean("PREFERENCES SET",false);
                System.out.println("Splash-Pref Set? "+set);
                if(set){
                    Intent i = new Intent(SplashScreenActivity.this, CustomFeed.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(SplashScreenActivity.this, WelcomeScreen.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
