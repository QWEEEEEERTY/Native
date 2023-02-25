package com.example.myapplicationjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        User user = users.get(position);

        TextView textView1 = (TextView) rowView.findViewById(R.id.text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
        TextView textView3 = (TextView) rowView.findViewById(R.id.text3);
        TextView time_since = (TextView) rowView.findViewById(R.id.time_since);
        ImageView imageView = rowView.findViewById(R.id.user_picture);

        textView1.setText(user.getUsername());
        textView2.setText(user.getEmail());
        textView3.setText(user.getId());
        time_since.setText(user.getRegistrationTime());
        try
        {
            if(user.getGender().equals("female"))
            {
                imageView.setImageResource(R.drawable.woman);
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return rowView;
    }
}
