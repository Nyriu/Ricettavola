package nyriu.ricettavola.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import Database.DatabaseShoppingListIngredient;
import nyriu.ricettavola.R;


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
        viewHolder.setId(mShoppingList.get(i).getId());
        viewHolder.setTitle(mShoppingList.get(i).getIngredient());
        viewHolder.setBought(mShoppingList.get(i).isBought());
        viewHolder.mShoppingListRecyclerAdapter = this;
    }

    @Override
    public int getItemCount() {
        return this.mShoppingList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        CheckBox checkbox; // TODO

        int id;
        boolean bought;

        ShoppingListRecyclerAdapter mShoppingListRecyclerAdapter;



        public ViewHolder(@NonNull View itemView, @NonNull OnShoppingListListener onShoppingListListener) {
            super(itemView);
            title     = itemView.findViewById(R.id.ingredient_content);
            checkbox  = itemView.findViewById(R.id.checkbox_bought);

        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setBought(boolean b) {
            this.bought = b;
            if (b){
                // TODO
                // check
                // barrato
            }
            //this.title.setText(title);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkbox_bought:{
                    // TODO update on database
                    break;
                }
                ///case R.id.delete_button:{
                ///    Log.d("DEBUG", "onClick: ingredient delete button Clicked!!");
                ///    int position = getAdapterPosition();
                ///    mIngredients.remove(position);
                ///    mIngredientsRecyclerAdapter.notifyItemRemoved(position);
                ///    break;
                ///}
            }
        }
    }

    public interface OnShoppingListListener{
        void onShoppingListIngredientClick(int position);
    }
}
