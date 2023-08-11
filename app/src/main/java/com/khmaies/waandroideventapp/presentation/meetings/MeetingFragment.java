package com.khmaies.waandroideventapp.presentation.meetings;

import static com.khmaies.waandroideventapp.data.utils.DateUtils.showDateTimePicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.khmaies.waandroideventapp.R;
import com.khmaies.waandroideventapp.data.model.Meeting;
import com.khmaies.waandroideventapp.databinding.FragmentMeetingBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint

public class MeetingFragment extends Fragment {

    static MeetingViewModel meetingViewModel;
    private FragmentMeetingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMeetingBinding.inflate(inflater, container, false);
        binding.fabAddMeeting.setOnClickListener(v -> showCreateMeetingDialog());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        MeetingAdapter meetingAdapter = new MeetingAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(meetingAdapter);

        meetingViewModel.meetings.observe(getViewLifecycleOwner(), tasks -> meetingAdapter.setMeetings(tasks));
        // Observe the tasks LiveData from the ViewModel
        meetingViewModel.getMeetings();
    }

    private void showCreateMeetingDialog() {
        CreateMeetingDialogFragment dialog = new CreateMeetingDialogFragment();
        dialog.show(getParentFragmentManager(), "CreateMeetingDialog");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class CreateMeetingDialogFragment extends DialogFragment {
        EditText meetingDateTime;
        Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_create_meeting, null);
            EditText etMeetingTitle = view.findViewById(R.id.etMeetingTitle);
            EditText etMeetingGuests = view.findViewById(R.id.etMeetingGuests);
            meetingDateTime = view.findViewById(R.id.meetingDateTime);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = Calendar.getInstance();

            meetingDateTime.setOnClickListener(v -> date = showDateTimePicker(getContext(), date, dateTime ->
                    meetingDateTime.setText(date.getTime().toString())));

            builder.setView(view)
                    .setTitle("Create New Meeting")
                    .setPositiveButton("Create", (dialog, id) -> {


                        formattedDate = sdf.format(date.getTime());

                        int meetingId = Objects.requireNonNull(meetingViewModel.meetings.getValue()).get(meetingViewModel.meetings.getValue().size() - 1).getId() + 1;

                        String[] namesArray = etMeetingGuests.getText().toString().split(",");

                        List<String> namesList = Arrays.asList(namesArray);
                        Meeting meeting = new Meeting(meetingId, etMeetingTitle.getText().toString(), formattedDate, namesList);

                        meetingViewModel.createMeeting(meeting, new Callback<Meeting>() {
                            @Override
                            public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                                Snackbar.make(view, "Task created!", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Meeting> call, Throwable t) {
                                Snackbar.make(view, "Task creation error!", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    });

            return builder.create();
        }

    }


}