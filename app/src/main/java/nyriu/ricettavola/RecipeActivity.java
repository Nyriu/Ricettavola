package nyriu.ricettavola;

import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Toolbar mToolbar;
    private ImageButton mBackArrow, mEditButton, mCheckButton, mShareButton;
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

        mToolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(mToolbar);

        // Create the adapter that will return a fragment for each
        // of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this.mRecipe, this.mEditMode);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        mRecipeTitle = findViewById(R.id.toolbar_recipe_title);
        mRecipeTitle.setText(mRecipe.getTitle()); // TODO modificare/spostare

        mBackArrow   = findViewById(R.id.toolbar_back_arrow);
        mEditButton  = findViewById(R.id.toolbar_edit);
        mCheckButton = findViewById(R.id.toolbar_check);
        mShareButton = findViewById(R.id.toolbar_share);

        setListeners();


        Log.d("DEBUG", "Activity onCreate: fine");
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    private void setListeners() {
        mBackArrow  .setOnClickListener(this);
        mEditButton .setOnClickListener(this);
        mCheckButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    //getMenuInflater().inflate(R.menu.menu_recipe, menu);
    //    return true;
    //}


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbar_back_arrow:{
                finish(); // distrugge activity
                break;
            }

            case R.id.toolbar_edit:{
                if (!ismEditMode()) {
                    putEditModeOn();
                }
                break;
            }

            case R.id.toolbar_check:{
                if (ismEditMode()) {
                    putEditModeOff();
                }
                break;
            }

            case R.id.toolbar_share:{
                Toast.makeText(this, "Sharing not implemented yet!",Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }





    @Override
    public void onBackPressed() {
        if (ismEditMode()){
            putEditModeOff();
        } else {
            super.onBackPressed();
        }
    }


    private boolean ismEditMode() {
        return mEditMode;
    }

    private void putEditModeOn() {
        this.mEditMode = true;
        putToolbarEditModeOn();
        this.mSectionsPagerAdapter.putEditModeOn();
    }
    private void putToolbarEditModeOn(){
        mRecipeTitle.setText("Modify your recipe!");
        //mToolbar.getMenu().findItem(R.id.action_settings).setVisible(false);

        this.mEditButton .setVisibility(View.GONE);
        this.mBackArrow  .setVisibility(View.GONE);
        this.mShareButton.setVisibility(View.GONE);
        this.mCheckButton.setVisibility(View.VISIBLE);
    }


    private void putEditModeOff() { // TODO non funziona con summary
        this.mEditMode = false;
        putToolbarEditModeOff();
        this.mSectionsPagerAdapter.putEditModeOff();
        hideSofKeyboard();
    }
    private void hideSofKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void putToolbarEditModeOff(){
        mRecipeTitle.setText(mRecipe.getTitle());
        //mToolbar.getMenu().findItem(R.id.action_settings).setVisible(true);

        this.mEditButton .setVisibility(View.VISIBLE);
        this.mBackArrow  .setVisibility(View.VISIBLE);
        this.mShareButton.setVisibility(View.VISIBLE);
        this.mCheckButton.setVisibility(View.GONE);
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
        private TextView difficulty_content; // TODO modificare
        private TextView tags_content;       // TODO modificare

        // UI edit mode
        private EditText edit_recipe_title;
        private EditText edit_preparation_content;
        private EditText edit_cooking_content;
        private EditText edit_portions_content;
        private TextView edit_difficulty_content; // TODO modificare
        private TextView edit_tags_content;       // TODO modificare




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
            this.recipe_title        = rootView.findViewById(R.id.toolbar_recipe_title);
            this.preparation_content = rootView.findViewById(R.id.preparation_content);
            this.cooking_content     = rootView.findViewById(R.id.cooking_content);
            this.portions_content    = rootView.findViewById(R.id.portions_content);
            this.portions_content    = rootView.findViewById(R.id.portions_content);
            this.difficulty_content  = rootView.findViewById(R.id.difficulty_content);
            this.tags_content        = rootView.findViewById(R.id.tags_content);
            // UI edit mode
            this.edit_recipe_title        = rootView.findViewById(R.id.edit_recipe_title);
            this.edit_preparation_content = rootView.findViewById(R.id.edit_preparation_content);
            this.edit_cooking_content     = rootView.findViewById(R.id.edit_cooking_content);
            this.edit_portions_content    = rootView.findViewById(R.id.edit_portions_content);
            this.edit_difficulty_content  = rootView.findViewById(R.id.edit_difficulty_content);
            this.edit_tags_content        = rootView.findViewById(R.id.edit_tags_content);


            initializeFields();

            if (isEditMode()) {
                putEditModeOn();
            }

            Log.d("DEBUG", "Summary onCreateView fine");
            return rootView;
        }



        private void initializeFields() {
            // //this.recipe_image   // TODO
            this.recipe_title       .setText(this.mRecipe.getTitle());
            this.preparation_content.setText(this.mRecipe.getPreparation_time());
            this.cooking_content    .setText(this.mRecipe.getCooking_time());
            this.portions_content   .setText(this.mRecipe.getPortions());

            // mantengo allineata anche la parte editabile
            this.edit_recipe_title       .setText(this.recipe_title.getText());
            this.edit_preparation_content.setText(this.preparation_content.getText());
            this.edit_cooking_content    .setText(this.cooking_content.getText());
            this.edit_portions_content   .setText(this.portions_content.getText());

            // Evito che gli edit text possano contenere piu' di una riga
            this.edit_recipe_title       .setSingleLine(true);
            this.edit_preparation_content.setSingleLine(true);
            this.edit_cooking_content    .setSingleLine(true);
            this.edit_portions_content   .setSingleLine(true);
            this.edit_recipe_title       .setImeOptions(EditorInfo.IME_ACTION_NEXT);
            this.edit_preparation_content.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            this.edit_cooking_content    .setImeOptions(EditorInfo.IME_ACTION_NEXT);
            this.edit_portions_content   .setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }

        @Override
        void putEditModeOn() {
            Log.d("DEBUG", "putEditModeOn: Inside");
            super.putEditModeOn();
            this.recipe_title       .setVisibility(View.GONE);
            this.preparation_content.setVisibility(View.GONE);
            this.cooking_content    .setVisibility(View.GONE);
            this.portions_content   .setVisibility(View.GONE);
            this.difficulty_content .setVisibility(View.GONE);
            this.tags_content       .setVisibility(View.GONE);

            // mantengo allineata anche la parte editabile
            this.edit_recipe_title       .setText(this.recipe_title.getText());
            this.edit_preparation_content.setText(this.preparation_content.getText());
            this.edit_cooking_content    .setText(this.cooking_content.getText());
            this.edit_portions_content   .setText(this.portions_content.getText());

            this.edit_recipe_title       .setVisibility(View.VISIBLE);
            this.edit_preparation_content.setVisibility(View.VISIBLE);
            this.edit_cooking_content    .setVisibility(View.VISIBLE);
            this.edit_portions_content   .setVisibility(View.VISIBLE);
            this.edit_difficulty_content .setVisibility(View.VISIBLE);
            this.edit_tags_content       .setVisibility(View.VISIBLE);
        }

        @Override
        void putEditModeOff() {
            Log.d("DEBUG", "putEditModeOff: Inside");
            super.putEditModeOff();
            this.edit_recipe_title       .setVisibility(View.GONE);
            this.edit_preparation_content.setVisibility(View.GONE);
            this.edit_cooking_content    .setVisibility(View.GONE);
            this.edit_portions_content   .setVisibility(View.GONE);
            this.edit_difficulty_content .setVisibility(View.GONE);
            this.edit_tags_content       .setVisibility(View.GONE);

            // aggiorno la parte non editabile
            this.recipe_title       .setText(String.valueOf(this.edit_recipe_title.getText()));
            this.preparation_content.setText(String.valueOf(this.edit_preparation_content.getText()));
            this.cooking_content    .setText(String.valueOf(this.edit_cooking_content.getText()));
            this.portions_content   .setText(String.valueOf(this.edit_portions_content.getText()));

            this.recipe_title       .setVisibility(View.VISIBLE);
            this.preparation_content.setVisibility(View.VISIBLE);
            this.cooking_content    .setVisibility(View.VISIBLE);
            this.portions_content   .setVisibility(View.VISIBLE);
            this.difficulty_content .setVisibility(View.VISIBLE);
            this.tags_content       .setVisibility(View.VISIBLE);
            updateRecipe();
        }

        private void updateRecipe(){
            this.mRecipe.setTitle           (String.valueOf(this.edit_recipe_title.getText()));
            this.mRecipe.setPreparation_time(String.valueOf(this.edit_preparation_content.getText()));
            this.mRecipe.setCooking_time    (String.valueOf(this.edit_cooking_content.getText()));
            this.mRecipe.setPortions        (String.valueOf(this.edit_portions_content.getText()));
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

            setUserVisibleHint(false);

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
            if (isEditMode()) {
            }
        }

        @Override
        public void putEditModeOn() {
            super.putEditModeOn();
            mIngredients.add(new Ingredient(""));
            mIngredientsRecyclerAdapter.notifyDataSetChanged();
            mIngredientsRecyclerAdapter.putEditModeOn();
       }

        @Override
        public void putEditModeOff() {
            super.putEditModeOff();
            List<Ingredient> toRemove = new ArrayList<>();
            for (Ingredient i:
                 mIngredients) {
               if (i.getDescription().equals("")) {
                   toRemove.add(i);
               }
            }
            mIngredients.removeAll(toRemove);
            mIngredientsRecyclerAdapter.notifyDataSetChanged();
            mIngredientsRecyclerAdapter.putEditModeOff();
        }


        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
        }

        @Override
        public boolean getUserVisibleHint() {
            Log.d("DEBUG", "now visible");
            return super.getUserVisibleHint();
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
            Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        void putEditModeOn() {
            this.mEditMode = true;
            // TODO verificare che non ci sia la possibilta di perdere le modifiche fatte entrando
            // TODO due volte nella EditMode
        }

        void putEditModeOff() {
            this.mEditMode = false;
        }
    }
}
