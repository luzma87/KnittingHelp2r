package com.lzm.KnittingHelp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lzm.KnittingHelp.db.entity.PartEntity;
import com.lzm.KnittingHelp.db.entity.SectionEntity;

import java.util.List;

@Dao
public interface PartDao {
    @Query("SELECT * FROM parts")
    LiveData<List<PartEntity>> loadAllParts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PartEntity> parts);

    @Query("select * from parts where id = :partId")
    LiveData<PartEntity> loadPart(int partId);

    @Query("select * from parts where id = :partId")
    PartEntity loadPartSync(int partId);
}
