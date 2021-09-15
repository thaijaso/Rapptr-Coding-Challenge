package com.datechnologies.androidtest.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("Tests/scripts/login.php")
    Call<LoginResponse> login(@Field("email") String username, @Field("password") String password);
}
