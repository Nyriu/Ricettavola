package nyriu.ricettavola.adapters;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import Database.DatabaseShoppingListIngredient;
import nyriu.ricettavola.R;


/**
 * Adapter per gestire le gli ingredienti nella lista della spesa
 * Ogni elemento presenta due stati: barratto o non-barrato
 * Lo stato dei ViewHolder viene modificato tramite un checkbox
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ViewHolder> {

    private ArrayList<DatabaseShoppingListIngredient> mShoppingList;
    private OnShoppingListListener mOnShoppingListListener;

    public ShoppingListRecyclerAdapter(ArrayList<DatabaseShoppingListIngredient> shoppingList, @NonNull OnShoppingListListener onShoppingListListener) {
        this.mShoppingList = shoppingList;
        this.mOnShoppingListListener = onShoppingListListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_shoppinglist_recyclerview_item, viewGroup, false);
        return new ViewHolder(view, mOnShoppingListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setTitle(mShoppingList.get(i).getIngredient());
        viewHolder.setBought(mShoppingList.get(i).isBought());
    }

    @Override
    public int getItemCount() {
        return this.mShoppingList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private CheckBox checkbox;
        private boolean bought;

        private OnShoppingListListener mOnShoppingListListener;



        public ViewHolder(@NonNull View itemView, @NonNull OnShoppingListListener onShoppingListListener) {
            super(itemView);
            this.mOnShoppingListListener = onShoppingListListener;
            title     = itemView.findViewById(R.id.ingredient_content);
            checkbox  = itemView.findViewById(R.id.checkbox_bought);
            checkbox.setOnClickListener(this);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setBought(boolean b) {
            this.bought = b;
            checkbox.setChecked(b);
            if (b){
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        @Override
        public void onClick(View v) {
            setBought(!bought);
            this.mOnShoppingListListener.onShoppingListIngredientClick(getAdapterPosition());
        }
    }

    public interface OnShoppingListListener{
        void onShoppingListIngredientClick(int position);
    }
}
