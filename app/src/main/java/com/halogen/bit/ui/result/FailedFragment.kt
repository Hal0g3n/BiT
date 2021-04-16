package com.halogen.bit.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.halogen.bit.R
import kotlinx.android.synthetic.main.fragment_failed.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class FailedFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Starts the call to the API
        GlobalScope.launch {

            //Establish Connection to Quote API
            val connection = URL("https://api.quotable.io/random?tags=success|inspirational").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            //Retrieving the information
            var response = ""
            BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)).forEachLine {
                response += it
            }

            //Parse as JsonObject
            val data = Gson().fromJson(response, JsonObject::class.java)

            //Return the Quote
            quote.postValue(Quote(data.get("content").asString, data.get("author").asString))
        }

        return inflater.inflate(R.layout.fragment_failed, container, false)
    }

    val quote = MutableLiveData<Quote>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        content.visibility = View.INVISIBLE
        author.visibility = View.INVISIBLE
        button3.visibility = View.INVISIBLE

        quote.observe(viewLifecycleOwner) {
            content.text = "\"${quote.value?.content}\""
            author.text = "~ ${quote.value?.author}"


            GlobalScope.async {

                content.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.fade_slide_left
                    )
                )
                requireActivity().runOnUiThread { content.visibility = View.VISIBLE }

                delay(1000)

                author.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.fade_slide_left
                    )
                )
                requireActivity().runOnUiThread { author.visibility = View.VISIBLE }

                delay(1000)

                button3.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.fade_in
                    )
                )
                requireActivity().runOnUiThread { button3.visibility = View.VISIBLE }
            }
        }

        //Button Listener to go back
        button3.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_failedFragment_to_setTimeFragment)
        }
    }

}

data class Quote(
    val content: String,
    val author: String
) {

}