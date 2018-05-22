package pratik.com.newsstand.NewsActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import pratik.com.newsstand.R;

public class PreferenceInput extends AppCompatActivity {

    HashMap new_feedmap,old_feedmap;
    boolean savePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_input);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        savePressed = false;
    }

    public void savePreferences(View v){
        savePressed = true;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("PREFERENCES SET",true);
        editor.commit();
        //System.out.println("After Save-Pref Set? "+pref.getBoolean("PREFERENCES SET",false));
        Intent intent = new Intent(PreferenceInput.this,CustomFeed.class);
        startActivity(intent);
        finish();
    }

    public void goToAddSourcesScreen(View view) {
        Intent intent = new Intent(PreferenceInput.this,AddTopicsActivity.class);
        old_feedmap = new_feedmap;
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2){
            Log.d("Returning","Returned Now");
            Bundle b = data.getExtras();
            if(b!=null)
                new_feedmap = (HashMap) b.getSerializable("Feed Map");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if( !savePressed && hashMapChanged()){
            //User trying to ge to previous screen but didnt save his feed.
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Feed Changed")
                    .setMessage("Save changes to your feed ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            old_feedmap = new_feedmap;
                            Intent intent = new Intent(PreferenceInput.this,CustomFeed.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public boolean hashMapChanged(){
        return old_feedmap.equals(new_feedmap);
    }


}
