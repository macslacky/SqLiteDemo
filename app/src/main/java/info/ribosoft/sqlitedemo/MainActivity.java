package info.ribosoft.sqlitedemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu;
import android.view.MenuInflater;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DBHelper myDB;
    private ListView listViewNote;

    TextView titolo, descrizione;
    DatePicker data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // create a reference to the class that manages the database
        myDB = new DBHelper(this);
        // read the entire table and insert the indicated field into a list
        ArrayList arrayListNote = myDB.getAllDati(DBHelper.CONTACTS_COLUMN_TITOLO);

        // converts an ArrayList of objects to display items loaded into the ListView container
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayListNote);
        listViewNote = (ListView) findViewById(R.id.listViewNote);
        listViewNote.setAdapter(arrayAdapter);

        // activates the listening of the click on the element
        listViewNote.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> arrayID = new ArrayList<>();
                // // read the entire table and insert the indicated field into a list
                arrayID = myDB.getAllDati(DBHelper.CONTACTS_COLUMN_ID);

                Intent intent = new Intent(getApplicationContext(), ManageContactActivity.class);
                // writes data extended by intent
                intent.putExtra("id", Integer.valueOf(arrayID.get(i)));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu mainMenu) {
        getMenuInflater().inflate(R.menu.main_menu, mainMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);

        switch (menuItem.getItemId()) {
            case R.id.newRec:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(), ManageContactActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}