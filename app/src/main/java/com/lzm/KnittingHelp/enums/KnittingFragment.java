package com.lzm.KnittingHelp.enums;

import com.lzm.KnittingHelp.R;

public enum KnittingFragment {
    NOTEBOOK(R.string.title_notebook),
    PATTERN(R.string.title_pattern),
    COUNTERS(R.string.title_counters);

    private int titleId;

    KnittingFragment(final int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}
