package nyriu.ricettavola.persistence;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import nyriu.ricettavola.models.Ingredient;
import nyriu.ricettavola.models.PreparationStep;
import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.models.TAG;


public class Converters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String uriToString(Uri data) {
        if (data == null) { return null; }
        String tmp = gson.toJson(data.toString());
        return tmp;
    }
    @TypeConverter
    public static Uri stringToUri(String s) {
        if (s == null) { return Recipe.DEFAULT_IMAGE_URI; }
        Log.d("DEBUG", "stringToUri: " + s);
        //return Uri.parse(gson.fromJson(s, String.class));
        return Recipe.DEFAULT_IMAGE_URI;
    }


    @TypeConverter
    public static String tagsToString(Set data) {
        if (data == null) { return null; }
        return gson.toJson(data);
    }
    @TypeConverter
    public static Set stringToTags(String s) {
        if (s == null) { return null; }
        return (TreeSet<TAG>) gson.fromJson(s, TreeSet.class);
    }


    @TypeConverter
    public static String ingredientsToString(ArrayList<Ingredient> data) {
        if (data == null) { return null; }
        return gson.toJson(data);
    }
    @TypeConverter
    public static ArrayList<Ingredient> stringToIngredients(String s) {
        if (s == null) { return null; }
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) gson.fromJson(s, ArrayList.class);
        return ingredients;
    }


    @TypeConverter
    public static String preparationStepsToString(ArrayList<PreparationStep> data) {
        if (data == null) { return null; }
        return gson.toJson(data);
    }
    @TypeConverter
    public static ArrayList<PreparationStep> stringToPreparationSteps(String s) {
        if (s == null) { return null; }
        ArrayList<PreparationStep> preparationSteps = (ArrayList<PreparationStep>) gson.fromJson(s, ArrayList.class);
        return preparationSteps;
    }




}
