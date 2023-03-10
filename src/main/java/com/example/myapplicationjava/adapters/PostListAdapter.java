package com.example.myapplicationjava.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplicationjava.R;
import com.example.myapplicationjava.models.Firebase;
import com.example.myapplicationjava.models.Post;
import com.example.myapplicationjava.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListAdapter extends ArrayAdapter
{
    ImageView userImg, postImg;
    TextView username, time, postTopic, postText;
    private View item;
    private User user;
    private Post post;
    private final Context context;
    private final List<Post> posts;

    public PostListAdapter(Context context, List<Post> posts)
    {
        super(context, R.layout.message_list_item, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item = inflater.inflate(R.layout.post_list_item, parent, false);
        post = posts.get(position);
        init();
        Firebase.copyObject("Users/"+post.getUserId(), User.class, task -> { user = task.getResult(); setItemValues();});
        return item;
    }
    private void init()
    {
        user = new User();
        userImg  =  item.findViewById(R.id.profile_picture);
        postImg  =  item.findViewById(R.id.post_picture);
        username = item.findViewById(R.id.username);
        time = item.findViewById(R.id.time);
        postTopic = item.findViewById(R.id.post_topic);
        postText = item.findViewById(R.id.post_text);
    }
    private void setItemValues()
    {
        if(user.getImageUrl() != null)
        {
            Picasso.get().load(user.getImageUrl()).into(userImg);
        }
        Picasso.get().load(post.getImageUrl()).into(postImg);
        username.setText(user.getUsername());
        time.setText(post.getTime());
        postTopic.setText(post.getTopic());
        postText.setText(post.getText());
    }
}
