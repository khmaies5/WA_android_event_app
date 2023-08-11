package com.khmaies.waandroideventapp.presentation.meetings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khmaies.waandroideventapp.data.model.Meeting;
import com.khmaies.waandroideventapp.data.repository.MeetingRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class MeetingViewModel extends ViewModel {
    private final MeetingRepository meetingRepository;

    @Inject
    public MeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    private MutableLiveData<List<Meeting>> _meetings = new MutableLiveData<>();
    public LiveData<List<Meeting>> meetings = _meetings;

    public void getMeetings() {
        meetingRepository.getMeetings(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                // Process the response as needed
                if (response.isSuccessful()) {
                    List<Meeting> meetingList = response.body();
                    // Apply your filter logic here if needed
                    // Call the provided callback with the filtered tasks
                    _meetings.postValue(meetingList);
                } else {
                    Log.e(MeetingViewModel.class.getName(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {
                Log.e(MeetingViewModel.class.getName(), t.toString());
            }
        });
    }

    public void createMeeting(Meeting meeting, Callback<Meeting> callback) {
        meetingRepository.createMeeting(meeting, new Callback<Meeting>() {
            @Override
            public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                List<Meeting> newMeetingList = _meetings.getValue();
                if (newMeetingList != null) {
                    newMeetingList.add(meeting);
                }
                _meetings.postValue(newMeetingList);
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Meeting> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}