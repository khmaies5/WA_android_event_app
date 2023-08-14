package com.khmaies.waandroideventapp.presentation.users;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.khmaies.waandroideventapp.databinding.FragmentUserBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserFragment extends Fragment {

    private FragmentUserBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserAdapter userAdapter = new UserAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(userAdapter);


        // Observe the users LiveData from the ViewModel
        userViewModel.users.observe(getViewLifecycleOwner(), userAdapter::setUsers);
        userViewModel.filteredUsers.observe(getViewLifecycleOwner(), userAdapter::setUsers);
        userViewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), "Something went wrong!", LENGTH_LONG).show();
            }
        });
        userViewModel.getUsers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}