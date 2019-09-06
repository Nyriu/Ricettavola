package nyriu.ricettavola;

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
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    // vars
    private ArrayList<Recipe> mRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipes = new ArrayList<>();
        insertFakeRecipes(2);


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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            mRecipes.add(recipe);
        }
    }

    /**
     * A fragment containing recipes list.
     */
    public static class RecipesFragment extends Fragment implements
            RecipesRecyclerAdapter.OnRecipeListener {
        // TODO perche static?


        // Ui compontents
        private RecyclerView mRecyclerView;

        // vars
        private ArrayList<Recipe> mRecipes = new ArrayList<>();
        private RecipesRecyclerAdapter mRecipesRecyclerAdapter;
        //private NoteRepository mNoteRepository; // TODO



        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public RecipesFragment() {
            //this.mRecipes = new ArrayList<>();
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
                // Log.d("DEBUG", "getting recipes");
                this.mRecipes = (ArrayList)getArguments().getParcelableArrayList("recipes");
                // Log.d("DEBUG", "got #" + mRecipes.size() + "  " + mRecipes);
            } catch (Exception e) {
                // TODO gestire
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
            //Log.d("DEBUG", "onRecipeClick: clicked");
            //Toast.makeText(getContext(), "ViewHolder Clicked!" + position,Toast.LENGTH_LONG).show();
            //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

            Intent intent = new Intent(getContext(), RecipeActivity.class);
            //intent.putExtra("selected_note", mNotes.get(position));
            //intent.putExtra("new_note", false);
            startActivity(intent);
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


        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Recipe> recipes) {
            super(fm);
            this.mRecipes = recipes;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO qua va messa logica per schermate differenti per ricetta e lista della spesa
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {

                case POSITION_RECIPES:
                    RecipesFragment recipesFragment = RecipesFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("recipes", this.mRecipes); // TODO sostituire con Parcelable?
                    recipesFragment.setArguments(bundle);
                    return recipesFragment;

                case POSITION_SHOPPING_LIST:
                    return ShoppingListFragment.newInstance();
                default:
                    // TODO throw error
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
