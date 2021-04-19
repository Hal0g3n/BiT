package com.halogen.bit.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.fragment_success.*

class SuccessFragment : Fragment() {

    val mViewModel: DatabaseManager by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView3.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in))

        button4.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_successFragment_to_setTimeFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        val args = arguments?.let { SuccessFragmentArgs.fromBundle(it) }
        if (mViewModel.user.value != null) textView2.text = "+ ${args!!.seconds.toFloat() / 3600f}bits"
    }
}