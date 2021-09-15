package com.datechnologies.androidtest.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.ChatLogMessageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A recycler view adapter used to display chat log messages in {@link ChatActivity}.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private Context context;
    private List<ChatLogMessageModel> chatLogMessageModelList;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public ChatAdapter(Context context) {
        this.context = context;
        chatLogMessageModelList = new ArrayList<>();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void setChatLogMessageModelList(List<ChatLogMessageModel> chatLogMessageModelList) {
        this.chatLogMessageModelList = chatLogMessageModelList;
        notifyDataSetChanged();
    }

    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_chat, parent, false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position) {
        ChatLogMessageModel chatLogMessageModel = chatLogMessageModelList.get(position);

        viewHolder.messageTextView.setText(chatLogMessageModel.message);
        viewHolder.nameTextView.setText(chatLogMessageModel.name);

        if (chatLogMessageModel.avatarUrl != null) {
            // Picasso.get().setLoggingEnabled(true); #debug
            String url = chatLogMessageModel.avatarUrl;
            String secureUrl = convertHttpToHttps(url);
             Picasso.get().load(secureUrl).into(viewHolder.avatarImageView);
            // Glide.with(context).load(secureUrl).apply(RequestOptions.circleCropTransform()).into(viewHolder.avatarImageView);
        }
    }

    /**
     * Unless we allow for clear text traffic communication,
     * Picasso cannot load images with non secure URLs.
     *
     * Therefore, we must use a secure URL to load the images.
     * Server sends us an HTTP url so we must convert to HTTPS
     *
     * @param originalUrl is the url coming from the server.
     * Example: 'http://dev.rapptrlabs.com/Tests/images/drew_avatar.png'
     *
     * @return a new URL with an 's' appended to the end of 'http'
     * Example: 'https://dev.rapptrlabs.com/Tests/images/drew_avatar.png'
     */
    private String convertHttpToHttps(String originalUrl) {
        String newUrl = new String();

        for (int i = 0; i < originalUrl.length(); i++) {
            newUrl += originalUrl.charAt(i);
            if (i == 3) newUrl += "s";
        }

        return newUrl;
    }

    @Override
    public int getItemCount() {
        return chatLogMessageModelList.size();
    }

    //==============================================================================================
    // ChatViewHolder Class
    //==============================================================================================

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView messageTextView;
        TextView nameTextView;

        public ChatViewHolder(View view) {
            super(view);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            messageTextView = (TextView) view.findViewById(R.id.messageTextView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        }
    }

}
