package com.natercio.myhome.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/18/13
 * Time: 4:16 PM
 */
public class ORMMyHomeDBHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "myhome";

    private Dao<Calendar, Integer> calendarDao = null;
    //private RuntimeExceptionDao<Calendar, Integer> calendarRuntimeExceptionDao = null;
    private Dao<Event, Integer> eventDao = null;
    //private RuntimeExceptionDao<Event, Integer> eventRuntimeExceptionDao = null;
    private Dao<Fact, Integer> factDao = null;
    //private RuntimeExceptionDao<Fact, Integer> caseRuntimeExceptionDao = null;
    private Dao<Similarity, Integer> similarityDao = null;

    public ORMMyHomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //reset();
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

    public Dao<Fact, Integer> getFactDao() throws SQLException {
        if (factDao == null)
            factDao = getDao(Fact.class);

        return factDao;
    }

    public Dao<Similarity, Integer> getSimilarityDao() throws SQLException {
        if (similarityDao == null)
            similarityDao = getDao(Similarity.class);

        return similarityDao;
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
        TableUtils.createTable(connectionSource, Fact.class);

        // 3# create loose tables
        TableUtils.createTable(connectionSource, Similarity.class);
    }

    /**
     *
     * @param connectionSource
     * @throws java.sql.SQLException
     */
    private void dropAllTables(ConnectionSource connectionSource) throws SQLException {
        // 1# remove star centers
        TableUtils.dropTable(connectionSource, Fact.class, false);

        // 2# remove star arms
        TableUtils.dropTable(connectionSource, Calendar.class, false);
        TableUtils.dropTable(connectionSource, Event.class, false);

        // 3# remove loose tables
        TableUtils.dropTable(connectionSource, Similarity.class, false);
    }

    /**
     * Insert default data into the database
     */
    private void populateAllTables () throws SQLException {
        populateCalendarTable();

        //populateSimilarityTable();
    }

    private void populateCalendarTable() throws SQLException {
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

    private void populateSimilarityTable() throws SQLException {
        List<String> similarValues = new ArrayList<>();
        Dao<Calendar, Integer> calendars = getCalendarDao();

        Cursor c = getReadableDatabase().query(true, Calendar.TABLE_NAME, new String[] {"month"}, null, null, null, null, null, null);

        while (c.moveToNext())
            similarValues.add(c.getString(0));

        createCosineSimilarities(Calendar.TABLE_NAME, "month", similarValues);

        c = getReadableDatabase().query(true, Calendar.TABLE_NAME, new String[] {"weekday"}, null, null, null, null, null, null);

    }

    private void createCosineSimilarities(String table, String column, List<String> list) throws SQLException {
        Dao<Similarity, Integer> similarities = getSimilarityDao();
        Similarity similarity = new Similarity();
        similarity.setTable(table);
        similarity.setColumn(column);

        for (int i = 0; i < list.size(); i++) {
            similarity.setValue_a(list.get(i));
            for (int j = 1; j < list.size(); j++) {
                similarity.setValue_b(list.get((i + j) % list.size()));
                similarity.setSimiliraty(Math.cos(j/list.size()));
                similarities.create(similarity);
            }
        }
    }

    private void reset() {
        try {
            dropAllTables(getConnectionSource());
            createAllTables(getConnectionSource());
            populateAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
