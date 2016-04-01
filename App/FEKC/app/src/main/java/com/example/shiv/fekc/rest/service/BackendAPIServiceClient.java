package com.example.shiv.fekc.rest.service;

import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.rest.client.BackendAPI;

import retrofit.RestAdapter;

/**
 * Created by shiv on 30/3/16.
 */
public class BackendAPIServiceClient {

    private String BASE_URL = Constants.BACKEND_SERVER;
    private BackendAPI backendAPI;

    public BackendAPIServiceClient(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        backendAPI = restAdapter.create(BackendAPI.class);
    }

    public BackendAPI getService(){
        return this.backendAPI;
    }
}
