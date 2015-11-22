package com.quintodg.mayr.viewpager1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;//that will host the section contents.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         */

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);


        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(1, ContextCompat.getColor(this.getApplicationContext(),R.color.android_blue)));
        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(5, getResources().getColor(R.color.android_darkpurple)));
        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(2, getResources().getColor(R.color.android_orange)));
        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(13, ContextCompat.getColor(getApplicationContext(), R.color.android_red)));

        mViewPager.setAdapter(mSectionsPagerAdapter);


        /**
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */

    }

    /**

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


    */

    /** A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages. Contains all the fragments
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragment;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragment.size();
        }

        public void addFragment(Fragment xfragment){
            this.fragment.add(xfragment);
        }

        /* ---------
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
         */
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String BACKGROUND_COLOR = "color";

        private int section_number;
        private int color;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int color) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(BACKGROUND_COLOR, color);

            fragment.setArguments(args);
            fragment.setRetainInstance(true);// para que no se pierda los valores de la instancia

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //cuando crea una instancia de tipo PlaceholderFragment
            //si lo enviamos parametros, guarda esos
            //si no le envio nada, toma el color gris y un número aleatroio
            if(getArguments() != null){
                this.section_number = getArguments().getInt(ARG_SECTION_NUMBER);
                this.color = getArguments().getInt(BACKGROUND_COLOR);
            }
            else {
                this.section_number = (int)(Math.random() * 5);
                this.color = Color.GRAY;
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            TextView mm_section = (TextView) rootView.findViewById(R.id.mm_section_label);


            mm_section.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //mm_section.setText(R.string.section_format);
            //mm_section.setText(""+this.section_number);

            rootView.setBackgroundColor(color);

            return rootView;
        }



    }

    /** Called when the user clicks the Send button (view) */
    public void recordAudio(View view){
        Intent intent = new Intent(this, RecordingActivity.class);

        startActivity(intent);

    }
}
