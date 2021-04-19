package com.halogen.bit.ui.menu

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView2.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in).apply {
            duration = 2000
            setAnimationListener(object: Animation.AnimationListener{
                //Unused
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    this@SplashFragment.requireActivity().runOnUiThread {
                        val sharedPreferences = requireContext().getSharedPreferences("OnBoardingDone", Context.MODE_PRIVATE)
                        if (sharedPreferences.getBoolean("OnBoardingDone", false)) {
                            (requireActivity() as MainActivity).toggleToolbar(true)
                            Navigation.findNavController(view).navigate(R.id.splash_to_login)
                        }
                        else {
                            sharedPreferences.edit().putBoolean("OnBoardingDone", true).apply()
                            Navigation.findNavController(view).navigate(R.id.splash_to_onBoarding)
                        }
                    }
                }

            })
        })

    }

}