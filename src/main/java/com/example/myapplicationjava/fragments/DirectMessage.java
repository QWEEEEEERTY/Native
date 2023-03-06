package com.example.myapplicationjava.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.adapters.MessageListAdapter;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DirectMessage extends Fragment {
    private View fragment;
    private ArrayList<Message> messages;
    private MessageListAdapter messageListAdapter;
    private ListView listView;
    private TextView textView;
    private ImageButton sendMessage;
    private String currentserId, partnerUsername, partnerId;

    public DirectMessage(String partner, String partnerId) {
        this.partnerUsername = partner;
        this.partnerId = partnerId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragment = inflater.inflate(R.layout.fragment_direct_message, container, false);

        this.init();
        this.loadMessages();
        this.sendMessageOnClick();

        return fragment;
    }

    private void init()
    {
        currentserId = getActivity().getIntent().getStringExtra("UserId");
        textView = fragment.findViewById(R.id.direct_user);
        textView.setText(partnerUsername);
        sendMessage= fragment.findViewById(R.id.message_send);
        messages = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(getActivity(), messages, currentserId);
        listView = fragment.findViewById(R.id.direct_message);
        listView.setAdapter(messageListAdapter);
    }

    private void loadMessages(){
        String messageId;
        if(currentserId.compareTo(partnerId) < 0)
            messageId = currentserId+partnerId;
        else
            messageId = partnerId+currentserId;
        Firebase.getObjectList("Messages/"+messageId, messages, Message.class, messageListAdapter);
    }

    private void sendMessageOnClick()
    {
        sendMessage.setOnClickListener(v ->
        {
            EditText editText = fragment.findViewById(R.id.message_input);
            String text = editText.getText().toString();
            Message message = new Message(currentserId, partnerId, text);
            message.pushMessage();
            editText.setText("");
        });
    }

}
