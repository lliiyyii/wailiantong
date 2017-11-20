package com.example.wailiantong.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 蔚克 on 2017/6/1.
 */

public class FragMainAdapter extends FragmentPagerAdapter {

    public List<Fragment> list;
    private List<String> titles;

    public FragMainAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        //构造方法
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    /**
     * 返回要显示的Fragment的某个实例
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * 返回Fragment的总数
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 返回每个Tab的标题
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
