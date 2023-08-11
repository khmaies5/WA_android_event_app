package com.khmaies.waandroideventapp.network;


import com.khmaies.waandroideventapp.data.model.Event;
import com.khmaies.waandroideventapp.data.model.Meeting;
import com.khmaies.waandroideventapp.data.model.Task;
import com.khmaies.waandroideventapp.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

public interface ApiService {

    @GET("tasks")
    Call<List<Task>> getTasks();

    @POST("tasks")
    Call<Task> createTask(@Body Task task);

    @GET("event")
    Call<List<Event>> getEvents();

    @POST("event")
    Call<Event> createEvent(@Body Event event);

    @GET("meetings")
    Call<List<Meeting>> getMeetings();

    @POST("meetings")
    Call<Meeting> createMeeting(@Body Meeting meeting);

    @GET("users")
    Call<List<User>> getUsers();
}

