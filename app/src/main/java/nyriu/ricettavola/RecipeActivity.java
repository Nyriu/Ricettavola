package nyriu.ricettavola;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nyriu.ricettavola.adapters.IngredientsRecyclerAdapter;
import nyriu.ricettavola.adapters.PreparationStepsRecyclerAdapter;
import nyriu.ricettavola.models.Ingredient;
import nyriu.ricettavola.models.PreparationStep;
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
    private boolean mEditMode = false;
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

        if (getIntent().hasExtra("new_recipe")) {
            mEditMode = true;
        }
        // END Intent stuff

        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each
        // of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this.mRecipe, this.mEditMode);

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


        Log.d("DEBUG", "Activity onCreate: fine");
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        //Log.d("DEBUG", "Activity onCreateView: fine");
        //Log.d("DEBUG", "Activity onCreateView: context = "+ context);
        //if (ismEditMode()) {
        //    putEditModeOn();
        //} else {
        //    putEditModeOff();
        //}
        return super.onCreateView(name, context, attrs);
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






    // TODO funzioni da collegare con il tasto edit
    private boolean ismEditMode() {
        return mEditMode;
    }

    private void putEditModeOn() {
        this.mEditMode = true;
        this.mRecipeTitle.setText("EDIT MODE!");
        this.mSectionsPagerAdapter.putEditModeOn();
    }


    private void putEditModeOff() {
        this.mEditMode = false;
        //this.mSectionsPagerAdapter.putEditModeOff(); // TODO
    }







    /**
     * A fragment containing recipe summary
     */
    public static class SummaryFragment extends EditableFragment {

        // UI normal mode
        private AppCompatImageView recipe_image;
        private TextView recipe_title;
        private TextView preparation_content;
        private TextView cooking_content;
        private TextView portions_content;
        //private TextView difficulty_content; // TODO modificare
        //private TextView tags_content;       // TODO modificare

        // UI edit mode
        private EditText edit_recipe_title;




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

            Log.d("DEUBG", "onCreateView summmary fragment");

            View rootView = inflater.inflate(R.layout.summary_fragment_recipe, container, false);

            // UI normal mode
            //this.recipe_image = getActivity().findViewById(R.id.recipe_image);
            this.recipe_title = rootView.findViewById(R.id.recipe_title);
            this.preparation_content = rootView.findViewById(R.id.preparation_content);
            this.cooking_content = rootView.findViewById(R.id.cooking_content);
            this.portions_content = rootView.findViewById(R.id.portions_content);
            // UI edit mode
            this.edit_recipe_title = rootView.findViewById(R.id.edit_recipe_title);
            Log.d("DEUBG", "edit_recipe_title " + edit_recipe_title);


            initializeFields();

            if (isEditMode()) {
                putEditModeOn();
            }

            Log.d("DEBUG", "Summary onCreateView fine");
            return rootView;
        }



        private void initializeFields() {
            // //this.recipe_image   // TODO
            this.recipe_title.setText(this.mRecipe.getTitle()); // TODO uncomment me
            this.preparation_content.setText(this.mRecipe.getPreparation_time());
            this.cooking_content.setText(this.mRecipe.getCooking_time());
            this.portions_content.setText(this.mRecipe.getPortions());

            // mantengo allineata anche la parte editabile
            this.edit_recipe_title.setText(this.recipe_title.getText());
        }

        @Override
        void putEditModeOn() {
            Log.d("DEBUG", "putEditModeOn: Inside");
            super.putEditModeOn();
            this.recipe_title.setVisibility(View.GONE);
            this.edit_recipe_title.setVisibility(View.VISIBLE);
        }
    }


    /**
     * A fragment containing recipe summary
     */
    public static class IngredientsFragment extends EditableFragment implements
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

        @Override
        public void putEditModeOn() {

        }

        @Override
        public void putEditModeOff() {

        }
    }


    /**
     * A fragment containing recipe summary
     */
    public static class PreparationFragment extends EditableFragment implements
            PreparationStepsRecyclerAdapter.OnPreparationStepListener {
        // Ui compontents
        private RecyclerView mRecyclerView;

       // vars
        private ArrayList<PreparationStep> mPreparationSteps = new ArrayList<>();
        private PreparationStepsRecyclerAdapter mPreparationStepsRecyclerAdapter;


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

        @ Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                assert getArguments() != null;
                this.mPreparationSteps = getArguments().getParcelableArrayList("steps");
            } catch (Exception e) {
                Log.d("DEBUG", "Missing preparation step");
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.preparationsteps_fragment_recipe, container, false);

            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            initRecyclerView();

            return rootView;
        }

        public void initRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
            mRecyclerView.addItemDecoration(itemDecorator);

            mPreparationStepsRecyclerAdapter = new PreparationStepsRecyclerAdapter(mPreparationSteps, this);
            mRecyclerView.setAdapter(mPreparationStepsRecyclerAdapter);

        }

        @Override
        public void onPreparationStepClick(int position) {
            Log.d("DEBUG", "onPreparationsStepClick: " + position);
            Toast.makeText(getContext(), "ViewHolder Clicked!" + position,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void putEditModeOn() {

        }

        @Override
        public void putEditModeOff() {

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
        private boolean mEditMode;
        EditableFragment[] mFragments;

        public SectionsPagerAdapter(FragmentManager fm, Recipe recipe, boolean editMode) {
            super(fm);
            this.mRecipe = recipe;
            this.mFragments = new EditableFragment[3];
            this.mEditMode = editMode;

            Bundle bundle = new Bundle();
            bundle.putBoolean("edit_mode", this.mEditMode);
            bundle.putParcelable("recipe", this.mRecipe);
            SummaryFragment summaryFragment = SummaryFragment.newInstance();
            summaryFragment.setArguments(bundle);
            this.mFragments[POSITION_SUMMARY] = summaryFragment;

            bundle = new Bundle();
            bundle.putBoolean("edit_mode", this.mEditMode);
            bundle.putParcelableArrayList("ingredients", this.mRecipe.getIngredients());
            IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance();
            ingredientsFragment.setArguments(bundle);
            this.mFragments[POSITION_INGREDIENTS] = ingredientsFragment;

            bundle = new Bundle();
            bundle.putBoolean("edit_mode", this.mEditMode);
            bundle.putParcelableArrayList("steps", this.mRecipe.getPreparationSteps());
            PreparationFragment preparationFragment = PreparationFragment.newInstance();
            preparationFragment.setArguments(bundle);
            this.mFragments[POSITION_PREPARATION] = preparationFragment;
            Log.d("DEBUG", "SectionPagerAdapter initialization");
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case POSITION_SUMMARY:
                    return this.mFragments[POSITION_SUMMARY];

                case POSITION_INGREDIENTS:
                    return this.mFragments[POSITION_INGREDIENTS];

                case POSITION_PREPARATION:
                    return this.mFragments[POSITION_PREPARATION];

                default:
                    // TODO throw error
                    return SummaryFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


        // Edit Mode On/Off

        private void putEditModeOn() {
            for (EditableFragment f:
                 mFragments) {
                f.putEditModeOn();
            }
        }

        private void putEditModeOff() {
            for (EditableFragment f:
                    mFragments) {
                f.putEditModeOff();
            }
        }

    }






    public static class EditableFragment extends Fragment {
        private boolean mEditMode;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                assert getArguments() != null;
                this.mEditMode = getArguments().getBoolean("edit_mode");
            } catch(Exception e) {
                Log.d("DEBUG", "Missing edit_mode");
            }
        }

        boolean isEditMode() {
            return this.mEditMode;
        }

        void putEditModeOn() {}
        void putEditModeOff() {}
    }

}
