package nyriu.ricettavola.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import nyriu.ricettavola.R;
import nyriu.ricettavola.models.Recipe;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecipesRecyclerAdapter.ViewHolder> {
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private OnRecipeListener mOnRecipeListener;

    public RecipesRecyclerAdapter (ArrayList<Recipe> recipes, @NonNull OnRecipeListener onRecipeListener) {
        this.mRecipes = recipes;
        this.mOnRecipeListener = onRecipeListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipes_recyclerview_item, viewGroup, false);
        return new ViewHolder(view, mOnRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        try {
            viewHolder.title.setText(mRecipes.get(i).getTitle());

            Uri imageUri = mRecipes.get(i).getImageUri();
            if (imageUri.equals(Recipe.DEFAULT_IMAGE_URI)) {
                viewHolder.recipe_image.setAlpha((float) 0.3);
            } else {
                viewHolder.recipe_image.setAlpha((float) 1);
            }
            viewHolder.recipe_image.setImageURI(imageUri);
        } catch (NullPointerException e) {
            Log.e("DEBUG", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //return mRecipes.size();
        return this.mRecipes.size();
    }







    /**
     * TODO describe
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView title;
        AppCompatImageView recipe_image;
        OnRecipeListener mOnRecipeListener;

        public ViewHolder(@NonNull View itemView, @NonNull OnRecipeListener onRecipeListener) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            recipe_image = itemView.findViewById(R.id.recipe_image);
            this.mOnRecipeListener = onRecipeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnRecipeListener.onRecipeClick(getAdapterPosition());
        }
    }

    public interface OnRecipeListener{
        void onRecipeClick(int position);
    }
}
