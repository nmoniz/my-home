package com.natercio.myhome.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/18/13
 * Time: 4:00 PM
 */
@DatabaseTable(tableName = "calendar")
public class Calendar {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(canBeNull = false)
    String hour;

    @DatabaseField(canBeNull = false)
    String weekday;

    @DatabaseField(canBeNull = false)
    String month;

    //////////////////
    // CONSTRUCTORS //
    //////////////////

    public Calendar() {
    }

    ///////////////////////
    // GETTERS & SETTERS //
    ///////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
