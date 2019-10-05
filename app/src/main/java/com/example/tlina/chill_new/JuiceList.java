package com.example.tlina.chill_new;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JuiceList extends ArrayAdapter<Juice> {

    private Activity context;
    private List<Juice> juiceList;

    public JuiceList(Activity context, List<Juice> juiceList){

        super(context, R.layout.list_layout, juiceList);

        this.context = context;
        this.juiceList = juiceList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView txtViewName = (TextView) listViewItem.findViewById(R.id.textName);
        TextView txtViewType = (TextView) listViewItem.findViewById(R.id.textType);
        TextView txtViewTime = (TextView) listViewItem.findViewById(R.id.textTime);

        Juice juice = juiceList.get(position);

        txtViewName.setText(juice.getJuiceName());
        txtViewType.setText(juice.getJuiceType());
        txtViewTime.setText(juice.getJuiceTime());

        return listViewItem;
    }

}
