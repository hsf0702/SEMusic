package com.se.music.pastmusic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.neu.gaojin.MyOkHttpClient;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.past.music.pastmusic", appContext.getPackageName());
    }

    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14";
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36")
                .build();
        MyOkHttpClient.getInstance(appContext).getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("1112", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("11122", response.body().string());
            }
        });

        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        builder = builder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        Request request1 = builder.build();
        try {
            Response response = MyOkHttpClient.getInstance(appContext).getClient().newCall(request1).execute();
            if (response.isSuccessful()) {
                Log.i("111222", response.body().string());
            } else {
                Log.e("1112222", "请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
