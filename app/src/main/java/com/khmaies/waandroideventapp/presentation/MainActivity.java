package com.khmaies.waandroideventapp.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.khmaies.waandroideventapp.R;
import com.khmaies.waandroideventapp.databinding.ActivityMainBinding;
import com.khmaies.waandroideventapp.presentation.events.EventViewModel;
import com.khmaies.waandroideventapp.presentation.meetings.MeetingViewModel;
import com.khmaies.waandroideventapp.presentation.tasks.TaskViewModel;
import com.khmaies.waandroideventapp.presentation.users.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    static TaskViewModel taskViewModel;
    static MeetingViewModel meetingViewModel;
    static EventViewModel eventViewModel;
    static UserViewModel userViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();


        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set up BottomNavigationView with NavController
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int destinationId = item.getItemId();
            navController.navigate(destinationId);
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (binding.bottomNavigationView.getSelectedItemId()) {
                    case R.id.taskFragment:
                        taskViewModel.getFilteredTasksById(newText);
                        break;
                    case R.id.meetingFragment:
                        meetingViewModel.getFilteredMeetingsById(newText);
                        break;
                    case R.id.eventFragment:
                        eventViewModel.getFilteredEventsById(newText);
                        break;
                    case R.id.userFragment:
                        userViewModel.getFilteredUsersById(newText);
                        break;
                }

                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            switch (binding.bottomNavigationView.getSelectedItemId()) {
                case R.id.taskFragment:
                    taskViewModel.getFilteredTasksById("");
                    break;
                case R.id.meetingFragment:
                    meetingViewModel.getFilteredMeetingsById("");
                    break;
                case R.id.eventFragment:
                    eventViewModel.getFilteredEventsById("");
                    break;
                case R.id.userFragment:
                    userViewModel.getFilteredUsersById("");
                    break;
            }
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }
}