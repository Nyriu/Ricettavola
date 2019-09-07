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
import nyriu.ricettavola.models.PreparationStep;


public class PreparationStepsRecyclerAdapter extends RecyclerView.Adapter<PreparationStepsRecyclerAdapter.ViewHolder> {
    private ArrayList<PreparationStep> mPreparationSteps = new ArrayList<>();
    private OnPreparationStepListener mOnPreparationStepListener;

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

        try {
            viewHolder.step_number.setText("" + mPreparationSteps.get(i).getNumber());
            viewHolder.step_description.setText(mPreparationSteps.get(i).getDescription());
        } catch (NullPointerException e) {
            Log.e("DEBUG", "onBindViewHolder: " + e.getMessage());
        }
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView step_number;
        TextView step_description;
        OnPreparationStepListener mOnPreparationStepListener;

        public ViewHolder(@NonNull View itemView, @NonNull OnPreparationStepListener onPreparationStepListener) {
            super(itemView);
            step_number = itemView.findViewById(R.id.step_number);
            step_description = itemView.findViewById(R.id.step_description);
            this.mOnPreparationStepListener = onPreparationStepListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnPreparationStepListener.onPreparationStepClick(getAdapterPosition());
        }
    }

    public interface OnPreparationStepListener{
        void onPreparationStepClick(int position);
    }
}
