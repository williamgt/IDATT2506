package no.ntnu.idatt2506.stud.williagt.persistence.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        const val DATABASE_NAME = "MovieDatabase"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        const val TABLE_MOVIE = "MOVIE"
        const val MOVIE_NAME = "name"

        const val TABLE_DIRECTOR = "DIRECTOR"
        const val DIRECTOR_NAME = "name"

        const val TABLE_ACTOR = "ACTOR"
        const val ACTOR_NAME = "name"

        const val TABLE_MOVIE_DIRECTOR = "MOVIE_DIRECTOR"
        const val TABLE_MOVIE_ACTOR = "MOVIE_ACTOR"
        const val MOVIE_ID = "movie_id"
        const val DIRECTOR_ID = "director_id"
        const val ACTOR_ID = "actor_id"

        val JOIN_MOVIE_DIRECTOR = arrayOf(
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_DIRECTOR.$MOVIE_ID",
            "$TABLE_DIRECTOR.$ID=$TABLE_MOVIE_DIRECTOR.$DIRECTOR_ID"
        )

        val JOIN_MOVIE_ACTOR = arrayOf(
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
            "$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"
        )

        val JOIN_MOVIE_DIRECTOR_ACTOR = arrayOf(
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_DIRECTOR.$MOVIE_ID",
            "$TABLE_DIRECTOR.$ID=$TABLE_MOVIE_DIRECTOR.$DIRECTOR_ID",
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
            "$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"
        )
    }

    /**
     *  Create the tables
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """create table $TABLE_MOVIE (
						$ID integer primary key autoincrement, 
						$MOVIE_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_DIRECTOR (
						$ID integer primary key autoincrement, 
						$DIRECTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_ACTOR (
						$ID integer primary key autoincrement, 
						$ACTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIE_DIRECTOR (
						$ID integer primary key autoincrement, 
						$MOVIE_ID numeric, 
						$DIRECTOR_ID numeric,
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID), 
						FOREIGN KEY($DIRECTOR_ID) REFERENCES $TABLE_DIRECTOR($ID)
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIE_ACTOR (
						$ID integer primary key autoincrement, 
						$MOVIE_ID numeric, 
						$ACTOR_ID numeric,
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID), 
						FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID)
						);"""
        )
    }

    /**
     * Drop and recreate all the tables
     */
    override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_DIRECTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
        onCreate(db)
    }

    /**
     *  Drop all information is the database
     */
    fun clear() {
        writableDatabase.use { onUpgrade(it, 0, 0) }
    }


    /**
     *  Inserts the movie, the director, two actors, and then the connection between them.
     */
    fun insert(movie: String, director: String, actor1: String, actor2: String) {
        writableDatabase.use { database ->
            val movieId = insertValueIfNotExists(database, TABLE_MOVIE, MOVIE_NAME, movie)
            val directorId = insertValueIfNotExists(database, TABLE_DIRECTOR, DIRECTOR_NAME, director)
            val actor1Id = insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME, actor1)
            val actor2Id = insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME, actor2)
            linkMovieAndDirector(database, movieId,directorId)
            linkMovieAndActor(database, movieId,actor1Id)
            linkMovieAndActor(database, movieId,actor2Id)
        }
    }

    /**
     * Pseudo-code for method
     * ```
     *  if (value exists){
     *      return valueId
     *  } else {
     *      insert and return new Id
     *  }
     * ```
     */
    private fun insertValueIfNotExists(
        database: SQLiteDatabase, table: String, field: String, value: String
    ): Long {
        // Query for the value
        query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
            // insert if value doesn't exist
            return if (cursor.count != 0) {
                cursor.moveToFirst()
                cursor.getLong(0) //id of found value
            } else {
                insertValue(database, table, field, value)
            }
        }
    }



    /**
     * Insert the value in given table and field, then return its ID
     */
    private fun insertValue(
        database: SQLiteDatabase, table: String, field: String, value: String
    ): Long {
        val values = ContentValues()
        values.put(field, value.trim())
        return database.insert(table, null, values)
    }

    /**
     *  Insert in the *TABLE_MOVIE_ACTOR*, essentially linking an movie and an actor
     */
    private fun linkMovieAndActor(database: SQLiteDatabase, movieId: Long, actorId: Long) {
        val values = ContentValues()
        values.put(MOVIE_ID, movieId)
        values.put(ACTOR_ID, actorId)
        database.insert(TABLE_MOVIE_ACTOR, null, values)
    }

    /**
     *  Insert in the *TABLE_MOVIE_DIRECTOR*, essentially linking an movie and a director
     */
    private fun linkMovieAndDirector(database: SQLiteDatabase, movieId: Long, directorId: Long) {
        val values = ContentValues()
        values.put(MOVIE_ID, movieId)
        values.put(DIRECTOR_ID, directorId)
        database.insert(TABLE_MOVIE_DIRECTOR, null, values)
    }

    /**
     * Perform a simple query
     *
     * Not the query() function has almost all parameters as *null*, you should check up on these.
     * maybe you don't even need the performRawQuery() function?
     */
    fun performQuery(table: String, columns: Array<String>, selection: String? = null):
            ArrayList<String> {
        assert(columns.isNotEmpty())
        readableDatabase.use { database ->
            query(database, table, columns, selection).use { cursor ->
                return readFromCursor(cursor, columns.size)
            }
        }
    }

    /**
     * Run a raw query, the parameters are for easier debugging and reusable code
     */
    fun performRawQuery(
        select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null
    ): ArrayList<String> {

        val query = StringBuilder("SELECT ")
        for ((i, field) in select.withIndex()) {
            query.append(field)
            if (i != select.lastIndex) query.append(", ")
        }

        query.append(" FROM ")
        for ((i, table) in from.withIndex()) {
            query.append(table)
            if (i != from.lastIndex) query.append(", ")
        }

        query.append(" WHERE ")
        for ((i, link) in join.withIndex()) {
            query.append(link)
            if (i != join.lastIndex) query.append(" and ")
        }

        if (where != null) query.append(" and $where")

        readableDatabase.use { db ->
            db.rawQuery("$query;", null).use { cursor ->
                return readFromCursor(cursor, select.size)
            }
        }
    }

    /**
     * Read the contents from the cursor and return it as an arraylist
     */
    private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = StringBuilder("")
            for (i in 0 until numberOfColumns) {
                item.append("${cursor.getString(i)} ")
            }
            result.add("$item")
            cursor.moveToNext()
        }
        return result
    }

    /**
     * Use a query with default arguments
     */
    private fun query(
        database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?
    ): Cursor {
        return database.query(table, columns, selection, null, null, null, null, null)
    }
}