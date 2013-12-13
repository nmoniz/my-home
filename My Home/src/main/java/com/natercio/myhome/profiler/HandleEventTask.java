package com.natercio.myhome.profiler;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.natercio.myhome.db.Calendar;
import com.natercio.myhome.db.Event;
import com.natercio.myhome.db.Fact;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/27/13
 * Time: 1:14 PM
 */
public class HandleEventTask extends MyHomeDBTask {

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

            Calendar currentCalendar = getCurrentCalendar();

            Dao<Fact, Integer> facts = dbHelper.getFactDao();

            if (matchingEvents.isEmpty()) {
                events.create(event);
                matchingEvents = events.queryForMatching(event);

                Fact fact = new Fact();
                fact.setFkCalendar(currentCalendar);
                fact.setFkEvent(matchingEvents.get(0));
                facts.create(fact);
            } else {
                UpdateBuilder<Fact, Integer> updateBuilder = facts.updateBuilder();
                updateBuilder.updateColumnExpression("reinforcement", "reinforcement+1");
                updateBuilder.where().eq("fkEvent_id", matchingEvents.get(0).getId()).and().eq("fkCalendar_id", currentCalendar.getId());
                updateBuilder.update();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
