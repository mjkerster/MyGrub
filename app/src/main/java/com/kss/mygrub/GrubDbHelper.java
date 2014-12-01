package com.kss.mygrub;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GrubDbHelper extends SQLiteOpenHelper {

    private static GrubDbHelper dbInstance = null;
    public static final String dbName = "GRUB_DB";
    public static final int version = 1;
    private static final String tableGrub = "GRUB";
    private static final String createTable = "CREATE TABLE IF NOT EXISTS "+ tableGrub +" ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                                          " EAT_DATE DATE," +
                                                                                          " TYPE TEXT," +
                                                                                          " NAME TEXT," +
                                                                                          " DESC TEXT," +
                                                                                          " RATING INTEGER," +
                                                                                          " PHOTO_CAM BLOB," +
                                                                                          " PHOTO_THUMB BLOB," +
                                                                                          " LAT REAL," +
                                                                                          " LONGI REAL)";


    public GrubDbHelper(Context context){
        super(context,dbName, null, version);
    }

    public static GrubDbHelper getInstance(Context context){
        if(dbInstance != null)
            dbInstance = new GrubDbHelper(context);

        return dbInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+tableGrub);
        this.onCreate(db);
    }

    public Cursor getAllGrub(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM "+ tableGrub +" ORDER BY EAT_DATE ASC", null);
    }
}
