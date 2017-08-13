package com.lzm.KnittingHelp.notebook;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzm.KnittingHelp.R;
import com.lzm.KnittingHelp.databinding.NotebookFragmentBinding;
import com.lzm.KnittingHelp.db.entity.PatternEntity;
import com.lzm.KnittingHelp.model.Pattern;

import java.util.List;

public class NotebookFragment extends LifecycleFragment {

    public static final String TAG = "NotebookFragment";

    private NotebookAdapter notebookAdapter;

    private NotebookFragmentBinding binding;

    public NotebookFragment() {
    }

    public static NotebookFragment newInstance() {
        return new NotebookFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.notebook_fragment, container, false);

        notebookAdapter = new NotebookAdapter(patternClickCallback);
        binding.patternsList.setAdapter(notebookAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final NotebookViewModel viewModel =
                ViewModelProviders.of(this).get(NotebookViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(NotebookViewModel viewModel) {
        viewModel.getPatterns().observe(this, new Observer<List<PatternEntity>>() {
            @Override
            public void onChanged(@Nullable List<PatternEntity> myPatterns) {
                if (myPatterns != null) {
                    binding.setIsLoading(false);
                    notebookAdapter.setPatternList(myPatterns);
                } else {
                    binding.setIsLoading(true);
                }
            }
        });
    }

    private final PatternClickCallback patternClickCallback = new PatternClickCallback() {
        @Override
        public void onClick(Pattern pattern) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
//                ((MainActivity) getActivity()).show(pattern);
            }
        }
    };
}
