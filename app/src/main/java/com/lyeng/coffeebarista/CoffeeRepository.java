package com.lyeng.coffeebarista;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CoffeeRepository {
    private CoffeeDao coffeDao;
    private LiveData<List<Coffee>> allFavCoffee;
    private LiveData<List<Coffee>> allSortedCoffee;
//    public String randomCoffee;

    public CoffeeRepository(Application app) {
        CoffeeDatabase coffeedb = CoffeeDatabase.getInstance(app);
        coffeDao = coffeedb.coffeeDao();
    }

    public LiveData<List<Coffee>> getSortedFavCoffeeinRepo(String sortId) {
//        allFavCoffee = coffeDao.getSortedFavCoffeeinDao(sortId);
        allFavCoffee = coffeDao.getSortedFavCoffeeinDao(
                new SimpleSQLiteQuery("SELECT * FROM coffee_table WHERE favouriteCoffee='true' ORDER BY " + sortId));
        return allFavCoffee;
    }

    public LiveData<List<Coffee>> getSortedCoffeeinRepo(String sortId) {
//        allSortedCoffee = coffeDao.getSortedCoffeeinDao(SortUtils.getAllQuery(sortId, "false"));
        allSortedCoffee = coffeDao.getSortedCoffeeinDao(
                new SimpleSQLiteQuery("SELECT * FROM coffee_table ORDER BY " + sortId));
        return allSortedCoffee;
    }

//    public String getRandomCoffee() {
//        new GetRandomCoffeeAsyncTask(coffeDao).execute();
//        //randomCoffee = coffeDao.getRandomCoffee();
//        return randomCoffee;
//    }

    public void insertTheCoffee(Coffee coffee) {
        new InsertCoffeeAsyncTask(coffeDao).execute(coffee);
    }

    public void updateTheCoffee(Coffee coffee) {
        new UpdateCoffeeAsyncTask(coffeDao).execute(coffee);
    }

    public void updateTheFavCoffee(String name, String fav) {
        String[] coff = {name, fav};
        new UpdateFavAsyncTask(coffeDao).execute(coff);
    }

    public void deleteTheCoffee(Coffee coffee) {
        new DeleteCoffeeAsyncTask(coffeDao).execute(coffee);
    }

    public void deleteAllTheCoffee() {
        new DeleteAllCoffeeAsyncTask(coffeDao).execute();
    }

    private static class InsertCoffeeAsyncTask extends AsyncTask<Coffee, Void, String> {
        private CoffeeDao coffDao;

        private InsertCoffeeAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }

        @Override
        protected String doInBackground(Coffee... pCoffees) {
            coffDao.insertCoffee(pCoffees[0]);
            return "Coffee data inserted";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    private static class UpdateCoffeeAsyncTask extends AsyncTask<Coffee, Void, Void> {
        private CoffeeDao coffDao;

        private UpdateCoffeeAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }

        @Override
        protected Void doInBackground(Coffee... pCoffees) {
            coffDao.updateCoffee(pCoffees[0]);
            return null;
        }
    }

    private static class UpdateFavAsyncTask extends AsyncTask<String, Void, Void> {
        private CoffeeDao coffDao;

        private UpdateFavAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }


        @Override
        protected Void doInBackground(String... pStrings) {
            coffDao.updateFavCoffee(pStrings[0], pStrings[1]);
            return null;
        }
    }

    private static class DeleteCoffeeAsyncTask extends AsyncTask<Coffee, Void, Void> {
        private CoffeeDao coffDao;

        private DeleteCoffeeAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }

        @Override
        protected Void doInBackground(Coffee... pCoffees) {
            coffDao.deleteCoffee(pCoffees[0]);
            return null;
        }
    }

    private static class DeleteAllCoffeeAsyncTask extends AsyncTask<Void, Void, Void> {
        private CoffeeDao coffDao;

        private DeleteAllCoffeeAsyncTask(CoffeeDao coffDao) {
            this.coffDao = coffDao;
        }

        @Override
        protected Void doInBackground(Void... pVoids) {
            coffDao.deleteAllCoffee();
            return null;
        }
    }

//    private class GetRandomCoffeeAsyncTask extends AsyncTask<Void, Void, String> {
//        private CoffeeDao coffDao;
//
//        private GetRandomCoffeeAsyncTask(CoffeeDao coffDao) {
//            this.coffDao = coffDao;
//        }
//
//        @Override
//        protected String doInBackground(Void... pVoids) {
//            /**
//             *Use tey and catch
//             URL url = new URL(stringUrl);
//             HttpURLConnection con = new url.openConnection();
//             con.setRequestMethosd("GET");
//             con.connect();
//
//             BufferedReader br = new BufferedReader(new InpurStreamReader(con.getInputStream()));
//             String value = bf.readLine();
//
//             */
//            String rC = coffDao.getRandomCoffee();
//            System.out.println("Value is "+rC);
//            return rC;
//        }
//
//        @Override
//        protected void onPostExecute(String pS) {
//            super.onPostExecute(pS);
//            randomCoffee = pS;
//        }
//    }
}
