package com.jooik.nibbelboard.com.jooik.nibbelboard.frags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.jooik.nibbelboard.R;

/**
 * Created by tzhmufl2 on 06.11.13.
 */
public class FragmentThirdTab extends Fragment
{
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private  View view;
    private int rowHeight;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_borat_tab,container,false);

            // Lets do some magic in order to have so called "app_square buttons":
            // use dynamic width of a button (width is determined by device,
            // we dont have any clue which device and corresponding screen width
            // our app is running with), the apply width as a row height...

            // dynamically calculate button height for first row...
            final View topLeft = view.findViewById(R.id.frag_211);

            ViewTreeObserver vto = topLeft.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // simply set current row height to button width => should result in nice
                    // app_square buttons...
                    rowHeight = topLeft.getWidth();
                    applyRowHeightTowRow((LinearLayout) view.findViewById(R.id.ll_row21));
                    applyRowHeightTowRow((LinearLayout)view.findViewById(R.id.ll_row22));
                    applyRowHeightTowRow((LinearLayout)view.findViewById(R.id.ll_row23));
                    applyRowHeightTowRow((LinearLayout)view.findViewById(R.id.ll_row24));
                    applyRowHeightTowRow((LinearLayout)view.findViewById(R.id.ll_row25));
                }
            });
        }
        else
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        return view;
    }

    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------

    /**
     * Applies the global height of a app_square button to the row passed in.
     * @param row
     */
    private void applyRowHeightTowRow(LinearLayout row)
    {
        ViewGroup.LayoutParams params = row.getLayoutParams();
        params.height = rowHeight;
        row.setLayoutParams(params);
    }
}