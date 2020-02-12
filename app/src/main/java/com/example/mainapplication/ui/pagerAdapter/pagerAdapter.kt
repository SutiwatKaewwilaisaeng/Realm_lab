package com.example.likelab.pagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.likelab.SettingFragment
import com.example.mainapplication.EditProfile.EditProfileFragment

class pagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                EditProfileFragment.newInstance()
            }
            1 -> {
                com.example.likelab.List.ListFragment()
            }
            2 -> {

                SettingFragment.newInstance()

            }
            else -> {
                return EditProfileFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "EditProfile"
            1 -> "List"
            2 -> "Setting"
            else -> {
                return "EditProfile"
            }
        }
    }

}