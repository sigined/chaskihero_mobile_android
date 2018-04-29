package pe.chaskihero.chaskihero;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static synchronized Context getContext() {
        return context;
    }
    /*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }*/

}