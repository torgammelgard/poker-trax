package se.torgammelgard.pokertrax.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.model.entities.GameType

class GameTypeDialogFragment : DialogFragment() {

    interface GameTypeDialogListener {
        fun onGameTypeDialogPositiveCheck(gameType: GameType)
        fun onGameTypeDialogNegativeCheck()
    }

    private lateinit var mListener: GameTypeDialogListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as GameTypeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement GameTypeDialogListener")
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val gameTypeText = dialog.findViewById<EditText>(R.id.edit_game_type)?.text.toString()
            if (gameTypeText.isBlank()) {
                Toast.makeText(activity, "Enter a game type", Toast.LENGTH_SHORT).show()
            } else {
                mListener.onGameTypeDialogPositiveCheck(GameType().apply { type = gameTypeText })
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.game_type_dialog, null, false)
        return AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(R.string.gameTypeDialogTitle)
                .setPositiveButton(R.string.ok) { _, _ ->

                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dismiss()
                }
                .create()

    }
}