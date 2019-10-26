package com.lyeng.coffeebarista;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.RawQuery;

public class SortUtils {

    public static SimpleSQLiteQuery getAllQuery(String sortBy, String showOnlyFavorites) {
        SupportSQLiteQueryBuilder queryBuilder = SupportSQLiteQueryBuilder
                .builder("coffee_table")
                .orderBy(getSortColumn(sortBy));
        if (showOnlyFavorites.equals("true")) {
            queryBuilder.selection("favouriteCoffee", new String[]{"1"});
        }
        return new SimpleSQLiteQuery(queryBuilder.create().getSql());
    }

    private static String getSortColumn(String value) {
        switch (value) {
            case "CAFFEINE":
                return "coffeeCaffeineLevel";
            case "TYPE":
                return "coffeeType";
            default:
                return "coffeeName";
        }
    }

}
