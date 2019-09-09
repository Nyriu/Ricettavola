package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import nyriu.ricettavola.R;
import nyriu.ricettavola.RecipeActivity;
import nyriu.ricettavola.models.Ingredient;


public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> implements
    EditableRecyclerAdapter {

    private boolean mEditMode;

    private ArrayList<Ingredient> mIngredients = new ArrayList<>();
    private OnIngredientListener mOnIngredientListener;

    public RecipeActivity mRecipeActivity;

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
        viewHolder.setEditMode(isEditMode());
        viewHolder.mIngredient = mIngredients.get(i);
        viewHolder.setTitle(mIngredients.get(i).getDescription());
        viewHolder.mIngredientsRecyclerAdapter = this;
    }

    @Override
    public int getItemCount() {
        //return mIngredients.size();
        return this.mIngredients.size();
    }




    /**
     * TODO describe
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {

        TextView title;
        EditText edit_title;
        OnIngredientListener mOnIngredientListener;

        Ingredient mIngredient;
        IngredientsRecyclerAdapter mIngredientsRecyclerAdapter;



        boolean editMode = false;

        public ViewHolder(@NonNull View itemView, @NonNull OnIngredientListener onIngredientListener) {
            super(itemView);
            title      = itemView.findViewById(R.id.ingredient_content);
            edit_title = itemView.findViewById(R.id.edit_ingredient_content);

            edit_title.setSingleLine(true);
            edit_title.setImeOptions(EditorInfo.IME_ACTION_NEXT);


            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //edit_title.setOnKeyListener(new View.OnKeyListener() {
            //    public boolean onKey(View v, int keyCode, KeyEvent event) {
            //        // If the event is a key-down event on the "enter" button
            //        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
            //                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            //            // Perform action on key press
            //            Log.d("DEBUG", "onKey: return pressed");
            //            // TODO unfocus edittext e passa al prossimo
            //            //IngredientsRecyclerAdapter.ViewHolder viewHolder = (IngredientsRecyclerAdapter.ViewHolder) edit_title.getParent();
            //            Log.d("DEBUG", "getParent " + edit_title.getParent().toString());
            //            Log.d("DEBUG", "getRootView " + edit_title.getRootView().toString());
            //            Log.d("DEBUG", "nextFocusDownId " + edit_title.getNextFocusDownId());
            //            edit_title.findViewById()

            //            //viewHolder.mIngredientsRecyclerAdapter.focusNext(viewHolder.getAdapterPosition());

            //            Log.d("DEBUG", "mandi!!");
            //            return true;
            //        }
            //        return false;
            //    }
            //});
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            this.mOnIngredientListener = onIngredientListener;
            itemView.setOnClickListener(this);
        }

        public void setTitle(String s) {
            title     .setText(s);
            edit_title.setText(s);
            edit_title.addTextChangedListener(this);
            if (isEditMode()) {
                title     .setVisibility(View.GONE);
                edit_title.setVisibility(View.VISIBLE);
            } else {
                title     .setVisibility(View.VISIBLE);
                edit_title.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            //if (isEditMode()) {
            //    edit_title.hasFocus();
            //}
            mOnIngredientListener.onIngredientClick(getAdapterPosition());
        }

        public boolean isEditMode() {
            return editMode;
        }

        public void setEditMode(boolean editMode) {
            this.editMode = editMode;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        private boolean firstChange = true;
        @Override
        public void afterTextChanged(Editable s) {
            mIngredient.setDescription(String.valueOf(edit_title.getText()));
            int count = mIngredientsRecyclerAdapter.getItemCount();
            boolean isLast = this.getAdapterPosition() == count-1;

            if (firstChange && isLast) {
                Log.d("DEBUG", "Creo uno nuovo");
                mIngredients.add(new Ingredient(""));
                //mIngredientsRecyclerAdapter.mRecipeActivity.hideNShowSofKeyboard();
                //edit_title.callOnClick();
                //notifyItemInserted(count);
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
    }

    public void putEditModeOff() {
        this.mEditMode = false;
        //notifyDataSetChanged();
    }

}
