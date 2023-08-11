package com.khmaies.waandroideventapp.presentation.tasks;

import static com.khmaies.waandroideventapp.data.utils.DateUtils.showDateTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.khmaies.waandroideventapp.R;
import com.khmaies.waandroideventapp.data.model.Task;
import com.khmaies.waandroideventapp.databinding.FragmentTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class TaskFragment extends Fragment {

    static TaskViewModel taskViewModel;
    private FragmentTaskBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        binding.fabAddTask.setOnClickListener(v -> showCreateTaskDialog());
        return binding.getRoot();
    }

    private void showCreateTaskDialog() {
        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
        dialog.show(getParentFragmentManager(), "CreateTaskDialog");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Set up RecyclerView and adapter
        TaskAdapter taskAdapter = new TaskAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(taskAdapter);

        taskViewModel.tasks.observe(getViewLifecycleOwner(), tasks -> taskAdapter.setTasks(tasks));
        // Observe the tasks LiveData from the ViewModel
        taskViewModel.getTasks();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class CreateTaskDialogFragment extends DialogFragment {
        EditText etDateTime;
        Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_create_task, null);
            EditText etTaskTitle = view.findViewById(R.id.etTaskTitle);
            CheckBox taskStatus = view.findViewById(R.id.cbTaskStatus);
            etDateTime = view.findViewById(R.id.taskDateTime);

            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = Calendar.getInstance();

            etDateTime.setOnClickListener(v -> date = showDateTimePicker(getContext(), date, dateTime ->
                    etDateTime.setText(date.getTime().toString())));

            builder.setView(view)
                    .setTitle("Create New Task")
                    .setPositiveButton("Create", (dialog, id) -> {

                        formattedDate = sdf.format(date.getTime());

                        int taskId = Objects.requireNonNull(taskViewModel.tasks.getValue()).get(taskViewModel.tasks.getValue().size() - 1).getId() + 1;

                        Task task = new Task(taskId, etTaskTitle.getText().toString(), formattedDate, taskStatus.isChecked());
                        taskViewModel.createTask(task, new Callback<Task>() {
                            @Override
                            public void onResponse(Call<Task> call, Response<Task> response) {
                                Snackbar.make(view, "Task created!", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Task> call, Throwable t) {
                                Snackbar.make(view, "Task creation error!", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    });

            return builder.create();
        }
    }
}