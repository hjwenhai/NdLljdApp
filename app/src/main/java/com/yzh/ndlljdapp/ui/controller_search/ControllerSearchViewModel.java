package com.yzh.ndlljdapp.ui.controller_search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ControllerSearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ControllerSearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}