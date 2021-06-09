package com.saturdev.logx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDevicesActivity extends AppCompatActivity {
    @BindView(R.id.newTextDevices)
    TextView mNew;
    @BindView(R.id.menuTexDevices)
    TextView mMenu;
    @BindView(R.id.devices_page)
    RelativeLayout mDevices;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_devices);
        ButterKnife.bind(this);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mDevices.setOnTouchListener(new OnSwipeTouchListener(HomeDevicesActivity.this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(HomeDevicesActivity.this, HomeArticlesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        });

    }
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
}