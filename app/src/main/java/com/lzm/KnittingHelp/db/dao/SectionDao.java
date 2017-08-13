package com.lzm.KnittingHelp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lzm.KnittingHelp.db.entity.PatternEntity;
import com.lzm.KnittingHelp.db.entity.SectionEntity;

import java.util.List;

@Dao
public interface SectionDao {
    @Query("SELECT * FROM sections")
    LiveData<List<SectionEntity>> loadAllSections();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SectionEntity> sections);

    @Query("select * from sections where id = :sectionId")
    LiveData<SectionEntity> loadSection(int sectionId);

    @Query("select * from sections where id = :sectionId")
    SectionEntity loadSectionSync(int sectionId);
}
