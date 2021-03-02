package com.lifefighter.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author xzp
 * @created on 2020/11/19.
 */
class TabFragmentAdapter(
    fragmentManager: FragmentManager,
    private val fragmentTabList: List<FragmentTab>
) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return fragmentTabList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentTabList[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTabList[position].title
    }
}

class ExFragmentAdapter(
    fragmentManager: FragmentManager,
    private val fragmentTabList: List<Fragment>
) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return fragmentTabList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentTabList[position]
    }
}

class FragmentTab(val fragment: Fragment, val title: CharSequence)