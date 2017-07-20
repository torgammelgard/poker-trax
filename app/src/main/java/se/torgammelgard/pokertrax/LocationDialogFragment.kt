package se.torgammelgard.pokertrax

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class LocationDialogFragment : DialogFragment() {
    var location = ""
        private set

    /* interfaces */
    interface LocationDialogListener {
        fun onDialogPositiveCheck(dialog: LocationDialogFragment)
        fun onDialogNegativeCheck()
    }

    internal var mListener: LocationDialogListener? = null

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

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        val view = inflater.inflate(R.layout.location_dialog, null)
        builder.setView(view)
        builder.setTitle(R.string.locationDialogText)
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            //overridden in onStart
        }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    mListener!!.onDialogNegativeCheck()
                    dialog.dismiss()
                }
        return builder.create()
    }
}
