package com.example.squad.atg.Interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface getData {

    @GET("services/rest/")
    Call<String> getAllPhotos(@Query("method") String m,
                              @Query("per_page") String ppage,
                              @Query("page") int page,
                              @Query("api_key") String key,
                              @Query("format") String format,
                              @Query("nojsoncallback") String i,
                              @Query("extras") String s);

    @GET("services/rest/")
    Call<String> getAllPhotos(@Query("method") String m,
                              @Query("api_key") String key,
                              @Query("format") String format,
                              @Query("nojsoncallback") String i,
                              @Query("extras") String s,
                              @Query("text") String es);
}
