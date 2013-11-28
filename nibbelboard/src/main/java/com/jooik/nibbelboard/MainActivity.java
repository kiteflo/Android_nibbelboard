package com.jooik.nibbelboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jooik.nibbelboard.com.jooik.nibbelboard.domain.SoundHolder;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentFirstTab;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentImpressum;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentRingtone;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentSecondTab;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentThirdTab;
import com.jooik.nibbelboard.com.jooik.nibbelboard.frags.FragmentVolume;

public class MainActivity extends ActionBarActivity
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    // ------------------------------------------------------------------------
    // pseudo model stuff....
    // ------------------------------------------------------------------------

    private View currentlySelectedButton;
    private SoundHolder soundHolder;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_volume:
            {
                // display volume fragment
                FragmentManager fm = getSupportFragmentManager();
                FragmentVolume fv = new FragmentVolume();
                fv.show(fm,"fragment_dialog_volume");
                break;
            }
            case R.id.action_impressum:
            {
                // display volume fragment
                FragmentManager fm = getSupportFragmentManager();
                FragmentImpressum fi = new FragmentImpressum();
                fi.show(fm,"fragment_dialog_impressum");
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Deselct current square...
     * @param view
     */
    public void onPressSquareButton(View view,SoundHolder soundHolder)
    {
        if (currentlySelectedButton != null)
        {
            currentlySelectedButton.setSelected(false);
        }

        this.soundHolder = soundHolder;
        currentlySelectedButton = view;
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e)
    {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:

                if (currentlySelectedButton != null)
                {
                    // display sound settings dialog...
                    int rawIdSoundFile = getResources().getIdentifier(soundHolder.getSoundFile(), "raw", getPackageName());

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentRingtone fr = new FragmentRingtone();
                    fr.setRawSound(rawIdSoundFile);
                    fr.setRawSoundLabel(soundHolder.getSoundLabel());
                    fr.setRawSoundFilename(soundHolder.getSoundFile());
                    fr.show(fm,"fragment_dialog_ringtone");

                    return true;
                }
        }

        return super.onKeyDown(keycode, e);
    }


    // ------------------------------------------------------------------------
    // inner classes
    // ------------------------------------------------------------------------

    /**
     * Used for tab paging...
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                // find first fragment...
                FragmentFirstTab fft = new FragmentFirstTab();
                return fft;
            }
            if (position == 1)
            {
                // find first fragment...
                FragmentSecondTab fft = new FragmentSecondTab();
                return fft;
            }
            else if (position == 2)
            {
                // find first fragment...
                FragmentThirdTab fst = new FragmentThirdTab();
                return fst;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_movies);
                case 1:
                    return getString(R.string.tab_tourette);
                case 2:
                    return getString(R.string.tab_borat);
            }
            return null;
        }
    }

}
