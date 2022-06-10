package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String id;
    public long id_long;
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;
    public String displayTime;
    public Integer retweetCount;
    public Integer replyCount;
    public Integer favoriteCount;
    public Boolean retweeted;
    public Boolean favorited;
    public static final String TAG = "Tweet Class";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;



    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("retweeted_status"))
            return null;
        Tweet tweet = new Tweet();
        tweet.id = jsonObject.getString("id_str");
        tweet.id_long = jsonObject.getLong("id");
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        try {
            if(jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("type").equals("photo"))
                tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
        }
        catch (JSONException e) {
            Log.i(TAG, "No media entity "+e);
        }
        tweet.displayTime = tweet.getRelativeTimeAgo(tweet.createdAt);
        tweet.replyCount = 0;
        try {
            tweet.retweetCount = jsonObject.getJSONObject("retweeted_status").getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getJSONObject("retweeted_status").getInt("favorite_count");
        }
        catch (Exception e){
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
        }
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorited = jsonObject.getBoolean("favorited");


        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            Tweet tweet = fromJson(jsonArray.getJSONObject(i));
            if (tweet != null){

                tweets.add(fromJson(jsonArray.getJSONObject(i)));
            }
        }
        return tweets;

    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }

}
