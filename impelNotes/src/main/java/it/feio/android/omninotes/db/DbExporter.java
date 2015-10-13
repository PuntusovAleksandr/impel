package it.feio.android.omninotes.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import roboguice.util.Ln;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DbExporter {

    private static final String DATASUBDIRECTORY = "exampledata";

    private SQLiteDatabase db;
    private XmlBuilder xmlBuilder;


    public DbExporter(SQLiteDatabase db) {
        this.db = db;
    }


    public void export(String dbName, String exportFileNamePrefix)
            throws IOException {
        Ln.i("exporting database - " + dbName
                + " exportFileNamePrefix=" + exportFileNamePrefix);

        this.xmlBuilder = new XmlBuilder();
        this.xmlBuilder.start(dbName);

        // get the tables
        String sql = "select * from sqlite_master";
        Cursor c = this.db.rawQuery(sql, new String[0]);
        Ln.d("select * from sqlite_master, cur size "
                + c.getCount());
        if (c.moveToFirst()) {
            do {
                String tableName = c.getString(c.getColumnIndex("name"));
                Ln.d("table name " + tableName);

                // skip metadata, sequence, and uidx (unique indexes)
                if (!tableName.equals("android_metadata")
                        && !tableName.equals("sqlite_sequence")
                        && !tableName.startsWith("uidx")) {
                    this.exportTable(tableName);
                }
            } while (c.moveToNext());
        }
        String xmlString = this.xmlBuilder.end();
        this.writeToFile(xmlString, exportFileNamePrefix + ".xml");
        Ln.i("exporting database complete");
    }


    private void exportTable(final String tableName) throws IOException {
        Ln.d("exporting table - " + tableName);
        this.xmlBuilder.openTable(tableName);
        String sql = "select * from " + tableName;
        Cursor c = this.db.rawQuery(sql, new String[0]);
        if (c.moveToFirst()) {
            int cols = c.getColumnCount();
            do {
                this.xmlBuilder.openRow();
                for (int i = 0; i < cols; i++) {
                    this.xmlBuilder.addColumn(c.getColumnName(i),
                            c.getString(i));
                }
                this.xmlBuilder.closeRow();
            } while (c.moveToNext());
        }
        c.close();
        this.xmlBuilder.closeTable();
    }


    private void writeToFile(String xmlString, String exportFileName)
            throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(),
                DATASUBDIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, exportFileName);
        file.createNewFile();

        ByteBuffer buff = ByteBuffer.wrap(xmlString.getBytes());
        FileChannel channel = new FileOutputStream(file).getChannel();
        try {
            channel.write(buff);
        } finally {
            if (channel != null)
                channel.close();
        }
    }


    /**
     * XmlBuilder is used to write XML tags (open and close, and a few
     * attributes) to a StringBuilder. Here we have nothing to do with IO or
     * SQL, just a fancy StringBuilder.
     *
     * @author ccollins
     */
    class XmlBuilder {

        private static final String OPEN_XML_STANZA = "";
        private static final String CLOSE_WITH_TICK = "'>";
        private static final String DB_OPEN = "<database name='";
        private static final String DB_CLOSE = "";
        private static final String TABLE_OPEN = "<table name='";
        private static final String TABLE_CLOSE = "";
        private static final String ROW_OPEN = "";
        private static final String ROW_CLOSE = "";
        private static final String COL_OPEN = "<col name='";
        private static final String COL_CLOSE = "";

        private final StringBuilder sb;


        public XmlBuilder() throws IOException {
            this.sb = new StringBuilder();
        }


        void start(String dbName) {
            this.sb.append(OPEN_XML_STANZA);
            this.sb.append(DB_OPEN + dbName + CLOSE_WITH_TICK);
        }


        String end() throws IOException {
            this.sb.append(DB_CLOSE);
            return this.sb.toString();
        }


        void openTable(String tableName) {
            this.sb.append(TABLE_OPEN + tableName + CLOSE_WITH_TICK);
        }


        void closeTable() {
            this.sb.append(TABLE_CLOSE);
        }


        void openRow() {
            this.sb.append(ROW_OPEN);
        }


        void closeRow() {
            this.sb.append(ROW_CLOSE);
        }


        void addColumn(final String name, final String val) throws IOException {
            this.sb.append(COL_OPEN + name + CLOSE_WITH_TICK + val + COL_CLOSE);
        }
    }
}