package io.github.cluo29.contextdatareading;


import io.github.cluo29.contextdatareading.noisiness.*;

import io.github.cluo29.contextdatareading.semantization.parser.*;
import io.github.cluo29.contextdatareading.table.Battery;

import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import java.sql.SQLException;
import java.util.UUID;


/**
 * Created by Comet on 16/03/16.
 */
public class BatterDataReading extends Service {



    @Override
    public void onCreate() {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // read data file here
        try {

            Log.d("Tester", "34");
            final DataSource ds = new MysqlDataSource("awareframework.com", 3306, "Luo_661", "", "Luo_661");
            Log.d("Tester", "35");
            final DataNoiser dn = new SimpleDataNoiser(1391062684000L, 3600000L, 0.3, 0.1, 0.4);
            final AwareSimulator sim = new AwareSimulator(ds, dn, 1458126481750L, UUID.fromString("83bc93f4-e631-4007-b87f-9f0e47669537"));
            sim.setSpeed(100.0);
            Log.d("Tester", "39");
            sim.battery.addListener(new AwareSimulator.Listener<Battery>() {
                public void onEvent(Battery event) {
                    Log.d("Tester", "49");
                    Log.d("Tester", "Battery: " + event);
                }
            });
            sim.start();

        } catch (SQLException|ClassNotFoundException e) {
            Log.d("Tester", "48");
            e.printStackTrace();

        }





    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
