package com.henry.project.network;

import com.henry.project.model.ItemCollection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ShunMing on 2017/10/4.
 */
/* we will call server to get item list of specify type and page
   for example, we call /city_guide?page=0, then we should receive
   a 10 items list which are the top 10 datas for city guide, call
   /city_guide?page=1 will get the next 10 items
*/
public interface ApiService {
    //the type could be city_guide, shop, or eat...
    //ex: URL/city_guide?page=0
    @GET("/{type}")
    Call<ItemCollection> getItems(@Path("type") String type, @Query("page") int page);
}
