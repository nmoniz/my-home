package com.natercio.myhome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 7/25/13
 * Time: 6:14 PM
 */
public class MyHomeDBHelper extends SQLiteOpenHelper {

    public MyHomeDBHelper(Context context) {
        super(context, MyHomeDB.DATABASE_NAME, null, MyHomeDB.DATABASE_VERSION);

        //resetData(getWritableDatabase());
    }


    ////////////////////
    // PUBLIC METHODS //
    ////////////////////

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createAllTables(sqLiteDatabase);

        initDatabase(sqLiteDatabase);

        populateAllTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //the current upgrade db procedure will reset everything in the database

        dropAllTables(sqLiteDatabase);

        onCreate(sqLiteDatabase);
    }

    public long insertIntoCalendar(MyHomeDB.Calendar calendar) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MyHomeDB.Calendar.COLUMN_HOUR, calendar.getHour());
        values.put(MyHomeDB.Calendar.COLUMN_WEEKDAY, calendar.getWeekday());
        values.put(MyHomeDB.Calendar.COLUMN_MONTH, calendar.getMonth());

        return db.insert(MyHomeDB.Calendar.TABLE_NAME, "null", values);
    }

    public ArrayList<MyHomeDB.Calendar> selectFromCalendar(String whereClause, String [] whereArgs) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                MyHomeDB.Calendar.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        if (c == null || 0 > c.getCount()) {
            return null;
        } else {
            ArrayList<MyHomeDB.Calendar> result = new ArrayList<>(c.getCount());

            c.moveToFirst();
            while (!c.isAfterLast()) {
                MyHomeDB.Calendar row = new MyHomeDB.Calendar();

                row.setId(c.getInt(c.getColumnIndex(MyHomeDB.Calendar.COLUMN_ID)));
                row.setHour(c.getString(c.getColumnIndex(MyHomeDB.Calendar.COLUMN_HOUR)));
                row.setWeekday(c.getString(c.getColumnIndex(MyHomeDB.Calendar.COLUMN_WEEKDAY)));
                row.setMonth(c.getString(c.getColumnIndex(MyHomeDB.Calendar.COLUMN_MONTH)));

                result.add(row);

                c.moveToNext();
            }

            return result;
        }
    }

    public long insertIntoEvent(MyHomeDB.Event event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MyHomeDB.Event.COLUMN_NAME, event.getName());

        return db.insert(MyHomeDB.Event.TABLE_NAME, "null", values);
    }

    public ArrayList<MyHomeDB.Event> selectFromEvent(String whereClause, String [] whereArgs) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                MyHomeDB.Event.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        if (c == null || 0 > c.getCount()) {
            return null;
        } else {
            ArrayList<MyHomeDB.Event> result = new ArrayList<>(c.getCount());

            c.moveToFirst();
            while (!c.isAfterLast()) {
                MyHomeDB.Event row = new MyHomeDB.Event();

                row.setId(c.getInt(c.getColumnIndex(MyHomeDB.Event.COLUMN_ID)));
                row.setName(c.getString(c.getColumnIndex(MyHomeDB.Event.COLUMN_NAME)));

                result.add(row);

                c.moveToNext();
            }

            return result;
        }
    }

    public long insertIntoFact(MyHomeDB.Fact fact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MyHomeDB.Fact.COLUMN_FK_CALENDAR, fact.getFkCalendar());
        values.put(MyHomeDB.Fact.COLUMN_FK_EVENT, fact.getFkEvent());

        return db.insert(MyHomeDB.Event.TABLE_NAME, "null", values);
    }

    public void updateFacts(MyHomeDB.Calendar calendar, MyHomeDB.Event event) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = MyHomeDB.Fact.COLUMN_FK_CALENDAR + "=? AND " +
                MyHomeDB.Fact.COLUMN_FK_EVENT + "=?";
        String[] whereArgs = {String.valueOf(calendar.getId()),
                String.valueOf(event.getId())};

        Cursor c = db.query(
                MyHomeDB.Fact.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        if (c == null)
            return;

        ContentValues values = new ContentValues();

        if (c.getCount() == 0) {
            MyHomeDB.Fact fact = new MyHomeDB.Fact();

            fact.setFkCalendar(calendar.getId());
            fact.setFkEvent(event.getId());

            values.put(MyHomeDB.Fact.COLUMN_FK_CALENDAR, fact.getFkCalendar());
            values.put(MyHomeDB.Fact.COLUMN_FK_EVENT, fact.getFkEvent());

            db.insert(MyHomeDB.Fact.TABLE_NAME, "null", values);
        } else {
            c.moveToFirst();

            values.put(MyHomeDB.Fact.COLUMN_REINFORCEMENT, c.getInt(c.getColumnIndex(MyHomeDB.Fact.COLUMN_REINFORCEMENT)) + 1);

            Log.d("xxx", c.getString(c.getColumnIndex(MyHomeDB.Fact.COLUMN_REINFORCEMENT)));

            db.update(MyHomeDB.Fact.TABLE_NAME,
                    values,
                    MyHomeDB.Fact.COLUMN_ID + "=?",
                    new String[]{c.getString(c.getColumnIndex(MyHomeDB.Fact.COLUMN_ID))});
        }
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    private void createAllTables(SQLiteDatabase db) {
        // 1# create star arms
        db.execSQL(MyHomeDB.Calendar.STATEMENT_CREATE);
        db.execSQL(MyHomeDB.Event.STATEMENT_CREATE);
        //db.execSQL(MyHomeDB.Location.STATEMENT_CREATE);
        db.execSQL(MyHomeDB.Network.STATEMENT_CREATE);

        // 2# create star centers
        db.execSQL(MyHomeDB.Fact.STATEMENT_CREATE);

        // 3# create metadata table
        db.execSQL(MyHomeDB.MetaTable.STATEMENT_CREATE);
    }

    private void dropAllTables(SQLiteDatabase db) {

        // 1# remove star centers
        db.execSQL(MyHomeDB.Fact.STATEMENT_DROP);

        // 2# remove star arms
        db.execSQL(MyHomeDB.Calendar.STATEMENT_DROP);
        db.execSQL(MyHomeDB.Event.STATEMENT_DROP);
        //db.execSQL(MyHomeDB.Location.STATEMENT_DROP);
        db.execSQL(MyHomeDB.Network.STATEMENT_DROP);

        // 3# remove metadata table
        db.execSQL(MyHomeDB.MetaTable.STATEMENT_DROP);
    }

    /**
     * Initialize metadata tables and create all triggers
     */
    private void initDatabase(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // 1# populate metadata table

        Cursor c = db.rawQuery("SELECT tbl_name FROM sqlite_master WHERE type='table' AND tbl_name!='" + MyHomeDB.MetaTable.TABLE_NAME + "'", null);

        if (c.moveToFirst())
        {
            while ( !c.isAfterLast() ){
                values.put(MyHomeDB.MetaTable.COLUMN_TABLENAME, c.getString(c.getColumnIndex("tbl_name")));
                db.insert(MyHomeDB.MetaTable.TABLE_NAME, "null", values);

                c.moveToNext();
            }
        }

        // 2# create triggers

        db.execSQL(MyHomeDB.Fact.CREATE_TRIGGER_INSERT);
        db.execSQL(MyHomeDB.Fact.CREATE_TRIGGER_UPDATE);

        db.execSQL(MyHomeDB.Calendar.CREATE_TRIGGER_INSERT);
        db.execSQL(MyHomeDB.Calendar.CREATE_TRIGGER_UPDATE);

        db.execSQL(MyHomeDB.Event.CREATE_TRIGGER_INSERT);
        db.execSQL(MyHomeDB.Event.CREATE_TRIGGER_UPDATE);
    }

    /**
     * Insert default data into the database
     */
    private void populateAllTables(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // populate Calendar table
        ArrayList<String> weekDays = new ArrayList<>(7);
        weekDays.add("Mon");		weekDays.add("Tue");
        weekDays.add("Wed");		weekDays.add("Thu");
        weekDays.add("Fri");		weekDays.add("Sat");
        weekDays.add("Sun");

        ArrayList<String> months = new ArrayList<>(12);
        months.add("Jan");          months.add("Feb");
        months.add("Mar");          months.add("Apr");
        months.add("May");          months.add("Jun");
        months.add("Jul");          months.add("Aug");
        months.add("Sep");          months.add("Oct");
        months.add("Nov");          months.add("Dec");

        values.clear();
        for (String m : months) {
            values.put(MyHomeDB.Calendar.COLUMN_MONTH, m);
            for (String w : weekDays) {
                values.put(MyHomeDB.Calendar.COLUMN_WEEKDAY, w);
                for (int h = 0;  h < 24; h++) {
                    values.put(MyHomeDB.Calendar.COLUMN_HOUR, String.valueOf(h));
                    db.insert(MyHomeDB.Calendar.TABLE_NAME, "null", values);
                }
            }
        }
    }

    private void resetData(SQLiteDatabase db) {
        //1# clear star centers
        db.execSQL("DELETE FROM " + MyHomeDB.Fact.TABLE_NAME);

        //2# clear star arms
        db.execSQL("DELETE FROM " + MyHomeDB.Event.TABLE_NAME);
    }

}
