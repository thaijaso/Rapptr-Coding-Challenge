package com.datechnologies.androidtest.animation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    private int windowWidth;
    private int windowHeight;

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

        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        addTouchHandler();
    }

    /**
     * This method allows the logo to be moved on touch.
     */
    private void addTouchHandler() {
        ImageView logo = findViewById(R.id.logo);

        // https://stackoverflow.com/questions/3669239/how-to-drag-an-image-by-touching-in-android
        logo.setOnTouchListener(new View.OnTouchListener() {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) logo.getLayoutParams();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int xCordinate = (int) event.getRawX();
                        int yCoordinate = (int) event.getRawY();

                        if (xCordinate > windowWidth) {
                            xCordinate = windowWidth;
                        }
                        if (yCoordinate > windowHeight) {
                            yCoordinate = windowHeight;
                        }

                        layoutParams.leftMargin = xCordinate;
                        layoutParams.topMargin = yCoordinate;

                        logo.setLayoutParams(layoutParams);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
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
