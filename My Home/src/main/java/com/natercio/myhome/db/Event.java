package com.natercio.myhome.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/22/13
 * Time: 8:17 PM
 */
@DatabaseTable(tableName = Event.TABLE_NAME)
public class Event {

    public static final String TABLE_NAME = "event";

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(canBeNull = false)
    String action;

    @DatabaseField(canBeNull = false)
    String what;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }
}
