package com.youknow.timeisgold.view.activity.details.manual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youknow.timeisgold.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManualInputFragment extends Fragment {


    public ManualInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_input, container, false);
    }

}
