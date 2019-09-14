package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import static Database.DatabaseRecipe.COOKING_TIME;
import static Database.DatabaseRecipe.ID_FIELD;
import static Database.DatabaseRecipe.IMAGE_URI_FIELD;
import static Database.DatabaseRecipe.INGREDIENTS_FIELD;
import static Database.DatabaseRecipe.PEOPLE_FIELD;
import static Database.DatabaseRecipe.PREPARATION_TIME_FIELD;
import static Database.DatabaseRecipe.STEPS_FIELD;
import static Database.DatabaseRecipe.TITLE_FIELD;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Database";
    public final static String DATABASE_NAME = "recipes.db";
    public static String RECIPE_TABLE_NAME = "recipes";
    public static String SHOPPING_LIST_TABLE_NAME = "shoppinglist";
    public final static int DATABASE_VERSION = 2;


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getRecipeTableQuery());
        db.execSQL(getShoppingListTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SHOPPING_LIST_TABLE_NAME);
        onCreate(db);
    }

    // RECIPE //////////////////////////////////////////////////////////////////////////////////////
    private static String getRecipeTableQuery() {
        return "CREATE TABLE \""+ RECIPE_TABLE_NAME + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"title\"\tTEXT NOT NULL,\n" +
                "\t\"image_uri\"\tTEXT NOT NULL,\n" +
                "\t\"prep_time\"\tTEXT NOT NULL,\n" +
                "\t\"cook_time\"\tTEXT NOT NULL,\n" +
                "\t\"people\"\tTEXT NOT NULL,\n" +
                "\t\"diff\"\tINTEGER NOT NULL,\n" +
                "\t\"tag\"\tTEXT NOT NULL,\n" +
                "\t\"ingredients\"\tTEXT NOT NULL,\n" +
                "\t\"steps\"\tTEXT NOT NULL\n" +
                ");";
    }

    public boolean addRecipe(DatabaseRecipe recipe) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseRecipe.TITLE_FIELD, recipe.getTitle());
        contentValues.put(DatabaseRecipe.IMAGE_URI_FIELD, convertUri(recipe.getImageUri()));
        contentValues.put(DatabaseRecipe.PREPARATION_TIME_FIELD, recipe.getPrepTime());
        contentValues.put(DatabaseRecipe.COOKING_TIME, recipe.getCookTime());
        contentValues.put(DatabaseRecipe.DIFFICULTY_FIELD, recipe.getDifficulty());
        contentValues.put(DatabaseRecipe.PEOPLE_FIELD, recipe.getPeople());
        contentValues.put(DatabaseRecipe.TAG_FIELD, convertTags(recipe.getTags()));

        contentValues.put(DatabaseRecipe.INGREDIENTS_FIELD, convertIngredients(recipe.getIngredients()));
        contentValues.put(DatabaseRecipe.STEPS_FIELD, convertSteps(recipe.getSteps()));

        Log.d(TAG, "Adding user "+ recipe.getTitle() + " to the database...");
        long result = database.insert(RECIPE_TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean deleteRecipe(int id) {

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(RECIPE_TABLE_NAME, ID_FIELD + " = ? ", new String[]{id + ""});
        return result == 1;
    }

    public boolean deleteRecipe(DatabaseRecipe recipe) {

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(RECIPE_TABLE_NAME, ID_FIELD + " = ? ", new String[]{recipe.getId() + ""});
        return result == 1;
    }

    public boolean updateRecipeTitle(int id, String newTitle) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, newTitle);

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }

        cursor.close();

        return result != -1;

    }

    public boolean updateRecipeImageUri(int id, Uri imageUri) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_URI_FIELD, convertUri(imageUri));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }

        cursor.close();

        return result != -1;

    }

    public boolean updateRecipePrepTime(int id, String preparationTime) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PREPARATION_TIME_FIELD, preparationTime);

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }
        cursor.close();
        return result != -1;
    }

    public boolean updateRecipeCookTime(int id, String cookTime) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COOKING_TIME, cookTime);

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }
        cursor.close();
        return result != -1;
    }

    public boolean updateRecipePortions(int id, String portions) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PEOPLE_FIELD, portions);

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }
        cursor.close();
        return result != -1;
    }

    public boolean updateRecipeIngredients(int id, ArrayList<String> ingredients) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INGREDIENTS_FIELD, convertIngredients(ingredients));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }
        cursor.close();
        return result != -1;
    }

    public boolean updateRecipeSteps(int id, ArrayList<String> steps) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS_FIELD, convertSteps(steps));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(RECIPE_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }
        cursor.close();
        return result != -1;
    }


    public DatabaseRecipe getRecipe(int id) {

        ArrayList<DatabaseRecipe> recipes = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 1) {
            DatabaseRecipe recipe = decodeDatabaseRecipe(cursor);
            cursor.close();
            return recipe;
        }
        Log.d(TAG, "Chiave non unica...");
        return null;
    }

    public ArrayList<DatabaseRecipe> getAllRecipes() {

        ArrayList<DatabaseRecipe> recipes = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            DatabaseRecipe recipe = decodeDatabaseRecipe(cursor);
            recipes.add(recipe);
        }
        while (cursor.moveToNext()) {
            DatabaseRecipe recipe = decodeDatabaseRecipe(cursor);
            recipes.add(recipe);
        }

        cursor.close();
        Log.d(TAG, "Registrazioni recuperate: " + recipes.size());
        return recipes;
    }



    // Formato uri: "uri"
    private String convertUri(Uri uri) {
        return uri.toString();
    }
    private Uri deconvertUri(String enc) {
        return Uri.parse(enc);
    }

    // Formato tags: "tag1;tag2;..."
    private String convertTags(Set tags) {

        StringBuilder result = new StringBuilder();
        Iterator<String> iterator = tags.iterator();
        while (iterator.hasNext()) {
            result.append(iterator.next() + ";");
        }
        return result.toString();
    }
    private Set deconvertTags(String enc) {

        Set result = new TreeSet();

        String[] tokens = enc.split(";");

        for (int i=0; i<tokens.length; i++) {
            if (!tokens[i].isEmpty()) {
                result.add(tokens[i]);
            }
        }

        return result;
    }

    // Formato ingredients: "ingredient1;ingredient2;..."
    private String convertIngredients(ArrayList<String> ingredients) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<ingredients.size(); i++) {
            result.append(ingredients.get(i) + ";");
        }
        return result.toString();
    }
    private ArrayList<String> deconvertIngreedients(String enc) {
        String[] tokens = enc.split(";");
        ArrayList<String> result = new ArrayList<>();

        for (int i=0; i<tokens.length; i++) {
            if (!tokens[i].isEmpty()) {
                result.add(tokens[i]);
            }
        }
        return result;
    }

    // Formato step: "descrizione1;descrizione2;..."
    private String convertSteps(ArrayList<String> steps) {
        StringBuilder result = new StringBuilder();

        for (int i=0; i<steps.size(); i++) {
            result.append(steps.get(i) + ";");
        }
        return result.toString();
    }
    private ArrayList<String> deconvertSteps(String enc) {
        String[] tokens = enc.split(";");
        ArrayList<String> result = new ArrayList<>();

        for (int i=0; i<tokens.length; i++) {
            if (!tokens[i].isEmpty()) {
                result.add(tokens[i]);
            }
        }
        return result;
    }


    private DatabaseRecipe decodeDatabaseRecipe(Cursor cursor) {
        //String title, String prepTime, String cookTime, String people, int difficulty, Set tags, String[] ingredients, Map<Integer, String> steps
        return new DatabaseRecipe(
                cursor.getInt(DatabaseRecipe.COLUMN_ID_FIELD),
                cursor.getString(DatabaseRecipe.COLUMN_TITLE_FIELD),
                deconvertUri(cursor.getString(DatabaseRecipe.COLUMN_IMAGE_URI_FIELD)),
                cursor.getString(DatabaseRecipe.COLUMN_PREPARATION_TIME_FIELD),
                cursor.getString(DatabaseRecipe.COLUMN_COOKING_TIME),
                cursor.getString(DatabaseRecipe.COLUMN_PEOPLE_FIELD),
                cursor.getInt(DatabaseRecipe.COLUMN_DIFFICULTY_FIELD),
                deconvertTags(cursor.getString(DatabaseRecipe.COLUMN_TAG_FIELD)),
                deconvertIngreedients(cursor.getString(DatabaseRecipe.COLUMN_INGREDIENTS_FIELD)),
                deconvertSteps(cursor.getString(DatabaseRecipe.COLUMN_STEPS_FIELD))
        );

    }

    // SHOPPING LIST ///////////////////////////////////////////////////////////////////////////////
    private static String getShoppingListTableQuery() {
        return "CREATE TABLE \""+ SHOPPING_LIST_TABLE_NAME + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"ingredient\"\tTEXT NOT NULL,\n" +
                "\t\"bought\"INTEGER NOT NULL\n" +
                ");";
    }


    public boolean addShoppingListIngredient(DatabaseShoppingListIngredient ingredient) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseShoppingListIngredient.INGREDIENT_FIELD, ingredient.getIngredient());
        contentValues.put(DatabaseShoppingListIngredient.BOUGHT_FIELD, convertBoolean(ingredient.isBought()));

        long result = database.insert(SHOPPING_LIST_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean deleteAllShoppingListIngredients() {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SHOPPING_LIST_TABLE_NAME, null, null);
        return result == 1;
    }

    public boolean deleteShoppingListIngredient(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SHOPPING_LIST_TABLE_NAME, ID_FIELD + " = ? ", new String[]{id + ""});
        return result == 1;
    }

    public boolean deleteShoppingListIngredient(DatabaseShoppingListIngredient ingredient) {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SHOPPING_LIST_TABLE_NAME, ID_FIELD + " = ? ", new String[]{ingredient.getId() + ""});
        return result == 1;
    }

    public boolean updateShoppingListIngredient(int id, String newIngredient, boolean newBought) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseShoppingListIngredient.INGREDIENT_FIELD, newIngredient);
        contentValues.put(DatabaseShoppingListIngredient.BOUGHT_FIELD, convertBoolean(newBought));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOPPING_LIST_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(SHOPPING_LIST_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }

        cursor.close();

        return result != -1;

    }


    public boolean updateShoppingListIngredient(DatabaseShoppingListIngredient ingredient) {
        int id = ingredient.getId();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseShoppingListIngredient.INGREDIENT_FIELD, ingredient.getIngredient());
        contentValues.put(DatabaseShoppingListIngredient.BOUGHT_FIELD, convertBoolean(ingredient.isBought()));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOPPING_LIST_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(SHOPPING_LIST_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }

        cursor.close();

        return result != -1;

    }
    public boolean updateShoppingListIngredient(int id, boolean newBought) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseShoppingListIngredient.BOUGHT_FIELD, convertBoolean(newBought));

        int result = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOPPING_LIST_TABLE_NAME + " WHERE " + ID_FIELD + " = " + id, null);

        if (cursor.getCount() > 1) {
            Log.d(TAG, "Chiave primaria non unica");
        } else if (cursor.getCount() == 1) {
            result = database.update(SHOPPING_LIST_TABLE_NAME, contentValues, ID_FIELD + " = ? ", new String[]{id + ""});
        } else {
            Log.d(TAG, "Error");
        }

        cursor.close();

        return result != -1;

    }

    public ArrayList<DatabaseShoppingListIngredient> getShoppingList() {

        ArrayList<DatabaseShoppingListIngredient> shoppingList= new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOPPING_LIST_TABLE_NAME, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            DatabaseShoppingListIngredient ingredient = decodeDatabaseShoppingListIngredient(cursor);
            shoppingList.add(ingredient);
        }
        while (cursor.moveToNext()) {
            DatabaseShoppingListIngredient ingredient = decodeDatabaseShoppingListIngredient(cursor);
            shoppingList.add(ingredient);
        }

        cursor.close();
        return shoppingList;
    }

    private int convertBoolean(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }
    private boolean deconvertBoolean(int enc) {
        return enc == 1;
    }

    private DatabaseShoppingListIngredient decodeDatabaseShoppingListIngredient(Cursor cursor) {
        return new DatabaseShoppingListIngredient(
                cursor.getInt(DatabaseRecipe.COLUMN_ID_FIELD),
                cursor.getString(DatabaseShoppingListIngredient.COLUMN_INGREDIENT_FIELD),
                deconvertBoolean(cursor.getInt(DatabaseShoppingListIngredient.COLUMN_BOUGHT_FIELD))
        );

    }


}
