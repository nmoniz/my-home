package com.natercio.myhome.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by natercio on 11/21/13.
 */
@DatabaseTable(tableName = Similarity.TABLE_NAME)
public class Similarity {

    public static final String TABLE_NAME = "similarity";

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(canBeNull = false)
    String table;

    @DatabaseField(canBeNull = false)
    String column;

    @DatabaseField(canBeNull = false)
    String value_a;

    @DatabaseField(canBeNull = false)
    String value_b;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    double similiraty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue_a() {
        return value_a;
    }

    public void setValue_a(String value_a) {
        this.value_a = value_a;
    }

    public String getValue_b() {
        return value_b;
    }

    public void setValue_b(String value_b) {
        this.value_b = value_b;
    }

    public double getSimiliraty() {
        return similiraty;
    }

    public void setSimiliraty(double similiraty) {
        this.similiraty = similiraty;
    }
}