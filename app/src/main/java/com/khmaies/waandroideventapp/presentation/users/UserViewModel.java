package com.khmaies.waandroideventapp.presentation.users;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khmaies.waandroideventapp.data.model.User;
import com.khmaies.waandroideventapp.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    private MutableLiveData<List<User>> _users = new MutableLiveData<>();
    public LiveData<List<User>> users = _users;

    private MutableLiveData<List<User>> _filteredUsers = new MutableLiveData<>();
    public LiveData<List<User>> filteredUsers = _filteredUsers;

    private MutableLiveData<Boolean> _error = new MutableLiveData<>(false);
    public LiveData<Boolean> error = _error;

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
                    _error.postValue(false);

                    _users.postValue(userList);
                } else {
                    _error.postValue(true);

                    Log.e(UserViewModel.class.getName(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                _error.postValue(true);

                Log.e(UserViewModel.class.getName(), t.toString());
            }
        });
    }

    public void getFilteredUsersById(String id) {
        List<User> allUsers = users.getValue();
        if (allUsers == null) {
            return;
        }

        if (TextUtils.isEmpty(id)) {
            _filteredUsers.postValue(allUsers);
            return;
        }

        List<User> searched = new ArrayList<>();
        for (User user : allUsers) {
            if (Integer.toString(user.getId()).contains(id)) {
                searched.add(user);
            }
        }
        _filteredUsers.postValue(searched);
    }
}