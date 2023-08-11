package com.khmaies.waandroideventapp.data.repository;

import com.khmaies.waandroideventapp.data.model.Meeting;
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
public class MeetingRepository {

    private ApiService apiService;

    @Inject
    public MeetingRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getMeetings(Callback<List<Meeting>> callback) {
        Call<List<Meeting>> call = apiService.getMeetings();
        call.enqueue(callback);
    }

    public void createMeeting(Meeting meeting, Callback<Meeting> callback) {
        Call<Meeting> call = apiService.createMeeting(meeting);
        call.enqueue(callback);
    }
}
