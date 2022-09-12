package no.ntnu.idatt2506.stud.williagt.menu_choice

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import no.ntnu.idatt2506.stud.williagt.menu_choice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG: String = "IDATT2506"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.add("William")
        menu.add("Tresselt")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.title.equals("William")) {
            Log.w(TAG, "User clicked " + item.title)
        }
        if(item.title.equals("Tresselt")) {
            Log.e(TAG, "User clicked " + item.title)
        }
        return true
    }
}