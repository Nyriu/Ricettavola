package nyriu.ricettavola.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nyriu.ricettavola.R;

/**
 * Represents a recipe
 * It includes general informations (title, cooking time,...),
 * necessary ingredients and preparation steps
 */
public class Recipe implements Parcelable {

    // Summary Stuff
    // TODO matrix
    private String title;
    private String preparation_time;
    private String cooking_time;
    private String portions;
    private int difficulty; /** MUST be in [0,5] **/
    private Set<TAG> tags = new TreeSet<>();

    // Ingredients Stuff
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    // Preparation Stuff
    private ArrayList<PreparationStep> preparationSteps = new ArrayList<>();


    public Recipe() {
        initEmptyRecipe();
    }

    public Recipe(String title) {
        this.title = title;
    }


    private void initEmptyRecipe() {
        this.title = "";
        this.preparation_time = "";
        this.cooking_time = "";
        this.portions = "";
        setDifficulty(0);
    }

    public void initPlaceholderRecipe() {
        this.title = "Biscuits with Nutella";
        this.preparation_time = "2 mins";
        this.cooking_time = "0 mins";
        this.portions = "1 people";
        setDifficulty(1);

        addTag(TAG.SNACK);

        addIngredient(new Ingredient("Nutella"));
        addIngredient(new Ingredient("2 biscuits"));

        addStep(new PreparationStep(4, "Eat"));
        addStep(new PreparationStep(1, "Get Nutella"));
        addStep(new PreparationStep(2, "Get biscuits"));
        addStep(new PreparationStep(3, "Spread the Nutella on the biscuits"));
    }


    public void setDifficulty(int value) {
        if (value < 0 || value > 5) {
            // TODO throw exception if not value in [0,5]
            this.difficulty = 0;
        } else {
            this.difficulty = value;
        }
    }


    public void addTag(TAG tag) {
        // TODO if element already in?
        this.tags.add(tag);
    }

    public void removeTag(TAG tag) {
        // TODO if element already in?
        this.tags.remove(tag);
    }


    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }


    public void addStep(PreparationStep preparationStep) {
        // TODO verificare che siano numerati correttamente (no buchi, ordine corretto, ...)
        this.preparationSteps.add(preparationStep);
        reorderSteps();
    }

    public void removeStep(PreparationStep preparationStep) {
        // TODO verificare che siano numerati correttamente (no buchi, ordine corretto, ...)
        this.preparationSteps.remove(preparationStep);
        reorderSteps();
    }


    /**
     * Order steps by their number
     * If number missing or not contiguous they will be reassigned starting from 0
     */
    private void reorderSteps() {
        // TODO cosa succede se due step stesso numero?
        this.preparationSteps.sort(new Comparator<PreparationStep>() {
            @Override
            public int compare(PreparationStep p1, PreparationStep p2) {
                // return 1 if p2 should be before p1
                // return -1 if p1 should be before p2
                // return 0 otherwise (meaning the order stays the same)
                return p2.compareTo(p1);
            }
        });

        Iterator<PreparationStep> iterator = this.preparationSteps.iterator();

        // TODO verificare correttezza
        // NON serve
        // int last = -1;
        // while (iterator.hasNext()) {
        //     PreparationStep p = iterator.next();
        //     if ( !(p.getNumber() == (last+1)) ) {
        //         break;
        //     }
        // }

        // TODO verificare correttezza
        // // A prescindere resetto i numeri da 0 a len(steps)
        // int number = 1;
        // while (iterator.hasNext()) {
        //     PreparationStep p = iterator.next();
        //     p.setNumber(number++);
        // }

    }

    @Override
    public String toString() {
        String s = "Recipe{";
        s += "title='" + this.title + "', ";
        s += "preparation='" + this.preparation_time + "', ";
        s += "cooking='" + this.cooking_time + "', ";
        s += "portions='" + this.portions + "', ";
        s += "difficulty='" + this.difficulty + "', ";
        s += "tags='" + this.tags + "', ";

        s += "ingredients='" + this.ingredients.toString() + "', ";
        s += "preparation='" + this.preparationSteps.toString();

        s += '}';
        return s;
    }


    // #############################################################################################
    // Getter & Setter

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Set<TAG> getTags() {
        return tags;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<PreparationStep> getPreparationSteps() {
        return preparationSteps;
    }



    // #############################################################################################
    // Parcelable Stuff

    protected Recipe(Parcel in) {
        title = in.readString();
        preparation_time = in.readString();
        cooking_time = in.readString();
        portions = in.readString();
        difficulty = in.readInt();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        preparationSteps = in.createTypedArrayList(PreparationStep.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(preparation_time);
        dest.writeString(cooking_time);
        dest.writeString(portions);
        dest.writeInt(difficulty);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(preparationSteps);
    }

}
