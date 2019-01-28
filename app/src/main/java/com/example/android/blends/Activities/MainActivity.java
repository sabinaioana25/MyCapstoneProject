package com.example.android.blends.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.blends.Adapters.FragmentAdapter;
import com.example.android.blends.R;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TO_SEE = "To see";
    private static final String FAVORITE = "Favorites";
//    private static int SPLASH_TIME_OUT = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent splashIntent = new Intent(MainActivity.this, SplashActivity.class);
//                startActivity(splashIntent);
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.main_activity_id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        ViewPager viewPager = findViewById(R.id.view_pager_fragments);
        FragmentAdapter fragmentAdapter = new
                FragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        NestedScrollView scrollView = findViewById(R.id.main_activity_nested_scroll);
        scrollView.setFillViewport(true);

        final TabLayout fragmentTabs = findViewById(R.id.main_tab_layout);
        fragmentTabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_view_map) {
            Intent goToMap = new Intent(this, MapActivity.class);
            startActivity(goToMap);
        } else if (id == R.id.action_settings) {
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
