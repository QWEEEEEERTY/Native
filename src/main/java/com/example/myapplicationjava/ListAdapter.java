package com.example.myapplicationjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ListAdapter extends ArrayAdapter {
    private final Context context;
    private final List<User> users;

    public ListAdapter(Context context, List<User> users) {
        super(context, R.layout.message_list_item, users);
        this.context = context;
        this.users = users;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message_list_item, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
        TextView textView3 = (TextView) rowView.findViewById(R.id.text3);
        User user = users.get(position);
        textView1.setText(user.getId());
        textView2.setText(user.getUsername());
        textView3.setText(user.getEmail());
        return rowView;
    }
}
