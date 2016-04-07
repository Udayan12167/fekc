package com.example.shiv.fekc.rest.client;

import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.rest.response.RegisterUserResponse;
import com.example.shiv.fekc.rest.response.TaskCreateResponse;
import com.example.shiv.fekc.rest.response.TaskDeleteResponse;
import com.example.shiv.fekc.rest.response.TaskMessageResponse;
import com.example.shiv.fekc.rest.response.TrackedFriendsTaskResponse;
import com.example.shiv.fekc.rest.response.UpdateGCMTokenResponse;
import com.example.shiv.fekc.rest.response.UpdateUserMessageResponse;
import com.example.shiv.fekc.rest.response.ViolationObjectResponse;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by shiv on 30/3/16.
 */
public interface BackendAPI {

    @FormUrlEncoded
    @POST(Constants.REGISTER_USER_ENDPOINT)
    void registerUser(@FieldMap Map<String, String> options, Callback<RegisterUserResponse> callback);

    @FormUrlEncoded
    @PUT(Constants.UPDATE_USER_GCM_ENDPOINT + Constants.ID_PARAMETER)
    void updateUserGCMToken(@Path(Constants.ID) String id , @FieldMap Map<String, String> options, Callback<UpdateGCMTokenResponse> callback);

    @FormUrlEncoded
    @POST(Constants.CREATE_TASK_ENDPOINT)
    void createTask(@FieldMap Map<String, String> options, Callback<TaskCreateResponse> callback);

    @GET(Constants.GET_TRACKED_TASK_ENDPOINT + Constants.ID_PARAMETER)
    void getTrackedTasks(@Path(Constants.ID) String id, @QueryMap Map<String, String> options, Callback<TrackedFriendsTaskResponse> callback);

    @FormUrlEncoded
    @PUT(Constants.PUT_MESSAGE_ENDPOINT + Constants.ID_PARAMETER)
    void updateUserTaskMessage(@Path(Constants.ID)  String taskId, @FieldMap Map<String, String> options, Callback<UpdateUserMessageResponse> callback);

    @GET(Constants.GET_MESSAGES_ENDPOINT + Constants.ID_PARAMETER)
    void getUserTaskMessages(@Path(Constants.ID) String id, @QueryMap Map<String, String> options, Callback<TaskMessageResponse> callback);

    @FormUrlEncoded
    @POST(Constants.POST_VIOLATION_ENDPOINT)
    void postViolation(@FieldMap Map<String, String> options, Callback<ViolationObjectResponse> callback);

    @FormUrlEncoded
    @POST(Constants.POST_WIN_ENDPOINT)
    void postWinWin(@FieldMap Map<String, String> options, Callback<ViolationObjectResponse> callback);

    @DELETE(Constants.DELETE_TASK_ENDPOINT + Constants.ID_PARAMETER)
    void deleteTask(@Path(Constants.ID) String id, @QueryMap Map<String, String> options, Callback<TaskDeleteResponse> callback);
}
