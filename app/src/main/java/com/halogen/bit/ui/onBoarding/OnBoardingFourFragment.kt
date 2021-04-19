package com.halogen.bit.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import kotlinx.android.synthetic.main.fragment_step_four.*

class OnBoardingFourFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step_four, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            (requireActivity() as MainActivity).toggleToolbar(true)
            Navigation.findNavController(view).navigate(R.id.onBoarding_to_login)
        }
    }
}