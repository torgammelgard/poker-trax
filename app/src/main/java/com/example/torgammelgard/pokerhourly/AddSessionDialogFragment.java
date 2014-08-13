package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSessionDialogFragment extends DialogFragment {

    Session mSession;

    public AddSessionDialogFragment() {
    }

    public interface AddSessionDialogListener {
        public void onDialogPositiveCheck(AddSessionDialogFragment addSessionDialogFragment);
    }

    private AddSessionDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddSessionDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    "Must implement AddSessionDialogListener.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean invalid = false;
                    //validate
                    mSession = new Session();
                    String gameInfo = ((EditText) dialog.findViewById(R.id.gameInfo)).getText().toString();
                    if (gameInfo.equals("")) {
                        Toast.makeText(getActivity(), "Enter game info", Toast.LENGTH_SHORT).show();
                        invalid = true;
                    }
                    else
                        mSession.setGameInfo(gameInfo);

                    String hoursStr = ((EditText) dialog.findViewById(R.id.time)).getText().toString();
                    if (!hoursStr.equals("") && Integer.valueOf(hoursStr) > 0)
                        mSession.setHours(Integer.valueOf(hoursStr));
                    else {
                        Toast.makeText(getActivity(), "Enter hours", Toast.LENGTH_SHORT).show();
                        invalid = true;
                    }

                    String resultStr = ((EditText) dialog.findViewById(R.id.result)).getText().toString();
                    if (!resultStr.equals(""))
                        mSession.setResult(Integer.valueOf(resultStr));

                    else{
                        Toast.makeText(getActivity(), "Enter result", Toast.LENGTH_SHORT).show();
                        invalid = true;
                    }

                    if (!invalid) {
                        mListener.onDialogPositiveCheck(AddSessionDialogFragment.this);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.addsession, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setTitle("Add session")
            .setView(view)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddSessionDialogFragment.this.dismiss();
                }
            });

        return builder.create();
    }
}
