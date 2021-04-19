package com.halogen.bit.ui.account

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.passwordField
import kotlinx.android.synthetic.main.fragment_register.pwTextLayout
import kotlinx.android.synthetic.main.fragment_register.registerButton
import kotlinx.android.synthetic.main.fragment_register.nameField
import kotlinx.android.synthetic.main.fragment_register.nameTextLayout
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RegisterFragment : Fragment() {

    private val mViewModel : DatabaseManager by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener {
            val username = nameField.text.toString()
            val password = passwordField.text.toString()

            if (username == "") nameTextLayout.error = "Field Required"
            if (password == "") nameTextLayout.error = "Password is weak"

            if (username == "" || password == "") return@setOnClickListener

            //Check if user registered
            GlobalScope.async {

                //If Username already taken
                if (mViewModel.getUser(username) != null) {
                    requireActivity().runOnUiThread {
                        //User Not Registered
                        nameTextLayout.error = "Username Taken!"
                        pwTextLayout.error = ""
                    }
                    return@async
                }

                //Register with no issues
                mViewModel.register(username, password) { //Callback
                    success ->

                    //Navigate to Timer Fragment
                    if (success) Navigation.findNavController(requireView()).navigate(R.id.register_done)
                    //Error on Register
                    else { Toast.makeText(requireContext(), "Register Failed, Try Again!", Toast.LENGTH_LONG).show() }
                }

            }

        }

    }

    override fun onPause() {
        super.onPause()

        //Close keyboard
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).toolbar.setNavigationIcon(R.drawable.ic_menu_back)

        (requireActivity() as MainActivity).toolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

}