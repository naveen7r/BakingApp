package com.naveen.backingapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naveen.backingapp.R;
import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.listener.RecipesListener;
import com.naveen.backingapp.widget.ReceipeService;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeHolder> {

    private Recipes[] recipes;
    private RecipesListener recipesListener;

    public RecipeListAdapter(Recipes[] recipes, RecipesListener recipesListener) {
        this.recipes = new WeakReference<Recipes[]>(recipes).get();
        this.recipesListener = new WeakReference<RecipesListener>(recipesListener).get();
    }


    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recipe_item, null);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, final int position) {
        if (position < recipes.length) {
            final Recipes recipe = recipes[position];
            holder.tvRecipeName.setText(recipe.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipesListener != null) {
                        ReceipeService.startActionEventCount(v.getContext(), position);
                        recipesListener.navigateToRecipes(recipe);
                    }
                    SharedPreferences sharedPref = v.getContext().getSharedPreferences(
                            v.getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(v.getContext().getString(R.string.position), position);
                    editor.commit();

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;
        return recipes.length;
    }

    class RecipeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvRecipeName)
        TextView tvRecipeName;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
