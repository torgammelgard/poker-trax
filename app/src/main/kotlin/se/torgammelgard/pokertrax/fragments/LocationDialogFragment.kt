package se.torgammelgard.pokertrax.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import se.torgammelgard.pokertrax.R


class LocationDialogFragment : DialogFragment() {
    var location = ""
        private set

    /* interfaces */
    interface LocationDialogListener {
        fun onDialogPositiveCheck(dialog: LocationDialogFragment)
        fun onDialogNegativeCheck()
    }

    private var mListener: LocationDialogListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as LocationDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement NoticeDialogListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val editText = dialog.findViewById<EditText>(R.id.edit_location)
            location = editText.text.toString()
            if (location == "") {
                Toast.makeText(activity, "Enter a location", Toast.LENGTH_SHORT).show()
            } else {
                mListener!!.onDialogPositiveCheck(this@LocationDialogFragment)
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        val v = inflater!!.inflate(R.layout.location_dialog, null, false)
        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.locationDialogText)
                .setPositiveButton(R.string.ok) { dialog, which ->
                    //overridden in onStart
                }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    mListener!!.onDialogNegativeCheck()
                    dialog.dismiss()
                }
                .create()
    }
}
