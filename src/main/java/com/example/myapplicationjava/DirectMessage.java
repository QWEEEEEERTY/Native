package com.example.myapplicationjava;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DirectMessage extends Fragment {

    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private ListView listView;
    private TextView textView;
    private String me, partner, partnerId;

    public DirectMessage(String partner, String partnerId) {
        this.partner = partner;
        this.partnerId = partnerId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragment = inflater.inflate(R.layout.fragment_direct_message, container, false);
        me = getActivity().getIntent().getStringExtra("UserId");

        textView = fragment.findViewById(R.id.direct_user);
        textView.setText(partner);

        messages = new ArrayList<Message>();
        loadMessages();

        messages.add(new Message(me, "12345678", "Hello everyone"));
        messages.add(new Message("12345678", me, "Hello everyone"));
        messageAdapter = new MessageAdapter(getActivity(), messages, me);
        listView = fragment.findViewById(R.id.direct_message);
        listView.setAdapter(messageAdapter);

        ImageButton send = fragment.findViewById(R.id.message_send);
        send.setOnClickListener(v ->
        {
            EditText editText = fragment.findViewById(R.id.message_input);
            String input = editText.getText().toString();
            Message message = new Message(me, partnerId, input);
            message.pushMessage();
            editText.setText("");
        });

        return fragment;
    }

    private void loadMessages(){
        String messageId;
        if(me.compareTo(partnerId) < 0)
            messageId = me+partnerId;
        else
            messageId = partnerId+me;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("Messages").child(messageId);
        usersReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                messages.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    Message message = userSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

}