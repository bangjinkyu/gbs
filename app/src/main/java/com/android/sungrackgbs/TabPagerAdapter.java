package com.android.sungrackgbs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    private String[] tabTitles = new String[]{"", "", ""};

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FirstFragment firstFragment = new FirstFragment();

                return firstFragment;
            case 1:

                SecondFragment secondFragment = new SecondFragment();

                return secondFragment;
            case 2:

                ThirdFragment thirdFragment = new ThirdFragment();

                return thirdFragment;

            default:
                return null;
          }
    }

    @Override
    public int getCount() {
        return tabCount ;
    }

}
