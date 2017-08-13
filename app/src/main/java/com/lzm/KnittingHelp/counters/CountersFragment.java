package com.lzm.KnittingHelp.counters;

import android.arch.lifecycle.LifecycleFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzm.KnittingHelp.R;

public class CountersFragment extends LifecycleFragment {

    public static final String TAG = "CountersFragment";

    public CountersFragment() {
    }

    public static CountersFragment newInstance() {
        return new CountersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.counters_fragment, container, false);
    }

}
