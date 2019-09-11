package Database;


import java.util.Map;
import java.util.Set;

public class DatabaseRecipe {

    public final static String ID_FIELD               = "id";
    public final static String TITLE_FIELD            = "title";
    public final static String PREPARATION_TIME_FIELD = "prep_time";
    public final static String COOKING_TIME           = "cook_time";
    public final static String PEOPLE_FIELD           = "people";
    public final static String DIFFICULTY_FIELD       = "diff";
    public final static String TAG_FIELD              = "tag";
    public final static String INGREDIENTS_FIELD      = "ingredients";
    public final static String STEPS_FIELD            = "steps";

    public final static int COLUMN_ID_FIELD               = 0;
    public final static int COLUMN_TITLE_FIELD            = 1;
    public final static int COLUMN_PREPARATION_TIME_FIELD = 2;
    public final static int COLUMN_COOKING_TIME           = 3;
    public final static int COLUMN_PEOPLE_FIELD           = 4;
    public final static int COLUMN_DIFFICULTY_FIELD       = 5;
    public final static int COLUMN_TAG_FIELD              = 6;
    public final static int COLUMN_INGREDIENTS_FIELD      = 7;
    public final static int COLUMN_STEPS_FIELD            = 8;



    private int id;
    private String title;
    private String prepTime;
    private String cookTime;
    private String people;
    private int difficulty;

    private Set tags;
    private String[] ingredients;
    private Map<Integer, String> steps;

    public DatabaseRecipe(String title, String prepTime, String cookTime, String people, int difficulty, Set tags, String[] ingredients, Map<Integer, String> steps) {
        this.title = title;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.people = people;
        this.difficulty = difficulty;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public DatabaseRecipe(int id, String title, String prepTime, String cookTime, String people, int difficulty, Set tags, String[] ingredients, Map<Integer, String> steps) {
        this.id = id;
        this.title = title;
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

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Map<Integer, String> getSteps() {
        return steps;
    }

    public void setSteps(Map<Integer, String> steps) {
        this.steps = steps;
    }
}
