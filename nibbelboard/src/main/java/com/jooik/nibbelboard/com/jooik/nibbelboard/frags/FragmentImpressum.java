package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.LinearLayout;

import com.jooik.nibbelboard.R;

/**
 * Fragmemt holding the impressum.
 */
public class FragmentImpressum extends DialogFragment
{
    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------

    private View view = null;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Impressum");
        builder.setIcon(R.drawable.app_impressum);

        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_impressum, null);
        builder.setView(view);

        // follow twitter intent...
        LinearLayout twitterButton = (LinearLayout)view.findViewById(R.id.ll_twitter_button);
        twitterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=_flomueller")));
                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/_flomueller")));
                }
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}