package se.torgammelgard.pokertrax.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import se.torgammelgard.pokertrax.R


class LocationDialogFragment : DialogFragment() {

    interface LocationDialogListener {
        fun onLocationDialogPositiveCheck(location: String)
        fun onLocationDialogNegativeCheck()
    }

    private var mListener: LocationDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as LocationDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement NoticeDialogListener")
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
            val location = dialog.findViewById<EditText>(R.id.edit_location)?.text.toString()
            if (location.isBlank()) {
                Toast.makeText(activity, "Enter a location", Toast.LENGTH_SHORT).show()
            } else {
                mListener!!.onLocationDialogPositiveCheck(location)
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val v = inflater?.inflate(R.layout.location_dialog, null, false)
        return AlertDialog.Builder(requireContext())
                .setView(v)
                .setTitle(R.string.locationDialogText)
                .setPositiveButton(R.string.ok) { dialog, which ->
                    //overridden in onStart
                }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    //overridden in onStart
                }
                .create()
    }
}
