package com.example.torgammelgard.pokerhourly;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class AskForUserNameDialogFragment extends DialogFragment {
    String username = "";

    public AskForUserNameDialogFragment() {
    }

    /*interfaces */
    public interface NoticeDialogListener {
        public void onDialogPositiveCheck(AskForUserNameDialogFragment dialog);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.askforusernamedialog, null);
        builder.setView(view);
        builder.setMessage(R.string.askforusernameMessage);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText)view.findViewById(R.id.askForUsernameEditText);
                username = editText.getText().toString();

                mListener.onDialogPositiveCheck(AskForUserNameDialogFragment.this);

                    //username = text;
                    //Toast.makeText(getActivity(), username, Toast.LENGTH_SHORT).show();

            }
        });
        return builder.create();
    }
}
