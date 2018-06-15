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
import se.torgammelgard.pokertrax.model.database.AppDatabase
import se.torgammelgard.pokertrax.fragments.GameStructureDialogFragment
import se.torgammelgard.pokertrax.fragments.LocationDialogFragment
import se.torgammelgard.pokertrax.model.old_entities.GameStructure

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

import se.torgammelgard.pokertrax.model.old_entities.Session

/**
 * Lets the user add a session
 */
class AddSessionActivity : Activity(),
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        LocationDialogFragment.LocationDialogListener,
        GameStructureDialogFragment.GameStructureListener {

    private var mLocation: String = ""
    private var mGameTypeRef: Int = 0
    private var mGameStructureRef: Int = 0
    private var mHoursPlayed = 0
    private var mMinutesPlayed = 0
    private var mCalendar: Calendar? = null

    private var mDurationPickButton: Button? = null
    private var mDatePickButton: Button? = null
    private val mFormatter = SimpleDateFormat("dd MMM, yyyy")

    private var mLocationAdapter: ArrayAdapter<String>? = null
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

        // Game type stuff
        val gameTypes = (application as MainApp).mDataSource!!.allGameTypes
        val gameTypeSpinner = findViewById<Spinner>(R.id.gameType_spinner)
        val gameTypeAdapter = ArrayAdapter(
                this, R.layout.my_simple_spinner_dropdown_item, gameTypes)
        gameTypeSpinner.adapter = gameTypeAdapter
        gameTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mGameTypeRef = id.toInt() + 1
                // TODO: add a new addGameTypeActivity
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        //Location stuff
        val locationList = (application as MainApp).mDataSource!!.locations
        locationList.add(NEW_ITEM_STR)

        mLocationSpinner = findViewById(R.id.location_spinner)
        mLocationAdapter = ArrayAdapter(this,
                R.layout.my_simple_spinner_dropdown_item, locationList)
        mLocationSpinner!!.adapter = mLocationAdapter
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
        val gameStructureStringList = gameStructureList!!.mapTo(ArrayList()) { it.toString() }
        gameStructureStringList.add(NEW_ITEM_STR)
        mGameStructureSpinner = findViewById(R.id.game_structure_spinner)
        mGameStructureAdapter = ArrayAdapter(this,
                R.layout.my_simple_spinner_dropdown_item, gameStructureStringList)
        mGameStructureSpinner!!.adapter = mGameStructureAdapter
        mGameStructureSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position + 1 == parent.count && (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text
                        .toString() == NEW_ITEM_STR) {
                    val g = GameStructureDialogFragment.newInstance()
                    g.show(fragmentManager, "g")

                    //((MainApp) getApplication()).mDataSource.addGameStructure(gamestruct); // TODO
                    Log.d(LOG, "Adding new game structure")
                } else {
                    mGameStructureRef = id.toInt() + 1
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

    fun durationPickOnClick(view: View) {
        val timePickerDialog = TimePickerDialog(this, this,
                mHoursPlayed, mMinutesPlayed, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker, h: Int, m: Int) {
        mHoursPlayed = h
        mMinutesPlayed = m
        mDurationPickButton!!.text = String.format("%d h : %d min" , mHoursPlayed, mMinutesPlayed)
    }

    fun pickDateOnClick(view: View) {
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
        session.game_type_ref = mGameTypeRef
        session.location = mLocation
        session.game_structure_ref = mGameStructureRef
        session.duration = mHoursPlayed * 60 + mMinutesPlayed
        session.date = mCalendar!!.time
        val resultEditText = findViewById<EditText>(R.id.result_editText)

        var resultFloat: Float? = java.lang.Float.valueOf(resultEditText.text.toString())
        resultFloat = 100 * resultFloat!! //store in cents
        session.result = resultFloat.toInt()
        session.game_notes = "" //TODO: add note functionality
        return session
    }

    fun cancelActivityOnClick(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun createEntitySession(): se.torgammelgard.pokertrax.model.entities.Session {
        val session = se.torgammelgard.pokertrax.model.entities.Session()
        session.gameTypeReference = mGameTypeRef
        session.location = mLocation
        session.gameStructureReference= mGameStructureRef
        session.duration = mHoursPlayed * 60 + mMinutesPlayed
        session.date = mCalendar!!.time
        val resultEditText = findViewById<EditText>(R.id.result_editText)

        var resultFloat: Float? = java.lang.Float.valueOf(resultEditText.text.toString())
        resultFloat = 100 * resultFloat!! //store in cents
        session.result = resultFloat.toInt()
        session.gameNotes = "" //TODO: add note functionality
        return session
    }

    /** Adds the session to the database and finishes this activity
     * if this form is correctly filled in */
    fun addSessionOnClick(view: View) {
        //val resultSession = createSession()
        //if (resultSession != null) {
        //    (application as MainApp).mDataSource!!.addSession(resultSession)
        //    setResult(Activity.RESULT_OK)
        //    finish()
        //}
        val session = createEntitySession()
        val sessionDao = AppDatabase.getInstance(view.context)?.sessionDao()
        sessionDao?.add(session)
        setResult(Activity.RESULT_OK)
        finish()
    }

    /** Adds a location to the location spinner */
    override fun onDialogPositiveCheck(dialog: LocationDialogFragment) {
        mLocation = dialog.location
        mLocationAdapter?.remove(NEW_ITEM_STR)
        mLocationAdapter?.add(mLocation)
        mLocationAdapter?.notifyDataSetChanged()
    }

    override fun onDialogNegativeCheck() {
        mLocationSpinner?.setSelection(0)
        mLocationSpinner?.invalidate()
    }

    override fun doGameStructureDialogPositiveClick(g: GameStructure) {
        mGameStructureAdapter?.remove(NEW_ITEM_STR)
        mGameStructureAdapter?.add(g.toString())
        mGameStructureAdapter?.notifyDataSetChanged()
        mGameStructureRef = mGameStructureAdapter?.count as Int
        (application as MainApp).mDataSource?.addGameStructure(g)
    }

    override fun doGameStructureDialogNegativeClick() {
        mGameStructureSpinner?.setSelection(0)
        mGameStructureSpinner?.invalidate()
    }

    companion object {
        private const val LOG = "AddSessionActivity"
        private const val NEW_ITEM_STR = "---new---"
    }
}

