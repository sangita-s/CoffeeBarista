package com.lyeng.coffeebarista;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = Coffee.class, version = 1)
public abstract class CoffeeDatabase extends RoomDatabase {

    public abstract CoffeeDao coffeeDao();

    public static CoffeeDatabase cdInstance;

    public static synchronized CoffeeDatabase getInstance(Context pContext){
        if(cdInstance == null){
            cdInstance = Room.databaseBuilder(pContext.getApplicationContext(),
                    CoffeeDatabase.class, "coffee_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return cdInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(cdInstance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void>{
        private CoffeeDao coffeeDao;
        public PopulateAsyncTask(CoffeeDatabase db){
            coffeeDao = db.coffeeDao();
        }

        @Override
        protected Void doInBackground(Void... pVoids) {
            coffeeDao.insertCoffee(new Coffee("Latte","Black","true","It's a Latte", "Italy and Arabia", "Water, coffee, Sugar, frosting", "High", String.valueOf(5)));
            coffeeDao.insertCoffee(new Coffee("Filter","Milk","true","It's a Madras Filter Coffee", "South India", "Coffee, milk, Sugar, goodness", "Low", String.valueOf(3)));
            coffeeDao.insertCoffee(new Coffee("Jasmine","Herbal","true","It's a scented one", "Great Jasmania", "Coffee, milk, Sugar, jasmine", "Medium", String.valueOf(1)));
            coffeeDao.insertCoffee(new Coffee("Cream Cheese Latte","Black","false","It's a Latte", "Italy and Arabia", "Water, coffee, Sugar, frosting", "High", String.valueOf(5)));
            coffeeDao.insertCoffee(new Coffee("Peach","Milk","false","It's a Madras Filter Coffee", "South India", "Coffee, milk, Sugar, goodness", "Low", String.valueOf(3)));
            coffeeDao.insertCoffee(new Coffee("Ginger","Herbal","false","It's a scented one", "Great Jasmania", "Coffee, milk, Sugar, jasmine", "Medium", String.valueOf(1)));
            return null;
        }
    }
}
