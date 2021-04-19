package com.halogen.bit.ui.presets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialContainerTransform
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import com.halogen.bit.model.Duration
import com.halogen.bit.model.Plan
import com.halogen.bit.ui.set_time.RepeatListener
import com.halogen.bit.ui.set_time.SetTimeViewModel
import kotlinx.android.synthetic.main.fragment_preset_detail.*

class PresetDetailFragment : Fragment() {

    private val db: DatabaseManager by activityViewModels()
    private val viewModel: SetTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            duration = 500
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().obtainStyledAttributes(intArrayOf(R.attr.colorSurface)).use{it.getColor(0, Color.GREEN)})
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset_detail, container, false)
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

    }

    override fun onStart() {
        super.onStart()

        val args = arguments?.let { PresetDetailFragmentArgs.fromBundle(it); } ?: return
        if (args.planIndex == -1) {
            button2.text = "Create"
            delButton.visibility = View.GONE
            button2.setOnClickListener {
                db.addPlan(Plan(nameField.text.toString(), Duration(viewModel.hours.value!!, viewModel.mins.value!!, viewModel.secs.value!!)))
                Navigation.findNavController(requireView()).popBackStack()
            }
        }
        else {
            button2.text = "Confirm"
            button2.setOnClickListener {
                //If the duration is less than 10 min
                if (viewModel.hours.value == 0 && viewModel.mins.value ?: 0 < 10) {
                    Toast.makeText(requireContext(), "Duration too short", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                db.updatePlan(args.planIndex, Plan(nameField.text.toString(), Duration(viewModel.hours.value!!, viewModel.mins.value!!, viewModel.secs.value!!)))
                Navigation.findNavController(requireView()).popBackStack()
            }

            delButton.setOnClickListener {
                AlertDialog     .Builder(requireContext())
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this?")
                    .setPositiveButton("Yes") { _, _->
                        db.removePlan(db.plans[args.planIndex])
                        Navigation.findNavController(requireView()).popBackStack()
                    }
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }

            viewModel.hours.value = db.plans[args.planIndex].duration.hours
            viewModel.mins.value = db.plans[args.planIndex].duration.mins
            viewModel.secs.value = db.plans[args.planIndex].duration.secs
            nameField.setText(db.plans[args.planIndex].name)
        }

    }

}