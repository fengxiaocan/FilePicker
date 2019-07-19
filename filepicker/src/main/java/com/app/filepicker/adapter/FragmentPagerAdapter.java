package com.app.filepicker.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Fragment结合ViewPager一起使用的Adapter
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleArray;


    public FragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
            List<String> titleArray)
    {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleArray = titleArray;
    }


    //    public void setFragments(ArrayList<Fragment> fragments) {
    //        if (this.fragmentList != null) {
    //            FragmentTransaction ft = fm.beginTransaction();
    //            for (Fragment f : this.fragmentList) {
    //                ft.remove(f);
    //            }
    //            ft.commit();
    //            ft = null;
    //            fm.executePendingTransactions();
    //        }
    //        this.fragmentList = fragments;
    //        notifyDataSetChanged();
    //    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleArray != null) {
            return titleArray.get(position);
        }
        return "";
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
