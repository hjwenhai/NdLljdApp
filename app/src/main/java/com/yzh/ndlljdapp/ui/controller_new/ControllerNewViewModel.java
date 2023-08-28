package com.yzh.ndlljdapp.ui.controller_new;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ControllerNewViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ControllerNewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}