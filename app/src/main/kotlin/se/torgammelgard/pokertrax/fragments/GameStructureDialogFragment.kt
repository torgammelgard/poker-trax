package se.torgammelgard.pokertrax.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.*
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.GameStructure
import se.torgammelgard.pokertrax.model.repositories.GameStructureRepository
import javax.inject.Inject

/**
 * Dialog for adding a game structure
 */
class GameStructureDialogFragment : DialogFragment(), AnkoLogger {

    @Inject
    lateinit var gameStructureRepository: GameStructureRepository

    private var mListener: GameStructureListener? = null

    interface GameStructureListener {
        fun doGameStructureDialogPositiveClick(gameStructure: GameStructure)
        fun doGameStructureDialogNegativeClick()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        try {
            mListener = context as GameStructureListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement " +
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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            doAsync {
                info { "Adding game structure" }
                gameStructure?.let { gameStructure ->
                    val id = gameStructureRepository.add(gameStructure)
                    val addedGameStructure = gameStructureRepository.get(id)
                    onComplete {
                        info { "Finished adding game structure" }
                        mListener?.doGameStructureDialogPositiveClick(addedGameStructure)
                        dialog.dismiss()
                    }
                }
                dialog.dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.add_game_structure, null)
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

    private val gameStructure: GameStructure?
        get() {
            val smallBlindText = (dialog.findViewById<View>(R.id.editText) as EditText).text.toString()
            val bigBlindText = (dialog.findViewById<View>(R.id.editText2) as EditText).text.toString()
            val anteText = (dialog.findViewById<View>(R.id.editText3) as EditText).text.toString()
            val smallBlind: Int?
            val bigBlind: Int?
            val ante: Int?
            try {
                smallBlind = if (smallBlindText.isNotBlank()) (100 * smallBlindText.toDouble()).toInt() else null
                bigBlind = if (bigBlindText.isNotBlank()) (100 * bigBlindText.toDouble()).toInt() else null
                ante = if (anteText.isNotBlank()) (anteText.toDouble() * 100).toInt() else null
                info { String.format("sb:%d bb:%d ante:%d", smallBlind, bigBlind, ante) }
            } catch (e: NumberFormatException) {
                error { "Couldn't cast input strings to numbers (Int). $e" }
                return null
            }

            return GameStructure(smallBlind, bigBlind, ante)
        }
}