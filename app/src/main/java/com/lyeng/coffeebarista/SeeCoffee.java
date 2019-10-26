package com.lyeng.coffeebarista;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.lyeng.coffeebarista.AddCoffeeActivity.EXTRA_NAME;

public class SeeCoffee extends AppCompatActivity {

    private TextView coffeetype, coffeename, coffeedesc, coffeeorigin, coffeeing, coffeecaff, coffeetime;
    private ImageView coffeeImage;
    private Menu menu;
    Button steepTIme;

    Chip chip;
    Button btnaddtag, btnshowselected;
    ChipGroup chipGroup;
    TextInputEditText tagip;

    public static String favIcon;
    public static String name;
    private CoffeeCaffeineView mViewCaffeineLevel;
    private String caff;
    //    public int isChanged = 0;

    public static String getCoffeeName() {
        return name;
    }

    public static String getfav() {
        return favIcon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_coffee);

        chip = findViewById(R.id.chip_example);
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "Checked change to ", Toast.LENGTH_LONG).show();
            }
        });
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Close clicked", Toast.LENGTH_LONG).show();
            }
        });
        btnaddtag = findViewById(R.id.btn_add_tag);
        btnshowselected = findViewById(R.id.btn_show_tag);
        chipGroup = findViewById(R.id.chip_group);
        tagip = findViewById(R.id.input);
        btnaddtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tags = tagip.getText().toString().split(" ");
                LayoutInflater inflater = LayoutInflater.from(SeeCoffee.this);
                for (String tag : tags) {
                    Chip c = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                    c.setText(tag);
                    c.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(v);
                        }
                    });
                    chipGroup.addView(c);
                }
            }
        });
        btnshowselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder result = new StringBuilder("");
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip c = (Chip) chipGroup.getChildAt(i);
                    if (c.isChecked()) {
                        if (i < chipGroup.getChildCount() - 1) {
                            result.append(c.getText()).append(",");
                        }
                        else{
                            result.append(c.getText());
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), ""+result.toString(), Toast.LENGTH_LONG).show();
            }
        });


        //ini views
        CollapsingToolbarLayout c = findViewById(R.id.collapsing_toolbar);
        c.setTitleEnabled(true);

        coffeedesc = findViewById(R.id.tv_coff_desc);
        coffeeorigin = findViewById(R.id.tv_coff_origin);
        coffeetime = findViewById(R.id.tv_steep_time);
        coffeeing = findViewById(R.id.tv_coff_ing);
        coffeecaff = findViewById(R.id.caffeine_level);
        coffeeImage = findViewById(R.id.tea_image);
        coffeetype = findViewById(R.id.tv_coff_type);
        steepTIme = findViewById(R.id.start_timer);

        Intent data = getIntent();
        name = data.getStringExtra(EXTRA_NAME);
        coffeetype.setText(data.getStringExtra(AddCoffeeActivity.EXTRA_TYPE));
        coffeeorigin.setText(data.getStringExtra(AddCoffeeActivity.EXTRA_ORIGIN));
        coffeeing.setText(data.getStringExtra(AddCoffeeActivity.EXTRA_INGREDIENTS));
        coffeedesc.setText(data.getStringExtra(AddCoffeeActivity.EXTRA_DESC));
        caff = data.getStringExtra(AddCoffeeActivity.EXTRA_CAFFEINE);
        coffeecaff.setText(caff);
        if (data.getStringExtra(AddCoffeeActivity.EXTRA_TYPE).equals("Black")) {
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.black_tea);
            coffeeImage.setImageBitmap(icon);
        } else if (data.getStringExtra(AddCoffeeActivity.EXTRA_TYPE).equals("Milk")) {
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.herbal_tea);
            coffeeImage.setImageBitmap(icon);
        } else {
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.green_tea);
            coffeeImage.setImageBitmap(icon);
        }
        final String steepString = data.getStringExtra(AddCoffeeActivity.EXTRA_TIME);
        String steep = data.getStringExtra(AddCoffeeActivity.EXTRA_TIME);
        if (steep.equals("1")) steep += " minute";
        else steep += " minutes";
        coffeetime.setText(steep);
        c.setTitle(data.getStringExtra(EXTRA_NAME));
        steepTIme.setText(steep);
        steepTIme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timerIntent = new Intent(SeeCoffee.this, CoffeeTimerActivity.class);
                timerIntent.putExtra("EXTRA_NAME", name);
                timerIntent.putExtra("TIMER_TIME", steepString);
                startActivityForResult(timerIntent, 3);
            }
        });

        favIcon = data.getStringExtra("EXTRA_FAV");
        Toast.makeText(SeeCoffee.this, "fav icon set to " + favIcon,
                Toast.LENGTH_LONG).show();

        mViewCaffeineLevel = findViewById(R.id.coffeeCaffeineView);
        loadCaffeineView();
    }

    private void loadCaffeineView() {
        int totalLevel = 3;
        int currentCaffLevel = 0;
        if (caff.equals("Low")) {
            currentCaffLevel = 1;
        } else if (caff.toString().equals("Medium")) {
            currentCaffLevel = 2;
        } else if (caff.toString().equals("High")) {
            currentCaffLevel = 3;
        }

        boolean[] caffLevel = new boolean[totalLevel];
        for (int i = 0; i < currentCaffLevel; i++)
            caffLevel[i] = true;
        mViewCaffeineLevel.setCaffeineLevelCup(caffLevel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Toast.makeText(SeeCoffee.this, R.string.coffee_ready_msg, Toast.LENGTH_LONG).show();
        } else if (requestCode == 3 && resultCode == RESULT_CANCELED) {
            Toast.makeText(SeeCoffee.this, R.string.ctr_cncl_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fav = menu.findItem(R.id.saveFav);
        if (favIcon.equals("true")) {
            fav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink));
        } else {
            fav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveFav: {
                if (favIcon.equals("true")) favIcon = "false";
                else favIcon = "true";
                Toast.makeText(SeeCoffee.this, name + " set to " + favIcon,
                        Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("EXTRA_FAV", favIcon);
        data.putExtra(EXTRA_NAME, name);
        setResult(RESULT_OK, data);
        finish();
    }
}
