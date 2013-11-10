package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jooik.nibbelboard.R;

/**
 * Created by tzhmufl2 on 06.11.13.
 */
public class FragmentSquareButton extends Fragment
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private View view = null;
    private String imageSource = null;
    private String buttonLabel = null;

    private SoundPool soundPool = null;
    private String sound = null;
    private int soundID;
    boolean loaded = false;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view =  inflater.inflate(R.layout.fragment_button, container, false);

        // fetch image component and apply icon...
        if (imageSource != null)
        {
            ImageView iv = (ImageView)view.findViewById(R.id.iv_icon);

            Resources res = getActivity().getResources();
            int resID = res.getIdentifier(imageSource, "drawable", getActivity().getPackageName());
            iv.setImageResource(resID);
        }

        if (buttonLabel != null)
        {
            TextView label = (TextView)view.findViewById(R.id.tv_buttonlabel);
            label.setText(buttonLabel);
        }

        if (sound != null)
        {
            // initialize sound pool
            if (soundPool == null)
            {
                soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId,
                                               int status) {
                        loaded = true;
                    }
                });
                Resources res = getActivity().getResources();
                int resID = res.getIdentifier(sound, "raw", getActivity().getPackageName());
                try
                {
                    soundID = soundPool.load(getActivity().getApplicationContext(),resID, 1);
                }
                catch (Resources.NotFoundException ex)
                {
                    // set sound to default sound...
                    // TODO: shift default sound to configuration file....
                    resID = res.getIdentifier("tourette_tourette01", "raw", getActivity().getPackageName());
                    soundID = soundPool.load(getActivity().getApplicationContext(),resID, 1);
                }
            }

            LinearLayout box = (LinearLayout)view.findViewById(R.id.ll_box);

            // add long touch support
            final GestureDetector gdt = new GestureDetector(new GestureListener());
            box.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    gdt.onTouchEvent(event);
                    return true;
                }
            });
        }

        return view;
    }

    /**
     * Within this inflate routine we grap some parameters passed in to the fragment
     * via XML - that's a pretty smart way of passing attributes to a fragment as
     * otherwise shit fucking default consturctor automatic calls will not allow you
     * to do so...
     * @param activity
     * @param attrs
     * @param savedInstanceState
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState)
    {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs,R.styleable.FragmentSquareButton);

        // read and apply strings....
        CharSequence passedInString = a.getText(R.styleable.FragmentSquareButton_image_string);
        if(passedInString != null) {
            imageSource = passedInString.toString();
        }
        passedInString = a.getText(R.styleable.FragmentSquareButton_button_label);
        if(passedInString != null) {
            buttonLabel = passedInString.toString();
        }
        passedInString = a.getText(R.styleable.FragmentSquareButton_sound_string);
        if(passedInString != null) {
            sound = passedInString.toString();
        }

        // read and apply integer values...currently used for demo log statements only
        int passedInteger = a.getInt(R.styleable.FragmentSquareButton_demo_integer, -1);
        if(passedInteger != -1) {
            Log.v("INFO","My Integer Received :" + passedInteger);
        }

        a.recycle();
    }

    // ------------------------------------------------------------------------
    // inner classes
    // ------------------------------------------------------------------------

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public void onLongPress(MotionEvent e)
        {
            // get the currently selected sound URI
            int rawIdSoundFile = getActivity().getResources().getIdentifier(sound, "raw", getActivity().getPackageName());

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentRingtone fr = new FragmentRingtone();
            fr.setRawSound(rawIdSoundFile);
            fr.setRawSoundLabel(buttonLabel);
            fr.setRawSoundFilename(sound);
            fr.show(fm,"fragment_dialog_ringtone");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            // play sound
            // Getting the user sound settings
            AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
            float volume = (float) audioManager
                    .getStreamVolume(AudioManager.STREAM_SYSTEM);
            // Is the sound loaded already?
            if (loaded)
            {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                Log.e("Test", "Played sound");
            }
            return true;
        }
    }


    // ------------------------------------------------------------------------
    // GETTER & SETTER
    // ------------------------------------------------------------------------

    public String getImageSource()
    {
        return imageSource;
    }

    public void setImageSource(String imageSource)
    {
        this.imageSource = imageSource;
    }
}