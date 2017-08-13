package com.lzm.KnittingHelp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.lzm.KnittingHelp.db.exceptions.PartException;
import com.lzm.KnittingHelp.db.exceptions.StepException;
import com.lzm.KnittingHelp.model.Part;
import com.lzm.KnittingHelp.model.Step;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "parts")
public class PartEntity implements Part {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private int sectionId;
    private int order;
    private int activeStepId;

    @Ignore
    private List<StepEntity> steps;
    @Ignore
    private int activeStepIndex;

    public PartEntity() {

    }

    @Ignore
    public PartEntity(SectionEntity section, String content) {
        this.sectionId = section.getId();
        this.content = content;
        initializeSteps();
    }

    @Ignore
    public PartEntity(Part part) {
        this.id = part.getId();
        this.content = part.getContent();
        this.sectionId = part.getSectionId();
        this.order = part.getOrder();
        this.activeStepId = part.getActiveStepId();
        initializeSteps();
    }

    private void initializeSteps() {
        this.steps = new ArrayList<>();
        StepEntity step = new StepEntity(this, content);
        step.setOrder(1);
        steps.add(step);
        this.activeStepIndex = -1;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public int getSectionId() {
        return this.sectionId;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public int getActiveStepId() {
        return this.activeStepId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setActiveStepId(int activeStepId) {
        this.activeStepId = activeStepId;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public void split(int position, Separator separator) {
        StepEntity step = steps.remove(position);
        step.setSeparator(separator);
        steps.addAll(position, step.split());
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setOrder(i + 1);
        }
    }

    public void next() throws PartException, StepException {
        checkInitialized();

        if (steps.size() == 1) {
            throw new StepException("No steps found");
        }
        if (activeStepIndex == steps.size() - 1) {
            throw new StepException("Next step not found");
        }
        activeStepIndex += 1;
    }

    public void prev() throws PartException, StepException {
        checkInitialized();

        if (steps.size() == 1) {
            throw new StepException("No steps found");
        }
        if (activeStepIndex == 0) {
            throw new StepException("Prev step not found");
        }
        activeStepIndex -= 1;
    }

    public StepEntity getActiveStep() throws PartException {
        checkInitialized();

        return steps.get(activeStepIndex);
    }

    private void checkInitialized() throws PartException {
        if (activeStepIndex == -1) {
            throw new PartException("Part not initialized!");
        }
    }

    public void start() {
        first();
    }

    public void first() {
        activeStepIndex = 0;
    }

    public void last() {
        activeStepIndex = steps.size() - 1;
    }

    public void setSelected(int partOrder) {
        activeStepIndex = partOrder - 1;
    }

}
