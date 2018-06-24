package se.torgammelgard.pokertrax.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.jetbrains.anko.*
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.fragments.GameStructureDialogFragment
import se.torgammelgard.pokertrax.fragments.LocationDialogFragment
import se.torgammelgard.pokertrax.model.entities.GameStructure
import se.torgammelgard.pokertrax.model.entities.Session
import se.torgammelgard.pokertrax.model.repositories.GameStructureRepository
import se.torgammelgard.pokertrax.model.repositories.GameTypeRepository
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Lets the user add a session
 */
class AddSessionActivity : FragmentActivity(),
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        LocationDialogFragment.LocationDialogListener,
        GameStructureDialogFragment.GameStructureListener,
        HasSupportFragmentInjector,
        AnkoLogger {

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return fragmentInjector
    }

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject lateinit var gameTypeRepository: GameTypeRepository
    @Inject lateinit var gameStructureRepository: GameStructureRepository
    @Inject lateinit var sessionRepository: SessionRepository

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
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_session)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Game type stuff
        doAsync {
            val gameTypes = gameTypeRepository.getAllGameTypes()
            ctx.runOnUiThread {
                val gameTypeSpinner = findViewById<Spinner>(R.id.gameType_spinner)
                val gameTypeAdapter = ArrayAdapter(
                        this, R.layout.my_simple_spinner_dropdown_item, gameTypes)
                gameTypeSpinner.adapter = gameTypeAdapter
                gameTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        mGameTypeRef = id.toInt() + 1
                        // TODO: add a new addGameTypeActivity
                        info { "Game type selected" }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                } }
        }



        //Location stuff
        doAsync {
            val locations = sessionRepository.locations()
            val locationsWithNewItem = locations.toMutableList()
            locationsWithNewItem.add(NEW_ITEM_STR)

            activityUiThreadWithContext {
                mLocationSpinner = findViewById(R.id.location_spinner)
                mLocationAdapter = ArrayAdapter(this,
                        R.layout.my_simple_spinner_dropdown_item, locationsWithNewItem)
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
            }
        }

        doAsync {
            val gameStructureList = gameStructureRepository.getAllGameStructures()
            val gameStructureStringList = gameStructureList.mapTo(ArrayList()) { it.toString() }
            gameStructureStringList.add(NEW_ITEM_STR)

            activityUiThreadWithContext {
                mGameStructureSpinner = findViewById(R.id.game_structure_spinner)
                mGameStructureAdapter = ArrayAdapter(this,
                        R.layout.my_simple_spinner_dropdown_item, gameStructureStringList)
                mGameStructureSpinner!!.adapter = mGameStructureAdapter
                mGameStructureSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (position + 1 == parent.count && (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text
                                        .toString() == NEW_ITEM_STR) {
                            val g = GameStructureDialogFragment()
                            g.show(supportFragmentManager, "g")

                            // TODO add a real game structure
                            doAsync {
                                gameStructureRepository.add(GameStructure(100, 28, 56, 14))
                                Log.d(LOG, "Adding new game structure")
                            }

                        } else {
                            mGameStructureRef = id.toInt() + 1
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
        }

        //duration pick stuff
        mDurationPickButton = findViewById(R.id.button_duration)
        mDurationPickButton!!.text = String.format("%d h : %d min", mHoursPlayed, mMinutesPlayed)

        //date pick stuff
        mCalendar = Calendar.getInstance()
        mDatePickButton = findViewById(R.id.button_pick_date)
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
        session.gameTypeReference = mGameTypeRef
        session.location = mLocation
        session.gameStructureReference = mGameStructureRef
        session.duration = mHoursPlayed * 60 + mMinutesPlayed
        session.date = mCalendar!!.time
        val resultEditText = findViewById<EditText>(R.id.result_editText)
        var resultFloat: Float? = java.lang.Float.valueOf(resultEditText.text.toString())
        resultFloat = 100 * resultFloat!! //store in cents
        session.result = resultFloat.toInt()
        session.gameNotes = "" //TODO: add note functionality
        return session
    }

    fun cancelActivityOnClick(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    /** Adds the session to the database and finishes this activity
     * if this form is correctly filled in */
    fun addSessionOnClick(view: View) {
        val session = createSession()
        if (session != null) {
            doAsync {
                sessionRepository.addSession(session)

                uiThread{
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
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

    override fun doGameStructureDialogPositiveClick(gameStructure: GameStructure) {
        mGameStructureAdapter?.remove(NEW_ITEM_STR)
        mGameStructureAdapter?.add(gameStructure.toString())
        mGameStructureAdapter?.notifyDataSetChanged()
        mGameStructureRef = mGameStructureAdapter?.count as Int
        gameStructureRepository.add(gameStructure)
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

