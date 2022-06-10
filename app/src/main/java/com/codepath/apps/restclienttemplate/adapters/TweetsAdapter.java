package com.codepath.apps.restclienttemplate.adapters;


//import static androidx.core.app.ActivityCompat.startActivityForResult;
//import static androidx.core.content.ContextCompat.startActivity;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.ComposeFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    Context context;
    List<Tweet> tweets;
    public final int REQUEST_CODE = 20;


    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

//pass in context and list of tweets

    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }


    //bind values on the position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //define a viewholder

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetPic;
        TextView tvName;
        TextView tvDisplayDate;
        ImageView ivRetweet;
        ImageView ivReply;
        ImageView ivLikeTweet;
        TextView tvRetweetCount;
        TextView tvReplyCount;
        TextView tvLikeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetPic = itemView.findViewById(R.id.ivTweetPic);
            tvName = itemView.findViewById(R.id.tvName);
            tvDisplayDate = itemView.findViewById(R.id.tvDisplayDate);
            ivReply = itemView.findViewById(R.id.ivReplyTweet);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivLikeTweet = itemView.findViewById(R.id.ivLikeTweet);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvReplyCount = itemView.findViewById(R.id.tvReplyCount);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);



        }

        public void bind(Tweet tweet) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(65));
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .apply(requestOptions)
                    .into(ivProfileImage);
            if (tweet.imageUrl  != null){
                ivTweetPic.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.imageUrl)
                        .apply(requestOptions)
                        .into(ivTweetPic);
            }
            else{
                ivTweetPic.setVisibility(View.GONE);
            }
            if (!tweet.retweeted) {
                Glide.with(context)
                        .load(R.drawable.ic_vector_retweet_stroke)
                        .into(ivRetweet);
            }
            else{
                Glide.with(context)
                        .load(R.drawable.ic_vector_retweet)
                        .into(ivRetweet);
            }


            if (!tweet.favorited) {
                Glide.with(context)
                        .load(R.drawable.ic_vector_heart_stroke)
                        .into(ivLikeTweet);
            }
            else{
                Glide.with(context)
                        .load(R.drawable.ic_vector_heart)
                        .into(ivLikeTweet);
            }
            tvBody.setText(tweet.body);
            tvScreenName.setText("@"+tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvDisplayDate.setText(tweet.displayTime);
            tvRetweetCount.setText(tweet.retweetCount.toString());
            tvLikeCount.setText(tweet.favoriteCount.toString());
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigate to the compose activity

                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("responding_to", "@"+tweet.user.screenName);
                    intent.putExtra("tweet_id", tweet.id);
                    context.startActivity(intent);
                }
            });

            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set color filter of con instead of switching images
                    if (!tweet.retweeted){
                        TwitterApp.getRestClient(context).retweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetsAdapter", "This should've retweeted, go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetsAdapter", throwable.toString());
                            }
                        });
                        Glide.with(context)
                                .load(R.drawable.ic_vector_retweet)
                                .into(ivRetweet);
                        int old_count = new Integer((String) tvRetweetCount.getText());
                        tvRetweetCount.setText(new Integer(old_count+1).toString());
                        tweet.retweeted = true;
                    }
                    else{
                        Glide.with(context)
                                .load(R.drawable.ic_vector_retweet_stroke)
                                .into(ivRetweet);
                        int old_count = new Integer((String) tvRetweetCount.getText());
                        tvRetweetCount.setText(new Integer(old_count-1).toString());
                        tweet.retweeted=false;
                    }
                }
            });
            ivLikeTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tweet.favorited){
                        Glide.with(context)
                                .load(R.drawable.ic_vector_heart)
                                .into(ivLikeTweet);
                        int old_count = new Integer((String) tvLikeCount.getText());
                        tvLikeCount.setText(new Integer(old_count+1).toString());
                        tweet.favorited = true;
                    }
                    else{
                        Glide.with(context)
                                .load(R.drawable.ic_vector_heart_stroke)
                                .into(ivLikeTweet);
                        int old_count = new Integer((String) tvLikeCount.getText());
                        tvLikeCount.setText(new Integer(old_count-1).toString());
                        tweet.favorited=false;
                    }
                }
            });
        }

    }
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

}
