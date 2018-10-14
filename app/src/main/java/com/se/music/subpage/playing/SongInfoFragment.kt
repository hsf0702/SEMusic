package com.se.music.subpage.playing

import android.os.Bundle
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.se.music.R
import com.se.music.base.BaseFragment
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import com.se.music.service.MusicPlayer
import com.se.music.subpage.entity.OtherVersionInfo
import com.se.music.utils.GET_RELATED_SONG
import retrofit2.Call

/**
 *Author: gaojin
 *Time: 2018/10/7 下午7:45
 */

class SongInfoFragment : BaseFragment() {

    companion object {
        const val TAG = "SongInfoFragment"
        fun newInstance(): SongInfoFragment {
            return SongInfoFragment()
        }
    }

    private lateinit var songOrigin: TextView
    private lateinit var songArtist: TextView
    private lateinit var songAlbum: TextView
    private lateinit var songInfo: TextView
    private lateinit var songInfoDetail: TextView
    private lateinit var playingSongRelated: PlayingSongRelatedInfoBlock

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playing_song_info, container, false)
        songOrigin = view.findViewById(R.id.song_origin)
        songArtist = view.findViewById(R.id.song_artist)
        songAlbum = view.findViewById(R.id.song_album)
        songInfo = view.findViewById(R.id.playing_song_info)
        songInfoDetail = view.findViewById(R.id.playing_song_info_detail)
        playingSongRelated = view.findViewById(R.id.related_song_block)
        view.tag = TAG
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateData()
    }

    override fun musciChanged() {
        super.musciChanged()
        updateData()
    }

    private fun updateData() {
        if (MusicPlayer.isTrackLocal()) {
            songOrigin.text = "本地音乐"
        } else {
            songOrigin.text = "在线音乐"
        }
        songArtist.text = MusicPlayer.getArtistName()
        songAlbum.text = MusicPlayer.getAlbumName()
        songInfo.text = resources.getText(R.string.playing_song_info)

        if (!MusicPlayer.getTrackName().isEmpty()) {
            loaderManager.restartLoader(GET_RELATED_SONG, null, buildRelatedSongCallback(MusicPlayer.getTrackName()))
        }
    }

    private fun buildRelatedSongCallback(trackName: String): CallLoaderCallbacks<OtherVersionInfo> {
        return object : CallLoaderCallbacks<OtherVersionInfo>(context!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<OtherVersionInfo> {
                return MusicRetrofit.instance.getRelatedSongInfo(trackName)
            }

            override fun onSuccess(loader: Loader<*>, data: OtherVersionInfo) {
                playingSongRelated.addOtherVersionInfo(data)
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
            }
        }
    }
}