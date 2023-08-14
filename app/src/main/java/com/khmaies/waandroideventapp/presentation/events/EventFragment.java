package com.khmaies.waandroideventapp.presentation.events;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;
import static com.khmaies.waandroideventapp.data.utils.DateUtils.showDateTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.khmaies.waandroideventapp.R;
import com.khmaies.waandroideventapp.data.model.Event;
import com.khmaies.waandroideventapp.databinding.FragmentEventBinding;
import com.khmaies.waandroideventapp.presentation.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    static EventViewModel eventViewModel;
    private static MainActivity sInstance;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.fabAddEvent.setOnClickListener(v -> showCreateEventDialog());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Set up RecyclerView and adapter
        EventAdapter eventAdapter = new EventAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(eventAdapter);

        eventViewModel.events.observe(getViewLifecycleOwner(), eventAdapter::setEvents);
        eventViewModel.filteredEvents.observe(getViewLifecycleOwner(), eventAdapter::setEvents);
        eventViewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), "Something went wrong!", LENGTH_LONG).show();
            }
        });

        // Observe the events LiveData from the ViewModel
        eventViewModel.getEvents();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sInstance = (MainActivity) getActivity();
    }

    private void showCreateEventDialog() {
        CreateEventDialogFragment dialog = new CreateEventDialogFragment();
        dialog.show(getParentFragmentManager(), "CreateEventDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        sInstance = null;
    }

    public static class CreateEventDialogFragment extends DialogFragment {
        EditText etDateTime;
        Calendar date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_create_event, null);
            EditText etEventTitle = view.findViewById(R.id.etEventTitle);
            etDateTime = view.findViewById(R.id.etDateTime);

            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = Calendar.getInstance();

            etDateTime.setOnClickListener(v -> date = showDateTimePicker(getContext(), date, dateTime ->
                    etDateTime.setText(date.getTime().toString())));

            builder.setView(view)
                    .setTitle("Create New Event")
                    .setPositiveButton("Create", (dialog, id) -> {

                        formattedDate = sdf.format(date.getTime());

                        int eventId = Objects.requireNonNull(eventViewModel.events.getValue()).get(eventViewModel.events.getValue().size() - 1).getId() + 1;

                        Event event = new Event(eventId, etEventTitle.getText().toString(), formattedDate);
                        eventViewModel.createTask(event, new Callback<Event>() {
                            @Override
                            public void onResponse(Call<Event> call, Response<Event> response) {
                                Toast.makeText(sInstance, "Event created", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Event> call, Throwable t) {
                                Toast.makeText(sInstance, "Event creation error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });

            return builder.create();
        }
    }
}
