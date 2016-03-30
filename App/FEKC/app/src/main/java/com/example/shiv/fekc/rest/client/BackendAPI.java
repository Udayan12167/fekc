package com.example.shiv.fekc.rest.client;

import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.rest.response.RegisterUserResponse;
import com.example.shiv.fekc.rest.response.TaskCreateResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by shiv on 30/3/16.
 */
public interface BackendAPI {

    @FormUrlEncoded
    @POST(Constants.REGISTER_USER_ENDPOINT)
    void registerUser(@FieldMap Map<String, String> options, Callback<RegisterUserResponse> callback);

    @FormUrlEncoded
    @PUT(Constants.UPDATE_USER_GCM_ENDPOINT + Constants.ID_PARAMETER)
    void updateUserGCMToken(@Path(Constants.ID) String id , @FieldMap Map<String, String> options, Callback<String> callback);

    @FormUrlEncoded
    @POST(Constants.CREATE_TASK_ENDPOINT)
    void createTask(@FieldMap Map<String, String> options, Callback<TaskCreateResponse> callback);

}
