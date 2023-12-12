package georgebrown.group7.personalrestaurantguide;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllRestaurantsListActivity extends AppCompatActivity {
    private List<Restaurant> restaurantList;
    private RecyclerView recyclerView;

    private DbManager dbManager;

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



    private List<Restaurant> getRestaurantList() {
        List<Restaurant> restaurantList = new ArrayList<>();
        dbManager = new DbManager(this);
        dbManager.open();
        Cursor c = dbManager.fetch();


        while (!c.isAfterLast()) {
            Restaurant restaurant = new Restaurant();

            restaurant.setId(c.getInt(c.getColumnIndexOrThrow(DbHelper._ID)));
            restaurant.setName(c.getString(c.getColumnIndexOrThrow(DbHelper.NAME)));
            restaurant.setAddress(c.getString(c.getColumnIndexOrThrow(DbHelper.ADDRESS)));
            restaurant.setRating(c.getFloat(c.getColumnIndexOrThrow(DbHelper.RATING)));
            restaurant.setPhone(c.getString(c.getColumnIndexOrThrow(DbHelper.PHONE)));
            restaurant.setDescription(c.getString(c.getColumnIndexOrThrow(DbHelper.DESC)));
            restaurant.setTags(c.getString(c.getColumnIndexOrThrow(DbHelper.TAGS)));
            restaurant.setFavorite(c.getInt(c.getColumnIndexOrThrow(DbHelper.ISFAVORITE)) == 1);
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
            holder.itemView.setOnClickListener(v -> {
                // Open DetailsRestaurantActivity for displaying restaurant details
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("restaurant", restaurant);
                v.getContext().startActivity(detailsIntent);
            });

            holder.editIcon.setOnClickListener(v -> {
                // Open AddEditActivity for editing the restaurant
                Intent editIntent = new Intent(v.getContext(), AddEditActivity.class);
                // Pass the restaurant data to the AddEditActivity for editing
                editIntent.putExtra("restaurantId", restaurant.getId());
                v.getContext().startActivity(editIntent);
                // After the edit is complete, refresh the list
            });

            holder.deleteIcon.setOnClickListener(v -> {
                // Show a delete confirmation dialog
                showDeleteDialog(v.getContext(), position);
            });
            // Set the heart icon based on isFavorite
            holder.heartIcon.setImageResource(restaurant.isFavorite() ? R.drawable.is_favourite_selected : R.drawable.favorite_icon);

            holder.heartIcon.setOnClickListener(v -> {
                boolean isFav = restaurant.isFavorite();
                restaurant.setFavorite(!isFav);
                dbManager.updateFavoriteStatus(restaurant.getId(), !isFav);
                notifyItemChanged(position);
            });



        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }

        private void showDeleteDialog(Context context, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete this restaurant?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Delete the restaurant from the list and notify adapter
                        dbManager.delete(restaurantList.get(position).getId());
                        Log.d("DATABASE", "Restaurant deleted"+restaurantList.get(position).getId());
                        restaurantList.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }



        public class RestaurantViewHolder extends RecyclerView.ViewHolder {
            private TextView restaurantNameTextView;
            private TextView restaurantAddressTextView;
            private RatingBar ratingBar;
            private ImageView editIcon;
            private ImageView deleteIcon;
            private ImageView heartIcon;




            public RestaurantViewHolder(View itemView) {
                super(itemView);
                restaurantNameTextView = itemView.findViewById(R.id.restaurant_name);
                restaurantAddressTextView = itemView.findViewById(R.id.restaurant_address);
                ratingBar = itemView.findViewById(R.id.rating_bar);
                ratingBar.setIsIndicator(true);
                heartIcon = itemView.findViewById(R.id.heart_icon);
                editIcon = itemView.findViewById(R.id.edit_icon);
                deleteIcon = itemView.findViewById(R.id.delete_icon);
                deleteIcon.setOnClickListener(v -> showDeleteDialog(itemView.getContext(), getAdapterPosition()));



            }

            public void bind(Restaurant restaurant) {
                restaurantNameTextView.setText(restaurant.getName());
                restaurantAddressTextView.setText(restaurant.getAddress());
                ratingBar.setRating(restaurant.getRating());
            }

        }
    }

    private String getRestaurantsListString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Restaurant restaurant : restaurantList) {
            stringBuilder.append("Name: ").append(restaurant.getName()).append("\n");
            stringBuilder.append("Address: ").append(restaurant.getAddress()).append("\n");
            stringBuilder.append("Rating: ").append(restaurant.getRating()).append("\n\n");

        }
        return stringBuilder.toString();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_button) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareRestaurantsList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareRestaurantsList() {
        String shareBody = getRestaurantsListString();
        if (shareBody.isEmpty()) {
            Toast.makeText(this, "No restaurants to share.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "List of Restaurants");
        emailIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AllRestaurantsListActivity.this,
                    "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }



}