package georgebrown.group7.personalrestaurantguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DbManager(Context c){
        context = c;
    }

    public DbManager open(){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();

        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insert(String name, String desc, String address, String phone, String tags, float rating, boolean isFavorite){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.NAME, name);
        cv.put(DbHelper.DESC,desc);
        cv.put(DbHelper.ADDRESS,address);
        cv.put(DbHelper.PHONE,phone);
        cv.put(DbHelper.TAGS,tags);
        cv.put(String.valueOf(DbHelper.RATING),rating);


        if (isFavorite) {
            cv.put(String.valueOf(DbHelper.ISFAVORITE), 1);
        } else {
            cv.put(String.valueOf(DbHelper.ISFAVORITE), 0);
        }

        // id of object added
        long id = db.insert(DbHelper.TABLE_NAME, null, cv);
        return id;
    }

    // Return a single record
    public Cursor fetchById(long id){
        String[] cols = new String[]{DbHelper._ID, DbHelper.NAME, DbHelper.ADDRESS,
                DbHelper.PHONE, DbHelper.TAGS, String.valueOf(DbHelper.RATING),
                String.valueOf(DbHelper.ISFAVORITE), DbHelper.DESC};
        Cursor c = db.query(DbHelper.TABLE_NAME, cols,
                DbHelper._ID+" = ?",new String[]{String.valueOf(id)},
                null,null,null);

        // prepare cursor
        if (c!=null){
            c.moveToFirst();
        }

        return c;
    }


    // Return the cursor
    public Cursor fetch(){
        String[] cols = new String[]{DbHelper._ID, DbHelper.NAME, DbHelper.ADDRESS,
                DbHelper.PHONE, DbHelper.TAGS, String.valueOf(DbHelper.RATING),
                String.valueOf(DbHelper.ISFAVORITE), DbHelper.DESC};

        Cursor c = db.query(DbHelper.TABLE_NAME, cols,
                null,null,null,null,null);

        // prepare cursor
        if (c!=null){
            c.moveToFirst();
        }

        return c;
    }
    public Cursor fetchFavorites() {
        String[] columns = new String[]{DbHelper._ID, DbHelper.NAME, DbHelper.ADDRESS,
                DbHelper.PHONE, DbHelper.TAGS, DbHelper.RATING,
                DbHelper.ISFAVORITE, DbHelper.DESC};
        String selection = DbHelper.ISFAVORITE + " = ?";
        String[] selectionArgs = new String[]{"1"}; // Assuming '1' represents true for favorites

        Cursor cursor = db.query(DbHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // return number of rows updated
    public int update(long id, String name, String desc, String address, String phone, String tags, float rating, boolean isFavorite){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.NAME, name);
        cv.put(DbHelper.DESC,desc);
        cv.put(DbHelper.ADDRESS,address);
        cv.put(DbHelper.PHONE,phone);
        cv.put(DbHelper.TAGS,tags);
        if (isFavorite) {
            cv.put(String.valueOf(DbHelper.ISFAVORITE), 1);
        } else {
            cv.put(String.valueOf(DbHelper.ISFAVORITE), 0);
        }
        cv.put(String.valueOf(DbHelper.RATING),rating);


        int i = db.update(DbHelper.TABLE_NAME, cv,
                DbHelper._ID+" = ?",new String[]{String.valueOf(id)});

        // return 0 or 1
        return i;
    }


    public int delete(long id){
        int i = db.delete(DbHelper.TABLE_NAME,
                DbHelper._ID+" = ?",new String[]{String.valueOf(id)});
        return i;


    }
    public void updateFavoriteStatus(long id, boolean isFavorite) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.ISFAVORITE, isFavorite ? 1 : 0);
        db.update(DbHelper.TABLE_NAME, cv, DbHelper._ID + "=?", new String[]{String.valueOf(id)});
    }
}
