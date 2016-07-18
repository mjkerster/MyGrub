package com.kss.mygrub;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ListView;

import java.lang.reflect.Field;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private GrubListAdapter grubListAdapter;
    Uri uri = Uri.parse("content://" + GrubContentProvider.AUTHORITY +"/" + GrubDbHelper.tableGrub);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //An apparent hack I found on StackOverflow. Always displays the three dots in the action bar.
        getOverflowMenu();

        //GrubDbHelper db = GrubDbHelper.getInstance(this.getApplicationContext());

        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);
        grubListAdapter = new GrubListAdapter(this.getApplicationContext(), null, false);
        ListView listView = (ListView) findViewById(R.id.grub_list_view);
        listView.setAdapter(grubListAdapter);

        //setContentView(R.layout.no_grub_main);


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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, uri, GrubDbHelper.ALL_COLUMNS, null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        grubListAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        grubListAdapter.changeCursor(null);
    }
}
