package com.yzh.ndlljdapp.ui.local_search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocalSearchModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocalSearchModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}