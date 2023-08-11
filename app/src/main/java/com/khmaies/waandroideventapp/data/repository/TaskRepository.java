package com.khmaies.waandroideventapp.data.repository;

import com.khmaies.waandroideventapp.data.model.Task;
import com.khmaies.waandroideventapp.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;

@Singleton
public class TaskRepository {

    private ApiService apiService;

    @Inject
    public TaskRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getTasks(Callback<List<Task>> callback) {
        Call<List<Task>> call = apiService.getTasks();
        call.enqueue(callback);
    }

    public void createTask(Task task, Callback<Task> callback) {
        Call<Task> call = apiService.createTask(task);
        call.enqueue(callback);
    }
}
