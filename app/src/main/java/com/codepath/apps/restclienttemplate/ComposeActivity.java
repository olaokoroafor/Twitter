package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;
    public static final int MAX_TWEET_LENGTH = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        //set click listner on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make an API call to Twitter to publish the tweet
                String tweetContent = etCompose.getText().toString();

                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this,
                            "Sorry, your tweet cannot be empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this,
                            "Sorry, your tweet cannot be over 140 characters",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this,
                        tweetContent,
                        Toast.LENGTH_LONG).show();


            }
        });


    }
}