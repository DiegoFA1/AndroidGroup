package georgebrown.group7.personalrestaurantguide;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String tags;
    private float rating;

    public Restaurant(String name, String address, String phone, String description, String tags, float rating) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.rating = rating;
    }
    protected Restaurant(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        description = in.readString();
        tags = in.readString();
        rating = in.readFloat();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public float getRating() {
        return rating;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(description);
        dest.writeString(tags);
        dest.writeFloat(rating);
    }
}
