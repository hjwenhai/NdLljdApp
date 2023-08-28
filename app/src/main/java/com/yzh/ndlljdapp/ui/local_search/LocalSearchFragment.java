package com.yzh.ndlljdapp.ui.local_search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yzh.ndlljdapp.databinding.FragmentLocalSearchBinding;

public class LocalSearchFragment extends Fragment {

    private FragmentLocalSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocalSearchModel localSearchModel =
                new ViewModelProvider(this).get(LocalSearchModel.class);

        binding = FragmentLocalSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        localSearchModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}