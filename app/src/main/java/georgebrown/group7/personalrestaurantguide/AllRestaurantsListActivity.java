package georgebrown.group7.personalrestaurantguide;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AllRestaurantsListActivity extends AppCompatActivity {
    private List<Restaurant> restaurantList;
    private RecyclerView recyclerView;

    private DbManager dbManager;

    // values to go into the cursor adapter
    final String[] from = new String[]{DbHelper.NAME,
            DbHelper.ADDRESS,String.valueOf(DbHelper.RATING)};

    // restaurant_list.xml
    final int[] to = new int[]{R.id.restaurant_name,
            R.id.restaurant_address,R.id.rating_bar};

    private ImageView addRestaurantImg;
    private ActivityResultLauncher<Intent> addRestaurantLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_restaurants_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.all_restaurants_toolbar);
        setSupportActionBar(myToolbar);

        restaurantList = getRestaurantList();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RestaurantAdapter(restaurantList));

        addRestaurantImg = findViewById(R.id.add_restaurant_img);

        addRestaurantLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Restaurant newRestaurant = data.getParcelableExtra("newRestaurant");
                            if (newRestaurant != null) {
                                restaurantList.add(newRestaurant);
                                recyclerView.getAdapter().notifyItemInserted(restaurantList.size() - 1);
                            }
                        }
                    }
                });

        addRestaurantImg.setOnClickListener(v -> {
            Intent addRestaurantIntent = new Intent(this, AddEditActivity.class);
            addRestaurantLauncher.launch(addRestaurantIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_allrest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_button) {
            finish();
            return true;
        }
        return false;

    }

    private List<Restaurant> getRestaurantList() {
        List<Restaurant> restaurantList = new ArrayList<>();
        dbManager = new DbManager(this);
        dbManager.open();
        Cursor c = dbManager.fetch();


        while (!c.isAfterLast()) {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(c.getString(c.getColumnIndexOrThrow(DbHelper.NAME)));
            restaurant.setAddress(c.getString(c.getColumnIndexOrThrow(DbHelper.ADDRESS)));
            restaurant.setRating(c.getFloat(c.getColumnIndexOrThrow(DbHelper.RATING)));
            restaurantList.add(restaurant);
            c.moveToNext();
        }
        return restaurantList;
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

        private List<Restaurant> restaurantList;

        public RestaurantAdapter(List<Restaurant> restaurantList) {
            this.restaurantList = restaurantList;
        }

        @Override
        public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list, parent, false);
            return new RestaurantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantViewHolder holder, int position) {
            Restaurant restaurant = restaurantList.get(position);
            holder.bind(restaurant);
        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }

        public class RestaurantViewHolder extends RecyclerView.ViewHolder {
            private TextView restaurantNameTextView;
            private TextView restaurantAddressTextView;
            private RatingBar ratingBar;

            public RestaurantViewHolder(View itemView) {
                super(itemView);
                restaurantNameTextView = itemView.findViewById(R.id.restaurant_name);
                restaurantAddressTextView = itemView.findViewById(R.id.restaurant_address);
                ratingBar = itemView.findViewById(R.id.rating_bar);
                ratingBar.setIsIndicator(true);
            }

            public void bind(Restaurant restaurant) {
                restaurantNameTextView.setText(restaurant.getName());
                restaurantAddressTextView.setText(restaurant.getAddress());
                ratingBar.setRating(restaurant.getRating());
            }
        }
    }

}