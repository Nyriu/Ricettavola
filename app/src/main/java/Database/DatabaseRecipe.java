package Database;


import android.net.Uri;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import nyriu.ricettavola.R;

public class DatabaseRecipe {
    static public final Uri DEFAULT_IMAGE_URI =
            Uri.parse("android.resource://nyriu.ricettavola/" +
                    R.drawable.ic_insert_photo_black_24dp);


    public final static String ID_FIELD               = "id";
    public final static String TITLE_FIELD            = "title";
    public final static String IMAGE_URI_FIELD        = "image_uri";
    public final static String PREPARATION_TIME_FIELD = "prep_time";
    public final static String COOKING_TIME           = "cook_time";
    public final static String PEOPLE_FIELD           = "people";
    public final static String DIFFICULTY_FIELD       = "diff";
    public final static String TAG_FIELD              = "tag";
    public final static String INGREDIENTS_FIELD      = "ingredients";
    public final static String STEPS_FIELD            = "steps";

    public final static int COLUMN_ID_FIELD               = 0;
    public final static int COLUMN_TITLE_FIELD            = 1;
    public final static int COLUMN_IMAGE_URI_FIELD        = 2;
    public final static int COLUMN_PREPARATION_TIME_FIELD = 3;
    public final static int COLUMN_COOKING_TIME           = 4;
    public final static int COLUMN_PEOPLE_FIELD           = 5;
    public final static int COLUMN_DIFFICULTY_FIELD       = 6;
    public final static int COLUMN_TAG_FIELD              = 7;
    public final static int COLUMN_INGREDIENTS_FIELD      = 8;
    public final static int COLUMN_STEPS_FIELD            = 9;



    private int id;
    private String title;
    private Uri imageUri;
    private String prepTime;
    private String cookTime;
    private String people;
    private int difficulty;

    private Set tags;
    private ArrayList<String> ingredients;
    private ArrayList<String> steps;

    public DatabaseRecipe() {
    }

    public DatabaseRecipe(String title, Uri imageUri, String prepTime, String cookTime, String people, int difficulty, Set tags, ArrayList<String> ingredients, ArrayList<String> steps) {
        this.title = title;
        this.imageUri = imageUri;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.people = people;
        this.difficulty = difficulty;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public DatabaseRecipe(int id, String title, Uri imageUri, String prepTime, String cookTime, String people, int difficulty, Set tags, ArrayList<String> ingredients, ArrayList<String> steps) {
        this.id = id;
        this.title = title;
        this.imageUri = imageUri;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.people = people;
        this.difficulty = difficulty;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Set getTags() {
        return tags;
    }

    public void setTags(Set tags) {
        this.tags = tags;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
