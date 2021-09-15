package com.datechnologies.androidtest.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.LoginResponse;
import com.datechnologies.androidtest.api.LoginService;
import com.datechnologies.androidtest.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 */
public class LoginActivity extends AppCompatActivity {

    public static String TAG = "LoginActivity";

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.activity_login_title);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);

        // TODO: The AlertDialog should also display how long the API call took in milliseconds.

        // TODO: The only valid login credentials are:
        // TODO: email: info@rapptrlabs.com
        // TODO: password: Test123
        // TODO: so please use those to test the login.
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

    public void onLoginClicked(View v) {
        Log.i(TAG, "Login button clicked");

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        performLoginRequest(username, password);
    }

    /**
     * Send an HTTP POST request to the server and display the response to the user.
     *
     * @param username is an email credential used to login
     * @param password is the password credential used to login
     */
    private void performLoginRequest(String username, String password) {
        Long startTime = System.currentTimeMillis();
        LoginService loginService = RetrofitClient.getInstance().create(LoginService.class);
        Call<LoginResponse> call = loginService.login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Long endTime = System.currentTimeMillis();
                Long responseTime = getResponseTime(startTime, endTime);
                Log.i(TAG, "Got a response from the server in " + responseTime + " ms");
                handleLoginResponse(response, responseTime);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login request failed");
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private Long getResponseTime(Long startTime, Long endTime) {
        return endTime - startTime;
    }

    private void handleLoginResponse(Response<LoginResponse> response, Long responseTime) {
        // login failed if response body is null
        if (response.body() == null) {
           handleLoginResponseFail(response, responseTime);
        } else {
            // login success
            handleLoginResponseSuccess(response, responseTime);
        }
    }

    private void handleLoginResponseFail(Response<LoginResponse> response, Long responseTime) {
        ResponseBody responseBody = response.errorBody();

        try {
            String errorResponse = responseBody.string(); // JSON String representation
            Log.i(TAG, errorResponse);

            // convert String to JSONObject and parse code and message:
            JSONObject errorResponseJSON = new JSONObject(errorResponse);
            String code = errorResponseJSON.getString("code");
            String message = errorResponseJSON.getString("message");

            showAlertDialog(code, message, responseTime);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginResponseSuccess(Response<LoginResponse> response, Long responseTime) {
        LoginResponse loginResponse = response.body();
        String code = loginResponse.getCode();
        String message = loginResponse.getMessage();

        showAlertDialog(code, message, responseTime);
    }

    private void showAlertDialog(String code, String message, Long responseTime) {
        Context applicationContext = this;

        // https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        new AlertDialog.Builder(applicationContext)
                .setTitle(code)
                .setMessage(message + " (" + responseTime + " ms)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (code.equals("Success")) MainActivity.start(applicationContext);
                    }
                })
                .show();
    }
}
