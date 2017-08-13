package com.lzm.KnittingHelp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lzm.KnittingHelp.db.entity.StepEntity;

import java.util.List;

@Dao
public interface StepDao {
    @Query("SELECT * FROM steps")
    LiveData<List<StepEntity>> loadAllSteps();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StepEntity> steps);

    @Query("select * from steps where id = :stepId")
    LiveData<StepEntity> loadStep(int stepId);

    @Query("select * from steps where id = :stepId")
    StepEntity loadStepSync(int stepId);
}
