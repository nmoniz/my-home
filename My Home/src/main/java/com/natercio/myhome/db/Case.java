package com.natercio.myhome.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/21/13
 * Time: 6:49 PM
 */
@DatabaseTable(tableName = "case")
public class Case {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    int reinforcement;

    @DatabaseField(canBeNull = false, foreign = true)
    Calendar fkCalendar;

    @DatabaseField(canBeNull = false, foreign = true)
    Event fkEvent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReinforcement() {
        return reinforcement;
    }

    public void setReinforcement(int reinforcement) {
        this.reinforcement = reinforcement;
    }

    public Calendar getFkCalendar() {
        return fkCalendar;
    }

    public void setFkCalendar(Calendar fkCalendar) {
        this.fkCalendar = fkCalendar;
    }

    public Event getFkEvent() {
        return fkEvent;
    }

    public void setFkEvent(Event fkEvent) {
        this.fkEvent = fkEvent;
    }
}
