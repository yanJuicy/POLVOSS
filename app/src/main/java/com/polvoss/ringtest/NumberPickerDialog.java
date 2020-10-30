package com.polvoss.ringtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

public class NumberPickerDialog extends DialogFragment {

    private NumberPicker.OnValueChangeListener valueChangeListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue((120-0)/5);
        //numberPicker.setValue(10);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        String[] values = getValues();
        numberPicker.setDisplayedValues(values);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("시간 설정");
        builder.setMessage("분 단위 설정");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    private String[] getValues() {
        String[] values = new String[25];
        for (int i=0; i<25; i++) {
            values[i] = String.valueOf(i * 5);
        }
        return values;
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
