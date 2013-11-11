package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.jooik.nibbelboard.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A visual interface enabling the user to use the currently selected sound as a
 * ic_ringtone...
 */
public class FragmentRingtone extends DialogFragment
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private int rawSound;
    private String rawSoundLabel;
    private String rawSoundFilename;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_ringtone_title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.dialog_ringtone_options, null,
                        new DialogInterface.OnMultiChoiceClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked)
                            {
                                if (isChecked)
                                {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which))
                                {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.but_save, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if (!applySoundToSystemSettings(selectedItems, rawSound))
                        {
                            Toast.makeText(getActivity(), R.string.ex_configureSound, Toast.LENGTH_LONG).show();
                        } else
                        {
                            Toast.makeText(getActivity(), R.string.suc_configureSound, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.but_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // ...do nothing as cancel is handled by default
                    }
                });

        builder.setIcon(R.drawable.ic_ringtone);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    /**
     * Fetch currently selected raw file and copy to SD-location - next
     * add entry to system DB pointing to this location as it is impossible
     * to read and play sounds rom your app's raw directory directly.
     * @param selectedItems
     * @param ressound
     * @return
     */
    public boolean applySoundToSystemSettings(ArrayList<Integer> selectedItems, int ressound)
    {
        byte[] buffer=null;
        InputStream fIn = getActivity().getBaseContext().getResources().openRawResource(ressound);
        int size=0;

        try
        {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }

        String path="/sdcard/media/audio/ringtones/";
        String filename=rawSoundFilename+".ogg";
        String sdCardPath = path + filename;

        boolean exists = (new File(path)).exists();
        if (!exists){new File(path).mkdirs();}

        FileOutputStream save;
        try {
            save = new FileOutputStream(path+filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + sdCardPath)));

        File chosenFile = new File(path, filename);

        //We now create a new content values object to store all the information
        //about the ringtone.
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, chosenFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, rawSoundLabel);
        values.put(MediaStore.MediaColumns.SIZE, chosenFile.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
        values.put(MediaStore.Audio.AudioColumns.ARTIST, getActivity().getString(R.string.app_name));
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, selectedItems.contains(0));
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, selectedItems.contains(1));
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM, selectedItems.contains(2));
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, false);

        //Work with the content resolver now
        //First get the file we may have added previously and delete it,
        //otherwise we will fill up the ringtone manager with a bunch of copies over time.
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(chosenFile.getAbsolutePath());
        getActivity().getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + chosenFile.getAbsolutePath() + "\"", null);

        //Ok now insert it
        Uri newUri = getActivity().getContentResolver().insert(uri, values);

        //Ok now set the ringtone from the content manager's uri, NOT the file's uri
        try
        {
            if (selectedItems.contains(0))
            {
                RingtoneManager.setActualDefaultRingtoneUri(
                        getActivity(),
                        RingtoneManager.TYPE_RINGTONE,
                        newUri
                );
            }
            if (selectedItems.contains(1))
            {
                RingtoneManager.setActualDefaultRingtoneUri(
                        getActivity(),
                        RingtoneManager.TYPE_NOTIFICATION,
                        newUri
                );
            }
            if (selectedItems.contains(2))
            {
                RingtoneManager.setActualDefaultRingtoneUri(
                        getActivity(),
                        RingtoneManager.TYPE_ALARM,
                        newUri
                );
            }
            // special treatment for "send sound case..."
            if (selectedItems.contains(3))
            {
                FragmentRingtoneSend frs = new FragmentRingtoneSend();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                frs.setSoundURI(Uri.parse("file://" + sdCardPath));
                frs.show(fm,"fragment_dialog_ringtone_send");

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    // ------------------------------------------------------------------------
    // GETTER & SETTER
    // ------------------------------------------------------------------------

    public int getRawSound()
    {
        return rawSound;
    }

    public void setRawSound(int rawSound)
    {
        this.rawSound = rawSound;
    }

    public String getRawSoundLabel()
    {
        return rawSoundLabel;
    }

    public void setRawSoundLabel(String rawSoundLabel)
    {
        this.rawSoundLabel = rawSoundLabel;
    }

    public String getRawSoundFilename()
    {
        return rawSoundFilename;
    }

    public void setRawSoundFilename(String rawSoundFilename)
    {
        this.rawSoundFilename = rawSoundFilename;
    }
}