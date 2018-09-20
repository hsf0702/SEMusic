// IMediaAidlInterface.aidl
package com.se.music;

// Declare any non-default types here with import statements

interface IMediaAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean isPlaying();
    void stop();
    void pause();
    void play();
    void nextPlay();
    void openFile(String path);
    void open(in Map infos, in long [] list, int position);
    String getArtistName();
    String getTrackName();
    String getAlbumName();
    String getAlbumPath();
    String getAlbumPic();
     long getAlbumId();
    String[] getAlbumPathtAll();
    String getPath();
    Map getPlaylistInfo();
    long [] getQueue();
    void setQueuePosition(int index);
    long getAudioId();
    int getQueueSize();
    int getQueuePosition();
    int removeTrack(long id);
    void enqueue(in long [] list,in Map infos, int action);
    int getRepeatMode();
    void setRepeatMode(int repeatmode);
    long duration();
    long position();
    long seek(long pos);
    boolean isTrackLocal();
    int secondPosition();
}
