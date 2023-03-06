package com.example.myapplicationjava.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.models.Message;

import java.util.List;

public class MessageListAdapter extends ArrayAdapter
{
    private final Context context;
    private final List<Message> messages;
    private final String currentUserId;


    public MessageListAdapter(Context context, List<Message> messages, String currentUserId)
    {
        super(context, R.layout.direct_message_item, messages);
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.direct_message_item, parent, false);
        Message message = messages.get(position);

        LinearLayout linearLayout = rowView.findViewById(R.id.message_box);
        TextView textView1 =  rowView.findViewById(R.id.message_text);
        TextView textView2 =  rowView.findViewById(R.id.message_time);

        if(message.getSender().equals(currentUserId))
        {
            linearLayout.setGravity(Gravity.END);
        }
        textView1.setText(message.getContent());
        textView2.setText(message.getTime());

        return rowView;
    }
}