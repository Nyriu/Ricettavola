package nyriu.ricettavola.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import Database.DatabaseRecipe;

import nyriu.ricettavola.R;


/**
 * Adapter per gestire le ricette raccolte
 * In modalita' edit gli elementi posono essere rimossi
 */
public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecipesRecyclerAdapter.ViewHolder> implements
        EditableRecyclerAdapter {


    private static final String TAG = "DEBUGRecipesRecyclerAdapter";

    private ArrayList<DatabaseRecipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;
    private boolean mEditMode = false;

    public RecipesRecyclerAdapter(ArrayList<DatabaseRecipe> recipes, @NonNull OnRecipeListener onRecipeListener) {
        //this.mRecipes = recipes;
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
        viewHolder.setTitle(mRecipes.get(i).getTitle());
        viewHolder.setTags(mRecipes.get(i).getTags());
        viewHolder.setEditMode(isEditMode());
        viewHolder.setImageUri(mRecipes.get(i).getImageUri());
    }


    @Override
    public int getItemCount() {
        return this.mRecipes.size();
    }



    public boolean isEditMode() {
        return this.mEditMode;
    }

    public void putEditModeOn() {
        this.mEditMode = true;
        notifyDataSetChanged();
    }

    public void putEditModeOff() {
        this.mEditMode = false;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {


        // UI
        private TextView titleView;
        private TextView tagsView;
        private ImageView recipeImageView;
        private AppCompatImageButton deleteButton;

        // vars
        private OnRecipeListener mOnRecipeListener;
        private String title;
        private Set tags;
        private Uri imageUri;
        private boolean mEditMode;

        public ViewHolder(@NonNull View itemView, @NonNull OnRecipeListener onRecipeListener) {
            super(itemView);

            titleView       = itemView.findViewById(R.id.recipe_title);
            tagsView        = itemView.findViewById(R.id.tags);
            recipeImageView = itemView.findViewById(R.id.recipe_image);
            deleteButton    = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(this);

            this.mOnRecipeListener = onRecipeListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            this.titleView.setText(title);
        }

        public Set getTags() {
            return tags;
        }

        public void setTags(Set tags) {
            this.tags = tags;
            updateTagsView();
        }

        private void updateTagsView() {
            String s = "";
            for (Object tag:
                    Objects.requireNonNull(tags.toArray())) {
               s += tag.toString() + " ";
            }
            this.tagsView.setText(s);
        }

        public Uri getImageUri() {
            return imageUri;
        }

        public void setImageUri(Uri uri) {
            this.imageUri = uri;
            updateImage();
        }

        private void updateImage() {
            if (imageUri.equals(DatabaseRecipe.DEFAULT_IMAGE_URI)) {
                recipeImageView.setAlpha((float) 0.3);
            } else {
                recipeImageView.setAlpha((float) 1);
            }
            recipeImageView.setImageURI(imageUri);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_button:{
                    Log.d(TAG, "onClick: recipe delete button Clicked!!");
                    mOnRecipeListener.onRecipeDelete(getAdapterPosition());
                    break;
                }
                default:{
                    mOnRecipeListener.onRecipeClick(getAdapterPosition());
                    break;
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mOnRecipeListener.onRecipeLongPress(getAdapterPosition());
            return true;
        }

        public void setEditMode(boolean editMode) {
            if (editMode != mEditMode) {
                if (editMode) {
                    putEditModeOn();
                } else {
                    putEditModeOff();
                }
                this.mEditMode = editMode;
            }
        }

        public void putEditModeOn() {
            this.mEditMode = true;
            deleteButton.setVisibility(View.VISIBLE);
        }

        public void putEditModeOff() {
            this.mEditMode = false;
            deleteButton.setVisibility(View.GONE);
            //notifyDataSetChanged();
        }
    }

    public interface OnRecipeListener{
        void onRecipeClick(int position);
        void onRecipeDelete(int position);
        void onRecipeLongPress(int position);
    }


}
