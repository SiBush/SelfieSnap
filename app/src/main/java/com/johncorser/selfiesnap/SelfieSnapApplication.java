package com.johncorser.selfiesnap;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jcorser on 10/10/14.
 */
public class SelfieSnapApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YBpAjt4kYAlxVnHaMS8UbH0rXVHiEaU1lzjEpmg6", "1DX9ERtLdPMPGaGAtPt8wn3ZVPf9iPMq4hC9FMcr");

    }
}
