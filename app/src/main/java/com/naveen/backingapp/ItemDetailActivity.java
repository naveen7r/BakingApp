package com.naveen.backingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.dto.StepsItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ItemDetailActivity extends AppCompatActivity {

    private int position;
    private boolean hasIngredient;
    private Recipes recipes;

    @BindView(R.id.llControlls)
    LinearLayout llControls;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        unbinder = ButterKnife.bind(this);



        toolbar.setNavigationIcon(R.mipmap.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            recipes = getIntent().getParcelableExtra(getString(R.string.ingredi));
            arguments.putParcelable(getString(R.string.ingredi), recipes);
            arguments.putParcelable(getString(R.string.step), getIntent().getParcelableExtra(getString(R.string.step)));
            boolean isTwoPane = getIntent().getBooleanExtra(getString(R.string.isTwoPane), false);
            arguments.putBoolean(getString(R.string.isTwoPane), isTwoPane);

            llControls.setVisibility(isTwoPane?View.GONE:View.VISIBLE);

            position = getIntent().getIntExtra(getString(R.string.position), 0);
            hasIngredient = getIntent().getBooleanExtra(getString(R.string.hasingredient), false);


            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position), position);
        outState.putBoolean(getString(R.string.hasingredient), hasIngredient);
        outState.putParcelable(getString(R.string.recipe_ingredients), recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(getString(R.string.position));
        hasIngredient = savedInstanceState.getBoolean(getString(R.string.hasingredient));
        recipes = savedInstanceState.getParcelable(getString(R.string.recipe_ingredients));
    }

    @OnClick(R.id.bNext)
    public void onNext(){
        int nextPosition = position + 1;

        if(hasIngredient){
            nextPosition--;
        }

        if(recipes != null && nextPosition < recipes.getSteps().size()){



            StepsItem stepsItem = null;

            if(position == 0){
                if(!hasIngredient){
                    stepsItem = recipes.getSteps().get(nextPosition);
                }
            } else {
                stepsItem = recipes.getSteps().get(nextPosition);
            }

            Bundle arguments = new Bundle();
            arguments.putParcelable(getString(R.string.ingredi), recipes);
            arguments.putParcelable(getString(R.string.step), stepsItem);
            arguments.putBoolean(getString(R.string.isTwoPane), getIntent().getBooleanExtra(getString(R.string.isTwoPane), false));



            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

            position = position + 1;

        }

    }

    @OnClick(R.id.bPrev)
    public void onPrev(){
        int nextPosition = position - 1;

        if(hasIngredient){
            nextPosition--;
        }

        if(recipes != null && nextPosition >= 0){



            StepsItem stepsItem = null;

            if(position == 0){
                if(!hasIngredient){
                    stepsItem = recipes.getSteps().get(nextPosition);
                }
            } else {
                stepsItem = recipes.getSteps().get(nextPosition);
            }

            Bundle arguments = new Bundle();
            arguments.putParcelable(getString(R.string.ingredi), recipes);
            arguments.putParcelable(getString(R.string.step), stepsItem);
            arguments.putBoolean(getString(R.string.isTwoPane), getIntent().getBooleanExtra(getString(R.string.isTwoPane), false));



            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

            position = position - 1;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }
}
