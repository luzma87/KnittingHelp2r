package com.lzm.KnittingHelp;

import android.arch.lifecycle.LifecycleFragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lzm.KnittingHelp.counters.CountersFragment;
import com.lzm.KnittingHelp.enums.KnittingFragment;
import com.lzm.KnittingHelp.notebook.NotebookFragment;

import static com.lzm.KnittingHelp.enums.KnittingFragment.COUNTERS;
import static com.lzm.KnittingHelp.enums.KnittingFragment.NOTEBOOK;
import static com.lzm.KnittingHelp.enums.KnittingFragment.PATTERN;



// <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    KnittingFragment activeFragment = NOTEBOOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int titleRes = NOTEBOOK.getTitleId();
//        NotebookFragment notebookFragment = NotebookFragment.newInstance();
//        FragmentHelper.openFragment(this, notebookFragment, getString(titleRes), false);
//        navigationView.setCheckedItem(R.id.nav_notebook);


        NotebookFragment fragment = NotebookFragment.newInstance();
        openFragment(fragment, NotebookFragment.TAG, titleRes);
        navigationView.setCheckedItem(R.id.nav_notebook);
    }

    private void openFragment(LifecycleFragment fragment, String tag, int titleRes) {
        this.setTitle(titleRes);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    public void setActiveFragment(KnittingFragment activeFragment) {
        this.activeFragment = activeFragment;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        makeToolbarIconsWhite(menu);

        MenuItem itemPatternEdit = menu.findItem(R.id.action_pattern_edit);

        if (activeFragment == PATTERN) {
            itemPatternEdit.setVisible(true);
        } else {
            itemPatternEdit.setVisible(false);
        }

        return true;
    }

    private void makeToolbarIconsWhite(Menu menu) {
        int white = ContextCompat.getColor(this, R.color.white);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(white, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        int titleRes = NOTEBOOK.getTitleId();
        LifecycleFragment fragment = NotebookFragment.newInstance();
        String tag = NotebookFragment.TAG;

        if (id == R.id.nav_notebook) {
            titleRes = NOTEBOOK.getTitleId();
            fragment = NotebookFragment.newInstance();
            tag = NotebookFragment.TAG;
        } else if (id == R.id.nav_counters) {
            titleRes = COUNTERS.getTitleId();
            fragment = CountersFragment.newInstance();
            tag = CountersFragment.TAG;
        }

        openFragment(fragment, tag, titleRes);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
