package com.lyeng.coffeebarista;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "coffee_table")
public class Coffee {
    @PrimaryKey
    @NonNull
    private String coffeeName;

    private String coffeeType;

    public String getFavouriteCoffee() {
        return favouriteCoffee;
    }

    public void setFavouriteCoffee(String pFavouriteCoffee) {
        favouriteCoffee = pFavouriteCoffee;
    }

    private String favouriteCoffee;

    @ColumnInfo(name = "coffeeDesc")
    private String coffeeDesc;

    private String coffeeOrigin;
    private String coffeeIngredients;
    private String coffeeCaffeineLevel;

    private String steepTime;

    public Coffee() {
    }

    public Coffee(String pCoffeeName, String pCoffeeType) {
        coffeeName = pCoffeeName;
        coffeeType = pCoffeeType;
    }

    public Coffee(String pCoffeeName, String pCoffeeDesc, String pCoffeeOrigin, String pCoffeeIngredients, String pCoffeeCaffeineLevel, String pSteepTime) {
        coffeeName = pCoffeeName;
        coffeeDesc = pCoffeeDesc;
        coffeeOrigin = pCoffeeOrigin;
        coffeeIngredients = pCoffeeIngredients;
        coffeeCaffeineLevel = pCoffeeCaffeineLevel;
        steepTime = pSteepTime;
    }

    public Coffee(String pCoffeeName, String pCoffeeType, String pFavouriteCoffee, String pCoffeeDesc, String pCoffeeOrigin, String pCoffeeIngredients, String pCoffeeCaffeineLevel, String pSteepTime) {
        coffeeName = pCoffeeName;
        coffeeType = pCoffeeType;
        favouriteCoffee = pFavouriteCoffee;
        coffeeDesc = pCoffeeDesc;
        coffeeOrigin = pCoffeeOrigin;
        coffeeIngredients = pCoffeeIngredients;
        coffeeCaffeineLevel = pCoffeeCaffeineLevel;
        steepTime = pSteepTime;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String pCoffeeName) {
        coffeeName = pCoffeeName;
    }

    public String getCoffeeDesc() {
        return coffeeDesc;
    }

    public void setCoffeeDesc(String pCoffeeDesc) {
        coffeeDesc = pCoffeeDesc;
    }

    public String getCoffeeOrigin() {
        return coffeeOrigin;
    }

    public void setCoffeeOrigin(String pCoffeeOrigin) {
        coffeeOrigin = pCoffeeOrigin;
    }

    public String getCoffeeIngredients() {
        return coffeeIngredients;
    }

    public void setCoffeeIngredients(String pCoffeeIngredients) {
        coffeeIngredients = pCoffeeIngredients;
    }

    public String getCoffeeCaffeineLevel() {
        return coffeeCaffeineLevel;
    }

    public void setCoffeeCaffeineLevel(String pCoffeeCaffeineLevel) {
        coffeeCaffeineLevel = pCoffeeCaffeineLevel;
    }

    public String getSteepTime() {
        return steepTime;
    }

    public void setSteepTime(String pSteepTime) {
        steepTime = pSteepTime;
    }


    public String getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(String pCoffeeType) {
        coffeeType = pCoffeeType;
    }

}
