package com.example.ejob.ui.employer.applications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ApplicationViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private Object[] mParams;

    public ApplicationViewModelFactory(Application application, Object... params) {
        mApplication = application;
        mParams = params;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return null;
    }

//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        if (modelClass == ApplicationViewModel.class) {
//            return (T) new ApplicationViewModel(mApplication, (String) mParams[0]);
//        } else
//            return create(modelClass);
//    }

}
