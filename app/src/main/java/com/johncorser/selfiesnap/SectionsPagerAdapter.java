package com.johncorser.selfiesnap;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by jcorser on 10/11/14.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

        protected Context mContext;
        public SectionsPagerAdapter(Context context, FragmentManager fm) {

            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);

            switch (position){
                case 0: return new InboxFragment();
                case 1: return new FriendsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Inbox".toUpperCase(l);
                case 1:
                    return "Friends".toUpperCase(l);
            }
            return null;
        }

}
