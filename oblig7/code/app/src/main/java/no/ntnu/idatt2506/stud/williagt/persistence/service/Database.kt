package no.ntnu.idatt2506.stud.williagt.persistence.service

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import no.ntnu.idatt2506.stud.williagt.persistence.R
import no.ntnu.idatt2506.stud.williagt.persistence.managers.DatabaseManager
import no.ntnu.idatt2506.stud.williagt.persistence.managers.FileManager

class Database(context: Context) : DatabaseManager(context) {
    init {
        try {
            val manager = FileManager(context as AppCompatActivity)
            val moviesAsString = manager.readFileFromResFolder(R.raw.movies) //Reading from file in raw
            manager.write(moviesAsString) //Writing to file 'filename.txt' in filesDir
            val moviesAsArray = moviesAsString.split("[\r\n]+".toRegex()).toTypedArray()

            this.clear()
            for(m in moviesAsArray) {
                val movieInfo = m.split(";".toRegex()).toTypedArray()
                this.insert(movieInfo[0], movieInfo[1], movieInfo[2], movieInfo[3])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIE, arrayOf(ID, MOVIE_NAME))

    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTOR, arrayOf(DIRECTOR_NAME))

    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ACTOR_NAME))

    val allMoviesAndDirectors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME", "$TABLE_DIRECTOR.$DIRECTOR_NAME")
            val from = arrayOf(TABLE_MOVIE, TABLE_DIRECTOR, TABLE_MOVIE_DIRECTOR)
            val join = JOIN_MOVIE_DIRECTOR

            return performRawQuery(select, from, join)
        }

    val allMoviesAndActors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME", "$TABLE_ACTOR.$ACTOR_NAME")
            val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
            val join = JOIN_MOVIE_ACTOR

            return performRawQuery(select, from, join)
        }

    val allMoviesDirectorsAndActors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME", "$TABLE_DIRECTOR.$DIRECTOR_NAME", "$TABLE_ACTOR.$ACTOR_NAME")
            val from = arrayOf(TABLE_MOVIE, TABLE_DIRECTOR, TABLE_MOVIE_DIRECTOR, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
            val join = JOIN_MOVIE_DIRECTOR_ACTOR

            return performRawQuery(select, from, join)
        }

    fun getMoviesByDirector(director: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE, TABLE_MOVIE_DIRECTOR)
        val join = JOIN_MOVIE_DIRECTOR
        val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"

        return performRawQuery(select, from, join, where)
    }

    fun getActorsByMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_ACTOR, TABLE_MOVIE, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_MOVIE.$MOVIE_NAME='$movie'"

        return performRawQuery(select, from, join, where)
    }

    fun getMoviesByActor(actor: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_ACTOR, TABLE_MOVIE, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_ACTOR.$ACTOR_NAME='$actor'"

        return performRawQuery(select, from, join, where)
    }
}
