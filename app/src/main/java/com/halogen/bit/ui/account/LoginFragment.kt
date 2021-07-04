package com.halogen.bit.ui.account

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class LoginFragment : Fragment() {

    private val mViewModel : DatabaseManager by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noAccButton.setOnClickListener { Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_setTimeFragment) }

        loginButton.setOnClickListener {
            val username = nameField.text.toString()
            val password = passwordField.text.toString()

            mViewModel.login(username, password) {
                success -> //Callback

                //Navigate to Timer Fragment
                if (success) Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_setTimeFragment)
                //Error on password
                else {
                    //Check if user registered
                    GlobalScope.async {
                        if (mViewModel.getUser(username) != null) {
                            //It is password wrong
                            requireActivity().runOnUiThread {
                                pwTextLayout.error = "Wrong Password"
                                nameTextLayout.error = ""
                            }
                        }
                        else {
                            requireActivity().runOnUiThread {
                                //User Not Registered
                                nameTextLayout.error = "User Not Registered"
                                pwTextLayout.error = ""
                            }
                        }
                    }
                }
            }

        }

        registerButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.register)
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
        (requireActivity() as MainActivity).toolbar.setNavigationIcon(R.drawable.ic_drawer)

        (requireActivity() as MainActivity).toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).drawer.openDrawer(GravityCompat.START)
        }
    }
}