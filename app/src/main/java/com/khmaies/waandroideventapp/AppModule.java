package com.khmaies.waandroideventapp;

import static com.khmaies.waandroideventapp.network.RetrofitClient.getApiService;

import com.khmaies.waandroideventapp.data.repository.EventRepository;
import com.khmaies.waandroideventapp.data.repository.MeetingRepository;
import com.khmaies.waandroideventapp.data.repository.TaskRepository;
import com.khmaies.waandroideventapp.data.repository.UserRepository;
import com.khmaies.waandroideventapp.network.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Created by Khmaies Hassen on 10,August,2023
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public ApiService provideApiService() {
        return getApiService();
    }

    @Provides
    @Singleton
    public EventRepository provideEventRepository(ApiService apiService) {
        return new EventRepository(apiService);
    }

    @Provides
    @Singleton
    public MeetingRepository provideMeetingRepository(ApiService apiService) {
        return new MeetingRepository(apiService);
    }

    @Provides
    @Singleton
    public TaskRepository provideTaskRepository(ApiService apiService) {
        return new TaskRepository(apiService);
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(ApiService apiService) {
        return new UserRepository(apiService);
    }
}
