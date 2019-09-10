package nyriu.ricettavola.persistence;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import com.google.gson.Gson;

import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.models.TAG;


public class Converters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String uriToString(Uri data) {
        if (data == null) { return null; }
        return gson.toJson(data.getPath());
    }
    @TypeConverter
    public static Uri stringToUri(String s) {
        if (s == null) { return Recipe.DEFAULT_IMAGE_URI; }
        return Uri.parse(gson.fromJson(s, String.class));
    }


    @TypeConverter
    public static String tagsToString(Set data) {
        if (data == null) { return null; }
        return gson.toJson(data);
    }
    @TypeConverter
    public static Set stringToTags(String s) {
        // TODO verificare che il tipo combaci con TAG
        if (s == null) { return null; }
        return gson.fromJson(s, TreeSet.class);
    }


    @TypeConverter
    public static String ingredientsToString(ArrayList data) {
        if (data == null) { return null; }
        return gson.toJson(data);
    }
    @TypeConverter
    public static ArrayList stringToIngredients(String s) {
        // TODO verificare che il tipo combaci con TAG
        if (s == null) { return null; }
        return gson.fromJson(s, ArrayList.class);
    }



}
