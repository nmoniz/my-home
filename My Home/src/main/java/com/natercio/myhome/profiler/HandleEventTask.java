package com.natercio.myhome.profiler;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.natercio.myhome.db.Calendar;
import com.natercio.myhome.db.Case;
import com.natercio.myhome.db.Event;
import com.natercio.myhome.utils.MyHomeLog;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/27/13
 * Time: 1:14 PM
 */
public class HandleEventTask extends MyHomeDBTask {

    private static final String LOG_FLAG = "HandleEventTask";

    Event event;

    public HandleEventTask(Context context, Event event) {
        super(context);
        this.event = event;
    }

    ///////////////////////
    // PROTECTED METHODS //
    ///////////////////////


    @Override
    protected void task() {
        try {
            Dao<Event, Integer> events = dbHelper.getEventDao();
            List<Event> matchingEvents = events.queryForMatching(event);

            Dao<Calendar, Integer> calendars = dbHelper.getCalendarDao();
            List<Calendar> matchingCalendars = calendars.queryForMatching(getCurrentCalendar());

            Dao<Case, Integer> cases = dbHelper.getCaseDao();
            Case c = new Case();
            c.setFkCalendar(matchingCalendars.get(0));
            c.setFkEvent(event);

            if (matchingEvents.isEmpty()) {
                event.setId(events.create(event));
                cases.create(c);
            } else {
                UpdateBuilder<Case, Integer> updateBuilder = cases.updateBuilder();
                updateBuilder.updateColumnExpression("reinforcement", "reinforcement+1");
                updateBuilder.where().eq("fkEvent_id", matchingEvents.get(0).getId()).and().eq("fkCalendar_id", matchingCalendars.get(0).getId());
                updateBuilder.update();
            }

            MyHomeLog.LogInfo(this.getClass().getName(), "Event handled");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
