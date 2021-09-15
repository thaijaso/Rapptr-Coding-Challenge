package com.datechnologies.androidtest.animation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;

/**
 * Screen that displays the D & A Technologies logo.
 * The icon can be moved around on the screen as well as animated.
 */

public class AnimationActivity extends AppCompatActivity {

    private static final String TAG = "AnimationActivity";

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, AnimationActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        setTitle(R.string.activity_animation_title);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);

        // TODO: The user should be able to touch and drag the D & A Technologies logo around the screen.

        // TODO: Add a bonus to make yourself stick out. Music, color, fireworks, explosions!!!
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onFadeButtonClicked(View v) {
        Log.i(TAG, "fade button clicked");

        ImageView logo = findViewById(R.id.logo);

        // https://stackoverflow.com/questions/6796139/fade-in-fade-out-android-animation-in-java
        Animation fadeOutFadeIn = new AlphaAnimation(1, 0);
        fadeOutFadeIn.setDuration(1000);
        fadeOutFadeIn.setRepeatCount(1);
        fadeOutFadeIn.setRepeatMode(Animation.REVERSE);

        logo.startAnimation(fadeOutFadeIn);
    }
}
