package com.lyeng.coffeebarista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddCoffeeActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_DESC = "EXTRA_DESC";
    public static final String EXTRA_ORIGIN = "EXTRA_ORIGIN";
    public static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    public static final String EXTRA_CAFFEINE = "EXTRA_CAFFEINE";
    public static final String EXTRA_TIME = "EXTRA_TIME";

    private EditText coffeeName, coffeeDesc, coffeeOrigin, coffeeIng, coffeeCaffLevel, coffeeSteepTime;
    private Spinner coffeeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coffee);
        coffeeName = findViewById(R.id.ti_coffeeName);
        coffeeType = findViewById(R.id.spinner_coffeeType);
        coffeeDesc = findViewById(R.id.ti_coffeeDesc);
        coffeeOrigin = findViewById(R.id.ti_coffeeOrigin);
        coffeeIng = findViewById(R.id.ti_coffeeIng);
        coffeeCaffLevel = findViewById(R.id.ti_coffeeCaffLevel);
        coffeeSteepTime = findViewById(R.id.ti_coffeeTime);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Coffee");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.add_coffee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCoffee: {
                saveCoffee();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCoffee() {
        String sT;
        String cN = coffeeName.getText().toString();
        String cD = coffeeDesc.getText().toString();
        String cO = coffeeOrigin.getText().toString();
        String cI = coffeeIng.getText().toString();
        String cC = coffeeCaffLevel.getText().toString();
        if (coffeeSteepTime.getText().toString().equals(""))
            sT = "0";
        else
            sT = coffeeSteepTime.getText().toString();
        String[] coffeeTypes = getResources().getStringArray(R.array.coffee_types);
        String cT = coffeeTypes[(int) coffeeType.getSelectedItemId()];
        if (cN.equals("") || cT.equals("")) {
            Toast.makeText(AddCoffeeActivity.this, "Enter type and name", Toast.LENGTH_LONG).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TYPE, cT);
            data.putExtra(EXTRA_NAME, cN);
            data.putExtra(EXTRA_DESC, cD);
            data.putExtra(EXTRA_ORIGIN, cO);
            data.putExtra(EXTRA_INGREDIENTS, cI);
            data.putExtra(EXTRA_CAFFEINE, cC);
            data.putExtra(EXTRA_TIME, sT);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
