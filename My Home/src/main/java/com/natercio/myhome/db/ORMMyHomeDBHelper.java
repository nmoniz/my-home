package com.natercio.myhome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.natercio.myhome.R;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/18/13
 * Time: 4:16 PM
 */
public class ORMMyHomeDBHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "myhome2";

    private Dao<Calendar, Integer> calendarDao = null;
    private RuntimeExceptionDao<Calendar, Integer> calendarRuntimeExceptionDao = null;
    private Dao<Event, Integer> eventDao = null;
    private RuntimeExceptionDao<Event, Integer> eventRuntimeExceptionDao = null;
    private Dao<Case, Integer> caseDao = null;
    private RuntimeExceptionDao<Case, Integer> caseRuntimeExceptionDao = null;

    public ORMMyHomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    ////////////////////
    // PUBLIC METHODS //
    ////////////////////

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            createAllTables(connectionSource);
            populateAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            dropAllTables(connectionSource);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Calendar, Integer> getCalendarDao() throws SQLException {
        if (calendarDao == null)
            calendarDao = getDao(Calendar.class);

        return calendarDao;
    }

    public Dao<Event, Integer> getEventDao() throws SQLException {
        if (eventDao == null)
            eventDao = getDao(Event.class);

        return eventDao;
    }

    public Dao<Case, Integer> getCaseDao() throws SQLException {
        if (caseDao == null)
            caseDao = getDao(Case.class);

        return caseDao;
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    /**
     *
     * @param connectionSource
     * @throws java.sql.SQLException
     */
    private void createAllTables(ConnectionSource connectionSource) throws SQLException {
        // 1# create star arms
        TableUtils.createTable(connectionSource, Calendar.class);
        TableUtils.createTable(connectionSource, Event.class);

        // 2# create star centers
        TableUtils.createTable(connectionSource, Case.class);

        // 3# create metadata table
    }

    /**
     *
     * @param connectionSource
     * @throws java.sql.SQLException
     */
    private void dropAllTables(ConnectionSource connectionSource) throws SQLException {

        // 1# remove star centers
        TableUtils.dropTable(connectionSource, Case.class, false);

        // 2# remove star arms
        TableUtils.dropTable(connectionSource, Calendar.class, false);
        TableUtils.dropTable(connectionSource, Event.class, false);

        // 3# remove metadata table
    }

    /**
     * Insert default data into the database
     */
    private void populateAllTables () throws SQLException {
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

        Dao<Calendar, Integer> calendars = getCalendarDao();
        Calendar calendar = new Calendar();

        for (String m : months) {
            calendar.setMonth(m);
            for (String w : weekDays) {
                calendar.setWeekday(w);
                for (int h = 0;  h < 24; h++) {
                    calendar.setHour(String.valueOf(h));
                    calendars.create(calendar);
                }
            }
        }
    }
}
