package nyriu.ricettavola;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.util.ArrayList;

import nyriu.ricettavola.adapters.IngredientsRecyclerAdapter;
import nyriu.ricettavola.models.Ingredient;
import nyriu.ricettavola.util.VerticalSpacingItemDecorator;

public class RecipeActivity extends AppCompatActivity implements
        View.OnClickListener
{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // UI
    private ImageButton mBackArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.summary_fragment_recipe, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }


    /**
     * A fragment containing recipe summary
     */
    public static class IngredientsFragment extends Fragment implements
        IngredientsRecyclerAdapter.OnIngredientListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        // Ui compontents
        private RecyclerView mRecyclerView;

        // vars
        private ArrayList<Ingredient> mIngredients; // TODO spostare nella IngredientActivity e passarlo qua alla creazione
        private IngredientsRecyclerAdapter mIngredientsRecyclerAdapter;
        //private NoteRepository mNoteRepository; // TODO


        public IngredientsFragment() {
                this.mIngredients = new ArrayList<>();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static IngredientsFragment newInstance() {
            IngredientsFragment fragment = new IngredientsFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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

            insertFakeIngredients(10);
        }

        private void insertFakeIngredients(int num) {
            for (int i=0; i<num; i++) {
                Ingredient note = new Ingredient("Ingredient #" + i);
                mIngredients.add(note);
            }
            mIngredientsRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onIngredientClick(int position) {
            Log.d("DEBUG", "onIngredientClick: " + position);
            Toast.makeText(getContext(), "ViewHolder Clicked!" + position,Toast.LENGTH_SHORT).show();
        }
    }


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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO qua va messa logica per schermate differenti per ricetta e lista della spesa
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case POSITION_SUMMARY:
                    return SummaryFragment.newInstance();
                case POSITION_INGREDIENTS:
                    return IngredientsFragment.newInstance();
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
