package com.example.dbms_app;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

interface Requests {
    @Multipart
    @POST("posts_image")
    Call<String> postimage(@Part MultipartBody.Part image_file);

    @GET("get_attendance")
    Call<List<Subjects>> get_attendance(
            @Query("uid") String uid
    );

    @POST("posts_info")
    Call<String> postinfo(@Body Stundent stundent);

    @GET("verify")
    Call<String> verify(
            @Query("username") String username,
            @Query("password") String password
    );


}
