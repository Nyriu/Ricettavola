package nyriu.ricettavola.async;

import android.os.AsyncTask;

import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.persistence.RecipeDao;


public class InsertAsyncTask extends AsyncTask<Recipe, Void, Void> {

    private RecipeDao mRecipeDao;

    public InsertAsyncTask(RecipeDao dao) {
        mRecipeDao = dao;
    }

    @Override
    protected Void doInBackground(Recipe[] recipes) {
        mRecipeDao.insertRecipe(recipes);
        return null;
    }
}
