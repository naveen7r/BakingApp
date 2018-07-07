package com.naveen.backingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.naveen.backingapp.R;
import com.naveen.backingapp.dto.IngredientsItem;
import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.utils.NetworkHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Naveen on 6/19/2018.
 */

public class ReceipeService extends IntentService {


    public static final String ACTION_EVENT_COUNT =
            "com.naveen.meetpoint.eventcount";


    public ReceipeService() {
        super("ReceipeService");
    }


    public static void startActionEventCount(Context context, int pos) {
        Intent intent = new Intent(context, ReceipeService.class);
        intent.putExtra(context.getString(R.string.position), pos);
        intent.setAction(ACTION_EVENT_COUNT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle bn = intent.getExtras();
            if (bn != null) {
                int pos = 0;
                if (bn.containsKey(getString(R.string.position))) {
                    pos = bn.getInt(getString(R.string.position));
                }
                if (ACTION_EVENT_COUNT.equals(action)) {
                    handleActionRecipe(pos);
                }
            }
        }
    }

    private void handleActionRecipe(int pos) {


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ReceipeWidget.class));



        try {
            URL url = new URL(NetworkHelper.BAKING_URL);
            String response = NetworkHelper.getResponseFromHttpUrl(url);
            Gson gson = new Gson();
            Recipes[] recipes = gson.fromJson(response, Recipes[].class);


            if (recipes != null && pos < recipes.length && recipes[pos].getIngredients() != null) {
                ReceipeWidget.onUpdateMeetPointWidget(this, appWidgetManager, appWidgetIds, recipes[pos].getName() + "\n" + getRecipies(recipes[pos]));
            } else {
                ReceipeWidget.onUpdateMeetPointWidget(this, appWidgetManager, appWidgetIds, getString(R.string.no_details));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String getRecipies(Recipes recipes) {
        if (recipes != null) {
            if (recipes.getIngredients() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                int index = 1;
                for (IngredientsItem ingredientsItem : recipes.getIngredients()) {
//                    stringBuilder.append(index);
//                    stringBuilder.append(" - ");
                    stringBuilder.append(ingredientsItem.getIngredient());
                    stringBuilder.append(" ");
                    stringBuilder.append(ingredientsItem.getQuantity());
                    stringBuilder.append(" ");
                    stringBuilder.append(ingredientsItem.getMeasure());
                    stringBuilder.append("\n");
                    index++;
                }
                return stringBuilder.toString();
            }
        }
        return "";
    }


}
