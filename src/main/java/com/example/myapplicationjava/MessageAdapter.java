package com.example.myapplicationjava;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends ArrayAdapter {
    private final Context context;
    private final List<Message> messages;
    private final String me;


    public MessageAdapter(Context context, List<Message> messages, String me) {
        super(context, R.layout.direct_message_item, messages);
        this.context = context;
        this.messages = messages;
        this.me = me;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.direct_message_item, parent, false);
        LinearLayout linearLayout = rowView.findViewById(R.id.message_box);
        TextView textView1 = (TextView) rowView.findViewById(R.id.message_text);
        TextView textView2 = (TextView) rowView.findViewById(R.id.message_time);
        Message message = messages.get(position);
        if(message.getSender().equals(me)){
            linearLayout.setGravity(Gravity.END);
        }
        textView1.setText(message.getContent());
        textView2.setText(message.getTime());
        return rowView;
    }
}