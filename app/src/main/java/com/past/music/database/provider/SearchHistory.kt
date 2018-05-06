package com.past.music.database.provider

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.past.music.database.MusicDBHelper

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:53
 */
class SearchHistory {

    private val MAX_ITEMS_IN_DB = 25

    companion object {
        val instance: SearchHistory by lazy { SearchHistory() }
    }

    fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SearchHistoryColumns.NAME + " ("
                + SearchHistoryColumns.SEARCHSTRING + " TEXT NOT NULL,"
                + SearchHistoryColumns.TIMESEARCHED + " LONG NOT NULL);")
    }

    fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryColumns.NAME)
        onCreate(db)
    }

    fun addSearchString(searchString: String?) {
        if (searchString == null) {
            return
        }

        val trimmedString = searchString.trim { it <= ' ' }

        if (trimmedString.isEmpty()) {
            return
        }

        val database = MusicDBHelper.instance.writableDatabase
        database.beginTransaction()

        try {

            database.delete(SearchHistoryColumns.NAME,
                    SearchHistoryColumns.SEARCHSTRING + " = ? COLLATE NOCASE",
                    arrayOf(trimmedString))

            val values = ContentValues(2)
            values.put(SearchHistoryColumns.SEARCHSTRING, trimmedString)
            values.put(SearchHistoryColumns.TIMESEARCHED, System.currentTimeMillis())
            database.insert(SearchHistoryColumns.NAME, null, values)

            var oldest: Cursor? = null
            try {
                database.query(SearchHistoryColumns.NAME,
                        arrayOf(SearchHistoryColumns.TIMESEARCHED), null, null, null, null,
                        SearchHistoryColumns.TIMESEARCHED + " ASC")

                if (oldest != null && oldest.count > MAX_ITEMS_IN_DB) {
                    oldest.moveToPosition(oldest.count - MAX_ITEMS_IN_DB)
                    val timeOfRecordToKeep = oldest.getLong(0)

                    database.delete(SearchHistoryColumns.NAME,
                            SearchHistoryColumns.TIMESEARCHED + " < ?",
                            arrayOf(timeOfRecordToKeep.toString()))

                }
            } finally {
                if (oldest != null) {
                    oldest.close()
                    oldest = null
                }
            }
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
        }
    }

    fun deleteRecentSearches(name: String) {
        val database = MusicDBHelper.instance.readableDatabase
        database.delete(SearchHistoryColumns.NAME, SearchHistoryColumns.SEARCHSTRING + " = ?", arrayOf(name))
    }


    fun queryRecentSearches(limit: String): Cursor {
        val database = MusicDBHelper.instance.readableDatabase
        return database.query(SearchHistoryColumns.NAME,
                arrayOf(SearchHistoryColumns.SEARCHSTRING), null, null, null, null,
                SearchHistoryColumns.TIMESEARCHED + " DESC", limit)
    }

    fun getRecentSearches(): java.util.ArrayList<String> {
        var searches: Cursor? = queryRecentSearches(MAX_ITEMS_IN_DB.toString())

        val results = ArrayList<String>(MAX_ITEMS_IN_DB)

        try {
            if (searches != null && searches.moveToFirst()) {
                val colIdx = searches.getColumnIndex(SearchHistoryColumns.SEARCHSTRING)

                do {
                    results.add(searches.getString(colIdx))
                } while (searches.moveToNext())
            }
        } finally {
            if (searches != null) {
                searches.close()
            }
        }

        return results
    }

    interface SearchHistoryColumns {
        companion object {
            /* Table name */
            val NAME = "searchhistory"

            /* What was searched */
            val SEARCHSTRING = "searchstring"

            /* Time of search */
            val TIMESEARCHED = "timesearched"
        }
    }
}