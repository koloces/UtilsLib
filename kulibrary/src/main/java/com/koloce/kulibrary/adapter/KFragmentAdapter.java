package com.koloce.kulibrary.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by koloces on 2019/4/22
 */
public class KFragmentAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragments;

    public KFragmentAdapter(@NonNull FragmentManager fm, List< ? extends Fragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
