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

    public long insert(String name, String desc){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.NAME, name);
        cv.put(DbHelper.DESC,desc);

        // id of object added
        long id = db.insert(DbHelper.TABLE_NAME, null, cv);
        return id;
    }

    // Return the cursor
    public Cursor fetch(){
        String[] cols = new String[]{DbHelper._ID,DbHelper.NAME,DbHelper.DESC};
        Cursor c = db.query(DbHelper.TABLE_NAME, cols,
                null,null,null,null,null);

        // prepare cursor
        if (c!=null){
            c.moveToFirst();
        }

        return c;
    }

    // return number of rows updated
    public int update(long id, String name, String desc){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.NAME, name);
        cv.put(DbHelper.DESC,desc);

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
}
