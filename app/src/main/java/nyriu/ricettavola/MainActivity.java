package nyriu.ricettavola;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import Database.DatabaseHelper;
import Database.DatabaseRecipe;
import nyriu.ricettavola.adapters.RecipesRecyclerAdapter;
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DEBUGMainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each
        // of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        //super.onBackPressed();

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
    }

    // TODO remove me
    public DatabaseRecipe newFakeRecipe(){
        Set tags = new TreeSet();
        tags.add("PrimoPiatto");

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Primo ingrediente");
        ingredients.add("Secondo ingrediente");

        ArrayList<String> steps = new ArrayList<>();
        steps.add("Primo step");

        return new DatabaseRecipe(
                "Titolo",
                DatabaseRecipe.DEFAULT_IMAGE_URI,
                "7 min",
                "7 min",
                "3 persone",
                1,
                tags,
                ingredients,
                steps
                );
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        static final int POSITION_RECIPES       = 0;
        static final int POSITION_SHOPPING_LIST = 1;

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
                    // TODO throw error
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
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
            //Toast.makeText(getContext(), "Recype long press" + position,Toast.LENGTH_SHORT).show();
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


    public static class ShoppingListFragment extends Fragment {

        public ShoppingListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ShoppingListFragment newInstance() {
            ShoppingListFragment fragment = new ShoppingListFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.shoppinglist_fragment_main, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.text);
            textView.setText("Shopping List!!");
            return rootView;
        }
    }

}
