package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {
    Tweet tweet;
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    ImageView ivTweetPic;
    TextView tvName;
    TextView tvDisplayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody = findViewById(R.id.tvDetailBody);
        ivProfileImage = findViewById(R.id.ivDetailProfile);
        tvScreenName = findViewById(R.id.tvDetailScreenName);
        tvName = findViewById(R.id.tvDetailName);
        tvDisplayDate = findViewById(R.id.tvDetailCreatedAt);
        ivTweetPic = findViewById(R.id.ivDetailTweetPic);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(65));
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .apply(requestOptions)
                .into(ivProfileImage);

        tvBody.setText(tweet.body);
        tvScreenName.setText("@"+tweet.user.screenName);
        tvName.setText(tweet.user.name);
        tvDisplayDate.setText(tweet.createdAt);
        if (tweet.imageUrl  != null){
            ivTweetPic.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.imageUrl)
                    .apply(requestOptions)
                    .into(ivTweetPic);
        }
        else{
            ivTweetPic.setVisibility(View.GONE);
        }

    }
}