package com.lzm.KnittingHelp.db;

import com.lzm.KnittingHelp.db.entity.PartEntity;
import com.lzm.KnittingHelp.db.entity.PatternEntity;
import com.lzm.KnittingHelp.db.entity.SectionEntity;
import com.lzm.KnittingHelp.db.entity.StepEntity;

import java.util.ArrayList;
import java.util.List;

class DatabaseInitUtil {

    static void initializeDb(AppDatabase db) {
        List<PatternEntity> patterns = new ArrayList<>();
        List<SectionEntity> sections = new ArrayList<>();
        List<PartEntity> parts = new ArrayList<>();
        List<StepEntity> steps = new ArrayList<>();

        patterns.add(StarterPatterns.tmntPattern());
        patterns.add(StarterPatterns.splinterPattern());

        for (PatternEntity pattern : patterns) {
            sections.addAll(pattern.getSections());

            for (SectionEntity section : sections) {
                parts.addAll(section.getParts());

                for (PartEntity part : parts) {
                    steps.addAll(part.getSteps());
                }
            }
        }

        insertData(db, patterns, sections, parts, steps);
    }

    private static void insertData(AppDatabase db,
                                   List<PatternEntity> patterns,
                                   List<SectionEntity> sections,
                                   List<PartEntity> parts,
                                   List<StepEntity> steps) {
        db.beginTransaction();
        try {
            db.patternDao().insertAll(patterns);
            db.sectionDao().insertAll(sections);
            db.partDao().insertAll(parts);
            db.stepDao().insertAll(steps);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
