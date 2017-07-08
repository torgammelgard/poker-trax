package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * TODO: Class header comment.
 */
public class GameStructureDialogFragment extends DialogFragment {

    private static final String LOG = "GameStructureDialogFragment";

    private GameStructureListener mListener;

    public static GameStructureDialogFragment newInstance() {
        return new GameStructureDialogFragment();
    }

    public interface GameStructureListener {
        void doGameStructureDialogPositiveClick(Game_Structure g);
        void doGameStructureDialogNegativeClick();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (GameStructureListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement " +
                "GameStructureDialogFragment.GameStructureListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //called when the dialog is visible to the user
    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog)getDialog();

        if (dialog != null) {
            (dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Game_Structure g = getGameStructure();
                        mListener.doGameStructureDialogPositiveClick(g);
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_game_structure, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getResources().getString(R.string.game_structure_dialog_title))
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // overridden in onStart
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.doGameStructureDialogNegativeClick();
                        dialog.dismiss();
                    }
                }).create();

    }

    private Game_Structure getGameStructure() throws NumberFormatException {
        int smallBlind;
        int bigBlind;
        int ante;
        try {
            Double sb = 100 * Double.valueOf(((EditText) getDialog().findViewById(R.id.editText)).getText().toString());
            smallBlind = sb.intValue();
            Double bb = 100 * Double.valueOf(((EditText) getDialog().findViewById(R.id.editText2)).getText().toString());
            bigBlind = bb.intValue();
            String anteStr = ((EditText) getDialog().findViewById(R.id.editText3)).getText().toString();
            if (!anteStr.equals("")) {
                Double a = 100 * Double.valueOf(anteStr);
                ante = a.intValue();
            }
            else
                ante = 0;
            Log.d(LOG, String.format("sb:%d bb:%d ante:%d", smallBlind, bigBlind, ante));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Fill in the blinds");
        }
        Game_Structure g = new Game_Structure();
        g.setSmall_blind(smallBlind);
        g.setBig_blind(bigBlind);
        g.setAnte(ante);
        return g;
    }
}