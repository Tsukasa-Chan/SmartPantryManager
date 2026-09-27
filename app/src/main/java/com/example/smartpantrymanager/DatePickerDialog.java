package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {
    private Calendar selectDate;

    public interface SaveDateListener{
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(){
        // empty constructor required for DialogFragment
    }

    private void saveItem(Calendar selectedTime){
        SaveDateListener activity = (SaveDateListener) getActivity();

        activity.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.select_date,
                container,
                false
        );

        getDialog().setTitle("Select Date");
        selectDate = Calendar.getInstance();

        CalendarView calendar = view.findViewById(R.id.calendarView);

        calendar.setOnDateChangeListener(
                (calendarView, year, month, dayOfMonth)->{
                    selectDate.set(year,month,dayOfMonth);
                }
        );

        Button selectButton = view.findViewById(R.id.buttonSelect);
        selectButton.setOnClickListener(v->{
            saveItem(selectDate);
        });

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v->{
            getDialog().dismiss();
        });
        return view;
    }



}

