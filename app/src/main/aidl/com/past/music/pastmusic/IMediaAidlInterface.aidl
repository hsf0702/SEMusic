// IMediaAidlInterface.aidl
package com.past.music.pastmusic;

// Declare any non-default types here with import statements

interface IMediaAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    boolean isPlaying();
    void stop();
    void pause();
    void play();
}
