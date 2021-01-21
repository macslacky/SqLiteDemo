package info.ribosoft.sqlitedemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManageContactActivity extends AppCompatActivity {
    private DBHelper myDB;
    EditText editTitolo, editDescrizione;
    TextView textAppData, textAppModifica, textAppModData, textData;
    DatePicker pickerData;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);

        editTitolo = (EditText) findViewById(R.id.editTextTitolo);
        textAppData = (TextView) findViewById(R.id.textAppData);
        editDescrizione = (EditText) findViewById(R.id.editTextDescrizione);
        textAppModifica = (TextView) findViewById(R.id.textAppModifica);
        textAppModData = (TextView) findViewById(R.id.textAppModData);
        textData = (TextView) findViewById(R.id.textData);
        pickerData = (DatePicker) findViewById(R.id.datePickerData);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        // create a reference to the class that manages the database
        myDB = new DBHelper(this);

        // return the intent that started this activity
        Intent intent = getIntent();
        // retrieve extended data from the intent
        int extras = intent.getIntExtra("id", 0);

        if (extras == 0) {
            // prepares the layout for inserting the record
            textAppData.setVisibility(View.INVISIBLE);
            textAppModifica.setVisibility(View.INVISIBLE);
            textAppModData.setVisibility(View.INVISIBLE);
            textData.setVisibility(View.VISIBLE);
            pickerData.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
        } else {
            // prepares the layout for editing or deleting the record
            textAppData.setVisibility(View.VISIBLE);
            textAppModifica.setVisibility(View.VISIBLE);
            textAppModData.setVisibility(View.VISIBLE);
            textData.setVisibility(View.INVISIBLE);
            pickerData.setVisibility(View.INVISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);

            // read the record sent by id
            Cursor cursorRec =  myDB.getData(extras);

            // return the required column values as a string
            String titolo = cursorRec.getString(cursorRec.getColumnIndex
                    (DBHelper.CONTACTS_COLUMN_TITOLO));
            String data = cursorRec.getString(cursorRec.getColumnIndex
                    (DBHelper.CONTACTS_COLUMN_DATA));
            String descrizione = cursorRec.getString(cursorRec.getColumnIndex
                    (DBHelper.CONTACTS_COLUMN_DESCRIZIONE));
            String aggiornamento = cursorRec.getString(cursorRec.getColumnIndex
                    (DBHelper.CONTACTS_COLUMN_TIMESTAMP));

            // sets the texts to be displayed using a string resource identifier
            editTitolo.setText(titolo);
            textAppData.setText(data);
            editDescrizione.setText(descrizione);
            textAppModData.setText(aggiornamento);
        }
    }

    // writes the data of the new record
    public void saveRecord(View view) {
        String stringData;
        String sDay, sMonth, sYear;
        Integer day, month, year;

        // returns the primitive int value for the day-of-month
        day = pickerData.getDayOfMonth();
        sDay = day.toString();
        if (sDay.length() == 1) sDay = "0" + sDay;
        // returns the primitive int value for the month
        month = 1 + pickerData.getMonth();
        sMonth = month.toString();
        if (sMonth.length() == 1) sMonth = "0" + sMonth;
        // return the primitive int value for the year
        year = pickerData.getYear();
        stringData = year + "-" + sMonth + "-" + sDay;
        // writes the data of the new record
        if (myDB.insertContact(editTitolo.getText().toString(),
                editDescrizione.getText().toString(), stringData)) {
            Toast.makeText(getApplicationContext(), "Salvato", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "ERRORE: salvataggio",
                    Toast.LENGTH_SHORT).show();
        }
        // start the activity MainActivity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    // create a menu in the activity
    public boolean onCreateOptionsMenu(Menu manageMenu) {
        // inflate your menu resource
        getMenuInflater().inflate(R.menu.manage_menu, manageMenu);
        return true;
    }

    @Override
    // the user has selected a menu item
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);

        // // return the intent that started this activity
        Intent intentGet = getIntent();
        // retrieve extended data from the intent
        int extras = intentGet.getIntExtra("id", 0);

        // finds an element present in the layout and assigns it to a variable
        editTitolo = (EditText) findViewById(R.id.editTextTitolo);
        textAppData = (TextView) findViewById(R.id.textAppData);
        editDescrizione = (EditText) findViewById(R.id.editTextDescrizione);

        // create a reference to the class that manages the database
        myDB = new DBHelper(this);

        // menu item selected by the user
        switch (menuItem.getItemId()) {
            case R.id.modRec: // updates the record with the new values
                if(myDB.updateContact(extras, editTitolo.getText().toString(),
                        editDescrizione.getText().toString(), textAppData.getText().toString())) {
                    Toast.makeText(this, "Dati modificati", Toast.LENGTH_SHORT).show();
                    // // start the activity MainActivity
                    Intent intentAct = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentAct);
                }
                return true;
            case R.id.delRec: // deletes the indicated record
                /* displays a pop-up with two buttons,
                   if the user confirms or cancels the request to delete the record */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact).setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(myDB.deleteContact(extras)) {
                            Toast.makeText(getApplicationContext(), "Cancellato il Record", Toast.LENGTH_SHORT).show();
                            // start the activity MainActivity
                            Intent intentAct = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intentAct);
                        }
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel the request
                    }
                });
                AlertDialog d = builder.create();
                d.setTitle("Sei sicuro?");
                d.show();
                return true;
            case R.id.listRec:
                // start the activity MainActivity
                Intent intentAct = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentAct);
                return true;
            default:
                Toast.makeText(this, "******DEFAULT******", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

}