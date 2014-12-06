package com.jerry.serverlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {

    //Variables
    private Context mContext = null;
    private ArrayList<Chespirito> mArrayList = null;
    private LayoutInflater mLayoutInflater = null;

    public ListItemAdapter (Context context, ArrayList <Chespirito> arrayList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mArrayList = arrayList;
    }

    @Override
    public Object getItem(int position) {

        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getCount (){
        return mArrayList.size();
    }

    static class Holder {
        TextView txtPersonaje;
        TextView txtActor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;
        //Check if view is null
        if (view == null){
            holder = new Holder ();
            view = mLayoutInflater.inflate(R.layout.list_item_1,null);
            holder.txtPersonaje = (TextView) view.findViewById(R.id.txtPersonaje);
            holder.txtActor = (TextView) view.findViewById(R.id.txtActor);
            view.setTag(holder);
        }
        else{
            holder=(Holder)view.getTag();

        }
        holder.txtPersonaje.setText(mArrayList.get(position).getPersonaje());
        holder.txtActor.setText(mArrayList.get(position).getActor());

        return view;
    }

}
