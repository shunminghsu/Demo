package com.henry.project.network;


import android.content.Context;

import com.henry.project.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ShunMing on 2017/10/7.
 */

public class ApiInterceptor implements Interceptor {
    private Context mContext;

    public ApiInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // intercept will be called for every request you make
        // we will return mock response when using debug build

        Response response = null;
        if (BuildConfig.DEBUG) {
            // http://sample.com/city_guide?page=0 will return "/city_guide"
            String jsonFile = chain.request().url().encodedPath() + ".json";
            String responseString = readJsonFromAssets(jsonFile);

            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        } else {
            response = chain.proceed(chain.request());
        }

        return response;
    }

    private String readJsonFromAssets(String local_json_file) {
        // read the json array in /assets/mockJson/local_json_file.json
        InputStream stream = null;
        JSONObject newsCollection = new JSONObject();
        try {
            stream = mContext.getAssets().open("mockJson" + local_json_file);
            String json = parseStream(stream);
            newsCollection.put("results", new JSONArray(json));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsCollection.toString();
    }

    private String parseStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }
        in.close();
        return builder.toString();
    }
}
