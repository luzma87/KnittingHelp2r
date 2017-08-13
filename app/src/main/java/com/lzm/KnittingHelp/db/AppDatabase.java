package com.lzm.KnittingHelp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.lzm.KnittingHelp.db.converter.DateConverter;
import com.lzm.KnittingHelp.db.dao.PartDao;
import com.lzm.KnittingHelp.db.dao.PatternDao;
import com.lzm.KnittingHelp.db.dao.SectionDao;
import com.lzm.KnittingHelp.db.dao.StepDao;
import com.lzm.KnittingHelp.db.entity.PartEntity;
import com.lzm.KnittingHelp.db.entity.PatternEntity;
import com.lzm.KnittingHelp.db.entity.SectionEntity;
import com.lzm.KnittingHelp.db.entity.StepEntity;

@Database(entities = {
        PatternEntity.class,
        SectionEntity.class,
        PartEntity.class,
        StepEntity.class
},
        version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "knitting-help-db";

    public abstract PatternDao patternDao();

    public abstract SectionDao sectionDao();

    public abstract PartDao partDao();

    public abstract StepDao stepDao();
}
