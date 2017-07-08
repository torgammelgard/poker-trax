package se.torgammelgard.pokertrax;

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


public class LocationDialogFragment extends DialogFragment {
    private String mLocation = "";

    public LocationDialogFragment() {
    }

    /* interfaces */
    public interface LocationDialogListener {
        void onDialogPositiveCheck(LocationDialogFragment dialog);
        void onDialogNegativeCheck();
    }

    LocationDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LocationDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog)getDialog();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)dialog.findViewById(R.id.edit_location);
                mLocation = editText.getText().toString();
                if (mLocation.equals("")) {
                    Toast.makeText(getActivity(), "Enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    mListener.onDialogPositiveCheck(LocationDialogFragment.this);
                    dismiss();
                }
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.location_dialog, null);
        builder.setView(view);
        builder.setTitle(R.string.locationDialogText);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //overridden in onStart
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeCheck();
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public String getLocation() {
        return mLocation;
    }
}
