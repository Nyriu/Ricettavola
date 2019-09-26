package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

import nyriu.ricettavola.R;


/**
 * Adapter per gestire gli ingredienti di una ricetta
 * In modalita' edit gli elementi posono essere rimossi
 */
public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> implements
    EditableRecyclerAdapter {

    private boolean mEditMode;

    private ArrayList<String> mIngredients;
    private OnIngredientListener mOnIngredientListener;

    public IngredientsRecyclerAdapter(ArrayList<String> ingredients, @NonNull OnIngredientListener onIngredientListener) {
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
        viewHolder.setTitle(mIngredients.get(i));
        viewHolder.setEditMode(isEditMode());
        viewHolder.mIngredientsRecyclerAdapter = this;
    }

    @Override
    public int getItemCount() {
        return this.mIngredients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageButton delete_button;
        IngredientsRecyclerAdapter mIngredientsRecyclerAdapter;

        boolean editMode = false;

        public ViewHolder(@NonNull View itemView, @NonNull OnIngredientListener onIngredientListener) {
            super(itemView);
            title         = itemView.findViewById(R.id.ingredient_content);
            delete_button = itemView.findViewById(R.id.delete_button);

            delete_button.setOnClickListener(this);
        }

        public void setTitle(String s) {
            title     .setText(s);
        }

        public boolean isEditMode() {
            return editMode;
        }

        public void setEditMode(boolean editMode) {
            this.editMode = editMode;
            if (editMode) {
                putEditModeOn();
            } else {
                putEditModeOff();
            }
        }

        public void putEditModeOn() {
            this.editMode = true;
            delete_button.setVisibility(View.VISIBLE);
        }

        public void putEditModeOff() {
            this.editMode = false;
            delete_button.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_button:{
                    int position = getAdapterPosition();
                    mIngredients.remove(position);
                    mIngredientsRecyclerAdapter.notifyItemRemoved(position);
                    break;
                }
            }
        }
    }

    public interface OnIngredientListener{
        void onIngredientClick(int position);
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

}
