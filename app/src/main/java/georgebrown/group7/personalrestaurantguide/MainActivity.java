package georgebrown.group7.personalrestaurantguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView allRestaurantstxt;
    private TextView favoriteRestauranttxt;
    private TextView moreRestaurantstxt;

    private ImageView addRestaurantimg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        allRestaurantstxt = findViewById(R.id.all_restaurants_txt);
        favoriteRestauranttxt = findViewById(R.id.favorite_restaurants_txt);
        addRestaurantimg = findViewById(R.id.add_restaurant_img);

        allRestaurantstxt.setOnClickListener(v -> {
            Intent all_restaurants_intent = new Intent(this,AllRestaurantsListActivity.class);
            startActivity(all_restaurants_intent);
        });


        favoriteRestauranttxt.setOnClickListener(v -> {
            Intent favorite_restaurants_intent = new Intent(this, FavoritesActivity.class);
            startActivity(favorite_restaurants_intent);
        });

        addRestaurantimg.setOnClickListener(v -> {
            Intent add_restaurant_intent = new Intent(this, AddEditActivity.class);
            startActivity(add_restaurant_intent);
        });


    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_button) {
            Intent about_intent = new Intent(this,AboutActivity.class);
            startActivity(about_intent);
            return true;
        }
        return false;
    }


}