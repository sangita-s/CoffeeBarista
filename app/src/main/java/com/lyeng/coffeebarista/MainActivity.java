package com.lyeng.coffeebarista;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

//CoffeeListActivity
public class MainActivity extends AppCompatActivity {

    private CoffeeViewModel mCoffeeViewModel;
    public static final int ADD_REQ_CODE = 1;
    public static final int SEE_REQ_CODE = 2;
    private Boolean showFav = false;

    public static final int JOB_ID = 39;
    private JobScheduler mScheduler;

    public static final String TAG = "MainActivity";

    public static final String KEY_PREF_NOTIFICATION = "notification";
    public static final String KEY_PREF_SORT = "sort";

    public static String sortId = "coffeeName";
    public static String filter = "false";

    SharedPreferences sharedPref;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    CoffeeAdapter adapterCoffee;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        readFromPreference();
        setupViewModel(adapterCoffee);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init RecyclerView and Adapter
        RecyclerView coffeerv = findViewById(R.id.rv_coffeeList);
        coffeerv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        coffeerv.setHasFixedSize(true);
        adapterCoffee = new CoffeeAdapter();
        coffeerv.setAdapter(adapterCoffee);

        //Read from preference for the first time
        readFromPreference();

        //Shared pref changed listener - start
        prefChangeListener();
        //SHared pref changed listener - end

        //Set up View Model - start
        setupViewModel(adapterCoffee);
        //Set up View Model - end

