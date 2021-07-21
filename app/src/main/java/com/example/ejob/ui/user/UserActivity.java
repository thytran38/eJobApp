package com.example.ejob.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.example.ejob.utils.CommonUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class UserActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    Button logout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    UserHomeFragment userHomeFragment;
    SwipeRefreshLayout swipeRefreshLayout;
    ChipNavigationBar chipNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        swipeRefreshLayout = findViewById(R.id.swipeJoblist);

//        tabLayout = findViewById(R.id.tabLayout);
//        viewPager2 = findViewById(R.id.view_pager_user);
        chipNavigationBar = findViewById(R.id.menu);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_main, true);

//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
//        viewPager2.setAdapter(viewPagerAdapter);
//        new TabLayoutMediator(tabLayout, viewPager2,
//                (tab, position) -> tab.setText(CommonUtils.getTitlefromUser(position)))
//                .attach();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {
            case R.id.nav_main:
                selectedFragment = new UserHomeFragment();
                break;
            case R.id.nav_applications:
                selectedFragment = new ApplicationFragment();
                break;
            case R.id.nav_saved:
                selectedFragment = new UserFavoriteFragment();
                break;
            case R.id.nav_profile:
                selectedFragment = new UserProfileFragment();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}