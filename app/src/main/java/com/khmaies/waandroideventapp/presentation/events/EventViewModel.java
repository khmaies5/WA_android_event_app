package com.khmaies.waandroideventapp.presentation.events;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khmaies.waandroideventapp.data.model.Event;
import com.khmaies.waandroideventapp.data.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class EventViewModel extends ViewModel {
    private final EventRepository eventRepository;

    @Inject
    public EventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private MutableLiveData<List<Event>> _events = new MutableLiveData<>();
    public LiveData<List<Event>> events = _events;

    private MutableLiveData<List<Event>> _filteredEvents = new MutableLiveData<>();
    public LiveData<List<Event>> filteredEvents = _filteredEvents;

    public void getEvents() {
        eventRepository.getEvents(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                // Process the response as needed
                if (response.isSuccessful()) {
                    List<Event> eventList = response.body();
                    // Apply your filter logic here if needed
                    // Call the provided callback with the filtered tasks
                    _events.postValue(eventList);
                } else {
                    Log.e(EventViewModel.class.getSimpleName(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(EventViewModel.class.getSimpleName(), t.toString());
            }
        });
    }

    public void createTask(Event event, Callback<Event> callback) {
        eventRepository.createEvent(event, new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                List<Event> newEventList = _events.getValue();
                if (newEventList != null) {
                    newEventList.add(event);
                }
                _events.postValue(newEventList);
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void getFilteredEventsById(String id) {
        List<Event> allEvents = events.getValue();
        if (allEvents == null) {
            return;
        }

        if (TextUtils.isEmpty(id)) {
            _filteredEvents.postValue(allEvents);
            return;
        }

        List<Event> searched = new ArrayList<>();
        for (Event event : allEvents) {

            if (Integer.toString(event.getId()).contains(id)) {
                searched.add(event);
            }
        }
        _filteredEvents.postValue(searched);
    }
}