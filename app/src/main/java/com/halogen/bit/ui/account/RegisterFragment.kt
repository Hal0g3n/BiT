package com.halogen.bit.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val mViewModel : DatabaseManager by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username == "") usernameTextLayout.error = "Field Required"
            if (password == "") usernameTextLayout.error = "Field Required"

            if (username == "" || password == "") return@setOnClickListener
            try {
                mViewModel.register(username, password) { //Callback
                    success ->

                    //Navigate to Timer Fragment
                    if (success) Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_setTimeFragment)
                    //Error on Register
                    else { Toast.makeText(requireContext(), "Register Failed, Try Again!", Toast.LENGTH_LONG).show() }
                }
            }
            catch (e: IllegalArgumentException) { usernameTextLayout.error = "User Not Registered" }

        }

    }

}