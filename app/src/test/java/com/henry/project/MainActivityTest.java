package com.henry.project;

import android.util.Log;

import com.henry.project.network.ApiComponent;
import com.henry.project.network.ApiInterceptor;
import com.henry.project.network.ApiManager;
import com.henry.project.network.ApiService;
import com.henry.project.network.ComponentHolder;
import com.henry.project.network.DaggerApiComponent;
import com.henry.project.network.GetItemsModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by ShunMing on 2017/10/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {

    @Test
    public void testMainActivity() throws IOException, InterruptedException {
        GetItemsModule mockGetItemsModule = spy(new GetItemsModule(RuntimeEnvironment.application));
        ApiManager mockApiManager = mock(ApiManager.class);

        //mock the mApiManager in MainActivity
        Mockito.when(mockGetItemsModule.provideApiManager(any(ApiService.class)))
                .thenReturn(mockApiManager);

        //replace original interceptor, so we can generate json string instead of reading json from assets
        Mockito.when(mockGetItemsModule.provideApiInterceptor())
                .thenReturn( new ApiInterceptor(RuntimeEnvironment.application) {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = null;
                        String responseString = getMockRespones();

                        response = new Response.Builder()
                                .code(200)
                                .message(responseString)
                                .request(chain.request())
                                .protocol(Protocol.HTTP_1_0)
                                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                                .addHeader("content-type", "application/json")
                                .build();

                        return response;
                    }
                });


        //using mockGetItemsModule to build DaggerAppComponent
        ApiComponent appComponent = DaggerApiComponent.builder()
                .getItemsModule(mockGetItemsModule)
                .build();
        ComponentHolder.setAppComponent(appComponent);

        //MainActivity will start to do onCreate, onResume...
        Robolectric.setupActivity(MainActivity.class);

        //the tab number is control by a string array
        String[] tabs = RuntimeEnvironment.application.getResources().getStringArray(R.array.tabs_array);
        //MainActivity will connect to server and update list for each tab
        //if there is 3 tabs, updateItems should be executed 3 times
        verify(mockApiManager, times(tabs.length)).updateItems(anyInt(), any(RecyclerViewAdapter.class), anyInt());

        //Retrofit, OkHttpClient, and Interceptor are created only once
        verify(mockGetItemsModule, times(1)).provideRetrofit(any(OkHttpClient.class));
        verify(mockGetItemsModule, times(1)).provideOkHttpClient(any(ApiInterceptor.class));
        verify(mockGetItemsModule, times(1)).provideApiInterceptor();
    }


    private String getMockRespones() {
        String example = "This is s sample description for testing. This is s sample description for testing. This is s sample description for testing.";

        JSONArray array = new JSONArray();
        array.put(createJson("http://via.placeholder.com/100x100?text=item1", "title1", example));
        array.put(createJson("http://via.placeholder.com/300x150?text=item2", "", ""));
        array.put(createJson("http://via.placeholder.com/100x100?text=item3", "title3", example));
        array.put(createJson("http://via.placeholder.com/300x150?text=item4", "", ""));
        array.put(createJson("http://via.placeholder.com/100x100?text=item5", "title5", example));
        array.put(createJson("http://via.placeholder.com/300x150?text=item6", "", ""));
        array.put(createJson("http://via.placeholder.com/100x100?text=item7", "title7", example));
        array.put(createJson("http://via.placeholder.com/300x150?text=item8", "", ""));
        array.put(createJson("http://via.placeholder.com/100x100?text=item9", "title9", example));
        array.put(createJson("http://via.placeholder.com/300x150?text=item10", "", ""));

        JSONObject itemsCollection = new JSONObject();
        try {
            itemsCollection.put("results", array);
        } catch (JSONException e) {
               Log.d("debug", e.getMessage());
        }
        return itemsCollection.toString();
    }

    private JSONObject createJson(String image_path, String title, String description) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("image_path", image_path);
            obj.put("title", title);
            obj.put("description", description);
            obj.put("just_image", title.isEmpty());
        } catch (JSONException e) {
               Log.d("debug", e.getMessage());
        }
        return obj;
    }
}
