package com.natercio.myhome.db;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 7/25/13
 * Time: 6:28 PM
 */
public class MyHomeDB {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "myhome";

    ////////////////////
    // PUBLIC CLASSES //
    ////////////////////

    public static class Calendar {

        public static final String TABLE_NAME = "calendar";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_WEEKDAY = "weekday";
        public static final String COLUMN_MONTH = "month";

        public static final String STATEMENT_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID +         " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOUR +       " TEXT, " +
                COLUMN_WEEKDAY +    " TEXT, " +
                COLUMN_MONTH +      " TEXT )";
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String CREATE_TRIGGER_INSERT = "CREATE TRIGGER " + TABLE_NAME + "_ins_trg" +
                " AFTER INSERT ON " + TABLE_NAME +
                " BEGIN " +
                "UPDATE " + MetaTable.TABLE_NAME + " SET " +
                MetaTable.COLUMN_LASTINSERT + "=datetime('now'), " +
                MetaTable.COLUMN_TOTALROWS + "=" + MetaTable.COLUMN_TOTALROWS + "+1 " +
                "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";
        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER " + TABLE_NAME + "_upd_trg" +
                " AFTER UPDATE ON " + TABLE_NAME +
                " BEGIN " +
                "UPDATE " + MetaTable.TABLE_NAME + " SET " + MetaTable.COLUMN_LASTUPDATE + "=datetime('now') " +
                "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";

        private int id;
        private String hour;
        private String weekday;
        private String month;

        ////////////////////
        // PUBLIC METHODS //
        ////////////////////

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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            return o instanceof Calendar && ((Calendar) o).getId() == id;
        }
    }

    public static class Location {

        public static final String TABLE_NAME = "location";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_POSTAL_CODE = "postal_code";

        public static final String STATEMENT_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID +             " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS +        " TEXT, " +
                COLUMN_POSTAL_CODE +    " TEXT)";
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;


        private int id;
        private String address;
        private String postalCode;

        ////////////////////
        // PUBLIC METHODS //
        ////////////////////

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
    }

    public static class Network {

        public static final String TABLE_NAME = "network";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";

        public static final String STATEMENT_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID +     " INTEGER PRIMARY KEY," +
                COLUMN_NAME +   " TEXT)";
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String CREATE_TRIGGER_INSERT = "CREATE TRIGGER " + TABLE_NAME + "_ins_trg" +
                " AFTER INSERT ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " +
                        MetaTable.COLUMN_LASTINSERT + "=datetime('now'), " +
                        MetaTable.COLUMN_TOTALROWS + "=" + MetaTable.COLUMN_TOTALROWS + "+1 " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";
        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER " + TABLE_NAME + "_upd_trg" +
                " AFTER UPDATE ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " + MetaTable.COLUMN_LASTUPDATE + "=datetime('now') " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";


        private int id;
        private String name;

        ////////////////////
        // PUBLIC METHODS //
        ////////////////////

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
    }

    public static class Event {

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";

        public static final String STATEMENT_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID +         " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME +       " TEXT)";
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String CREATE_TRIGGER_INSERT = "CREATE TRIGGER " + TABLE_NAME + "_ins_trg" +
                " AFTER INSERT ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " +
                        MetaTable.COLUMN_LASTINSERT + "=datetime('now'), " +
                        MetaTable.COLUMN_TOTALROWS + "=" + MetaTable.COLUMN_TOTALROWS + "+1 " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";
        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER " + TABLE_NAME + "_upd_trg" +
                " AFTER UPDATE ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " + MetaTable.COLUMN_LASTUPDATE + "=datetime('now') " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";

        private int id;
        private String name;

        ////////////////////
        // PUBLIC METHODS //
        ////////////////////

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
    }

    public static class Fact {

        public static final String TABLE_NAME = "fact";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_REINFORCEMENT = "reinforcement";
        public static final String COLUMN_FK_CALENDAR = Calendar.TABLE_NAME + "_" + Calendar.COLUMN_ID;
        public static final String COLUMN_FK_LOCATION = Location.TABLE_NAME + "_" + Location.COLUMN_ID;
        public static final String COLUMN_FK_NETWORK = Network.TABLE_NAME + "_" + Network.COLUMN_ID;
        public static final String COLUMN_FK_EVENT = Event.TABLE_NAME + "_" + Event.COLUMN_ID;

        public static final String STATEMENT_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID +             " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_REINFORCEMENT +  " INTEGER DEFAULT 1, " +
                COLUMN_FK_CALENDAR +    " INTEGER, " +
                COLUMN_FK_LOCATION +    " INTEGER, " +
                COLUMN_FK_NETWORK +     " INTEGER, " +
                COLUMN_FK_EVENT +       " INTEGER, " +
                "CONSTRAINT " + Fact.COLUMN_FK_CALENDAR + "_cns FOREIGN KEY (" + COLUMN_FK_CALENDAR + ") " + " REFERENCES " + Calendar.TABLE_NAME + "(" + Calendar.COLUMN_ID + "))";
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String CREATE_TRIGGER_INSERT = "CREATE TRIGGER " + TABLE_NAME + "_ins_trg" +
                " AFTER INSERT ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " +
                        MetaTable.COLUMN_LASTINSERT + "=datetime('now'), " +
                        MetaTable.COLUMN_TOTALROWS + "=" + MetaTable.COLUMN_TOTALROWS + "+1 " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";
        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER " + TABLE_NAME + "_upd_trg" +
                " AFTER UPDATE ON " + TABLE_NAME +
                " BEGIN " +
                    "UPDATE " + MetaTable.TABLE_NAME + " SET " + MetaTable.COLUMN_LASTUPDATE + "=datetime('now') " +
                        "WHERE " + MetaTable.COLUMN_TABLENAME + "='" + TABLE_NAME + "';" +
                " END";

        private int id;
        private int reinforcement;
        private int fkCalendar;
        private int fkLocation;
        private int fkNetwork;
        private int fkEvent;

        ////////////////////
        // PUBLIC METHODS //
        ////////////////////

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

        public int getFkCalendar() {
            return fkCalendar;
        }

        public void setFkCalendar(int fkCalendar) {
            this.fkCalendar = fkCalendar;
        }

        public int getFkLocation() {
            return fkLocation;
        }

        public void setFkLocation(int fkLocation) {
            this.fkLocation = fkLocation;
        }

        public int getFkNetwork() {
            return fkNetwork;
        }

        public void setFkNetwork(int fkNetwork) {
            this.fkNetwork = fkNetwork;
        }

        public int getFkEvent() {
            return fkEvent;
        }

        public void setFkEvent(int fkEvent) {
            this.fkEvent = fkEvent;
        }
    }

    public static class Similarity {

    }

    public static class MetaTable {

        public static final String TABLE_NAME = "metadata";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TABLENAME = "table_name";
        public static final String COLUMN_LASTINSERT = "last_insert";
        public static final String COLUMN_LASTUPDATE = "last_update";
        public static final String COLUMN_TOTALROWS = "total_rows";

        //Create query
        public static final String STATEMENT_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID +         " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TABLENAME +  " TEXT UNIQUE ON CONFLICT ABORT, " +
                COLUMN_LASTINSERT + " TEXT, " +
                COLUMN_LASTUPDATE + " TEXT, " +
                COLUMN_TOTALROWS +  " INTEGER DEFAULT 0 )";

        //Drop query
        public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    /////////////////////
    // PRIVATE CLASSES //
    /////////////////////

    private static class Table {

        public static String TABLE_NAME;

        public static String COLUMN_ID;
    }
}
