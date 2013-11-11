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

import com.jooik.nibbelboard.MainActivity;
import com.jooik.nibbelboard.R;
import com.jooik.nibbelboard.com.jooik.nibbelboard.domain.SoundHolder;

/**
 * Fragemnet counter part for square button. Some important fragment magic is
 * performed within this class, for example XML fragment attributes are transformed
 * to class properties directly...have a cloaser look!
 */
public class FragmentSquareButton extends Fragment
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private View view = null;
    private SoundHolder soundHolder = new SoundHolder();

    private SoundPool soundPool = null;
    private boolean loaded = false;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view =  inflater.inflate(R.layout.fragment_button, container, false);

        // fetch image component and apply icon...
        if (soundHolder.getSoundImageFile() != null)
        {
            ImageView iv = (ImageView)view.findViewById(R.id.iv_icon);

            Resources res = getActivity().getResources();
            int resID = res.getIdentifier(soundHolder.getSoundImageFile(), "drawable", getActivity().getPackageName());
            iv.setImageResource(resID);
        }

        if (soundHolder.getSoundLabel() != null)
        {
            TextView label = (TextView)view.findViewById(R.id.tv_buttonlabel);
            label.setText(soundHolder.getSoundLabel());
        }

        if (soundHolder.getSoundFile() != null)
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
                int resID = res.getIdentifier(soundHolder.getSoundFile(), "raw", getActivity().getPackageName());
                try
                {
                    soundHolder.setSoundID(soundPool.load(getActivity().getApplicationContext(),resID, 1));
                }
                catch (Resources.NotFoundException ex)
                {
                    // set sound to default sound...
                    // TODO: shift default sound to configuration file....
                    resID = res.getIdentifier("tourette_tourette01", "raw", getActivity().getPackageName());
                    soundHolder.setSoundID(soundPool.load(getActivity().getApplicationContext(),resID, 1));
                }
            }

            LinearLayout box = (LinearLayout)view.findViewById(R.id.ll_box);

            // add long touch support
            final GestureDetector gdt = new GestureDetector(getActivity(),new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public void onLongPress(MotionEvent e)
                {
                    // get the currently selected sound URI
                    int rawIdSoundFile = getActivity().getResources().getIdentifier(soundHolder.getSoundFile(), "raw", getActivity().getPackageName());

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentRingtone fr = new FragmentRingtone();
                    fr.setRawSound(rawIdSoundFile);
                    fr.setRawSoundLabel(soundHolder.getSoundLabel());
                    fr.setRawSoundFilename(soundHolder.getSoundFile());
                    fr.show(fm,"fragment_dialog_ringtone");
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e)
                {
                    // play sound
                    // Getting the user sound settings
                    AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                    float volume = (float) audioManager
                            .getStreamVolume(AudioManager.STREAM_SYSTEM);
                    // Is the sound loaded already?
                    if (loaded)
                    {
                        soundPool.play(soundHolder.getSoundID(), volume, volume, 1, 0, 1f);
                    }

                    return true;
                }
            });

            box.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    boolean retVal =  gdt.onTouchEvent(event);

                    // notify MainActivity about event - handle delection etc.
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.onPressSquareButton(view,soundHolder);

                    view.setSelected(true);

                    return retVal;

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
            soundHolder.setSoundImageFile(passedInString.toString());
        }
        passedInString = a.getText(R.styleable.FragmentSquareButton_button_label);
        if(passedInString != null) {
            soundHolder.setSoundLabel(passedInString.toString());
        }
        passedInString = a.getText(R.styleable.FragmentSquareButton_sound_string);
        if(passedInString != null) {
            soundHolder.setSoundFile(passedInString.toString());
        }

        // read and apply integer values...currently used for demo log statements only
        int passedInteger = a.getInt(R.styleable.FragmentSquareButton_demo_integer, -1);
        if(passedInteger != -1) {
            Log.v("INFO","My Integer Received :" + passedInteger);
        }

        a.recycle();
    }

    // ------------------------------------------------------------------------
    // GETTER & SETTER
    // ------------------------------------------------------------------------


}