package com.lyeng.coffeebarista;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface CoffeeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCoffee(Coffee c);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateCoffee(Coffee c);

    @Delete
    void deleteCoffee(Coffee c);

    @Query("DELETE FROM coffee_table")
    void deleteAllCoffee();

//    @Query("SELECT * FROM coffee_table ORDER BY :sortId")
//    LiveData<List<Coffee>> getSortedCoffeeinDao(String sortId);

    @RawQuery(observedEntities = Coffee.class)
    LiveData<List<Coffee>> getSortedCoffeeinDao(SimpleSQLiteQuery sortQuery);

//    @Query("SELECT * FROM coffee_table WHERE favouriteCoffee='true' ORDER BY :sortId")
//    LiveData<List<Coffee>> getSortedFavCoffeeinDao(String sortId);

    @RawQuery(observedEntities = Coffee.class)
    LiveData<List<Coffee>> getSortedFavCoffeeinDao(SimpleSQLiteQuery sortQuery);

    @Query("UPDATE coffee_table SET favouriteCoffee=:fav WHERE coffeeName=:coffeName")
    void updateFavCoffee(String coffeName, String fav);

    @Query("SELECT coffeeName FROM coffee_table ORDER BY RANDOM() limit 1")
    String getRandomCoffee();

    @Query("SELECT * FROM coffee_table ORDER BY RANDOM() limit 1")
    Coffee getRandomCoffeeObject();
}
