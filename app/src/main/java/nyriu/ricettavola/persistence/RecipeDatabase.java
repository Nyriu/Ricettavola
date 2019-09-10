package nyriu.ricettavola.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import nyriu.ricettavola.models.Recipe;


@Database(entities = {Recipe.class}, version = 1) //se la classe delle entita cambia BISOGNA cambiare version
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes_db";

    private static RecipeDatabase instance;

    // Singleton
    static RecipeDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RecipeDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract RecipeDao getRecipeDao();

}
