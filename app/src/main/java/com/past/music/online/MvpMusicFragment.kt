package com.past.music.online

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.kmvp.KMvpPage
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2018/2/4.
 * 音乐馆Fragment
 */
class MvpMusicFragment : Fragment(), KMvpPage {
    companion object {
        fun newInstance(): MvpMusicFragment {
            return MvpMusicFragment()
        }
    }

    override fun onPageError(exception: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_mvp, container, false)
    }
}