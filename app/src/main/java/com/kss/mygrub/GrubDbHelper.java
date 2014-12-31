package com.kss.mygrub;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GrubDbHelper extends SQLiteOpenHelper {

    private static GrubDbHelper dbInstance = null;
    public static final String dbName = "GRUB_DB";
    public static final int version = 2;
    public static final String tableGrub = "GRUB";
    public static final String COL_ID = "_id";
    public static final String COL_EAT_DATE = "EAT_DATE";
    public static final String COL_TYPE = "TYPE";
    public static final String COL_NAME = "NAME";
    public static final String COL_DESC = "DESC";
    public static final String COL_RATING = "RATING";
    public static final String COL_PHOTO_PATH = "PHOTO_PATH";
    public static final String COL_LAT = "LAT";
    public static final String COL_LONGI = "LONGI";

    private static final String createTable = "CREATE TABLE IF NOT EXISTS "+ tableGrub +" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                          COL_EAT_DATE +" DATE DEFAULT CURRENT_DATE, " +
                                                                                          COL_TYPE +" TEXT, " +
                                                                                          COL_NAME +" TEXT, " +
                                                                                          COL_DESC +" TEXT, " +
                                                                                          COL_RATING +" INTEGER, " +
                                                                                          COL_PHOTO_PATH +" TEXT, " +
                                                                                          COL_LAT +" REAL, " +
                                                                                          COL_LONGI +" REAL)";


    public GrubDbHelper(Context context){
        super(context,dbName, null, version);
    }

    public static GrubDbHelper getInstance(Context context){
        if(dbInstance == null)
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
