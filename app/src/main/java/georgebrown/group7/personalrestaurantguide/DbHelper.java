package georgebrown.group7.personalrestaurantguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Restaurants";

    public static final String _ID = "_id"; //PK INT Autoincrement
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String TAGS = "tags";

    public static final String RATING = "rating";
    public static final String DESC = "description";

    public static final String ISFAVORITE = "isFavorite";

    //name of file
    static final String DB_NAME = "RestaurantDb.db";
    public static final int DB_VERSION = 9;

    public static final String CREATE_TABLE =
            "create table "+TABLE_NAME+" ("+
                    _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    NAME+" TEXT NOT NULL, "+
                    ADDRESS+" TEXT NOT NULL, "+
                    PHONE+" TEXT NOT NULL, "+
                    TAGS+" TEXT NOT NULL,"+
                    RATING+" REAL NOT NULL, "+
                    ISFAVORITE+" INTEGER NOT NULL, "+
                    DESC+" TEXT)";


    // CONSTRUCTOR
    public DbHelper(Context c){
        //Super constructor
        super(c,DB_NAME,null, DB_VERSION);
    }

    // on create is called when we called our app for first time and db is not created yet
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        Log.d("DATABASE","database created");
    }

    // update is called when we upgrade or version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.d("DATABASE","database re-created");
    }



}
