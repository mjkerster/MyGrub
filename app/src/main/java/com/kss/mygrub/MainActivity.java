package com.kss.mygrub;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ListView;

import java.lang.reflect.Field;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //An apparent hack I found on StackOverflow. Always displays the three dots in the action bar.
        getOverflowMenu();

        GrubDbHelper db = GrubDbHelper.getInstance(this.getApplicationContext());
        try {
            //TODO:Replace getAllGrub with ContentProvider query
            Cursor allGrub = db.getAllGrub();
            Log.d("ALL GRUB", String.valueOf(allGrub.getCount()));
            if(allGrub.getCount() > 0){
                setContentView(R.layout.activity_main);
                GrubListAdapter grubListAdapter = new GrubListAdapter(this.getApplicationContext(), allGrub, false);
                ListView listView = (ListView) findViewById(R.id.grub_list_view);
                listView.setAdapter(grubListAdapter);
            }
            else{
                setContentView(R.layout.no_grub_main);
            }

        }
        catch (NullPointerException e){
            setContentView(R.layout.no_grub_main);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_add:
                intent = new Intent(this, AddItem.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_map:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
