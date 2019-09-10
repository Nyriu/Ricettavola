package nyriu.ricettavola.persistence;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import nyriu.ricettavola.async.DeleteAsyncTask;
import nyriu.ricettavola.async.InsertAsyncTask;
import nyriu.ricettavola.async.UpdateAsyncTask;
import nyriu.ricettavola.models.Recipe;


public class RecipeRepository {

    private RecipeDatabase mRecipeDatabase;

    public RecipeRepository(Context context) {
        mRecipeDatabase = RecipeDatabase.getInstance(context);
    }

    public void insertRecipeTask(Recipe recipe){
        new InsertAsyncTask(mRecipeDatabase.getRecipeDao()).execute(recipe);
    }

    public void updateRecipeTask(Recipe recipe) {
        new UpdateAsyncTask(mRecipeDatabase.getRecipeDao()).execute(recipe);
    }

    public LiveData<List<Recipe>> retriveRecipeTask() {
        return mRecipeDatabase.getRecipeDao().getRecipes();
    }

    public void deleteRecipeTask(Recipe recipe) {
        new DeleteAsyncTask(mRecipeDatabase.getRecipeDao()).execute(recipe);
    }
}
