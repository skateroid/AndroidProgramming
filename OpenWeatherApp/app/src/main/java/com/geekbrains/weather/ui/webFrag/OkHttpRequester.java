package com.geekbrains.weather.ui.webFrag;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequester {
    private OnResponseCompleted onResponseCompleted;

    public OkHttpRequester(OnResponseCompleted onResponseCompleted) {
        this.onResponseCompleted = onResponseCompleted;
    }

    public void run(String url) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);

        final Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("OkHttp", e.getMessage(), e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String answer = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                      onResponseCompleted.onCompleted(answer);
                    }
                });
            }
        });
    }

    public interface OnResponseCompleted {
        void onCompleted(String content);
    }
}
