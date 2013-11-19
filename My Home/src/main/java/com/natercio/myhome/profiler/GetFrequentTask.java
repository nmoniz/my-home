package com.natercio.myhome.profiler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.natercio.myhome.db.MyHomeDB;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/28/13
 * Time: 2:47 PM
 */
public class GetFrequentTask extends MyHomeDBTask {

    private int count;

    private ArrayList<String> frequent;

    public GetFrequentTask(Context context, int count) {
        super(context);

        this.frequent = new ArrayList<>(count);
        this.count = count;
    }

    public ArrayList<String> getFrequent() {
        return frequent;
    }

    @Override
    protected void task() {
    }


    protected void old_task() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                MyHomeDB.Event.TABLE_NAME+", case",
                new String [] {MyHomeDB.Event.COLUMN_NAME, "SUM(case.reinforcement)"},
                MyHomeDB.Event.TABLE_NAME+"."+MyHomeDB.Event.COLUMN_ID+"=case.fkCalendar_id",
                null,
                "case.fkCalendar_id",
                null,
                "SUM(case.reinforcement) DESC",
                String.valueOf(count)
        );

        if (c == null || c.getCount() <= 0)
            return;

        while (c.moveToNext()) {
            frequent.add(c.getString(c.getColumnIndex(MyHomeDB.Event.COLUMN_NAME)));
        }
    }
}
