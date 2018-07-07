package com.naveen.backingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naveen.backingapp.adapter.RecipeListAdapter;
import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.listener.EventsListener;
import com.naveen.backingapp.listener.RecipesListener;
import com.naveen.backingapp.tasks.RecipeListTask;
import com.naveen.backingapp.utils.NetworkHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RecipeListActivity extends AppCompatActivity implements RecipesListener {

    @BindView(R.id.rvRecipeList)
    RecyclerView recyclerView;

    @BindView(R.id.rlNoNetwork)
    RelativeLayout rlNoNetwork;

    @BindView(R.id.bTryAgain)
    Button bTryAgain;

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        unbinder = ButterKnife.bind(this);


        fetchResult();


    }

    private void fetchResult(){
        if(NetworkHelper.isNetworkAvailable(this)) {
            RecipeListTask recipeListTask = new RecipeListTask(this, new EventsListener<Recipes[]>() {
                @Override
                public void onSuccess(Recipes[] recipeList) {
                    if (recipeList != null) {
                        populateList(recipeList);
                    }
                }
            }, null);
            recipeListTask.execute(NetworkHelper.BAKING_URL);
            rlNoNetwork.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, getString(R.string.no_network_availble), Toast.LENGTH_LONG).show();
            rlNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.bTryAgain)
    public void onTryAgain(){
        fetchResult();
    }

    private void populateList(Recipes[] recipeList) {
        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(recipeList, this);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recipeListAdapter);
    }

    @Override
    public void navigateToRecipes(Recipes recipe) {
        Intent intent = new Intent(this, RecipeStepsListActivity.class);
        intent.putExtra(getString(R.string.recipe), recipe);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }
}
