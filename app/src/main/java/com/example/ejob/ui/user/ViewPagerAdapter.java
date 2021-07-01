package com.example.ejob.ui.user;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new UserHomeFragment();

            case 1:
                return new UserFavoriteFragment();

            case 2:
                return new UserProfileFragment();
            default:
                return new UserHomeFragment();
        }
    }

//    public CharSequence getTitleFromPosition(int position){
//        String title;
//        switch (position){
//            case 0:
//                title = "Home";
//            case 1:
//                title = "Favorite";
//            case 2:
//                title =  "Profile";
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + position);
//        }
//        return title;
//    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
