package com.kss.mygrub;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddItem extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String PHOTO_PATH = Environment.getExternalStorageDirectory() +"/Android/data/com.kss.mygrub/photos";
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

    }

    public void saveGrub(View view){
        EditText grubName = (EditText) findViewById(R.id.item_name);
        EditText grubDescription  = (EditText) findViewById(R.id.item_description);
        Spinner typeSpinner = (Spinner) findViewById(R.id.item_type);
        String grubType = typeSpinner.getSelectedItem().toString();
        //TODO: Get Date information from view
        RatingBar ratingBar = (RatingBar) findViewById(R.id.item_rating);
        int grubRating = ratingBar.getNumStars();

        ContentValues grubValues = new ContentValues();
        grubValues.put(GrubDbHelper.COL_NAME, grubName.getText().toString());
        grubValues.put(GrubDbHelper.COL_DESC, grubDescription.getText().toString());
        grubValues.put(GrubDbHelper.COL_TYPE, grubType);
        grubValues.put(GrubDbHelper.COL_RATING, grubRating);
        grubValues.put(GrubDbHelper.COL_PHOTO_PATH, mCurrentPhotoPath);

        Uri uri = Uri.parse("content://" + GrubContentProvider.AUTHORITY +"/" + GrubDbHelper.tableGrub);
        getContentResolver().insert(uri, grubValues);

    }

    public void getCamera(View view){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        Uri fileUri = null;
        try{
            photoFile = createImageFile();
            fileUri = Uri.fromFile(photoFile);
        }
        catch (IOException err){
            Log.d("ERROR "," getCamera create photo file", err);
        }

        if(photoFile != null){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File storageDir = new File(PHOTO_PATH);
        if(!storageDir.exists())
            storageDir.mkdirs();

        File newPhoto = new File(storageDir, imageFileName);

        Log.d("PHOTO_PATH", newPhoto.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = newPhoto.getAbsolutePath();
        return newPhoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK) {

                File asd = new File(mCurrentPhotoPath);
                Log.d("New File Created", asd.toURI().toString());
                ImageView imageView = (ImageView) findViewById(R.id.grub_photo);
                Uri uri = Uri.fromFile(asd);
                imageView.setImageURI(uri);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
