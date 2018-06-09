package se.torgammelgard.pokertrax.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import se.torgammelgard.pokertrax.model.GameStructure
import se.torgammelgard.pokertrax.R

/**
 * TODO: Class header comment.
 */
class GameStructureDialogFragment : DialogFragment() {

    private var mListener: GameStructureListener? = null

    interface GameStructureListener {
        fun doGameStructureDialogPositiveClick(g: GameStructure)
        fun doGameStructureDialogNegativeClick()
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mListener = activity as GameStructureListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "must implement " +
                    "GameStructureDialogFragment.GameStructureListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    //called when the dialog is visible to the user
    override fun onStart() {
        super.onStart()

        val dialog = dialog as AlertDialog

        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            try {
                val g = gameStructure
                mListener!!.doGameStructureDialogPositiveClick(g)
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                Toast.makeText(activity, e.message,
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.add_game_structure, null)
        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(resources.getString(R.string.game_structure_dialog_title))
                .setPositiveButton(R.string.ok
                ) { dialog, which ->
                    // overridden in onStart
                }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    mListener!!.doGameStructureDialogNegativeClick()
                    dialog.dismiss()
                }.create()

    }

    private val gameStructure: GameStructure
        @Throws(NumberFormatException::class)
        get() {
            val smallBlind: Int
            val bigBlind: Int
            val ante: Int
            try {
                val sb = 100 * java.lang.Double.valueOf((dialog.findViewById<View>(R.id.editText) as EditText).text.toString())!!
                smallBlind = sb.toInt()
                val bb = 100 * java.lang.Double.valueOf((dialog.findViewById<View>(R.id.editText2) as EditText).text.toString())!!
                bigBlind = bb.toInt()
                val anteStr = (dialog.findViewById<View>(R.id.editText3) as EditText).text.toString()
                if (anteStr != "") {
                    val a = 100 * java.lang.Double.valueOf(anteStr)!!
                    ante = a.toInt()
                } else
                    ante = 0
                Log.d(LOG, String.format("sb:%d bb:%d ante:%d", smallBlind, bigBlind, ante))
            } catch (e: NumberFormatException) {
                throw NumberFormatException("Fill in the blinds")
            }

            val g = GameStructure()
            g.small_blind = smallBlind
            g.big_blind = bigBlind
            g.ante = ante
            return g
        }

    companion object {

        private val LOG = "GameStructureDialog"

        fun newInstance(): GameStructureDialogFragment {
            return GameStructureDialogFragment()
        }
    }
}