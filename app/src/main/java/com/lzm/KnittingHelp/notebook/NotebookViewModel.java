package com.lzm.KnittingHelp.notebook;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.lzm.KnittingHelp.db.DatabaseCreator;
import com.lzm.KnittingHelp.db.entity.PatternEntity;

import java.util.List;

class NotebookViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<List<PatternEntity>> observablePatterns;

    public NotebookViewModel(Application application) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        LiveData<Boolean> databaseCreated = databaseCreator.isDatabaseCreated();
        observablePatterns = Transformations.switchMap(databaseCreated,
                new Function<Boolean, LiveData<List<PatternEntity>>>() {
                    @Override
                    public LiveData<List<PatternEntity>> apply(Boolean isDbCreated) {
                        if (!Boolean.TRUE.equals(isDbCreated)) { // Not needed here, but watch out for null
                            //noinspection unchecked
                            return ABSENT;
                        } else {
                            //noinspection ConstantConditions
                            return databaseCreator.getDatabase().patternDao().loadAllPatterns();
                        }
                    }
                });

        databaseCreator.createDb(this.getApplication());
    }

    public LiveData<List<PatternEntity>> getPatterns() {
        return observablePatterns;
    }

}
