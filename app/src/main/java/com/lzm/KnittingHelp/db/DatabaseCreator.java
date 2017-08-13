package com.lzm.KnittingHelp.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzm.KnittingHelp.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lzm.KnittingHelp.db.AppDatabase.DATABASE_NAME;

public class DatabaseCreator {

    private static final String TAG = "DatabaseCreator";

    private static DatabaseCreator instance;

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    private AppDatabase appDatabase;

    private final AtomicBoolean initializing = new AtomicBoolean(true);

    private static final Object LOCK = new Object();

    public synchronized static DatabaseCreator getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new DatabaseCreator();
                }
            }
        }
        return instance;
    }

    public LiveData<Boolean> isDatabaseCreated() {
        return isDatabaseCreated;
    }

    @Nullable
    public AppDatabase getDatabase() {
        return appDatabase;
    }

    public void setDatabase(AppDatabase db) {
        this.appDatabase = db;
    }

    public void setDatabaseCreated(boolean val) {
        isDatabaseCreated.setValue(val);
    }

    public void createDb(Context context) {

        Log.d(TAG, "Creating DB from " + Thread.currentThread().getName());

        if (!initializing.compareAndSet(true, false)) {
            return; // Already initializing
        }

        isDatabaseCreated.setValue(false);// Trigger an update to show a loading screen.

        String preferencesKey = context.getString(R.string.preferences);
        SharedPreferences preferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);

        boolean resetDatabase = false;
        if (preferences.getBoolean("firstRun", true)) {
            preferences.edit().putBoolean("firstRun", false).apply();
            resetDatabase = true;
        }
        resetDatabase = true;
        MyAsyncTask myAsyncTask = new MyAsyncTask(this, resetDatabase);

        myAsyncTask.execute(context.getApplicationContext());
    }

    private static class MyAsyncTask extends AsyncTask<Context, Void, Void> {
        private final WeakReference<DatabaseCreator> databaseCreatorWeakReference;
        private boolean resetDatabase;

        public MyAsyncTask(DatabaseCreator databaseCreator, boolean resetDatabase) {
            databaseCreatorWeakReference = new WeakReference<DatabaseCreator>(databaseCreator);
            this.resetDatabase = resetDatabase;
        }

        @Override
        protected Void doInBackground(Context... params) {
            DatabaseCreator databaseCreator = databaseCreatorWeakReference.get();
            Log.d(TAG, "Starting bg job " + Thread.currentThread().getName());

            Context context = params[0].getApplicationContext();


            AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).build();

            if (resetDatabase) {
                context.deleteDatabase(DATABASE_NAME);
                DatabaseInitUtil.initializeDb(db);
            }

            Log.d(TAG, "DB was populated in thread " + Thread.currentThread().getName());

            databaseCreator.setDatabase(db);
            return null;
        }

        @Override
        protected void onPostExecute(Void ignored) {
            DatabaseCreator databaseCreator = databaseCreatorWeakReference.get();
            databaseCreator.setDatabaseCreated(true);
        }
    }

}
