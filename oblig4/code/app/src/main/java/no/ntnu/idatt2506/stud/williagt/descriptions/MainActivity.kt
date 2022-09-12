package no.ntnu.idatt2506.stud.williagt.descriptions

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), //Implementing AppCompatActivity instead of FragmentActivity fixed Menu issue
    GameTitles.OnFragmentInteractionListener //GameTitles fragment is to implement given interface
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOrientation(resources.configuration)
    }

    private fun setOrientation(newConfig: Configuration) {
        val layout = findViewById<LinearLayout>(R.id.root_layout)

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.orientation = LinearLayout.HORIZONTAL
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout.orientation = LinearLayout.VERTICAL
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.next).setOnMenuItemClickListener { _ ->
            val fragment = supportFragmentManager.findFragmentById(R.id.desc_img_fragment) as ImageAndDescription
            fragment.next()
            return@setOnMenuItemClickListener true
        }
        menu.findItem(R.id.prev).setOnMenuItemClickListener { _ ->
            val fragment = supportFragmentManager.findFragmentById(R.id.desc_img_fragment) as ImageAndDescription
            fragment.prev()
            return@setOnMenuItemClickListener true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        //Remember to set android:configChanges="orientation" in main layout
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig)
    }

    //Overriding interface implemented by the GameTitles fragment
    override fun onFragmentInteractionListener(indexClicked: Int) {
        val imageAndDescription = supportFragmentManager.findFragmentById(R.id.desc_img_fragment) as ImageAndDescription //Getting fragment that is to receive data
        imageAndDescription.displayNewImgDsc(indexClicked) //Calling methods for fragment and sending data
    }
}