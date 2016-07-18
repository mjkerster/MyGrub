package com.kss.mygrub;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


public class GrubContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.kss.mygrub.GrubContentProvider";
    private static final String GRUB_TABLE = GrubDbHelper.tableGrub;
    private static final int GRUB = 1;
    private static final int GRUB_PLUS_ID = 10;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static GrubDbHelper db;

    static {
        uriMatcher.addURI(AUTHORITY, GRUB_TABLE, GRUB);
        uriMatcher.addURI(AUTHORITY, GRUB_TABLE+"/#", GRUB_PLUS_ID);
    }

    @Override
    public boolean onCreate() {
        db = GrubDbHelper.getInstance(this.getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor returnCursor;
        queryBuilder.setTables(GrubDbHelper.tableGrub);
        int uriCase = uriMatcher.match(uri);

        switch(uriCase){
            case GRUB:
                returnCursor =  queryBuilder.query(db.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case GRUB_PLUS_ID:
                String id = uri.getLastPathSegment();
                String idWhereClause = GrubDbHelper.COL_ID+" = "+ id;
                returnCursor = queryBuilder.query(db.getReadableDatabase(), projection, idWhereClause+" AND "+ selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriCase = uriMatcher.match(uri);
        SQLiteDatabase insertDb = db.getWritableDatabase();
        Log.d("URI_CASE", String.valueOf(uriCase));

        switch(uriCase){
            case GRUB:
                insertDb.insert(GRUB_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        insertDb.close();
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriCase = uriMatcher.match(uri);
        int rowsDeleted;
        SQLiteDatabase deleteDb = db.getWritableDatabase();

        switch(uriCase){
            case GRUB:
                rowsDeleted = deleteDb.delete(GRUB_TABLE, selection, selectionArgs);
                break;
            case GRUB_PLUS_ID:
                String id = uri.getLastPathSegment();
                String idWhereClause = GrubDbHelper.COL_ID+" = "+ id;
                if(TextUtils.isEmpty(selection)){
                    rowsDeleted = deleteDb.delete(GRUB_TABLE, idWhereClause, null);
                }
                else{
                    rowsDeleted = deleteDb.delete(GRUB_TABLE, idWhereClause + " AND "+ selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        deleteDb.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriCase = uriMatcher.match(uri);
        int rowsUpdated;
        SQLiteDatabase updateDb = db.getWritableDatabase();

        switch (uriCase){
            case GRUB:
                rowsUpdated = updateDb.update(GRUB_TABLE, values, selection, selectionArgs);
                break;
            case GRUB_PLUS_ID:
                String id = uri.getLastPathSegment();
                String idWhereClause = GrubDbHelper.COL_ID+" = "+ id;
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = updateDb.update(GRUB_TABLE, values, idWhereClause, null);
                }
                else{
                    rowsUpdated = updateDb.update(GRUB_TABLE, values, idWhereClause + " AND "+ selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        updateDb.close();
        return rowsUpdated;
    }
}
