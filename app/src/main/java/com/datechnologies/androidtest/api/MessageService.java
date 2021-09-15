package com.datechnologies.androidtest.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageService {
    @GET("Tests/scripts/chat_log.php")
    Call<MessageResponse> getMessages();
}
