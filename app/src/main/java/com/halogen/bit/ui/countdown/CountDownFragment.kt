package com.halogen.bit.ui.countdown

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.halogen.bit.MainActivity.Companion.onActivityExit
import com.halogen.bit.R
import com.halogen.bit.model.Duration
import kotlinx.android.synthetic.main.set_time_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class CountDownFragment: Fragment() {

    private val mViewModel: CountDownViewModel by viewModels()
    lateinit var timerJob: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Determine how shared elements are handled
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.timer_transition)
        sharedElementReturnTransition =  TransitionInflater.from(context).inflateTransition(R.transition.timer_transition)

        return inflater.inflate(R.layout.countdown_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Binding the textView to the duration
        mViewModel.duration.observe(viewLifecycleOwner) {
            hour_text.text = it.hours.toString()
            min_text.text = it.mins.toString()
            sec_text.text = it.secs.toString()
        }

    }

    override fun onStart() {
        super.onStart()

        //Getting arguments
        val args = arguments?.let { CountDownFragmentArgs.fromBundle(it); } ?: return

        mViewModel.duration.value = Duration(args.hours, args.mins, args.secs)

        timerJob = mViewModel.start {
            Navigation.findNavController(requireView()).navigate(R.id.success)
            onActivityExit = null
        }

        onActivityExit = { mViewModel.hasFailed = true }

        if (mViewModel.hasFailed) {
            Navigation.findNavController(requireView()).navigate(R.id.failed)
            mViewModel.hasFailed = true
        }
    }

    override fun onPause() {
        super.onPause()
        timerJob.cancel()
    }

}