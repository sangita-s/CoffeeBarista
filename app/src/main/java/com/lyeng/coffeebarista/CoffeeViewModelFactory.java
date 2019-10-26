package com.lyeng.coffeebarista;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

public class CoffeeViewModelFactory implements ViewModelProvider.Factory {
    private final CoffeeRepository mRepository;
    private final String mSortBy;

    private Application mApplication;

    public CoffeeViewModelFactory(Application pApplication, String sortBy) {
        mApplication = pApplication;
        mRepository = new CoffeeRepository(pApplication);
        mSortBy = sortBy;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(CoffeeRepository.class, String.class)
                    .newInstance(mRepository, mSortBy);
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
//        return (T) new CoffeeViewModel(mApplication);
    }
}
