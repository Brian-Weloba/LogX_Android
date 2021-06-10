package com.saturdev.logx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDevicesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.newTextDevices)
    TextView mNew;
    @BindView(R.id.menuTexDevices)
    TextView mMenu;
    @BindView(R.id.devices_page)
    RelativeLayout mDevices;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;
    FirebaseAuth firebaseAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.articlesFragment) {
                        startActivity(new Intent(HomeDevicesActivity.this, HomeArticlesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return false;
                }
            };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_devices);
        ButterKnife.bind(this);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

//        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.START);
                    } else {
                    drawer.openDrawer(Gravity.START);
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        mDevices.setOnTouchListener(new OnSwipeTouchListener(HomeDevicesActivity.this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(HomeDevicesActivity.this, HomeArticlesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        });

        mNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeDevicesActivity.this, NewRepairActivity.class));
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_articles:
                startActivity(new Intent(HomeDevicesActivity.this, HomeArticlesActivity.class));
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "Clicked profile", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                break;
            case R.id.nav_devices:
                Toast.makeText(this, "Clicked devices", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                break;
            case R.id.nav_expences:
                Toast.makeText(this, "Clicked expenses", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                break;
            case R.id.nav_repairmen:
                Toast.makeText(this, "Clicked repairmen", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this,FavouritesActivity.class));
                break;
            case R.id.nav_logout:
                SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("isLogged", 0);
                prefEditor.apply();

                firebaseAuth.signOut();
                startActivity(new Intent(HomeDevicesActivity.this, SignInActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressLint("SetTextI18n")
    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
//            String phone = firebaseUser.getPhoneNumber();
            String username = firebaseUser.getDisplayName();
//            String email = firebaseUser.getEmail();
//                mHelloUser.setText("Hello " + username + ",");
        } else {
            finish();
        }
    }
}