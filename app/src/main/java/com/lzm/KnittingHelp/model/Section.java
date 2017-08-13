package com.lzm.KnittingHelp.model;

public interface Section {
    int getId();

    String getTitle();

    String getContent();

    int getPatternId();

    int getOrder();

    int getActivePartId();
}
