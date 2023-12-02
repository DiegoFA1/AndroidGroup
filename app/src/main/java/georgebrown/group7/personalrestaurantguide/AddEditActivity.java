package georgebrown.group7.personalrestaurantguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class AddEditActivity extends AppCompatActivity {
    private EditText editTextRestaurantName, editTextRestaurantAddress, editTextPhone,
            editTextRestaurantDescription, editTextTags;
    private RatingBar ratingBar;
    private Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize views
        editTextRestaurantName = findViewById(R.id.editTextRestaurantName);
        editTextRestaurantAddress = findViewById(R.id.editTextRestaurantAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextRestaurantDescription = findViewById(R.id.editTextRestaurantDescription);
        editTextTags = findViewById(R.id.editTextTags);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSave = findViewById(R.id.buttonSave);

        // Set click listener for save button
        buttonSave.setOnClickListener(v -> onSaveButtonClicked());
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

    private void onSaveButtonClicked() {
        // Get data from views
        String restaurantName = editTextRestaurantName.getText().toString().trim();
        String restaurantAddress = editTextRestaurantAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String description = editTextRestaurantDescription.getText().toString().trim();
        String tags = editTextTags.getText().toString().trim();
        float rating = ratingBar.getRating();

        // Create new Restaurant object
        Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, phone, description, tags, rating);

        // Pass the new restaurant data back to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newRestaurant", newRestaurant);
        setResult(RESULT_OK, resultIntent);

        // Finish the activity
        finish();
    }
}