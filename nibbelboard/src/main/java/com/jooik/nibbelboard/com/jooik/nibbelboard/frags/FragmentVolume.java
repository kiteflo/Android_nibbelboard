package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jooik.nibbelboard.R;

/**
 * Fragmemt holding a seekbar in order to adjust audio volume.
 */
public class FragmentVolume extends DialogFragment
{
    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------

    private View view = null;
    private AudioManager audioManager = null;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_volume);
        builder.setIcon(R.drawable.ic_audio_volume_slider);

        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_volume, null);
        builder.setView(view);

        // get current volume and apply to button...
        if (audioManager == null)
        {
            audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        }

        float volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);

        SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setMax(new Float(maxVolume).intValue());
        seekBar.setProgress(new Float(volume).intValue());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                // apply value to system...
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,seekBar.getProgress(),
                        AudioManager.FLAG_PLAY_SOUND);

                // display current volume...
                Toast.makeText(getActivity().getApplicationContext(), "" + seekBar.getProgress(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}