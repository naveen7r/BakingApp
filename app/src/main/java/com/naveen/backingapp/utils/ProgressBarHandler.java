package com.naveen.backingapp.utils;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by Naveen on 4/15/2018.
 */

public class ProgressBarHandler {
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ViewGroup rootView;

    public ProgressBarHandler(Activity activity) {

        if (activity != null) {
            rootView = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();

            progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleInverse);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            progressBarLayout = new RelativeLayout(activity);
            progressBarLayout.setGravity(Gravity.CENTER);
            progressBarLayout.addView(progressBar);
            progressBarLayout.setLayoutParams(params);

            progressBarLayout.setClickable(true);
        }
    }

    public void show() {
        if (rootView != null) {

            if (rootView.indexOfChild(progressBarLayout) == -1) {
                rootView.addView(progressBarLayout);
            }
            progressBarLayout.bringToFront();
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Log.d("TAG", "RootView " + null);
        }
    }

    public void hide() {
        if (rootView != null) {
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (rootView.indexOfChild(progressBarLayout) != -1) {
                rootView.removeView(progressBarLayout);
            }
        } else {
            Log.d("TAG", "RootView " + null);
        }
    }

    public boolean isLayoutVisible() {
        return progressBarLayout.getVisibility() == View.VISIBLE;
    }
}
