package com.khmaies.waandroideventapp.presentation.tasks;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khmaies.waandroideventapp.data.model.Task;
import com.khmaies.waandroideventapp.data.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class TaskViewModel extends ViewModel {

    private final TaskRepository taskRepository;

    @Inject
    public TaskViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private MutableLiveData<List<Task>> _tasks = new MutableLiveData<>();
    public LiveData<List<Task>> tasks = _tasks;

    private MutableLiveData<List<Task>> _filteredTasks = new MutableLiveData<>();
    public LiveData<List<Task>> filteredTasks = _filteredTasks;

    public void getTasks() {
        taskRepository.getTasks(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                // Process the response as needed
                if (response.isSuccessful()) {
                    List<Task> taskList = response.body();
                    // Apply your filter logic here if needed
                    // Call the provided callback with the filtered tasks
                    _tasks.postValue(taskList);
                } else {
                    Log.e(TaskViewModel.class.getName(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e(TaskViewModel.class.getName(), t.toString());
            }
        });
    }

    public void createTask(Task task, Callback<Task> callback) {
        taskRepository.createTask(task, new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                List<Task> newTaskList = _tasks.getValue();
                if (newTaskList != null) {
                    newTaskList.add(task);
                }
                _tasks.postValue(newTaskList);
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void getFilteredTasksById(String id) {
        List<Task> allTasks = tasks.getValue();
        if (allTasks == null) {
            return;
        }

        if (TextUtils.isEmpty(id)) {
            _filteredTasks.postValue(allTasks);
            return;
        }

        List<Task> searchedTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (Integer.toString(task.getId()).contains(id)) {
                searchedTasks.add(task);
            }
        }
        _filteredTasks.postValue(searchedTasks);
    }

}