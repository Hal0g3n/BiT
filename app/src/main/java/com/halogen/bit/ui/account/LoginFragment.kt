package com.halogen.bit.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class LoginFragment : Fragment() {

    private val mViewModel : DatabaseManager by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noAccButton.setOnClickListener { Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_setTimeFragment) }

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
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
                                usernameTextLayout.error = ""
                            }
                        }
                        else {
                            requireActivity().runOnUiThread {
                                //User Not Registered
                                usernameTextLayout.error = "User Not Registered"
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).toolbar.setNavigationIcon(R.drawable.ic_drawer)
    }
}