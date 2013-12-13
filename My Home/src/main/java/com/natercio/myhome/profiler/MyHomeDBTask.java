package com.natercio.myhome.profiler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.natercio.myhome.db.Calendar;
import com.natercio.myhome.db.Network;
import com.natercio.myhome.db.ORMMyHomeDBHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/28/13
 * Time: 11:07 AM
 */
public abstract class MyHomeDBTask extends ProfilerTask {

    protected ORMMyHomeDBHelper dbHelper;

    protected Context context;

    public MyHomeDBTask(Context context) {
        this.dbHelper = new ORMMyHomeDBHelper(context);
        this.context = context;
    }

    ///////////////////////
    // PROTECTED METHODS //
    ///////////////////////

    /**
     * @return the current matching calendar row from the database
     * @throws SQLException
     */
    protected Calendar getCurrentCalendar() throws SQLException {
        Calendar current = new Calendar();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("H");
        SimpleDateFormat dateFormatWeekDay = new SimpleDateFormat("E");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM");

        current.setHour(dateFormatHour.format(calendar.getTime()).toString());
        current.setWeekday(dateFormatWeekDay.format(calendar.getTime()));
        current.setMonth(dateFormatMonth.format(calendar.getTime()));

        List<Calendar> matching = dbHelper.getCalendarDao().queryForMatching(current);

        if (matching.size() != 1)
            return null;

        return matching.get(0);
    }

    /**
     *
     * @return
     */
    protected Network getCurrentNetwork() {
        Network network = new Network();

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            if (connectionInfo != null && !connectionInfo.getSSID().isEmpty()) {
                network.setName(connectionInfo.getSSID());
                network.setName("wifi");
            }
        } else {
            network.setName("local");
            network.setType("offline");
        }

        return network;
    }

}
