package com.lzm.KnittingHelp.model;

import java.util.Date;

public interface Pattern {
    int getId();

    String getName();

    String getContent();

    Date getCreationDate();

    Date getUpdateDate();

    int getActiveSectionId();
}
