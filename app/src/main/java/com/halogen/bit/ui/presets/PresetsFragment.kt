package com.halogen.bit.ui.presets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.halogen.bit.R

class PresetsFragment : Fragment() {

    companion object {
        fun newInstance() = PresetsFragment()
    }

    private lateinit var viewModel: PresetsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.presets_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PresetsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}