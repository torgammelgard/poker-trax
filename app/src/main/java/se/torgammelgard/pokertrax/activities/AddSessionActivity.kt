package se.torgammelgard.pokertrax.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import se.torgammelgard.pokertrax.MainApp
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.fragments.GameStructureDialogFragment
import se.torgammelgard.pokertrax.fragments.LocationDialogFragment
import se.torgammelgard.pokertrax.model.Game_Structure

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

import se.torgammelgard.pokertrax.model.Session

/**
 * Activity which lets the user add a session
 */
class AddSessionActivity : Activity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, LocationDialogFragment.LocationDialogListener, GameStructureDialogFragment.GameStructureListener {
    private var mLocation: String? = null
    private var mGame_type_ref: Int = 0
    private var mGame_structure_ref: Int = 0
    private var mHoursPlayed = 0
    private var mMinutesPlayed = 0
    private var mCalendar: Calendar? = null

    private var mDurationPickButton: Button? = null
    private var mDatePickButton: Button? = null
    private val mFormatter = SimpleDateFormat("dd MMM, yyyy")

    private var mLocation_adapter: ArrayAdapter<String>? = null
    private var mGameStructureAdapter: ArrayAdapter<String>? = null
    private var mLocationSpinner: Spinner? = null
    private var mGameStructureSpinner: Spinner? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("locationspinneritemposition", mLocationSpinner!!.selectedItemPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mLocationSpinner!!.setSelection(savedInstanceState.getInt("locationspinneritemposition", 0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_session)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        /* Game type stuff */
        val gameTypes = (application as MainApp).mDataSource!!.allGameTypes
        //gameTypes.add(NEW_ITEM_STR);
        val gameType_spinner = findViewById<Spinner>(R.id.gameType_spinner)
        val gameTypeAdapter = ArrayAdapter(
                this, R.layout.my_simple_spinner_dropdown_item, gameTypes)
        gameType_spinner.adapter = gameTypeAdapter
        gameType_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mGame_type_ref = id.toInt() + 1
                //if ((position + 1) == parent.getCount())
                //    Toast.makeText(parent.getContext(), "Not yet", Toast.LENGTH_SHORT).show();
                // TODO: add a new addGameTypeActivity
            }


            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        //Location stuff
        val location_list = (application as MainApp).mDataSource!!.locations
        location_list.add(NEW_ITEM_STR)

        mLocationSpinner = findViewById<Spinner>(R.id.location_spinner)
        mLocation_adapter = ArrayAdapter(this,
                R.layout.my_simple_spinner_dropdown_item, location_list)
        mLocationSpinner!!.adapter = mLocation_adapter
        mLocationSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view == null)
                    return
                // check if new location is selected
                if (position + 1 == parent.count && (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text
                        .toString() == NEW_ITEM_STR) {
                    //start new location dialog
                    val locationDialogFragment = LocationDialogFragment()
                    locationDialogFragment.show(fragmentManager, "locationDialog")
                } else {
                    mLocation = (view.findViewById<View>(android.R.id.text1) as CheckedTextView)
                            .text.toString()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val gameStructureList = (application as MainApp).mDataSource!!.allGameStructures
        val gameStructureStringList = ArrayList<String>()
        for (g in gameStructureList!!) {
            gameStructureStringList.add(g.toString())
        }
        gameStructureStringList.add(NEW_ITEM_STR)
        mGameStructureSpinner = findViewById<Spinner>(R.id.game_structure_spinner)
        mGameStructureAdapter = ArrayAdapter(this,
                R.layout.my_simple_spinner_dropdown_item, gameStructureStringList)
        mGameStructureSpinner!!.adapter = mGameStructureAdapter
        mGameStructureSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position + 1 == parent.count && (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text
                        .toString() == NEW_ITEM_STR) {
                    val g = GameStructureDialogFragment.newInstance()
                    g.show(fragmentManager, "g")

                    //((MainApp) getApplication()).mDataSource.addGameStructure(gamestruct);
                    Log.d(LOG, "Adding new game structure")
                } else {
                    mGame_structure_ref = id.toInt() + 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //duration pick stuff
        mDurationPickButton = findViewById<Button>(R.id.button_duration)
        mDurationPickButton!!.text = mHoursPlayed.toString() + " h :" +
                mMinutesPlayed.toString() + " min"

        //date pick stuff
        mCalendar = Calendar.getInstance()
        mDatePickButton = findViewById<Button>(R.id.button_pick_date)
        mDatePickButton!!.text = mFormatter.format(mCalendar!!.time)

        //result stuff

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

        try {
            (application as MainApp).mDataSource!!.close()
        } catch (e: NullPointerException) {
            Log.d(LOG, "Data source is null")
        }

    }

    fun duration_pick_onClick(view: View) {
        val timePickerDialog = TimePickerDialog(this, this,
                mHoursPlayed, mMinutesPlayed, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker, h: Int, m: Int) {
        mHoursPlayed = h
        mMinutesPlayed = m
        mDurationPickButton!!.text = mHoursPlayed.toString() + " h :" +
                mMinutesPlayed.toString() + " min"
    }

    fun pick_date_onClick(view: View) {
        val datePickerDialog = DatePickerDialog(this, this,
                0, 0, 0)
        datePickerDialog.datePicker.updateDate(
                mCalendar!!.get(Calendar.YEAR),
                mCalendar!!.get(Calendar.MONTH),
                mCalendar!!.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar!!.set(year, monthOfYear, dayOfMonth)
        mDatePickButton!!.text = mFormatter.format(mCalendar!!.time)
    }

    private fun createSession(): Session? {
        if (mHoursPlayed == 0 && mMinutesPlayed == 0)
            return null
        val session = Session()
        session.game_type_ref = mGame_type_ref
        session.location = mLocation
        session.game_structure_ref = mGame_structure_ref
        session.duration = mHoursPlayed * 60 + mMinutesPlayed
        session.date = mCalendar!!.time
        val result_EditText = findViewById<EditText>(R.id.result_editText)

        var result_float: Float? = java.lang.Float.valueOf(result_EditText.text.toString())
        result_float = 100 * result_float!! //store in cents
        session.result = result_float.toInt()
        session.game_notes = "" //TODO: add note functionality
        return session
    }

    /** Cancels this activity */
    fun cancel_onClick(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    /** Adds the session to the database and finishes this activity
     * if this form is correctly filled in */
    fun add_onClick(view: View) {
        val resultSession = createSession()
        if (resultSession != null) {
            (application as MainApp).mDataSource!!.addSession(resultSession)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    /*LocationDialogFragment callback*/
    /** Adds a location to the location spinner */
    override fun onDialogPositiveCheck(dialog: LocationDialogFragment) {
        mLocation = dialog.location
        mLocation_adapter!!.remove(NEW_ITEM_STR)
        mLocation_adapter!!.add(mLocation)
        mLocation_adapter!!.notifyDataSetChanged()
    }

    override fun onDialogNegativeCheck() {
        mLocationSpinner!!.setSelection(0)
        mLocationSpinner!!.invalidate()
    }

    override fun doGameStructureDialogPositiveClick(g: Game_Structure) {
        mGameStructureAdapter!!.remove(NEW_ITEM_STR)
        mGameStructureAdapter!!.add(g.toString())
        mGameStructureAdapter!!.notifyDataSetChanged()
        mGame_structure_ref = mGameStructureAdapter!!.count
        (application as MainApp).mDataSource!!.addGameStructure(g)
    }

    override fun doGameStructureDialogNegativeClick() {
        mGameStructureSpinner!!.setSelection(0)
        mGameStructureSpinner!!.invalidate()
    }

    companion object {

        private val LOG = "AddSessionActivity"
        private val NEW_ITEM_STR = "---new---"
    }
}

