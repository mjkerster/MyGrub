package com.kss.mygrub;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GrubListAdapter extends CursorAdapter {

    static class GrubViewHolder{
        TextView id;
        TextView name;
        TextView desc;
        ImageView photo;
        TextView grubDate;
        TextView rating;
    }

    public GrubListAdapter(Context context, Cursor cursor, boolean autoquery) {
        super(context, cursor, autoquery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        GrubViewHolder grubViewHolder = new GrubViewHolder();
        View grubListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grub_list_item, parent, false);

        grubViewHolder.name = (TextView) grubListView.findViewById(R.id.ls_name);
        grubViewHolder.desc = (TextView) grubListView.findViewById(R.id.ls_description);
        grubViewHolder.photo = (ImageView) grubListView.findViewById(R.id.ls_photo);

        grubListView.setTag(grubViewHolder);

        return grubListView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        GrubViewHolder grubViewHolder = (GrubViewHolder) view.getTag();

        grubViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_NAME)));
        grubViewHolder.desc.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_DESC)));

        Picasso pic = Picasso.with(context);
        pic.setIndicatorsEnabled(false); //true adds indicator to the image
        pic.setLoggingEnabled(false); //true add picasso logs (they aren't very helpful)

        //used to handle null values from grub items with no picture
        String path = (cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_PHOTO_PATH))==null)?"":cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_PHOTO_PATH));

        //file:// is needed so that Picasso can properly grab the file locally.
        //fit() changes the resolution and saves memory
        //centerCrop() gets a square version of the image from the center.
        pic.load("file://"+path).fit().centerCrop().into(grubViewHolder.photo);
    }
}
