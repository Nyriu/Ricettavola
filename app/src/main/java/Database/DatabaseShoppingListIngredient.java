package Database;


import android.net.Uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nyriu.ricettavola.R;

public class DatabaseShoppingListIngredient {

    public final static String ID_FIELD               = "id";
    public final static String INGREDIENT_FIELD       = "ingredient";
    public final static String BOUGHT_FIELD           = "bought";

    public final static int COLUMN_ID_FIELD           = 0;
    public final static int COLUMN_INGREDIENT_FIELD   = 1;
    public final static int COLUMN_BOUGHT_FIELD       = 2;



    private int id;
    private String ingredient;
    private boolean bought;

    public DatabaseShoppingListIngredient() {
    }

    public DatabaseShoppingListIngredient(String ingredient) {
        this.ingredient = ingredient;
        this.bought = false;
    }

    public DatabaseShoppingListIngredient(String ingredient, boolean bought) {
        this.ingredient = ingredient;
        this.bought = bought;
    }

    public DatabaseShoppingListIngredient(int id, String ingredient, boolean bought) {
        this.id = id;
        this.ingredient = ingredient;
        this.bought = bought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
