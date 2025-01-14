package org.mk.demo.streaming.states;

import com.squareup.okhttp.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.mk.demo.modles.GeoLocation;

import java.io.IOException;
import java.util.Collections;

public class HttpClientFunction extends RichAsyncFunction<GeoLocation, GeoLocation> {

    private transient OkHttpClient client;

    @Override
    public void open(Configuration parameters) {
        client = new OkHttpClient();
    }


    @Override
    public void asyncInvoke(GeoLocation geoLocation, ResultFuture<GeoLocation> resultFuture) throws Exception {
        Request request = new Request.Builder()
                .get()
                .url("https://geocoder.ls.hereapi.com/6.2/geocode.json?apiKey=bvOgkgslNqwYVYyKx3Ld3NCVh8BSTFDRusHEFt7USIU&searchtext=425+W+Randolph+Chicago")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                resultFuture.complete(Collections.emptyList());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                geoLocation.setApiResponse(response.body().string());
                resultFuture.complete(Collections.singleton(geoLocation));
            }
        });
    }
}
