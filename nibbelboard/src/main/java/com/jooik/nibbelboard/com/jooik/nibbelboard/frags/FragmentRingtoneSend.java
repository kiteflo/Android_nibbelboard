package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.jooik.nibbelboard.R;

import java.util.ArrayList;

/**
 * A visual interface enabling the user how to send the ringtone to the
 * universe...
 */
public class FragmentRingtoneSend extends DialogFragment
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private Uri soundURI;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> selectedItem = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_send_title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.dialog_send_options,-1,new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        selectedItem.add(which);
                    }
                })
                // Set the action buttons
                .setPositiveButton(R.string.but_save, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if (!shareSoundWithTheUniverse(selectedItem))
                        {
                            Toast.makeText(getActivity(), R.string.ex_shareSound, Toast.LENGTH_LONG).show();
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

        builder.setIcon(R.drawable.ic_ecard);
        return builder.create();
    }

    public boolean shareSoundWithTheUniverse(ArrayList<Integer> selectedItem)
    {
        // whatsApp
        if (selectedItem.contains(0))
        {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("audio/ogg");
            String text = "Listen to this sound...";
            waIntent.setPackage("com.whatsapp");
            if (waIntent != null) {
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                waIntent.putExtra(Intent.EXTRA_STREAM,soundURI);
                startActivity(Intent.createChooser(waIntent, "Share with"));
            } else {
                Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        // E-Mail
        else if (selectedItem.contains(1))
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("audio/ogg");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"kiteflo@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Listen to this sound...");
            intent.putExtra(Intent.EXTRA_TEXT, "haha - funny sound provided by Android business nibbelboard :-)");
            intent.putExtra(Intent.EXTRA_STREAM, soundURI);
            startActivity(intent);
        }

        return true;
    }

    // ------------------------------------------------------------------------
    // GETTER & SETTER
    // ------------------------------------------------------------------------

    public Uri getSoundURI()
    {
        return soundURI;
    }

    public void setSoundURI(Uri soundURI)
    {
        this.soundURI = soundURI;
    }
}