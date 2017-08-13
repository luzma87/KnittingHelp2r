package com.lzm.KnittingHelp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.lzm.KnittingHelp.model.Step;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "steps")
public class StepEntity implements Step {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private int partId;
    private int order;

    @Ignore
    private Separator separator;
    @Ignore
    private PartEntity part;

    public StepEntity() {

    }

    @Ignore
    public StepEntity(PartEntity part, String content) {
        this.partId = part.getId();
        this.content = content;
        this.part = part;
    }

    @Ignore
    public StepEntity(Step step) {
        this.id = step.getId();
        this.content = step.getContent();
        this.partId = step.getPartId();
        this.order = step.getOrder();
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
    public int getPartId() {
        return this.partId;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<StepEntity> split() {
        List<StepEntity> steps = new ArrayList<>();

        String[] parts = content.split(separator.getForSplit());
        for (int i = 0; i < parts.length; i++) {
            String stepContent = parts[i].trim();
            if (separator.isAfter() && i < parts.length - 1) {
                stepContent = stepContent + separator.getSeparator();
            }
            if (i == 0 && !separator.isAfter()) {
                stepContent = separator.getSeparator() + stepContent;
            }
            if (i == 0) {
                this.content = stepContent;
            }
            StepEntity step = new StepEntity(this.part, stepContent);
            step.setOrder(i + 1);
            steps.add(step);
        }
        return steps;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }
}
