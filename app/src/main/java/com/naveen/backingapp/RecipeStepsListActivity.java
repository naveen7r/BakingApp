package com.naveen.backingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.dto.StepsItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeStepsListActivity extends AppCompatActivity {


    private boolean mTwoPane;

    @BindView(R.id.item_list)
    View recyclerView;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        unbinder = ButterKnife.bind(this);

        Bundle bn = getIntent().getExtras();

        Recipes recipes = null;

        if(bn != null){
            if(bn.containsKey(getString(R.string.recipe))){
                recipes = (Recipes) bn.getParcelable(getString(R.string.recipe));
            }
        }


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, recipes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, Recipes recipes) {
        ReceipeDetailsAdapter receipeDetailsAdapter = new ReceipeDetailsAdapter(this, recipes, mTwoPane);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(receipeDetailsAdapter);
    }

    public static class ReceipeDetailsAdapter
            extends RecyclerView.Adapter<ReceipeDetailsAdapter.ViewHolder> {

        private final RecipeStepsListActivity mParentActivity;
        private final Recipes recipes;
        private final boolean mTwoPane;
        private boolean hasReceipeIngreidient;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());

                onItemSelected(position);
            }
        };

        private void onItemSelected(int position){
            int pos = position;
            if(hasReceipeIngreidient){
                pos--;
            }


            StepsItem stepsItem = null;

            if(position == 0){
                if(!hasReceipeIngreidient){
                    stepsItem = recipes.getSteps().get(pos);
                }
            } else {
                stepsItem = recipes.getSteps().get(pos);
            }


            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(mParentActivity.getString(R.string.ingredi), recipes);
                arguments.putParcelable(mParentActivity.getString(R.string.step), stepsItem);
                arguments.putBoolean(mParentActivity.getString(R.string.isTwoPane), mTwoPane);

                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(mParentActivity, ItemDetailActivity.class);
                intent.putExtra(mParentActivity.getString(R.string.ingredi), recipes);
                intent.putExtra(mParentActivity.getString(R.string.step), stepsItem);
                intent.putExtra(mParentActivity.getString(R.string.position), position);
                intent.putExtra(mParentActivity.getString(R.string.hasingredient), hasReceipeIngreidient);
                intent.putExtra(mParentActivity.getString(R.string.isTwoPane), mTwoPane);

                mParentActivity.startActivity(intent);
            }
        }

        ReceipeDetailsAdapter(RecipeStepsListActivity parent,
                              Recipes recipes,
                              boolean twoPane) {
            this.recipes = recipes;
            mParentActivity = parent;
            mTwoPane = twoPane;
            hasReceipeIngreidient = (recipes != null && recipes.getIngredients() != null &&  recipes.getIngredients().size() > 0);
            if(mTwoPane){
                onItemSelected(0);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            int pos = position;
            if(hasReceipeIngreidient){
                pos--;
            }
            if(position == 0 ) {
                if(hasReceipeIngreidient) {
                    holder.tvStepName.setText(mParentActivity.getString(R.string.recipe_ingredients));
                } else {
                    holder.tvStepName.setText(recipes.getSteps().get(pos).getShortDescription());
                }
            } else {
                holder.tvStepName.setText(recipes.getSteps().get(pos).getShortDescription());
            }

            holder.itemView.setTag(position + "");
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if(recipes != null && recipes.getIngredients() != null && recipes.getIngredients().size() > 0 && recipes.getSteps() != null){
                return 1 + recipes.getSteps().size();
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvStepName)
            TextView tvStepName;


            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }
    }
}
