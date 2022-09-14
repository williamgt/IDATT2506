package no.ntnu.idatt2506.stud.williagt.persistence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import no.ntnu.idatt2506.stud.williagt.persistence.databinding.ActivityMainBinding
import no.ntnu.idatt2506.stud.williagt.persistence.managers.FileManager
import no.ntnu.idatt2506.stud.williagt.persistence.managers.MyPreferenceManager
import no.ntnu.idatt2506.stud.williagt.persistence.service.Database
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var db: Database
    private lateinit var minLayout: ActivityMainBinding
    private lateinit var myPreferenceManager: MyPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        minLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(minLayout.root)

        db = Database(this)

        myPreferenceManager = MyPreferenceManager(this)
        myPreferenceManager.updateBgColor()
        FileManager(this)
    }

    private fun showResults(list: ArrayList<String>) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        minLayout.result.text = res
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "Alle filmer")
        menu.add(0, 2, 0, "Alle regissører")
        menu.add(0, 3, 0, "Alle skuespillere")
        menu.add(0, 4, 0, "Alle filmer og regissører")
        menu.add(0, 5, 0, "Alle filmer og skuespillere")
        menu.add(0, 6, 0, "Alle filmer, regissører og skuespillere")
        menu.add(0, 7, 0, "Filmer av \"Francis Ford Coppola\"")
        menu.add(0, 8, 0, "Filmer der \"Henry Fonda\" er med")
        menu.add(0, 9, 0, "Skuespillere som er med i \"The Dark Knight\"")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent("no.ntnu.idatt2506.stud.williagt.persistence.SettingsActivity"))
            1             -> showResults(db.allMovies)
            2             -> showResults(db.allDirectors)
            3             -> showResults(db.allActors)
            4             -> showResults(db.allMoviesAndDirectors)
            5             -> showResults(db.allMoviesAndActors)
            6             -> showResults(db.allMoviesDirectorsAndActors)
            7             -> showResults(db.getMoviesByDirector("Francis Ford Coppola"))
            8             -> showResults(db.getMoviesByActor("Henry Fonda"))
            9             -> showResults(db.getActorsByMovie("The Dark Knight"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val color = myPreferenceManager.getString(resources.getString(R.string.bg), resources.getString(R.string.bg_default_value))
        setBgColor(color)
    }

    fun setBgColor(color: String) {
        when(color) {
            "RED" -> minLayout.result.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            "GREEN" -> minLayout.result.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            "BLUE" -> minLayout.result.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            "WHITE" -> minLayout.result.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }
    }
}