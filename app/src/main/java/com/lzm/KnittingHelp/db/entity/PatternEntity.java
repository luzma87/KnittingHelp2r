package com.lzm.KnittingHelp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.lzm.KnittingHelp.db.exceptions.PartException;
import com.lzm.KnittingHelp.db.exceptions.PatternException;
import com.lzm.KnittingHelp.db.exceptions.SectionException;
import com.lzm.KnittingHelp.db.exceptions.StepException;
import com.lzm.KnittingHelp.model.Pattern;
import com.lzm.KnittingHelp.model.Section;
import com.lzm.KnittingHelp.model.Step;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "patterns")
public class PatternEntity implements Pattern {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String content;
    private Date creationDate;
    private Date updateDate;
    private int activeSectionId;

    @Ignore
    private List<SectionEntity> sections;
    @Ignore
    private int activeSectionIndex;

    public PatternEntity(String name, String content) {
        this.name = name;
        this.content = content;
        this.creationDate = new Date();
        this.updateDate = new Date();
        initializeSections();
    }

    public PatternEntity(Pattern pattern) {
        this.id = pattern.getId();
        this.name = pattern.getName();
        this.content = pattern.getContent();
        this.creationDate = pattern.getCreationDate();
        this.updateDate = pattern.getUpdateDate();
        this.activeSectionId = pattern.getActiveSectionId();
        initializeSections();
    }

    private void initializeSections() {
        activeSectionId = -1;
        sections = new ArrayList<>();

        String[] parts = content.split("\n\n");
        int order = 1;
        for (String part : parts) {
            SectionEntity section = new SectionEntity(this, part);
            section.setOrder(order);
            sections.add(section);
            order += 1;
        }
    }

    public List<SectionEntity> getSections() {
        return sections;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public Date getUpdateDate() {
        return this.updateDate;
    }

    @Override
    public int getActiveSectionId() {
        return this.activeSectionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setActiveSectionId(int activeSectionId) {
        this.activeSectionId = activeSectionId;
    }


    public void start() {
        first();
    }

    public void first() {
        activeSectionIndex = 0;
        try {
            getActiveSection().start();
        } catch (PatternException ignored) {
        }
    }

    public SectionEntity getActiveSection() throws PatternException {
        checkInitialized();
        return sections.get(activeSectionIndex);
    }

    public StepEntity getActiveStep() throws PartException, SectionException, PatternException {
        checkInitialized();
        return getActiveSection().getActiveStep();
    }

    public void nextPart() throws SectionException, PatternException {
        checkInitialized();

        try {
            getActiveSection().next();
        } catch (StepException e) {
            nextSection();
        }
    }

    public void prevPart() throws SectionException, PatternException {
        checkInitialized();

        try {
            getActiveSection().prev();
        } catch (StepException e) {
            prevSection();
            getActiveSection().last();
        }
    }

    public void nextSection() throws SectionException, PatternException {
        if (activeSectionIndex == sections.size() - 1) {
            throw new SectionException("Next section not found");
        }
        activeSectionIndex += 1;
        getActiveSection().start();
    }

    public void prevSection() throws SectionException, PatternException {
        checkInitialized();

        if (activeSectionIndex == 0) {
            throw new SectionException("Prev section not found");
        }
        activeSectionIndex -= 1;
    }

    public void setSelected(int sectionOrder, int partOrder, int stepOrder) {
        try {
            activeSectionIndex = sectionOrder - 1;
            getActiveSection().setSelected(partOrder, stepOrder);
        } catch (PatternException e) {
            e.printStackTrace();
        }
    }

    private void checkInitialized() throws PatternException {
        if (activeSectionIndex == -1) {
            throw new PatternException("Pattern not initialized!");
        }
    }
}
