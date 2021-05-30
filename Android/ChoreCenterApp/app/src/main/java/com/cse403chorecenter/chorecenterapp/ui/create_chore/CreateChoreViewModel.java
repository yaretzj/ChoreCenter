package com.cse403chorecenter.chorecenterapp.ui.create_chore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * A view model used by the create chore fragment class.
 */
public class CreateChoreViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateChoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is create chore fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
