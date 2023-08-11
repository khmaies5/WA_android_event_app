package com.khmaies.waandroideventapp.presentation.meetings;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khmaies.waandroideventapp.data.model.Meeting;
import com.khmaies.waandroideventapp.data.utils.DateUtils;
import com.khmaies.waandroideventapp.databinding.ItemMeetingBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private List<Meeting> meetings = new ArrayList<>();

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMeetingBinding binding = ItemMeetingBinding.inflate(inflater, parent, false);
        return new MeetingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.bind(meeting);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        private final ItemMeetingBinding binding;

        MeetingViewHolder(@NonNull ItemMeetingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Meeting meeting) {
            binding.txtMeetingId.setText(String.valueOf(meeting.getId()));
            binding.txtMeetingTitle.setText(meeting.getTitle());
            binding.txtMeetingDate.setText(DateUtils.formatDate(meeting.getDate()));

            // Join the list of guests using TextUtils
            String guests = TextUtils.join(", ", meeting.getGuests());
            binding.txtMeetingGuests.setText(guests);
        }
    }
}

