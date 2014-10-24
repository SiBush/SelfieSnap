package com.johncorser.selfiesnap;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by jcorser on 10/11/14.
 */
public class FriendsFragment extends android.support.v4.app.Fragment{

    public static final String TAG = FriendsFragment.class.getSimpleName();
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;
    protected List<ParseUser> mFriends;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);
        mGridView = (GridView)rootView.findViewById(R.id.friendsGrid);
        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation("friendsRelation");
        getActivity().setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (e == null){
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for(ParseUser user : mFriends){
                        usernames[i] = user.getUsername();
                        i++;

                    }
                    if (mGridView.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
                        mGridView.setAdapter(adapter);
                    }
                    else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                    }
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usernames );


                }

                else{
                    Log.e(TAG, e.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.signup_error_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


}
