package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nyriu.ricettavola.R;
import nyriu.ricettavola.models.Recipe;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecipesRecyclerAdapter.ViewHolder> {
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    //private OnNoteListener mOnNoteListener;

    public RecipesRecyclerAdapter (ArrayList<Recipe> recipes) {
        this.mRecipes = recipes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipes_recyclerview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    private static final String TAG = "NotesRecyclerAdapter";
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        try {
            viewHolder.title.setText(mRecipes.get(i).getTitle());
        } catch (NullPointerException e) {
            //Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //return mRecipes.size();
        return 3;
    }







    /**
     * TODO describe
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
        }
    }

}