        //For slide deletion - start
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView pRecyclerView, @NonNull RecyclerView.ViewHolder
                    pViewHolder, @NonNull RecyclerView.ViewHolder pViewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder pViewHolder, int pI) {
                mCoffeeViewModel.deleteCoffee(adapterCoffee.getCoffeeAt(pViewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, R.string.coffee_deleted_msg, Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(coffeerv);
        //For slide deletion - end

        //Open See Coffee Details
        adapterCoffee.setOnItemCoffeeClickListener(new CoffeeAdapter.OnItemCoffeeClickListener() {
            @Override
            public void onCoffeeItemClick(Coffee coffee) {
                Intent data = new Intent(MainActivity.this, SeeCoffee.class);
                data.putExtra(AddCoffeeActivity.EXTRA_TYPE, coffee.getCoffeeType());
                data.putExtra(AddCoffeeActivity.EXTRA_NAME, coffee.getCoffeeName());
                data.putExtra(AddCoffeeActivity.EXTRA_DESC, coffee.getCoffeeDesc());
                data.putExtra(AddCoffeeActivity.EXTRA_ORIGIN, coffee.getCoffeeOrigin());
                data.putExtra(AddCoffeeActivity.EXTRA_INGREDIENTS, coffee.getCoffeeIngredients());
                data.putExtra(AddCoffeeActivity.EXTRA_CAFFEINE, coffee.getCoffeeCaffeineLevel());
                data.putExtra(AddCoffeeActivity.EXTRA_TIME, coffee.getSteepTime());
                data.putExtra("EXTRA_FAV", coffee.getFavouriteCoffee());
                startActivityForResult(data, SEE_REQ_CODE);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddCoffeeActivity.class);
                startActivityForResult(i, ADD_REQ_CODE);
            }
        });
    }

    private void prefChangeListener() {
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(KEY_PREF_SORT)) {
                    sortId = sharedPreferences.getString(KEY_PREF_SORT, sortId);
                    mCoffeeViewModel.sortChanged(sortId);
                    Toast.makeText(getApplicationContext(), "Changed key : " + key + " to : " +
                            sharedPreferences.getString(KEY_PREF_SORT, "Def"), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Changed key : " + key + " to : " +
                            sharedPreferences.getString(KEY_PREF_SORT, "Def"));
                } else if (key.equals(KEY_PREF_NOTIFICATION)) {
                    Toast.makeText(getApplicationContext(), "Changed key : " + key + " to : " +
                            String.valueOf(sharedPreferences.getBoolean(KEY_PREF_NOTIFICATION, false)), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Changed key : " + key + " to : " +
                            String.valueOf(sharedPreferences.getBoolean(KEY_PREF_NOTIFICATION, false)));
                    if(sharedPreferences.getBoolean(KEY_PREF_NOTIFICATION, false)){
                        fireNotifNow();
                    }else{
                        if (mScheduler != null) {
                            mScheduler.cancelAll();
                            mScheduler = null;
                            Toast.makeText(getApplicationContext(), "Jobs cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);
    }

    private void fireNotifNow() {
        Log.e(TAG, "Gonna schedule notif");
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(this,
                //getApplication(),
                NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setPeriodic(15000);
        JobInfo myJobInfo = builder.build();
        mScheduler.schedule(myJobInfo);
        Toast.makeText(getApplicationContext(), R.string.job_sch_msg +
                "the constraints are met.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, getString(R.string.job_sch_msg) +
                "the constraints are met.");
    }

    private void setupViewModel(final CoffeeAdapter pAdapterCoffee) {
        mCoffeeViewModel = ViewModelProviders
                .of(this, new CoffeeViewModelFactory(this.getApplication(), sortId))
                .get(CoffeeViewModel.class);
        mCoffeeViewModel.getAllCoffeeinVM().observe(this, new Observer<List<Coffee>>() {
            @Override
            public void onChanged(@Nullable List<Coffee> pCoffees) {
                pAdapterCoffee.setCoffeelist(pCoffees);
            }
        });
    }

    private void readFromPreference() {
        PreferenceManager
                .setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);

        //Set sort from Preference
        String sortPref = sharedPref.getString(KEY_PREF_SORT, "DEFAULT");
        if (sortPref.equals("CAFFEINE")) sortId = "coffeeCaffeineLevel";
        else if (sortPref.equals("DEFAULT")) sortId = "coffeeName";
        else if (sortPref.equals("TYPE")) sortId = "coffeeType";
        Log.e(TAG, R.string.pref_id_in_sort_is_msg + sortId);
        //TODO Add fav to pref file
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_REQ_CODE && resultCode == RESULT_OK) {
            String cT = data.getStringExtra(AddCoffeeActivity.EXTRA_TYPE);
            String cN = data.getStringExtra(AddCoffeeActivity.EXTRA_NAME);
            String cD = data.getStringExtra(AddCoffeeActivity.EXTRA_DESC);
            String cO = data.getStringExtra(AddCoffeeActivity.EXTRA_ORIGIN);
            String cI = data.getStringExtra(AddCoffeeActivity.EXTRA_INGREDIENTS);
            String cC = data.getStringExtra(AddCoffeeActivity.EXTRA_CAFFEINE);
            String sT = data.getStringExtra(AddCoffeeActivity.EXTRA_TIME);

            Coffee coffee = new Coffee(cN, cT, "false", cD, cO, cI, cC, sT);
            mCoffeeViewModel.insertCoffee(coffee);
            Toast.makeText(this, R.string.coffee_added_msg, Toast.LENGTH_LONG).show();
        } else if (requestCode == SEE_REQ_CODE && resultCode == RESULT_OK) {
            String name = SeeCoffee.getCoffeeName();
            String fav = SeeCoffee.getfav();
            if (data != null) {
                name = data.getStringExtra("EXTRA_NAME");
                fav = data.getStringExtra("EXTRA_FAV");
            }
//            Toast.makeText(this, "Setting DB to "+name+" "+fav, Toast.LENGTH_LONG).show();
//            if(name != null) mCoffeeViewModel.updateFavCoffee(name, fav);
        } else {
            Toast.makeText(this, R.string.coffee_details_not_added, Toast.LENGTH_LONG).show();
            String name = SeeCoffee.getCoffeeName();
            String fav = SeeCoffee.getfav();
            Toast.makeText(this, "Setting DB to " + name + " " + fav, Toast.LENGTH_LONG).show();
            if (name != null) mCoffeeViewModel.updateFavCoffee(name, fav);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete_all) {
            mCoffeeViewModel.deleteAllCoffee();
            Toast.makeText(this, R.string.all_notes_deleted, Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_filter) {
            showFav = !showFav;
            Toast.makeText(this, R.string.show_fav_changed + String.valueOf(showFav), Toast.LENGTH_LONG).show();
            mCoffeeViewModel.showFavorites();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
