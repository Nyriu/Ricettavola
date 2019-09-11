package nyriu.ricettavola.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SimpleOnItemTouchListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import Database.DatabaseHelper;
import Database.DatabaseRecipe;
import nyriu.ricettavola.R;


public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecipesRecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUGRecipesRecyclerAdapter";

    private ArrayList<DatabaseRecipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;
    private boolean mEditMode = false;

    public RecipesRecyclerAdapter(ArrayList<DatabaseRecipe> recipes, @NonNull OnRecipeListener onRecipeListener) {
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
        viewHolder.setEditMode(isEditMode());
        //viewHolder.setImageUri(mRecipes.get(i).getImageUri()); // TODO
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
        private AppCompatImageView recipeImageView;
        private AppCompatImageButton deleteButton;

        // vars
        private OnRecipeListener mOnRecipeListener;
        private String title;
        private Uri imageUri;
        private boolean mEditMode;
        // TODO add image Uri

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
            updateTextView();
        }

        private void updateTextView() {
            this.titleView.setText(title);
        }

        public Uri getImageUri() {
            return imageUri;
        }

        public void setImageUri(Uri uri) {
            this.imageUri = uri;
            updateImage();
        }

        private void updateImage() {
            // TODO fare funzione
            //try {
            //    Uri imageUri = mRecipes.get(i).getImageUri();
            //    if (imageUri.equals(Recipe.DEFAULT_IMAGE_URI)) {
            //        viewHolder.recipe_image.setAlpha((float) 0.3);
            //    } else {
            //        viewHolder.recipe_image.setAlpha((float) 1);
            //    }
            //    viewHolder.recipe_image.setImageURI(imageUri);
            //} catch (NullPointerException e) {
            //    Log.e("DEBUG", "onBindViewHolder: " + e.getMessage());
            //}
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
