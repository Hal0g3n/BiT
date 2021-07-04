package com.halogen.bit.ui.presets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.presets_fragment.*

class PresetsFragment : Fragment(), PresetListAdapter.OnItemClickListener {

    internal val databaseManager: DatabaseManager by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.presets_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        floatingActionButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_nav_presets_to_presetDetailFragment,
                null,
                null,
                FragmentNavigatorExtras(it to "view")
            )
        }

        recyclerView.adapter = PresetListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onItemClick(position: Int, view: View) {
        val plan = databaseManager.plans[position]
        val action = PresetsFragmentDirections.actionPresetsFragmentToCountDownFragment(
            plan.duration.hours,
            plan.duration.mins,
            plan.duration.secs
        )


        AlertDialog.Builder(requireContext())
            .setTitle("Start")
            .setMessage("Start the plan now?")
            .setPositiveButton("Yes") { _, _->

                Navigation.findNavController(requireView()).navigate(
                    action,
                    FragmentNavigatorExtras(
                        view to "card",
                    )
                )

            }
            .setNegativeButton("No", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }

    override fun onItemLongClick(position: Int, view: View) {
        val action = PresetsFragmentDirections.actionNavPresetsToPresetDetailFragment(position)
        val extras = FragmentNavigatorExtras(view to "card")
        Navigation.findNavController(requireView()).navigate(action, extras)
    }
}