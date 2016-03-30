package com.example.shiv.fekc.rest.client;

import com.example.shiv.fekc.commons.Constants;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by shiv on 30/3/16.
 */
public interface BackendAPI {

    @FormUrlEncoded
    @POST(Constants.REGISTER_USER_ENDPOINT)
    void registerUser(@FieldMap Map<String, String> options, Callback<String> callback);

}
