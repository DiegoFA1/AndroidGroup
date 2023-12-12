package georgebrown.group7.personalrestaurantguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.favourites_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.favorites_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbManager = new DbManager(this);
        dbManager.open();
        loadFavorites();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
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
    private void loadFavorites() {
        if (adapter != null) {
            // Close the existing cursor if necessary
            adapter.setCursor(null);
        }

        Cursor cursor = dbManager.fetchFavorites();
        if (cursor != null && cursor.getCount() > 0) {
            if (adapter == null) {
                adapter = new FavoritesAdapter(this, cursor);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setCursor(cursor);
            }
        } else {

        }
    }
    public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public FavoritesAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        public void setCursor(Cursor newCursor) {
            if (cursor != null) {
                cursor.close();
            }
            cursor = newCursor;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (cursor.moveToPosition(position)) {
                // Fetch and set the name
                int nameColumnIndex = cursor.getColumnIndex(DbHelper.NAME);
                if (nameColumnIndex != -1) {
                    String name = cursor.getString(nameColumnIndex);
                    holder.restaurantNameTextView.setText(name);
                } else {
                    holder.restaurantNameTextView.setText("Name not found");
                }

                // Fetch and set the address
                int addressColumnIndex = cursor.getColumnIndex(DbHelper.ADDRESS);
                if (addressColumnIndex != -1) {
                    String address = cursor.getString(addressColumnIndex);
                    holder.restaurantAddressTextView.setText(address);
                } else {
                    holder.restaurantAddressTextView.setText("Address not found");
                }

                // Fetch and set the rating
                int ratingColumnIndex = cursor.getColumnIndex(DbHelper.RATING);
                if (ratingColumnIndex != -1) {
                    float rating = cursor.getFloat(ratingColumnIndex);
                    holder.ratingBar.setRating(rating);
                } else {
                    holder.ratingBar.setRating(0);
                }
            }
        }


        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView restaurantNameTextView;
            TextView restaurantAddressTextView;
            RatingBar ratingBar;


            public ViewHolder(View itemView) {
                super(itemView);
                restaurantNameTextView = itemView.findViewById(R.id.restaurant_name);
                restaurantAddressTextView = itemView.findViewById(R.id.restaurant_address);
                ratingBar = itemView.findViewById(R.id.rating_bar);


            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Refresh the list
    }

}