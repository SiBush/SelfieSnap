package com.johncorser.selfiesnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcorser on 10/11/14.
 */
public class InboxFragment extends android.support.v4.app.ListFragment{

    protected List<ParseObject> mMessages;
    public static final String TAG = InboxFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Messages");
        query.whereEqualTo("recipientIds", ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);
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

        ParseObject message = mMessages.get(position);
        String messageType = message.getString("fileType");
        ParseFile file = message.getParseFile("file");
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals("image")){
            //view image
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            Log.e(TAG, "running");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("What face did your partner make?");
            builder.setItems(new CharSequence[]
                            {"Ate too much pizza", "Stepped on Frog", "Just saw Hitler's ghost", "Quicksand!"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    Toast.makeText(getActivity(), "clicked 1", Toast.LENGTH_LONG).show();
                                    break;
                                case 1:
                                    Toast.makeText(getActivity(), "clicked 2", Toast.LENGTH_LONG).show();
                                    break;
                                case 2:
                                    Toast.makeText(getActivity(), "clicked 3", Toast.LENGTH_LONG).show();
                                    break;
                                case 3:
                                    Toast.makeText(getActivity(), "clicked 4", Toast.LENGTH_LONG).show();
                                    break;
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
}
