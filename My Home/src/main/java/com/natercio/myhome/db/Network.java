package com.natercio.myhome.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by natercio on 12/13/13.
 */
@DatabaseTable(tableName = Network.TABLE_NAME)
public class Network {

    public static final String TABLE_NAME = "network";

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String type;

    //////////////////
    // CONSTRUCTORS //
    //////////////////

    public Network() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
