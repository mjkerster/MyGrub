package com.kss.mygrub;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        grubViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_NAME)));
        grubViewHolder.desc.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_DESC)));
        try{ //TODO: CLEAN THIS UP
            grubViewHolder.photo.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_PHOTO_PATH))));
        }
        catch (NullPointerException e) {
            grubViewHolder.photo.setImageResource(R.drawable.ic_action_picture);
        }



        return grubListView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        GrubViewHolder grubViewHolder = (GrubViewHolder) view.getTag();

        grubViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_NAME)));
        grubViewHolder.desc.setText(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_DESC)));
        try{ //TODO: LIKE ABOVE, CLEAN THIS UP
            grubViewHolder.photo.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(GrubDbHelper.COL_PHOTO_PATH))));
        }
        catch (NullPointerException e) {
            grubViewHolder.photo.setImageResource(R.drawable.ic_action_picture);
        }

    }
}
