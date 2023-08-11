package com.khmaies.waandroideventapp.data.repository;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

import com.khmaies.waandroideventapp.data.model.Event;
import com.khmaies.waandroideventapp.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;

@Singleton
public class EventRepository {

    private ApiService apiService;

    @Inject
    public EventRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getEvents(Callback<List<Event>> callback) {
        Call<List<Event>> call = apiService.getEvents();
        call.enqueue(callback);
    }

    public void createEvent(Event event, Callback<Event> callback) {
        Call<Event> call = apiService.createEvent(event);
        call.enqueue(callback);
    }
}

