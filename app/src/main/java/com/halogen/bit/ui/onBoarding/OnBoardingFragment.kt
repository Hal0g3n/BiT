package com.halogen.bit.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.halogen.bit.ui.onBoarding.adapter.OnBoardingPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.halogen.bit.R
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardingPager.adapter = OnBoardingPagerAdapter(requireActivity() as AppCompatActivity)

        //Linking Tab Layout to View Pager
        TabLayoutMediator(tabDecor, onBoardingPager) {_,_->/*NOTHING*/}.attach()
    }

}