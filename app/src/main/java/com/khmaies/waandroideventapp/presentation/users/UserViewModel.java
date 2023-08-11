package com.khmaies.waandroideventapp.presentation.users;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khmaies.waandroideventapp.data.model.User;
import com.khmaies.waandroideventapp.data.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    private MutableLiveData<List<User>> _user = new MutableLiveData<>();
    public LiveData<List<User>> user = _user;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getUsers() {
        userRepository.getUsers(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // Process the response as needed
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    // Apply your filter logic here if needed
                    // Call the provided callback with the filtered tasks
                    Log.e(UserViewModel.class.getName(), response.body().toString());

                    _user.postValue(userList);
                } else {
                    Log.e(UserViewModel.class.getName(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(UserViewModel.class.getName(), t.toString());
            }
        });
    }
}