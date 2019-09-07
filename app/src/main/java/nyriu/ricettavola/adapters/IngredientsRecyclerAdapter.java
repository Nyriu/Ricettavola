package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nyriu.ricettavola.R;
import nyriu.ricettavola.models.Ingredient;



public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {
    private ArrayList<Ingredient> mIngredients = new ArrayList<>();
    private OnIngredientListener mOnIngredientListener;

    public IngredientsRecyclerAdapter(ArrayList<Ingredient> ingredients, @NonNull OnIngredientListener onIngredientListener) {
        this.mIngredients = ingredients;
        this.mOnIngredientListener = onIngredientListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ingredients_recyclerview_item, viewGroup, false);
        return new ViewHolder(view, mOnIngredientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        try {
            viewHolder.title.setText(mIngredients.get(i).getDescription());
        } catch (NullPointerException e) {
            Log.e("DEBUG", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //return mIngredients.size();
        return this.mIngredients.size();
    }







    /**
     * TODO describe
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView title;
        OnIngredientListener mOnIngredientListener;

        public ViewHolder(@NonNull View itemView, @NonNull OnIngredientListener onIngredientListener) {
            super(itemView);
            title = itemView.findViewById(R.id.ingredient_content);
            this.mOnIngredientListener = onIngredientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnIngredientListener.onIngredientClick(getAdapterPosition());
        }
    }

    public interface OnIngredientListener{
        void onIngredientClick(int position);
    }
}
