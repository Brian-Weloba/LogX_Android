package com.saturdev.logx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeArticlesActivity extends AppCompatActivity {
    @BindView(R.id.articles_page)
    RelativeLayout mArticles;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_articles);
        ButterKnife.bind(this);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mArticles.setOnTouchListener(new OnSwipeTouchListener(HomeArticlesActivity.this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeRight() {
                Intent intent = new Intent(HomeArticlesActivity.this, HomeDevicesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.devicesFragment) {
                        startActivity(new Intent(HomeArticlesActivity.this, HomeDevicesActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    return false;
                }
            };
}