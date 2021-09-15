package com.datechnologies.androidtest.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.ChatLogMessageModel;
import com.datechnologies.androidtest.api.MessageResponse;
import com.datechnologies.androidtest.api.MessageService;
import com.datechnologies.androidtest.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Screen that displays a list of chats from a chat log.
 */
public class ChatActivity extends AppCompatActivity {

    public static String TAG = "ChatActivity";

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, ChatActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(R.string.activity_chat_title);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);

        chatAdapter = new ChatAdapter(this);

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        performGetMessagesRequest();
    }

    private void performGetMessagesRequest() {
        MessageService messageService = RetrofitClient.getInstance().create(MessageService.class);
        Call<MessageResponse> call = messageService.getMessages();

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.i(TAG, "Get messages response success");
                handleGetMessagesResponse(response.body());
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.i(TAG, "Get messages request failed");
            }
        });
    }

    private void handleGetMessagesResponse(MessageResponse messageResponse) {
        List<ChatLogMessageModel> messages = messageResponse.getData();
        chatAdapter.setChatLogMessageModelList(messages);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
