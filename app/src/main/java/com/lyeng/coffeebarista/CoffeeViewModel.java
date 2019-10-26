package com.lyeng.coffeebarista;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class CoffeeViewModel extends ViewModel {
    private CoffeeRepository coffeeRepository;
    MutableLiveData<Boolean> mFavoriteFilter = new MutableLiveData<>();
    MutableLiveData<String> mSortFilter = new MutableLiveData<>();
    LiveData<List<Coffee>> allSortedCoffee;

    //Random Coffee
    public String rCoffee;

    //Default from Main Activity
    public String sort = MainActivity.sortId;
    public String fav = MainActivity.filter;

    public LiveData<List<Coffee>> getAllCoffeeinVM() {
        return allSortedCoffee;
    }

//    public String returnRandomCoffeeString() {
//        rCoffee = "randomrandomCoffee";
//        rCoffee = coffeeRepository.getRandomCoffee();
//        return rCoffee;
//    }

    public CoffeeViewModel(final CoffeeRepository pCoffeeRepository, final String sortId) {
        coffeeRepository = pCoffeeRepository;
        mFavoriteFilter.postValue(false);
        sort = sortId;
        Log.e("sortIdInViewModel", sortId);
        allSortedCoffee = Transformations.switchMap(mFavoriteFilter,
                new Function<Boolean, LiveData<List<Coffee>>>() {
                    @Override
                    public LiveData<List<Coffee>> apply(Boolean filterState) {
                        if (filterState)
                            return coffeeRepository.getSortedFavCoffeeinRepo(sort);
                        else
                            return coffeeRepository.getSortedCoffeeinRepo(sort);
                    }
                });
//        allSortedCoffee = Transformations.switchMap(mSortFilter,
//                new Function<String, LiveData<List<Coffee>>>() {
//                    @Override
//                    public LiveData<List<Coffee>> apply(String input) {
//                        return coffeeRepository.getSortedCoffeeinRepo(input);
//                    }
//                });
    }

    public void sortChanged(String newSort) {
        String sortString = mSortFilter.getValue();
        if (sortString == null) return;
        mSortFilter.postValue(newSort);
        Log.e("CoffeeViewModel", "Mutable Sort pref is " + mSortFilter.getValue());
    }

    public void showFavorites() {
        Boolean showFavorite = mFavoriteFilter.getValue();
        if (showFavorite == null) {
            return;
        }
        mFavoriteFilter.postValue(!showFavorite);
        Log.e("CoffeeViewModel", "Mutable Fav Filter is " + mFavoriteFilter.getValue().toString());
    }


    public void insertCoffee(Coffee coffee) {
        coffeeRepository.insertTheCoffee(coffee);
    }

    public void updateCoffee(Coffee coffee) {
        coffeeRepository.updateTheCoffee(coffee);
    }

    public void updateFavCoffee(String name, String fav) {
        coffeeRepository.updateTheFavCoffee(name, fav);
    }

    public void deleteCoffee(Coffee coffee) {
        coffeeRepository.deleteTheCoffee(coffee);
    }

    public void deleteAllCoffee() {
        coffeeRepository.deleteAllTheCoffee();
    }

}
