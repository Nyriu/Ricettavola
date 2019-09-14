package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import nyriu.ricettavola.R;
import nyriu.ricettavola.RecipeActivity;
import nyriu.ricettavola.util.ItemTouchHelperAdapter;


public class PreparationStepsRecyclerAdapter extends RecyclerView.Adapter<PreparationStepsRecyclerAdapter.ViewHolder> implements
        ItemTouchHelperAdapter,
        EditableRecyclerAdapter {

    private ArrayList<String> mPreparationSteps;
    private OnPreparationStepListener mOnPreparationStepListener;
    private ItemTouchHelper mItemTouchHelper;

    private boolean mEditMode;

    public PreparationStepsRecyclerAdapter(ArrayList<String> preparationSteps, @NonNull OnPreparationStepListener onPreparationStepListener) {
        Log.d("DEBUG", "PreparationStepsRycAdap constructor before " + mPreparationSteps);
        this.mPreparationSteps = preparationSteps;
        this.mOnPreparationStepListener = onPreparationStepListener;
        Log.d("DEBUG", "PreparationStepsRycAdap constructor after" + mPreparationSteps);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_preparationsteps_recyclerview_item, viewGroup, false);
        return new ViewHolder(view, mOnPreparationStepListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setEditMode(isEditMode());
        Log.d("DEBUG", "PreparationStepsRycAdap onBind after" + mPreparationSteps);
        viewHolder.step_number.setText("" + (i+1));
        viewHolder.step_description.setText(mPreparationSteps.get(i));
        viewHolder.mPreparationStepsRecyclerAdapter = this;
        //viewHolder.setItemTouchHelper(this.mItemTouchHelper);
        Log.d("DEBUG", "PreparationStepsRycAdap onBind after" + mPreparationSteps);
    }

    @Override
    public int getItemCount() {
        //return mIngredients.size();
        Log.d("DEBUG", "mPreparationSteps len " + mPreparationSteps.size());
        return this.mPreparationSteps.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Log.d("DEBUG", "fromPosition " + fromPosition + " toPosition " + toPosition);
        Log.d("DEBUG", "mPreparationStep lens " + this.mPreparationSteps.size());
        //PreparationStep p = mPreparationSteps.get(fromPosition);
        //mPreparationSteps.remove(p);
        //mPreparationSteps.add(toPosition, p);
        //notifyItemMoved(fromPosition, toPosition);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.mItemTouchHelper = itemTouchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView step_number;
        TextView step_description;
        PreparationStepsRecyclerAdapter mPreparationStepsRecyclerAdapter;
        boolean editMode = false;

        GestureDetector mGestureDetector;

        ImageButton delete_button;

        public ViewHolder(@NonNull View itemView, @NonNull OnPreparationStepListener onPreparationStepListener) {
            super(itemView);
            step_number = itemView.findViewById(R.id.step_number);
            step_description = itemView.findViewById(R.id.step_description);

            delete_button = itemView.findViewById(R.id.delete_button);
            delete_button.setOnClickListener(this);

            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);

            if (isEditMode()) {
                putEditModeOn();
            }
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
                    //mPreparationStepsRecyclerAdapter.notifyItemRemoved(position);
                    mPreparationStepsRecyclerAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("DEBUG", "onLongPress: mItemTouchHelper " + mItemTouchHelper);
            Log.d("DEBUG", "onLongPress: this " + this);
            Log.d("DEBUG", "onLongPress mPreparationSteps " + mPreparationSteps);
            mItemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //mGestureDetector.onTouchEvent(event); // uncomment to move items up and down
            return true;
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
