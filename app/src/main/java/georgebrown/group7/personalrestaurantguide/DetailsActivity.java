package georgebrown.group7.personalrestaurantguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewName, textViewAddress, textViewPhone, textViewDescription, textViewTags;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize views
        textViewName = findViewById(R.id.textViewRestaurantName);
        textViewAddress = findViewById(R.id.textViewRestaurantAddress);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewDescription = findViewById(R.id.textViewRestaurantDescription);
        textViewTags = findViewById(R.id.textViewTags);
        ratingBar = findViewById(R.id.ratingBar);

        // Receive restaurant data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Restaurant restaurant = extras.getParcelable("restaurant");
            if (restaurant != null) {
                // Set data to views
                textViewName.setText(restaurant.getName());
                textViewAddress.setText(restaurant.getAddress());
                textViewPhone.setText(restaurant.getPhone());
                textViewDescription.setText(restaurant.getDescription());
                textViewTags.setText(restaurant.getTags());
                ratingBar.setRating(restaurant.getRating());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_button) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareRestaurantDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareRestaurantDetails() {
        // Fetch restaurant details to share
        String name = textViewName.getText().toString();
        String address = textViewAddress.getText().toString();
        String phone = textViewPhone.getText().toString();
        String description = textViewDescription.getText().toString();
        String tags = textViewTags.getText().toString();
        float rating = ratingBar.getRating();

        String shareBody = "Check out this restaurant!\n" +
                "Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Phone: " + phone + "\n" +
                "Description: " + description + "\n" +
                "Tags: " + tags + "\n" +
                "Rating: " + rating;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Restaurant Details");
        emailIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailsActivity.this,
                    "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    // Method called when the map icon is clicked
    public void onMapIconClicked(View view) {
        // Fetch restaurant address
        String address = textViewAddress.getText().toString();

        // Open Google Maps for directions
        openMapForDirections(address);
    }

    private void openMapForDirections(String address) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app is not installed.", Toast.LENGTH_LONG).show();
        }
    }

}