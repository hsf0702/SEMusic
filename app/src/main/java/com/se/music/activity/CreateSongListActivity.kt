package com.se.music.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.*
import android.widget.EditText
import com.se.music.R
import com.se.music.common.ToolBarActivity
import com.se.music.provider.MetaData
import com.se.music.utils.IdUtils
import java.util.*

/**
 *Author: gaojin
 *Time: 2018/5/13 下午5:49
 */

class CreateSongListActivity : ToolBarActivity() {

    companion object {
        val resultCode = IdUtils.generateLoaderId()
    }

    var nameInput: EditText? = null
    var infoInput: EditText? = null

    override fun createContentView(inflater: LayoutInflater, rootView: ViewGroup): View {
        return inflater.inflate(R.layout.activity_create_song_list, rootView, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.create_new_song_list)

        nameInput = findViewById(R.id.et_list_name)
        infoInput = findViewById(R.id.et_list_info)
        nameInput!!.setText(resources.getString(R.string.create_new_song_list))
        nameInput!!.setSelectAllOnFocus(true)
        nameInput!!.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_save -> save()
        }
        return super.onOptionsItemSelected(item)
    }

    fun save() {
        val listName = nameInput!!.text.toString()
        val listInfo = infoInput!!.text.toString()

        val values = ContentValues()
        values.put(MetaData.SongList.ID, UUID.randomUUID().toString())
        values.put(MetaData.SongList.NAME, listName)
        values.put(MetaData.SongList.CREATE_TIME, System.currentTimeMillis())
        values.put(MetaData.SongList.INFO, listInfo)
        contentResolver.insert(MetaData.SongList.CONTENT_URI, values)
        setResult(resultCode)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }

}