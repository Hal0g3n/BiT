package com.halogen.bit.ui.onBoarding.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.halogen.bit.ui.onBoarding.OnBoardingFourFragment
import com.halogen.bit.ui.onBoarding.OnBoardingOneFragment
import com.example.assignment2.onBoarding.OnBoardingThreeFragment
import com.halogen.bit.ui.onBoarding.OnBoardingTwoFragment

class OnBoardingPagerAdapter(
    activity: AppCompatActivity
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {return 4;}

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            //Fragment 1
            0 -> OnBoardingOneFragment()
            1 -> OnBoardingTwoFragment()
            2 -> OnBoardingThreeFragment()
            else -> OnBoardingFourFragment()
        }
    }
}