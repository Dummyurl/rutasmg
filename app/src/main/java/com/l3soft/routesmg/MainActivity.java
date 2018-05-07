package com.l3soft.routesmg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.clans.fab.FloatingActionButton;
import com.l3soft.routesmg.activities.MapsActivity;
import com.l3soft.routesmg.activities.NewCommentActivity;
import com.l3soft.routesmg.activities.NewTravelActivity;
import com.l3soft.routesmg.fragment.BusFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_CODE = 2905;
    private static NavigationView navigationView;

    //menu and buttons in the more action
    private FloatingActionButton newBus, newTravel, newComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refreshComplains(BusFragment.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initCollapsingToolbar();
        initFabButtons();
    }

    private void initFabButtons(){
        newBus = findViewById(R.id.newBus);
        newTravel = findViewById(R.id.new_travel);
        newComment = findViewById(R.id.newComment);

        newBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
            }
        });

        newTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), NewTravelActivity.class);
                startActivity(intent);
            }
        });

        newComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), NewCommentActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void loadStatusConn(String userName, String email, int icon, String status){
        View headerView= navigationView.getHeaderView(0);

        if(userName != null){
            TextView userNameView = headerView.findViewById(R.id.user_name);
            userNameView.setText(userName);
        }
        if(email != null){
            TextView emailView = headerView.findViewById(R.id.email);
            emailView.setText(email);
        }

        if(icon != 0){
            ImageView iconView = headerView.findViewById(R.id.img_status);
            iconView.setImageResource(icon);
            TextView statusView = headerView.findViewById(R.id.text_status);
            statusView.setText(status);
        }
    }

    private void initCollapsingToolbar() {

        SimpleDraweeView backdrop = findViewById(R.id.backdrop);
        backdrop.setImageResource(R.drawable.bus);

        final CollapsingToolbarLayout collapsingToolbar =
                 findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Class fragmentClass = null;

        int id = item.getItemId();

        if (id == R.id.nav_bus) {
            fragmentClass = BusFragment.class;
        }else if(id == R.id.nav_profile){

        }else if(id == R.id.nav_comment){

        }else if(id == R.id.nav_favorite){

        }

        refreshComplains(fragmentClass);

        return true;
    }

    private void refreshComplains(Class fragmentClass){

        Fragment fragment;
        try {

            fragment = (Fragment) fragmentClass.newInstance();
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshComplains(BusFragment.class);
        Log.i("RESULT CODE",requestCode+"");
    }
}