package com.yzh.ndlljdapp.ui.controller_search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yzh.ndlljdapp.databinding.FragmentControllerSearchBinding;

public class ControllerSearchFragment extends Fragment {

    private FragmentControllerSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ControllerSearchViewModel controllerSearchViewModel =
                new ViewModelProvider(this).get(ControllerSearchViewModel.class);

        binding = FragmentControllerSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        controllerSearchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}