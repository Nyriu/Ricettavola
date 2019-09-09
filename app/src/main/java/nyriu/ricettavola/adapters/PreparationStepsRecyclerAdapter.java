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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import nyriu.ricettavola.R;
import nyriu.ricettavola.RecipeActivity;
import nyriu.ricettavola.models.Ingredient;
import nyriu.ricettavola.models.PreparationStep;


public class PreparationStepsRecyclerAdapter extends RecyclerView.Adapter<PreparationStepsRecyclerAdapter.ViewHolder> {
    private ArrayList<PreparationStep> mPreparationSteps = new ArrayList<>();
    private OnPreparationStepListener mOnPreparationStepListener;

    private boolean mEditMode;

    public PreparationStepsRecyclerAdapter(ArrayList<PreparationStep> preparationSteps, @NonNull OnPreparationStepListener onPreparationStepListener) {
        this.mPreparationSteps = preparationSteps;
        this.mOnPreparationStepListener = onPreparationStepListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_preparationsteps_recyclerview_item, viewGroup, false);
        return new ViewHolder(view, mOnPreparationStepListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.step_number.setText("" + mPreparationSteps.get(i).getNumber());
        viewHolder.step_description.setText(mPreparationSteps.get(i).getDescription());
        viewHolder.mPreparationStepsRecyclerAdapter = this;
    }

    @Override
    public int getItemCount() {
        //return mIngredients.size();
        Log.d("DEBUG", "mPreparationSteps len " + mPreparationSteps.size());
        return this.mPreparationSteps.size();
    }



    /**
     * TODO describe
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView step_number;
        TextView step_description;
        PreparationStepsRecyclerAdapter mPreparationStepsRecyclerAdapter;
        boolean editMode = false;

        ImageButton delete_button;

        public ViewHolder(@NonNull View itemView, @NonNull OnPreparationStepListener onPreparationStepListener) {
            super(itemView);
            step_number = itemView.findViewById(R.id.step_number);
            step_description = itemView.findViewById(R.id.step_description);

            delete_button = itemView.findViewById(R.id.delete_button);
            delete_button.setOnClickListener(this);
        }

        public void setdescription(String s) {
            step_description.setText(s);
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
                    Log.d("DEBUG", "onClick: ingredient delete button Clicked!!");
                    int position = getAdapterPosition();
                    mPreparationSteps.remove(position);
                    mPreparationStepsRecyclerAdapter.notifyItemRemoved(position);
                    break;
                }
            }
        }
    }

    public interface OnPreparationStepListener{
        void onPreparationStepClick(int position);
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
