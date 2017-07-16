package se.torgammelgard.pokertrax;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *  Activity which lets the user add a session
 */
public class AddSessionActivity extends Activity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        LocationDialogFragment.LocationDialogListener,
        GameStructureDialogFragment.GameStructureListener {

    private static final String LOG = "AddSessionActivity";
    private static final String NEW_ITEM_STR = "---new---";
    private String mLocation;
    private int mGame_type_ref;
    private int mGame_structure_ref;
    private int mHoursPlayed = 0;
    private int mMinutesPlayed = 0;
    private Calendar mCalendar;

    private Button mDurationPickButton;
    private Button mDatePickButton;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd MMM, yyyy");

    private ArrayAdapter<String> mLocation_adapter;
    private ArrayAdapter<String> mGameStructureAdapter;
    private Spinner mLocationSpinner;
    private Spinner mGameStructureSpinner;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("locationspinneritemposition", mLocationSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mLocationSpinner.setSelection(savedInstanceState.getInt("locationspinneritemposition", 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_session);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Game type stuff */
        ArrayList<String> gameTypes= ((MainApp) getApplication()).mDataSource.getAllGameTypes();
        //gameTypes.add(NEW_ITEM_STR);
        Spinner gameType_spinner = findViewById(R.id.gameType_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.my_simple_spinner_dropdown_item, gameTypes);
        gameType_spinner.setAdapter(adapter);
        gameType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGame_type_ref = (int)id + 1;
                //if ((position + 1) == parent.getCount())
                //    Toast.makeText(parent.getContext(), "Not yet", Toast.LENGTH_SHORT).show();
                // TODO: add a new addGameTypeActivity
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Location stuff
        ArrayList<String> location_list = ((MainApp) getApplication()).mDataSource.getLocations();
        location_list.add(NEW_ITEM_STR);

        mLocationSpinner = findViewById(R.id.location_spinner);
        mLocation_adapter = new ArrayAdapter<>(this,
                R.layout.my_simple_spinner_dropdown_item, location_list);
        mLocationSpinner.setAdapter(mLocation_adapter);
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null)
                    return;
                // check if new location is selected
                if ((position + 1) == parent.getCount() &&
                        ((CheckedTextView) view.findViewById(android.R.id.text1)).getText()
                                .toString().equals(NEW_ITEM_STR)) {
                    //start new location dialog
                    LocationDialogFragment a = new LocationDialogFragment();
                    a.show(getFragmentManager(), "locationDialog");
                } else {
                        mLocation = ((CheckedTextView) view.findViewById(android.R.id.text1))
                                .getText().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<Game_Structure> gameStructureList = ((MainApp) getApplication()).mDataSource.getAllGameStructures();
        ArrayList<String> gameStructureStringList = new ArrayList<>();
        for (Game_Structure g : gameStructureList) {
            gameStructureStringList.add(g.toString());
        }
        gameStructureStringList.add(NEW_ITEM_STR);
        mGameStructureSpinner = findViewById(R.id.game_structure_spinner);
        mGameStructureAdapter = new ArrayAdapter<>(this,
                R.layout.my_simple_spinner_dropdown_item, gameStructureStringList);
        mGameStructureSpinner.setAdapter(mGameStructureAdapter);
        mGameStructureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((position + 1) == parent.getCount() &&
                        ((CheckedTextView) view.findViewById(android.R.id.text1)).getText()
                                .toString().equals(NEW_ITEM_STR)) {
                    GameStructureDialogFragment g = GameStructureDialogFragment.newInstance();
                    g.show(getFragmentManager(), "g");

                    //((MainApp) getApplication()).mDataSource.addGameStructure(gamestruct);
                    Log.d(LOG, "Adding new game structure");
                }
                else {
                    mGame_structure_ref = (int)id + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //duration pick stuff
        mDurationPickButton = findViewById(R.id.button_duration);
        mDurationPickButton.setText(String.valueOf(mHoursPlayed) + " h :" +
                String.valueOf(mMinutesPlayed) + " min");

        //date pick stuff
        mCalendar = Calendar.getInstance();
        mDatePickButton = findViewById(R.id.button_pick_date);
        mDatePickButton.setText(mFormatter.format(mCalendar.getTime()));

        //result stuff

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause() ;

        try {
            ((MainApp) getApplication()).mDataSource.close();
        } catch (NullPointerException e) {
            Log.d(LOG, "Data source is null");
        }
    }

    public void duration_pick_onClick(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                mHoursPlayed, mMinutesPlayed, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int h, int m) {
        mHoursPlayed = h;
        mMinutesPlayed = m;
        mDurationPickButton.setText(String.valueOf(mHoursPlayed) + " h :" +
                String.valueOf(mMinutesPlayed) + " min");
    }

    public void pick_date_onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                0, 0, 0);
        datePickerDialog.getDatePicker().updateDate(
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        mDatePickButton.setText(mFormatter.format(mCalendar.getTime()));
    }

    private Session createSession() {
        if (mHoursPlayed == 0 && mMinutesPlayed == 0)
            return null;
        Session session = new Session();
        session.setGame_type_ref(mGame_type_ref);
        session.setLocation(mLocation);
        session.setGame_structure_ref(mGame_structure_ref);
        session.setDuration(mHoursPlayed * 60 + mMinutesPlayed);
        session.setDate(mCalendar.getTime());
        EditText result_EditText = findViewById(R.id.result_editText);

        Float result_float = (Float.valueOf(result_EditText.getText().toString()));
        result_float = 100 * result_float; //store in cents
        session.setResult(result_float.intValue());
        session.setGame_notes(""); //TODO: add note functionality
        return session;
    }

    /** Cancels this activity*/
    public void cancel_onClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /** Adds the session to the database and finishes this activity
     * if this form is correctly filled in*/
    public void add_onClick(View view) {
        Session resultSession = createSession();
        if (resultSession != null) {
            ((MainApp)getApplication()).mDataSource.addSession(resultSession);
            setResult(RESULT_OK);
            finish();
        }
    }

    /*LocationDialogFragment callback*/
    /** Adds a location to the location spinner*/
    @Override
    public void onDialogPositiveCheck(LocationDialogFragment dialog) {
        mLocation = dialog.getLocation();
        mLocation_adapter.remove(NEW_ITEM_STR);
        mLocation_adapter.add(mLocation);
        mLocation_adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeCheck() {
        mLocationSpinner.setSelection(0);
        mLocationSpinner.invalidate();
    }

    public void doGameStructureDialogPositiveClick(Game_Structure g) {
        mGameStructureAdapter.remove(NEW_ITEM_STR);
        mGameStructureAdapter.add(g.toString());
        mGameStructureAdapter.notifyDataSetChanged();
        mGame_structure_ref = mGameStructureAdapter.getCount();
        ((MainApp) getApplication()).mDataSource.addGameStructure(g);
    }

    @Override
    public void doGameStructureDialogNegativeClick() {
        mGameStructureSpinner.setSelection(0);
        mGameStructureSpinner.invalidate();
    }
}

