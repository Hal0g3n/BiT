package com.halogen.bit

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mViewModel: DatabaseManager by viewModels()

    companion object {
        var onActivityExit: (() -> Unit)? = null

        private const val REQUEST_PERMS = 420
        private val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val player : MediaPlayer by lazy {MediaPlayer.create(this, R.raw.music)}

    internal lateinit var toolbar: Toolbar
    internal lateinit var drawer: DrawerLayout

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the music
        player.isLooping = true
        player.setVolume(.2f, .2f)
        player.start()

        verifyPermissions()

        //Drawer layout for manual opening
        drawer = drawer_layout

        //Placeholder toolbar for replacing later
        toolbar = tool_bar
        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }

        //Setting up the navigation drawer
        val navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_about, R.id.nav_login), drawer_layout)

        nav_view.setupWithNavController(navController)

        //appBarConfiguration Configuration
        mViewModel.user.observe(this) { nUser ->
            appBarConfiguration =
                    // Menu items when a user is logged out
                    if (nUser == null) AppBarConfiguration(setOf(R.id.nav_about, R.id.nav_login), drawer_layout)
                    // Menu items when a user is logged out
                    else AppBarConfiguration(setOf(R.id.nav_about, R.id.nav_profile, R.id.nav_logout), drawer_layout)

            //Resets Menu
            nav_view.menu.clear()
            if (nUser != null) nav_view.inflateMenu(R.menu.drawer_menu_logged_in)
            else nav_view.inflateMenu(R.menu.drawer_menu_logged_out)

            if (nUser == null && mViewModel.notFirst) NavigationUI.onNavDestinationSelected(nav_view.menu.findItem(R.id.nav_login), navController)
            mViewModel.notFirst = true

            //Re init the drawer
            nav_view.setupWithNavController(navController)

            //This works no matter the drawer menu layout but must reinit on menu clear
            nav_view.setNavigationItemSelectedListener {
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(it, navController)
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START)

                if (it.itemId == R.id.nav_logout) {
                    AlertDialog.Builder(this)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to logout now?")
                            .setPositiveButton("Yes") { _, _-> mViewModel.logout() }
                            .setNegativeButton("No", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                }
                true
            }
        }

        homeImg.setOnClickListener {
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.back_home)
        }

        //Navigation Drawer Header Configuration
        mViewModel.user.observe(this) {
            val layout = nav_view.getHeaderView(0)
            val userName = layout.findViewById<TextView>(R.id.userName)
            userName.text = it?.username ?: "Guest"
        }

    }

    private fun verifyPermissions() {
        for (i in PERMISSIONS_STORAGE) {
            if (ActivityCompat.checkSelfPermission(this, i) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMS)
                break
            }
        }
    }

    override fun onPause() {
        super.onPause()

        //Invoke if activity exited
        onActivityExit?.invoke()
    }

    fun toggleToolbar(show: Boolean) {

        val transition: Transition = Slide(Gravity.TOP)
        transition.duration = 200
        transition.addTarget(toolbar)
        TransitionManager.beginDelayedTransition(toolbar, transition)

        toolbar.visibility = if (show) View.VISIBLE else View.GONE
    }
}