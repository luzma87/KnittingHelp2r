package com.lzm.KnittingHelp.model;

public interface Part {
    int getId();

    String getContent();

    int getSectionId();

    int getOrder();

    int getActiveStepId();
}
