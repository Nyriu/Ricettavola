package nyriu.ricettavola.async;

import android.os.AsyncTask;

import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.persistence.RecipeDao;



public class DeleteAsyncTask extends AsyncTask<Recipe, Void, Void> {

    private RecipeDao mRecipeDao;

    public DeleteAsyncTask(RecipeDao dao) {
        mRecipeDao = dao;
    }

    @Override
    protected Void doInBackground(Recipe[] recipes) {
        mRecipeDao.delete(recipes);
        return null;
    }
}
