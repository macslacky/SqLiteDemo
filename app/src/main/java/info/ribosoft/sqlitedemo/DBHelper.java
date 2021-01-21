package info.ribosoft.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "sqlnote";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_TITOLO = "titolo";
    public static final String CONTACTS_COLUMN_DESCRIZIONE = "descrizione";
    public static final String CONTACTS_COLUMN_DATA = "data";
    public static final String CONTACTS_COLUMN_TIMESTAMP = "timestamp";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLDB) {
        /* executes a single sql statement that returns no data,
           in this case the table is created if it doesn't already exist */
        sqLDB.execSQL("CREATE TABLE " + CONTACTS_TABLE_NAME + "(" +
                CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CONTACTS_COLUMN_TITOLO + " TEXT, " +
                CONTACTS_COLUMN_DESCRIZIONE + " TEXT, " +
                CONTACTS_COLUMN_DATA + " DATE, " +
                CONTACTS_COLUMN_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLDB, int oldV, int newV) {
        // Update the database if necessary
        sqLDB.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(sqLDB);
    }

    // read the record sent by id
    public Cursor getData(int id) {
        // opens the database for reading
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        String stringSql = "SELECT * FROM " + CONTACTS_TABLE_NAME + " WHERE id = " + id;
        // performs a read sql query
        Cursor cursorRec = sqlDB.rawQuery(stringSql, null);
        // move the cursor to the first row
        cursorRec.moveToFirst();
        return cursorRec;
    }

    // read the entire table and insert the indicated field into a list
    public ArrayList<String> getAllDati(String sCampo) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String stringCursor;
        // opens the database for reading
        SQLiteDatabase sqlDB = this.getReadableDatabase();

        String stringSql = "SELECT * FROM " + CONTACTS_TABLE_NAME +
                " ORDER BY " + CONTACTS_COLUMN_DATA + " ASC";
        // performs a read sql query
        Cursor cursor = sqlDB.rawQuery(stringSql, null);

        // move the cursor to the first row
        cursor.moveToFirst();
        // returns whether the cursor is pointing to the position after the last row
        while (cursor.isAfterLast() == false) {
            // returns the value of the requested column as a string
            stringCursor = cursor.getString(cursor.getColumnIndex(sCampo));
            // adds the string to the list
            arrayList.add(stringCursor);
            // move the cursor to the next row
            cursor.moveToNext();
        }
        return arrayList;
    }

    // writes the data of the new record
    public boolean insertContact(String titolo, String descrizione, String data) {
        // opens the database for writing
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        // stores the various values of the fields
        ContentValues contentValues = new ContentValues();
        contentValues.put("titolo", titolo);
        contentValues.put("descrizione", descrizione);
        contentValues.put("data", data);
        // insert data into the database
        sqlDB.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    // updates the record with the new values
    public boolean updateContact(Integer id, String titolo, String descrizione, String data) {
        // opens the database for writing
        SQLiteDatabase myDB = this.getWritableDatabase();
        // stores the various values of the fields
        ContentValues contentValues = new ContentValues();
        contentValues.put("titolo", titolo);
        contentValues.put("descrizione", descrizione);
        contentValues.put("data", data);
        // updates the record with the new values
        myDB.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ",
                new  String[] {Integer.toString(id)});
        return true;
    }

    // deletes the indicated record
    public boolean deleteContact(Integer id) {
        // opens the database for writing
        SQLiteDatabase myDB = this.getWritableDatabase();
        // deletes the indicated record
        return (myDB.delete(CONTACTS_TABLE_NAME, "id = ? ",
                new String[] {Integer.toString(id)}) != 0);
    }

}
