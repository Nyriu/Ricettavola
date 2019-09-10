package nyriu.ricettavola.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import nyriu.ricettavola.models.Recipe;


@Dao
public interface RecipeDao {
    @Insert
    long[] insertRecipe(Recipe[] recipes);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getRecipes();

    @Delete
    int delete(Recipe[] recipes);

    @Update
    int update(Recipe[] recipes);


}
