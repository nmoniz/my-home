package com.natercio.myhome.profiler;

import android.content.Context;
import com.natercio.myhome.db.Calendar;
import com.natercio.myhome.db.ORMMyHomeDBHelper;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/28/13
 * Time: 11:07 AM
 */
public abstract class MyHomeDBTask extends ProfilerTask {

    protected ORMMyHomeDBHelper dbHelper;

    public MyHomeDBTask(Context context) {
        this.dbHelper = new ORMMyHomeDBHelper(context);
    }

    ///////////////////////
    // PROTECTED METHODS //
    ///////////////////////

    protected Calendar getCurrentCalendar() {
        Calendar current = new Calendar();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("H");
        SimpleDateFormat dateFormatWeekDay = new SimpleDateFormat("E");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM");

        current.setHour(dateFormatHour.format(calendar.getTime()).toString());
        current.setWeekday(dateFormatWeekDay.format(calendar.getTime()));
        current.setMonth(dateFormatMonth.format(calendar.getTime()));

        return current;
    }

}
