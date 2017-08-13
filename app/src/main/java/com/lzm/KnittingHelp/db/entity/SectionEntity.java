package com.lzm.KnittingHelp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.lzm.KnittingHelp.db.exceptions.PartException;
import com.lzm.KnittingHelp.db.exceptions.SectionException;
import com.lzm.KnittingHelp.db.exceptions.StepException;
import com.lzm.KnittingHelp.model.Pattern;
import com.lzm.KnittingHelp.model.Section;
import com.lzm.KnittingHelp.model.Step;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "sections")
public class SectionEntity implements Section {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private int patternId;
    private int order;
    private int activePartId;

    @Ignore
    private List<PartEntity> parts;
    @Ignore
    private int activePartIndex;
    @Ignore
    private PatternEntity pattern;

    public SectionEntity() {

    }

    @Ignore
    public SectionEntity(PatternEntity pattern, String content) {
        this.patternId = pattern.getId();
        this.content = content;
        this.pattern = pattern;
        initializeParts();
    }

    @Ignore
    public SectionEntity(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.content = section.getContent();
        this.patternId = section.getPatternId();
        this.order = section.getOrder();
        this.activePartId = section.getActivePartId();
        initializeParts();
    }

    private void initializeParts() {
        activePartIndex = -1;
        parts = new ArrayList<>();
        String[] parts = content.split("\n");
        boolean isFirstLine = true;
        int order = 1;
        for (String part : parts) {
            part = part.trim();
            if (isFirstLine) {
                title = part;
                isFirstLine = false;
            } else {
                PartEntity newPart = new PartEntity(this, part);
                newPart.setOrder(order);
                this.parts.add(newPart);
                order += 1;
            }
        }
    }

    public List<PartEntity> getParts() {
        return this.parts;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public int getPatternId() {
        return this.patternId;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public int getActivePartId() {
        return this.activePartId;
    }

    public PatternEntity getPattern() {
        return pattern;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setActivePartId(int activePartId) {
        this.activePartId = activePartId;
    }

    public void start() {
        first();
    }

    public void first() {
        activePartIndex = 0;
        try {
            getActivePart().start();
        } catch (SectionException ignored) {

        }
    }

    public void last() throws SectionException {
        activePartIndex = parts.size() - 1;
        getActivePart().last();
    }

    public void next() throws SectionException, StepException {
        checkInitialized();

        try {
            getActivePart().next();
        } catch (PartException | StepException e) {
            if (activePartIndex == parts.size() - 1) {
                throw new StepException("Next step not found");
            }
            activePartIndex += 1;
            getActivePart().start();
        }
    }

    public void prev() throws StepException, SectionException {
        checkInitialized();

        try {
            getActivePart().prev();
        } catch (PartException | StepException e) {
            if (activePartIndex == 0) {
                throw new StepException("Prev step not found");
            }
            activePartIndex -= 1;
            getActivePart().last();
        }
    }

    public PartEntity getActivePart() throws SectionException {
        checkInitialized();

        return parts.get(activePartIndex);
    }

    private void checkInitialized() throws SectionException {
        if (activePartIndex == -1) {
            throw new SectionException("Section not initialized!");
        }
    }

    public StepEntity getActiveStep() throws PartException, SectionException {
        return getActivePart().getActiveStep();
    }

    public void setSelected(int partOrder, int stepOrder) {
        try {
            activePartIndex = partOrder - 1;
            getActivePart().setSelected(stepOrder);
        } catch (SectionException e) {
            e.printStackTrace();
        }
    }
}

