package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *  Activity which lets the user add a session
 */
public class AddSessionActivity extends Activity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private String location;
    private int game_type_ref;
    private int game_structure_ref;
    private int hoursPlayed = 0;
    private int minutesPlayed = 0;
    private int result;
    private Calendar calendar;

    //test to do this
    private ArrayList<String> addedGameTypes;
    DataSource dataSource;

    private Button durationPickButton;
    private Button datePickButton;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

    public DataSource getDataSource(){return dataSource;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_session);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        try {
            dataSource = new DataSource(this);
            dataSource.open();
        } catch (Exception e){
        }

        //Game type stuff
        ArrayList<String> gametypeslist = dataSource.getAllGameTypes();
        Spinner gameType_spinner = (Spinner)findViewById(R.id.gameType_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, gametypeslist);
        gameType_spinner.setAdapter(adapter);
        gameType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                game_type_ref = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Location stuff
        ArrayList<String> location_list = new ArrayList<String>();
        location_list.add("Pokerstars");
        location_list.add("Bill's place");
        location_list.add("<New>");

        Spinner location_Spinner = (Spinner)findViewById(R.id.location_spinner);
        ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, location_list);
        location_Spinner.setAdapter(location_adapter);
        location_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //big blind stuff
        ArrayList<String> game_structure_list = dataSource.getAllGameStructures();

        Spinner game_structure_spinner = (Spinner)findViewById(R.id.game_structure_spinner);
        ArrayAdapter<String> big_blind_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, game_structure_list);
        game_structure_spinner.setAdapter(big_blind_adapter);
        game_structure_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                game_structure_ref = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //duration pick stuff
        durationPickButton = (Button)findViewById(R.id.duration_pick_button);
        durationPickButton.setText(String.valueOf(hoursPlayed) + "h:" +
                String.valueOf(minutesPlayed) + "m");

        //date pick stuff
        calendar = Calendar.getInstance();
        datePickButton = (Button)findViewById(R.id.date_pick_button);
        datePickButton.setText(formatter.format(calendar.getTime()));

        //result stuff

    }

    @Override
    protected void onResume() {
        try { dataSource.open(); } catch (Exception e) { }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {dataSource.close();} catch (Exception e) {}
        super.onPause();
    }

    public void duration_pick_onClick(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                hoursPlayed, minutesPlayed, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int h, int m) {
        hoursPlayed = h;
        minutesPlayed = m;
        durationPickButton.setText(String.valueOf(hoursPlayed) + "h:" +
                String.valueOf(minutesPlayed) + "m");
    }

    public void date_pick_onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                0, 0, 0);
        datePickerDialog.getDatePicker().updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        datePickButton.setText(formatter.format(calendar.getTime()));
    }

    private Session createSession() {
        if (hoursPlayed == 0 && minutesPlayed == 0)
            return null;
        Session session = new Session();
        session.setGame_type_ref(game_type_ref);
        session.setLocation(location);
        session.setGame_structure_ref(game_structure_ref);
        session.setDuration(hoursPlayed*60 + minutesPlayed);
        session.setDate(calendar.getTime());
        EditText result_EditText = (EditText)findViewById(R.id.result_editText);
        result = Integer.valueOf(result_EditText.getText().toString());
        session.setResult(result);
        session.setGame_notes(""); //TODO
        return session;
    }

    //buttons
    public void cancel_onClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void add_onClick(View view) {
        Session resultsession = createSession();
        if (createSession() != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("session", resultsession);
            setResult(RESULT_OK, resultIntent);
            Log.d("MESS", "returning");
            finish();
        }
    }
}