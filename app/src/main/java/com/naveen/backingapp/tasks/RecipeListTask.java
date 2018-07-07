package com.naveen.backingapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.listener.EventsListener;
import com.naveen.backingapp.utils.NetworkHelper;
import com.naveen.backingapp.utils.ProgressBarHandler;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;

public class RecipeListTask extends AsyncTask<String, Void, String> {

    private ProgressBarHandler progressBarHandler;
    private EventsListener eventsListener;
    private HashMap<String, String> parms;

    public RecipeListTask(Activity activity, EventsListener eventsListener, HashMap<String, String> parms) {
        this.eventsListener = new WeakReference<EventsListener>(eventsListener).get();
        Activity activity1 = new WeakReference<Activity>(activity).get();
        this.progressBarHandler = new ProgressBarHandler(activity1);
        this.parms = new WeakReference<HashMap<String, String>>(parms).get();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressBarHandler != null){
            progressBarHandler.show();
        }
    }



    @Override
    protected String doInBackground(String... strings) {
        if (strings.length > 0) {
            String urlString = strings[0];

            URL url = NetworkHelper.constructURL(urlString);
            try {

                return NetworkHelper.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(progressBarHandler != null){
            progressBarHandler.hide();
        }

        Gson gson = new Gson();
        Recipes[] events = gson.fromJson(s, Recipes[].class);


//        JsonArray jsonArray = (JsonArray) new JsonParser().parse(s);

        if(eventsListener != null){
            eventsListener.onSuccess(events);
        }

    }

    @Override
    protected void onCancelled() {
        if(progressBarHandler != null){
            progressBarHandler.hide();
        }
        super.onCancelled();

    }
}
