package com.natercio.myhome.profiler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.natercio.myhome.db.Event;
import com.natercio.myhome.db.Fact;

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
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (db != null) {
            Cursor c = db.query(
                    Event.TABLE_NAME+", "+ Fact.TABLE_NAME,
                    new String [] {"what", "SUM(reinforcement)"},
                    Event.TABLE_NAME+".id=fkEvent_id",
                    null,
                    "fkEvent_id",
                    null,
                    "SUM(reinforcement) DESC",
                    String.valueOf(count)
            );

            if (c == null || c.getCount() <= 0)
                return;

            while (c.moveToNext()) {
                frequent.add(c.getString(c.getColumnIndex("what")));
            }
        }
    }
}
