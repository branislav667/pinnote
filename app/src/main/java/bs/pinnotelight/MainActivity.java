package bs.pinnotelight;

/*
Allows you to create one note and pin to home screen and notification drawer.
created by Branislav Sivcev
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText titleEditText, noteEditText, subtextEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ask for permission to post notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        new PinNote().createNotificationChannel(this);

        setContentView(R.layout.activity_pin_note);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Find all views
        titleEditText = findViewById(R.id.titleEditText);
        noteEditText = findViewById(R.id.noteEditText);
        subtextEditText = findViewById(R.id.subtextEditText);

        titleEditText.setText(sharedPreferences.getString("title", ""));
        noteEditText.setText(sharedPreferences.getString("note", ""));
        subtextEditText.setText(sharedPreferences.getString("subtext", ""));

        noteEditText.requestFocus();

        //Pin button
        FloatingActionButton pinFAB = findViewById(R.id.pinFAB);
        pinFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                sendBroadcast(new Intent(MainActivity.this, BroadcastReceiver.class).setAction("PIN"));
                finish();
            }
        });

        //Promo button
        ImageButton moreButton = findViewById(R.id.more_button);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=branislav667.pinnote"));
                startActivity(intent);
            }
        });

    }

    private void save(){
        sharedPreferences.edit().putString("title", titleEditText.getText().toString()).apply();
        sharedPreferences.edit().putString("note", noteEditText.getText().toString()).apply();
        sharedPreferences.edit().putString("subtext", subtextEditText.getText().toString()).apply();
    }

}