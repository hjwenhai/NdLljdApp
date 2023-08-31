package com.yzh.ndlljdapp.ui.controller_new;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yzh.ndlljdapp.databinding.FragmentControllerNewBinding;

public class ControllerNewFragment extends Fragment {

    private FragmentControllerNewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ControllerNewViewModel controllerNewViewModel =
                new ViewModelProvider(this).get(ControllerNewViewModel.class);

        binding = FragmentControllerNewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        controllerNewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button btnGetNestData = binding.btnGetNestData;
        btnGetNestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerNewViewModel.getNewestFlowData();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}