package de.braunschweig.braunschweigermedientagequiz;

import android.app.Application;

/**
 * Created by Iwan on 15/01/15.
 */
public class MyApplication extends Application {
    private static MyApplication app;

    public void onCreate(){
        app = this;
    }

    public static MyApplication get(){
        return app;
    }
}
