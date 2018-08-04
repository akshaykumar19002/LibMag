package com.kumar.akshay.libmag.Barcode;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;

public class NewBarCodeDialogFragment extends DialogFragment {

    BarCode barCode;
    String barCodeString, type;
    View view;
    Button generateBarCodeBtn, saveBarCodeBtn;
    TextView barCodeTextView;
    ImageView barCodeImageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        barCode = new BarCode(getContext());
        barCodeString = Integer.toString(getArguments().getInt("barCode"));
        type = getArguments().getString("type");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dialog_new_barcode, null);
        generateBarCodeBtn = view.findViewById(R.id.buttonGenerate);
        saveBarCodeBtn = view.findViewById(R.id.buttonSave);
        barCodeTextView = view.findViewById(R.id.resultTextView);
        barCodeImageView = view.findViewById(R.id.imageViewBarCode);
        if (type == "book") {
            barCodeTextView.setText("Book Id: " + barCodeString);
        } else {
            barCodeTextView.setText("Roll no: " + barCodeString);
        }
        generateBarCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barCode.generateBarCode(barCodeString, barCodeImageView);
                saveBarCodeBtn.setVisibility(View.VISIBLE);
            }
        });
        saveBarCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barCode.saveBarCode(barCodeString);
                if (type == "book")
                    startActivity(new Intent(getContext(), MainScreen.class));
            }
        });
        builder.setView(view)
                .setPositiveButton("Print Barcode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        barCode.printBarCode(barCodeString);
                        startActivity(new Intent(getContext(), MainScreen.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewBarCodeDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
