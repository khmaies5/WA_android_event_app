package com.khmaies.waandroideventapp.data.repository;

import com.khmaies.waandroideventapp.data.model.User;
import com.khmaies.waandroideventapp.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */
@Singleton
public class UserRepository {

    private ApiService apiService;

    @Inject
    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getUsers(Callback<List<User>> callback) {
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(callback);
    }

}
