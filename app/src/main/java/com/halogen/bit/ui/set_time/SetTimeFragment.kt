package com.halogen.bit.ui.set_time

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import kotlinx.android.synthetic.main.set_time_fragment.*

class SetTimeFragment : Fragment() {

    private val viewModel: SetTimeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_time_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Increment Listener + Animator
        val incListener = RepeatListener(500, 50) {

            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.inc_anim))

            when (it.id) {
                R.id.hour_inc -> //Check If Max Value Reached, if not ++
                    if (viewModel.hours.value != 99) viewModel.hours.value = (viewModel.hours.value ?: 0) + 1
                    else Toast.makeText(requireContext(), "Max Value Reached", Toast.LENGTH_SHORT).show()

                R.id.min_inc -> //Check If Max Value Reached, if not ++
                    if (viewModel.mins.value != 59) viewModel.mins.value = (viewModel.mins.value ?: 0) + 1
                    else Toast.makeText(requireContext(), "Max Value Reached", Toast.LENGTH_SHORT).show()

                R.id.sec_inc -> //Check If Max Value Reached, if not ++
                    if (viewModel.secs.value != 59) viewModel.secs.value = (viewModel.secs.value ?: 0) + 1
                    else Toast.makeText(requireContext(), "Max Value Reached", Toast.LENGTH_SHORT).show()
            }
        }
        //Setting the Increment Listeners
        hour_inc.setOnTouchListener(incListener)
        min_inc.setOnTouchListener(incListener)
        sec_inc.setOnTouchListener(incListener)

        //Decrement Listener + Animator
        val decListener = RepeatListener(500, 50) {

            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.dec_anim))

            when (it.id) {
                R.id.hour_dec ->
                    if (viewModel.hours.value ?: 0 != 0) viewModel.hours.value = (viewModel.hours.value ?: 0) - 1
                    else Toast.makeText(requireContext(), "Min Value Reached", Toast.LENGTH_SHORT).show()

                R.id.min_dec ->
                    if (viewModel.mins.value ?: 0 != 0) viewModel.mins.value = (viewModel.mins.value ?: 0) - 1
                    else Toast.makeText(requireContext(), "Min Value Reached", Toast.LENGTH_SHORT).show()

                R.id.sec_dec ->
                    if (viewModel.secs.value ?: 0 != 0) viewModel.secs.value = (viewModel.secs.value ?: 0) - 1
                    else Toast.makeText(requireContext(), "Min Value Reached", Toast.LENGTH_SHORT).show()

            }
        }
        //Setting the Decrement Listeners
        hour_dec.setOnTouchListener(decListener)
        min_dec.setOnTouchListener(decListener)
        sec_dec.setOnTouchListener(decListener)

        //Binding the textView to the duration set
        viewModel.hours.observe(viewLifecycleOwner) { hour_text.text = it.toString() }
        viewModel.mins.observe(viewLifecycleOwner) { min_text.text = it.toString() }
        viewModel.secs.observe(viewLifecycleOwner) { sec_text.text = it.toString() }

        button2.setOnClickListener {

            //If the duration is less than 10 min
            if (viewModel.hours.value == 0 && viewModel.mins.value ?: 0 < 10) {
                Toast.makeText(requireContext(), "Duration too short", Toast.LENGTH_SHORT).show()
                //TODO Reminder to add back the return statement
            }


            //All the shared elements are here
            val extras = FragmentNavigatorExtras(
                hour_text to "hour_text",
                min_text to "min_text",
                sec_text to "sec_text",
                card to "card",
                colon1 to "textView",
                colon2 to "textView4"
            )

            //Action with the current set time as param
            val action = SetTimeFragmentDirections.finishSetTime(
                    viewModel.hours.value!!,
                    viewModel.mins.value!!,
                    viewModel.secs.value!!,
            )

            //Navigate to the next fragment
            Navigation.findNavController(requireView()).navigate(
                action,
                extras
            )
        }
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).drawer.openDrawer(GravityCompat.START)
        }
    }

}