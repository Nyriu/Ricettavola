package nyriu.ricettavola.async;

import android.os.AsyncTask;

import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.persistence.RecipeDao;


public class UpdateAsyncTask extends AsyncTask<Recipe, Void, Void> {

    private RecipeDao mRecipeDao;

    public UpdateAsyncTask(RecipeDao dao) {
        mRecipeDao = dao;
    }

    @Override
    protected Void doInBackground(Recipe[] recipes) {
        mRecipeDao.update(recipes);
        return null;
    }
}
