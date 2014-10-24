package com.johncorser.selfiesnap;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Date;
import java.util.List;

/**
 * Created by jcorser on 10/13/14.
 */

public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
            holder.timeLabel = (TextView)convertView.findViewById(R.id.timeLabel);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject message = mMessages.get(position);

        Date createdAt = message.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS).toString();

        String[] userIds = new String[] {ParseUser.getCurrentUser().getObjectId(), message.getString("senderId")};
        String[] reverseUserIds = new String[] {message.getString("senderId"), ParseUser.getCurrentUser().getObjectId()};
        /*List<ParseObject> streaks = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Streak");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Streak");
        query.whereEqualTo("players", userIds);
        query2.whereEqualTo("players", reverseUserIds);
        List<ParseQuery<ParseObject>> queryList = new ArrayList<ParseQuery<ParseObject>>();
        queryList.add(query);
        queryList.add(query2);
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queryList);
        */
        ParseQuery<ParseObject> testQuery = new ParseQuery<ParseObject>("Streak");
        int curStreak = -1;
        int bestStreak = -1;


        try {
            List<ParseObject> streaks = testQuery.find();
            for (ParseObject streak : streaks){
                try {
                    if (streak.getJSONArray("players").getString(0).equals(ParseUser.getCurrentUser().getObjectId()) || streak.getJSONArray("players").getString(1).equals(ParseUser.getCurrentUser().getObjectId())){
                        if (streak.getJSONArray("players").getString(0).equals(message.getString("senderId")) || streak.getJSONArray("players").getString(1).equals(message.getString("senderId"))){
                            curStreak = (int)Float.parseFloat(streak.getNumber("bestStreak").toString());
                            bestStreak = (int)Float.parseFloat(streak.getNumber("streak").toString());
                        }

                    }
                }
                catch (JSONException e){
                    Log.e("MessageAdapter", "Error in loop: " + e);
                }
            }

        }
        catch (ParseException e){
            Log.e("MessageAdapter", "Error: " + e);
        }

        holder.timeLabel.setText(convertedDate + "\nCurrent Streak: " + curStreak + "\nBest Streak: " + bestStreak);

        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_picture);
        }
        else {
            holder.iconImageView.setImageResource(R.drawable.ic_video);
        }
        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));

        return convertView;

    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView timeLabel;
    }

    public void refill(List<ParseObject> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }
}

