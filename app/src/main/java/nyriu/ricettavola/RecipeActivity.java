package nyriu.ricettavola;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nyriu.ricettavola.adapters.IngredientsRecyclerAdapter;
import nyriu.ricettavola.models.Ingredient;
import nyriu.ricettavola.models.Recipe;
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;

public class RecipeActivity extends AppCompatActivity implements
        View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // UI
    private ImageButton mBackArrow;
    private TextView mRecipeTitle;

    // vars
    private Recipe mRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intent stuff
        if (getIntent().hasExtra("recipe")) {
            this.mRecipe = getIntent().getExtras().getParcelable("recipe");
        } else {
            this.mRecipe = new Recipe();
        }
        // END Intent stuff


        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each
        // of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this.mRecipe);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeTitle.setText(mRecipe.getTitle()); // TODO modificare/spostare
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        setListeners();
    }

    private void setListeners() {
        mBackArrow.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbar_back_arrow:{
                finish(); // distrugge activity
                break;
            }

        }
    }



    /**
     * A fragment containing recipe summary
     */
    public static class SummaryFragment extends Fragment {

        // UI
        private AppCompatImageView recipe_image;
        private TextView recipe_title;
        private TextView preparation_content;
        private TextView cooking_content;
        private TextView portions_content;
        //private TextView difficulty_content; // TODO modificare
        //private TextView tags_content;       // TODO modificare

        // vars
        private Recipe mRecipe;

        public SummaryFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SummaryFragment newInstance() {
            SummaryFragment fragment = new SummaryFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mRecipe = (Recipe)getArguments().getParcelable("recipe");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.summary_fragment_recipe, container, false);

            //this.recipe_image = getActivity().findViewById(R.id.recipe_image);
            this.recipe_title = rootView.findViewById(R.id.recipe_title);
            this.preparation_content = rootView.findViewById(R.id.preparation_content);
            this.cooking_content = rootView.findViewById(R.id.cooking_content);
            this.portions_content = rootView.findViewById(R.id.portions_content);

            initializeFields();
            return rootView;
        }



        private void initializeFields() {
            // //this.recipe_image   // TODO
            this.recipe_title.setText(this.mRecipe.getTitle());
            this.preparation_content.setText(this.mRecipe.getPreparation_time());
            this.cooking_content.setText(this.mRecipe.getCooking_time());
            this.portions_content.setText(this.mRecipe.getPortions());
        }

    }


    /**
     * A fragment containing recipe summary
     */
    public static class IngredientsFragment extends Fragment implements
        IngredientsRecyclerAdapter.OnIngredientListener {

        // Ui compontents
        private RecyclerView mRecyclerView;

        // vars
        private ArrayList<Ingredient> mIngredients = new ArrayList<>();
        private IngredientsRecyclerAdapter mIngredientsRecyclerAdapter;


        public IngredientsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static IngredientsFragment newInstance() {
            IngredientsFragment fragment = new IngredientsFragment();
            return fragment;
        }

        @ Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                assert getArguments() != null;
                this.mIngredients = getArguments().getParcelableArrayList("ingredients");
                Log.d("DEBUG", "mIngredients " + mIngredients.toString());
            } catch (Exception e) {
                Log.d("DEBUG", "Missing ingredients");
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.ingredients_fragment_recipe, container, false);

            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            initRecyclerView();

            return rootView;
        }

        public void initRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(2);
            mRecyclerView.addItemDecoration(itemDecorator);

            mIngredientsRecyclerAdapter = new IngredientsRecyclerAdapter(mIngredients, this);
            mRecyclerView.setAdapter(mIngredientsRecyclerAdapter);

        }

        @Override
        public void onIngredientClick(int position) {
            Log.d("DEBUG", "onIngredientClick: " + position);
            Toast.makeText(getContext(), "ViewHolder Clicked!" + position,Toast.LENGTH_SHORT).show();
        }
    }


    // TODO Fare Recycler view per gli step!!!!

    /**
     * A fragment containing recipe summary
     */
    public static class PreparationFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public PreparationFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PreparationFragment newInstance() {
            PreparationFragment fragment = new PreparationFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.ingredients_fragment_recipe, container, false); //TODO change
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }





    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int POSITION_SUMMARY = 0;
        private final int POSITION_INGREDIENTS = 1;
        private final int POSITION_PREPARATION = 2;


        // vars
        private Recipe mRecipe;

        public SectionsPagerAdapter(FragmentManager fm, Recipe recipe) {
            super(fm);
            this.mRecipe = recipe;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO qua va messa logica per schermate differenti per ricetta e lista della spesa
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle bundle = new Bundle();
            switch (position) {
                case POSITION_SUMMARY:
                    SummaryFragment summaryFragment = SummaryFragment.newInstance();
                    bundle.putParcelable("recipe", this.mRecipe);
                    summaryFragment.setArguments(bundle);
                    return summaryFragment;

                case POSITION_INGREDIENTS:
                    IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance();
                    bundle.putParcelableArrayList("ingredients", this.mRecipe.getIngredients());
                    ingredientsFragment.setArguments(bundle);
                    return ingredientsFragment;
                case POSITION_PREPARATION:
                    return PreparationFragment.newInstance();
                default:
                    // TODO throw error
                    return SummaryFragment.newInstance();
                //return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
