package io.github.cluo29.contextdatareading;


import io.github.cluo29.contextdatareading.noisiness.*;

import io.github.cluo29.contextdatareading.semantization.parser.*;
import io.github.cluo29.contextdatareading.table.Battery;

import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.sql.SQLException;
import java.util.UUID;


/**
 * Created by Comet on 16/03/16.
 */
public class BatterDataReading extends Service {



    @Override
    public void onCreate() {


        // read data file here
        try {

            final DataSource ds = new MysqlDataSource("localhost", 3306, "root", "root", "aware");

            final DataNoiser dn = new SimpleDataNoiser(1391062684000L, 3600000L, 0.3, 0.1, 0.4);
            final AwareSimulator sim = new AwareSimulator(ds, dn, 1391062684000L, UUID.fromString("92d47d9d-a600-4309-b340-b58314c2e429"));
            sim.setSpeed(100.0);
            sim.battery.addListener(new AwareSimulator.Listener<Battery>() {
                public void onEvent(Battery event) {

                    Log.d("Tester", "Battery: " + event);
                }
            });

        } catch (SQLException|ClassNotFoundException e) {

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
