package com.halogen.bit.ui.countdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialFadeThrough
import com.halogen.bit.MainActivity
import com.halogen.bit.MainActivity.Companion.onActivityExit
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import com.halogen.bit.model.Duration
import kotlinx.android.synthetic.main.countdown_fragment.*
import kotlinx.android.synthetic.main.set_time_fragment.*
import kotlinx.android.synthetic.main.set_time_fragment.hour_text
import kotlinx.android.synthetic.main.set_time_fragment.min_text
import kotlinx.android.synthetic.main.set_time_fragment.sec_text
import kotlinx.coroutines.Job

class CountDownFragment: Fragment() {

    private val mViewModel: CountDownViewModel by viewModels()
    private val databaseManager: DatabaseManager by activityViewModels()
    lateinit var timerJob: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Determine how shared elements are handled
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.timer_transition)
        sharedElementReturnTransition =  TransitionInflater.from(context).inflateTransition(R.transition.timer_transition)

        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()

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

        giveUpBtn.setOnClickListener {
            mViewModel.hasFailed = true

            val user = databaseManager.user.value

            //If there is a user
            if (user != null) {
                user.history[user.history.lastIndex] += mViewModel.passed
                user.bits += mViewModel.passed
            }

            Navigation.findNavController(requireView()).navigate(R.id.failed)
        }

        //Set up Wake Lock
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStart() {
        super.onStart()

        //Getting arguments
        val args = arguments?.let { CountDownFragmentArgs.fromBundle(it); } ?: return

        mViewModel.duration.value = Duration(args.hours, args.mins, args.secs)

        timerJob = mViewModel.start {
            //Has Failed
            if (mViewModel.hasFailed) return@start

            onActivityExit = null
            Navigation.findNavController(requireView()).navigate(R.id.success)

            val user = databaseManager.user.value ?: return@start
            user.history[user.history.lastIndex] += mViewModel.passed
            user.bits += mViewModel.passed

        }

        onActivityExit = { mViewModel.hasFailed = true }

        if (mViewModel.hasFailed) {
            Navigation.findNavController(requireView()).navigate(R.id.failed)
            mViewModel.hasFailed = true

            val user = databaseManager.user.value ?: return
            user.history[user.history.lastIndex] += mViewModel.passed
            user.bits += mViewModel.passed
        }

        (requireActivity() as MainActivity).toggleToolbar(false)
    }

    override fun onPause() {
        super.onPause()

        (requireActivity() as MainActivity).toggleToolbar(true)

        //Stops timer
        timerJob.cancel()

        //Release Wake Lock on done
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

}