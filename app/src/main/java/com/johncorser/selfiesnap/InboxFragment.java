package com.johncorser.selfiesnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcorser on 10/11/14.
 */
public class InboxFragment extends android.support.v4.app.ListFragment{

    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String TAG = InboxFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorScheme(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminateVisibility(true);
        retrieveMessages();
    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Messages");
        query.whereEqualTo("recipientIds", ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (e == null) {
                    //we found messages
                    mMessages = messages;

                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString("senderName");
                        i++;

                    }
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);

                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, usernames );
                        setListAdapter(adapter);

                    }
                    else{
                        //refill adapter
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);

                    }
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final ParseObject message = mMessages.get(position);
        String messageType = message.getString("fileType");
        ParseFile file = message.getParseFile("file");
        final String face = message.getString("face");
        Uri fileUri = Uri.parse(file.getUrl());


        if (messageType.equals("image")){
            //view image
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            RidiculousFaces ridiculousFaces = new RidiculousFaces();
            final CharSequence[] guesses = ridiculousFaces.getGuessesToMake(face);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("What face did your partner make?");
            builder.setItems(ridiculousFaces.shuffleArray(guesses),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            if (guesses[which] == face){
                                Toast.makeText(getActivity(), "You got it right!", Toast.LENGTH_LONG).show();
                                //TODO: Update the value for curStreak
                                //TODO: Check whether the curStreak > bestStreak, update bestStreak if so
                                ParseQuery<ParseObject> testQuery = new ParseQuery<ParseObject>("Streak");
                                try {
                                    List<ParseObject> streaks = testQuery.find();
                                    for (ParseObject streak : streaks) {
                                        try {
                                            if (streak.getJSONArray("players").getString(0).equals(ParseUser.getCurrentUser().getObjectId()) || streak.getJSONArray("players").getString(1).equals(ParseUser.getCurrentUser().getObjectId())) {
                                                if (streak.getJSONArray("players").getString(0).equals(message.getString("senderId")) || streak.getJSONArray("players").getString(1).equals(message.getString("senderId"))) {
                                                    int oldStreak = (int) Float.parseFloat(streak.getNumber("streak").toString());
                                                    int oldBestStreak = (int)Float.parseFloat(streak.getNumber("bestStreak").toString());
                                                    streak.put("streak", ++oldStreak);
                                                    if (oldStreak > oldBestStreak){
                                                        streak.put("bestStreak", oldStreak);
                                                    }
                                                    try {
                                                        streak.save();
                                                    } catch (ParseException e) {
                                                        Log.e(TAG, "Exception: " + e);
                                                    }
                                                }

                                            }
                                        } catch (JSONException e) {
                                            Log.e("MessageAdapter", "Error in loop: " + e);
                                        }
                                    }

                                } catch (ParseException e) {
                                    Log.e(TAG, "Error: " + e);
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), "You got it wrong. It was " + face, Toast.LENGTH_LONG).show();
                                //TODO: reset curStreak to zero
                                ParseQuery<ParseObject> testQuery = new ParseQuery<ParseObject>("Streak");
                                try {
                                    List<ParseObject> streaks = testQuery.find();
                                    for (ParseObject streak : streaks) {
                                        try {
                                            if (streak.getJSONArray("players").getString(0).equals(ParseUser.getCurrentUser().getObjectId()) || streak.getJSONArray("players").getString(1).equals(ParseUser.getCurrentUser().getObjectId())) {
                                                if (streak.getJSONArray("players").getString(0).equals(message.getString("senderId")) || streak.getJSONArray("players").getString(1).equals(message.getString("senderId"))) {
                                                    streak.put("streak", 0);
                                                    try {
                                                        streak.save();
                                                    } catch (ParseException e) {
                                                        Log.e(TAG, "Exception: " + e);
                                                    }
                                                }

                                            }
                                        } catch (JSONException e) {
                                            Log.e("MessageAdapter", "Error in loop: " + e);
                                        }
                                    }

                                } catch (ParseException e) {
                                    Log.e(TAG, "Error: " + e);
                                }
                            }
                        }
                    });
            builder.create().show();

            startActivity(intent);
        }
        else{
            //view video? might add this later
        }

        //Delete the message
        List<String> ids = message.getList("recipientIds");
        if (ids.size() == 1){
            //last recipient - delete the message
            message.deleteInBackground();
        }
        else {
            //remove the recipient and resave the image on Parse
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

            message.removeAll("recipientIds", idsToRemove);
            message.saveInBackground();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveMessages();

        }
    };
}
