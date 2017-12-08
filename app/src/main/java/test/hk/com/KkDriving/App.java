package test.hk.com.KkDriving;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Eric on 2017/1/16.
 */
public class App extends Application {



    private static Context sAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);





    }










}
