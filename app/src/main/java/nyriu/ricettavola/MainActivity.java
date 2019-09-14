package nyriu.ricettavola;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import Database.DatabaseHelper;
import Database.DatabaseRecipe;
import Database.DatabaseShoppingListIngredient;
import nyriu.ricettavola.adapters.RecipesRecyclerAdapter;
import nyriu.ricettavola.adapters.ShoppingListRecyclerAdapter;
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DEBUGMainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private DatabaseHelper mDatatbaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatatbaseHelper = new DatabaseHelper(
                this,
                DatabaseHelper.DATABASE_NAME,
                null,
                DatabaseHelper.DATABASE_VERSION);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toggle_language) {
            Configuration c = this.getResources().getConfiguration();
            String lang = c.getLocales().get(0).getLanguage();
            if (lang.equals("en")) {
                setLocale("it");
            } else {
                setLocale("en");
            }
            mSectionsPagerAdapter.getRecipesFragment().setEditMode(false);
            return true;
        }
        if (id == R.id.action_add_recipe) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            //clipboard.setText("Text to copy");
            String clipboardText = (String) clipboard.getText();
            DatabaseRecipe recipe = DatabaseRecipe.buildFromString(clipboardText);
            if (recipe != null){
                mDatatbaseHelper.addRecipe(recipe);
                mSectionsPagerAdapter.getRecipesFragment().setEditMode(false);
                mSectionsPagerAdapter.getRecipesFragment().refreshRecyclerView();
                Toast.makeText(this, getString(R.string.clipboard_ok), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.clipboard_error), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_remove_bought) {
            mDatatbaseHelper.deleteBoughtShoppingListIngredients();
            mSectionsPagerAdapter.getShoppingListFragment().refreshRecyclerView();
            return true;
        }
        if (id == R.id.action_empty_shopping_list) {
            emptyShoppingList();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void emptyShoppingList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.delete_shopping_list_title);
        builder.setMessage(R.string.delete_shopping_list_content);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatatbaseHelper.deleteAllShoppingListIngredients();
                        mSectionsPagerAdapter.getShoppingListFragment().refreshRecyclerView();
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                Intent intent = new Intent(this, RecipeActivity.class);
                intent.putExtra("new_recipe", true);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: count" + count);

        if (count == 0) {
            if (mSectionsPagerAdapter.getRecipesFragment().getEditMode()){
                mSectionsPagerAdapter.getRecipesFragment().setEditMode(false);
            } else {
                super.onBackPressed();
            }

        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSectionsPagerAdapter.getRecipesFragment().refreshRecyclerView();
        mSectionsPagerAdapter.getShoppingListFragment().refreshRecyclerView();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        static final int POSITION_RECIPES       = 0;
        static final int POSITION_SHOPPING_LIST = 1;
        private final int NUM_FRAGMENTS = 2;

        // vars
        private RecipesFragment mRecipesFragment;
        private ShoppingListFragment mShoppingListFragment;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mRecipesFragment = RecipesFragment.newInstance();
            this.mShoppingListFragment = ShoppingListFragment.newInstance();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POSITION_RECIPES:
                    return this.mRecipesFragment;

                case POSITION_SHOPPING_LIST:
                    return this.mShoppingListFragment;
                default:
                    Log.d(TAG, "fragment number incorrect");
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }

        public RecipesFragment getRecipesFragment() {
            return mRecipesFragment;
        }

        public ShoppingListFragment getShoppingListFragment() {
          return mShoppingListFragment;
        }
    }



    public static class RecipesFragment extends Fragment implements
            RecipesRecyclerAdapter.OnRecipeListener {

        // Ui compontents
        private RecyclerView mRecyclerView;

        // vars
        private ArrayList<DatabaseRecipe> mRecipes;
        private RecipesRecyclerAdapter mRecipesRecyclerAdapter;
        private boolean mEditMode = false;

        // Database
        private DatabaseHelper mDatatbaseHelper;


        public RecipesFragment() {
        }

        public static RecipesFragment newInstance() {
            RecipesFragment fragment = new RecipesFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mDatatbaseHelper = new DatabaseHelper(
                    getContext(),
                    DatabaseHelper.DATABASE_NAME,
                    null,
                    DatabaseHelper.DATABASE_VERSION);

            this.mRecipes = mDatatbaseHelper.getAllRecipes();
        }

        public void refreshRecyclerView() {
            try {
                this.mRecipes = mDatatbaseHelper.getAllRecipes();
                mRecipesRecyclerAdapter = new RecipesRecyclerAdapter(mRecipes, this);
                mRecyclerView.setAdapter(mRecipesRecyclerAdapter);
                mRecipesRecyclerAdapter.notifyDataSetChanged();
            } catch (NullPointerException e) {
                Log.d(TAG, "primo refreshRecyclerView");
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.recipes_fragment_main, container, false);

            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            initRecyclerView();

            return rootView;
        }

        public void initRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
            mRecyclerView.addItemDecoration(itemDecorator);

            mRecipesRecyclerAdapter = new RecipesRecyclerAdapter(mRecipes, this);
            mRecyclerView.setAdapter(mRecipesRecyclerAdapter);
        }


        @Override
        public void onRecipeClick(int position) {
            //Toast.makeText(getContext(), "ViewHolder clicked!" + position,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), RecipeActivity.class);
            intent.putExtra("recipe_id", this.mRecipes.get(position).getId());
            startActivity(intent);
        }

        @Override
        public void onRecipeDelete(final int position) {
            //Toast.makeText(getContext(), "Delete recype button clicked!" + position,Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle(R.string.delete_recipe_title);
            builder.setMessage(R.string.delete_recipe_content);
            builder.setPositiveButton(R.string.confirm,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatatbaseHelper.deleteRecipe(mRecipes.get(position));
                            mRecipes.remove(position);
                            //mRecipesRecyclerAdapter.notifyItemRangeRemoved(position,1);
                            mRecipesRecyclerAdapter.notifyDataSetChanged();
                            if (mRecipes.size() == 0) {
                                setEditMode(false);
                            }
                        }
                    });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onRecipeLongPress(int position) {
            this.setEditMode(true);
            Log.d(TAG, "onRecipeLongPress: ");
        }

        public boolean getEditMode() {
            return this.mEditMode;
        }
        public void setEditMode(boolean editMode) {
            if (editMode != mEditMode) {
                if (editMode) {
                    putEditModeOn();
                } else {
                    putEditModeOff();
                }
                this.mEditMode = editMode;
            }
        }

        public void putEditModeOn() {
            this.mEditMode = true;
            mRecipesRecyclerAdapter.putEditModeOn();
        }

        public void putEditModeOff() {
            this.mEditMode = false;
            mRecipesRecyclerAdapter.putEditModeOff();
        }

    }


    public static class ShoppingListFragment extends Fragment implements
            ShoppingListRecyclerAdapter.OnShoppingListListener {

        // Ui compontents
        private RecyclerView mRecyclerView;

        // vars
        private ArrayList<DatabaseShoppingListIngredient> mShoppingList;
        private ShoppingListRecyclerAdapter mShoppingListRecyclerAdapter;

        // Database
        private DatabaseHelper mDatatbaseHelper;


        public ShoppingListFragment() {
        }

        public static ShoppingListFragment newInstance() {
            ShoppingListFragment fragment = new ShoppingListFragment();
            return fragment;
        }

        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mDatatbaseHelper = new DatabaseHelper(
                    getContext(),
                    DatabaseHelper.DATABASE_NAME,
                    null,
                    DatabaseHelper.DATABASE_VERSION);
            this.mShoppingList = mDatatbaseHelper.getShoppingList();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.shoppinglist_fragment_main, container, false);

            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            initRecyclerView();

            return rootView;
        }

        public void initRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
            mRecyclerView.addItemDecoration(itemDecorator);

            mShoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(mShoppingList, this);
            mRecyclerView.setAdapter(mShoppingListRecyclerAdapter);
        }

        @Override
        public void onShoppingListIngredientClick(int position) {
            DatabaseShoppingListIngredient ingredient = mShoppingList.get(position);
            ingredient.setBought(!ingredient.isBought());
            mDatatbaseHelper.updateShoppingListIngredient(ingredient);
        }

        public void refreshRecyclerView() {
            try {
                this.mShoppingList = mDatatbaseHelper.getShoppingList();
                mShoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(mShoppingList, this);
                mRecyclerView.setAdapter(mShoppingListRecyclerAdapter);
                mShoppingListRecyclerAdapter.notifyDataSetChanged();
            } catch (NullPointerException e) {
                Log.d(TAG, "primo refreshRecyclerView");
            }

        }

    }

}
