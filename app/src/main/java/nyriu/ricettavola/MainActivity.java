package nyriu.ricettavola;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.List;

import nyriu.ricettavola.adapters.RecipesRecyclerAdapter;
import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.persistence.RecipeRepository;
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    // vars
    private ArrayList<Recipe> mRecipes;
    private RecipeRepository mRecipeRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipes = new ArrayList<>();
        //insertFakeRecipes(2);

        mRecipeRepository = new RecipeRepository(this);
        retriveRecipes();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each
        // of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mRecipes);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);


    }

    private void retriveRecipes() {
        mRecipeRepository.retriveRecipeTask().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (mRecipes.size() > 0) {
                    mRecipes.clear();
                }
                if (recipes != null) {
                    mRecipes.addAll(recipes);
                }
                //mRecipeRecyclerAdapter.notifyDataSetChanged(); // TODO rimuovere
            }
        });
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

    private void insertFakeRecipes(int num) {
        for (int i=0; i<num; i++) {
            Recipe recipe = new Recipe();
            Log.d("DEBUG", "insertFakeRecipes: " + i);
            recipe.initPlaceholderRecipe();
            mRecipes.add(recipe);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab:{
                Intent intent = new Intent(this, RecipeActivity.class);
                intent.putExtra("new_recipe", true);
                startActivity(intent);
                //this.mSectionsPagerAdapter.addRecipe(new Recipe("Nuova!"));
                break;
            }
        }
    }


    /**
     * A fragment containing recipes list.
     */
    public static class RecipesFragment extends Fragment implements
            RecipesRecyclerAdapter.OnRecipeListener {
        // TODO perche static?

        // Ui compontents
        public RecyclerView mRecyclerView; // TODO make rpivate

        // vars
        private ArrayList<Recipe> mRecipes = new ArrayList<>();
        private RecipesRecyclerAdapter mRecipesRecyclerAdapter;
        //private RecipeRepository mNoteRepository; // TODO



        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public RecipesFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RecipesFragment newInstance() {
            RecipesFragment fragment = new RecipesFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            try {
                this.mRecipes = (ArrayList)getArguments().getParcelableArrayList("recipes");

            } catch (Exception e) {
                // TODO gestire
                Log.d("DEBUG", "Missing recipes");
                Toast.makeText(getContext(), "Missing recipes!", Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getContext(), "ViewHolder Clicked!" + position,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), RecipeActivity.class);
            intent.putExtra("recipe", this.mRecipes.get(position));
            startActivity(intent);
        }

        public void addRecipe(Recipe r) {
            this.mRecipes.add(r);
            this.mRecipesRecyclerAdapter.notifyDataSetChanged();
        }

    }


    /**
     * A fragment containing the shopping list
     */
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int POSITION_RECIPES = 0;
        private final int POSITION_SHOPPING_LIST = 1;

        // vars
        private ArrayList<Recipe> mRecipes;
        private RecipesFragment mRecipesFragment;
        private ShoppingListFragment mShoppingListFragment;


        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Recipe> recipes) {
            super(fm);
            this.mRecipes = recipes;

            this.mRecipesFragment = RecipesFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("recipes", this.mRecipes); // TODO sostituire con Parcelable?
            this.mRecipesFragment.setArguments(bundle);

            this.mShoppingListFragment = ShoppingListFragment.newInstance();
        }

        @Override
        public Fragment getItem(int position) {
            // TODO qua va messa logica per schermate differenti per ricetta e lista della spesa
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {

                case POSITION_RECIPES:
                    return this.mRecipesFragment;

                case POSITION_SHOPPING_LIST:
                    return this.mShoppingListFragment;
                default:
                    // TODO throw error
                    return null;
            }
        }

        public void addRecipe(Recipe r) {
            this.mRecipesFragment.addRecipe(r);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
