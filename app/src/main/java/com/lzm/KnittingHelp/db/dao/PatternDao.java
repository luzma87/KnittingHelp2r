package com.lzm.KnittingHelp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lzm.KnittingHelp.db.entity.PatternEntity;

import java.util.List;

@Dao
public interface PatternDao {
    @Query("SELECT * FROM patterns")
    LiveData<List<PatternEntity>> loadAllPatterns();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PatternEntity> patterns);

    @Query("select * from patterns where id = :patternId")
    LiveData<PatternEntity> loadPattern(int patternId);

    @Query("select * from patterns where id = :patternId")
    PatternEntity loadPatternSync(int patternId);
}
