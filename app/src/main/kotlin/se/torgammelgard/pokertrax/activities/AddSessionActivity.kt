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
import se.torgammelgard.pokertrax.fragments.GameTypeDialogFragment
import se.torgammelgard.pokertrax.fragments.LocationDialogFragment
import se.torgammelgard.pokertrax.model.entities.GameStructure
import se.torgammelgard.pokertrax.model.entities.GameType
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
        GameTypeDialogFragment.GameTypeDialogListener,
        HasSupportFragmentInjector,
        AnkoLogger {

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return fragmentInjector
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var gameTypeRepository: GameTypeRepository
    @Inject
    lateinit var gameStructureRepository: GameStructureRepository
    @Inject
    lateinit var sessionRepository: SessionRepository

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
    private var mLocationSpinner: Spinner? = null
    private var mGameStructureAdapter: ArrayAdapter<String>? = null
    private var mGameStructureSpinner: Spinner? = null
    private lateinit var mGameTypeAdapter: ArrayAdapter<String>
    private lateinit var mGameTypeSpinner: Spinner

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

        initGameTypeSpinner()

        initLocationSpinner()

        initGameStructureSpinner()

        //duration pick stuff
        mDurationPickButton = findViewById(R.id.button_duration)
        mDurationPickButton!!.text = String.format("%d h : %d min", mHoursPlayed, mMinutesPlayed)

        //date pick stuff
        mCalendar = Calendar.getInstance()
        mDatePickButton = findViewById(R.id.button_pick_date)
        mDatePickButton!!.text = mFormatter.format(mCalendar!!.time)

        //result stuff

    }

    private fun initGameTypeSpinner() {
        doAsync {
            val gameTypes = gameTypeRepository.getAll().map { gameType -> gameType.type }.toMutableList()
            gameTypes.add(NEW_ITEM_STR)

            onComplete {
                mGameTypeSpinner = findViewById(R.id.gameType_spinner)
                mGameTypeAdapter = ArrayAdapter(
                        baseContext, R.layout.my_simple_spinner_dropdown_item, gameTypes)
                mGameTypeSpinner.adapter = mGameTypeAdapter
                mGameTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val gameTypeText = (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text.toString()
                        val itemIsAtLastPosition = position == (parent.count - 1)
                        if (itemIsAtLastPosition && gameTypeText == NEW_ITEM_STR) {
                            info { "Clicked new game type" }
                            val gameTypeDialogFragment = GameTypeDialogFragment()
                            gameTypeDialogFragment.show(supportFragmentManager, "game_type_fragment")
                        } else mGameTypeRef = id.toInt() + 1
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        }
    }

    private fun initLocationSpinner() {
        doAsync {
            val locations = sessionRepository.locations()
            val locationsWithNewItem = locations.toMutableList()
            locationsWithNewItem.add(NEW_ITEM_STR)

            onComplete {
                mLocationSpinner = findViewById(R.id.location_spinner)
                mLocationAdapter = ArrayAdapter(baseContext,
                        R.layout.my_simple_spinner_dropdown_item, locationsWithNewItem)
                mLocationSpinner!!.adapter = mLocationAdapter
                mLocationSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val locationText = (view?.findViewById<View>(android.R.id.text1) as CheckedTextView).text.toString()
                        val itemIsAtLastPosition = position == (parent.count - 1)

                        // check if new location is selected
                        if (itemIsAtLastPosition && locationText == NEW_ITEM_STR) {
                            //start new location dialog
                            val locationDialogFragment = LocationDialogFragment()
                            locationDialogFragment.show(supportFragmentManager, "locationDialog")
                        } else {
                            mLocation = locationText
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        }
    }

    private fun initGameStructureSpinner() {
        doAsync {
            val gameStructures = gameStructureRepository.getAllGameStructures().mapTo(ArrayList()) { it.toString() }.toMutableList()
            gameStructures.add(NEW_ITEM_STR)

            onComplete {
                mGameStructureSpinner = findViewById(R.id.game_structure_spinner)
                mGameStructureAdapter = ArrayAdapter(baseContext,
                        R.layout.my_simple_spinner_dropdown_item, gameStructures)
                mGameStructureSpinner!!.adapter = mGameStructureAdapter
                mGameStructureSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (position + 1 == parent.count && (view.findViewById<View>(android.R.id.text1) as CheckedTextView).text
                                        .toString() == NEW_ITEM_STR) {
                            info { "Clicked new game structure" }
                            GameStructureDialogFragment().show(supportFragmentManager, "g")
                        } else {
                            mGameStructureRef = id.toInt() + 1
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
        }
    }

    fun durationPickOnClick(view: View) {
        val timePickerDialog = TimePickerDialog(this, this,
                mHoursPlayed, mMinutesPlayed, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker, h: Int, m: Int) {
        mHoursPlayed = h
        mMinutesPlayed = m
        mDurationPickButton!!.text = String.format("%d h : %d min", mHoursPlayed, mMinutesPlayed)
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

                uiThread {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    /** Adds a location to the location spinner */
    override fun onLocationDialogPositiveCheck(location: String) {
        mLocation = location
        mLocationAdapter?.remove(NEW_ITEM_STR)
        mLocationAdapter?.add(mLocation)
        mLocationAdapter?.add(NEW_ITEM_STR)
        mLocationAdapter?.notifyDataSetChanged()
    }

    override fun onLocationDialogNegativeCheck() {
        mLocationSpinner?.setSelection(0)
        mLocationSpinner?.invalidate()
    }

    override fun doGameStructureDialogPositiveClick(gameStructure: GameStructure) {
        mGameStructureAdapter?.remove(NEW_ITEM_STR)
        mGameStructureAdapter?.add(gameStructure.toString())
        mGameStructureAdapter?.notifyDataSetChanged()
        mGameStructureRef = mGameStructureAdapter?.count as Int
    }

    override fun doGameStructureDialogNegativeClick() {
        mGameStructureSpinner?.setSelection(0)
        mGameStructureSpinner?.invalidate()
    }

    override fun onGameTypeDialogPositiveCheck(gameType: GameType) {
        mGameTypeAdapter.remove(NEW_ITEM_STR)
        mGameTypeAdapter.add(gameType.type)
        mGameTypeAdapter.add(NEW_ITEM_STR)
        mGameTypeAdapter.notifyDataSetChanged()
    }

    override fun onGameTypeDialogNegativeCheck() {
        mGameTypeSpinner.setSelection(0)
        mGameTypeSpinner.invalidate()
    }

    companion object {
        private const val LOG = "AddSessionActivity"
        private const val NEW_ITEM_STR = "---new---"
    }
}

