package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// popup dialog with a calendar so the user can select an expiry date
public class DatePickerDialog extends DialogFragment {
    // date that is currently selected in the calendar
    private Calendar selectDate;

    // interface so the dialog can send the chosen date back to the screen that opened it
    public interface SaveDateListener{

        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(){
        // empty constructor required for DialogFragment
    }

    // sends the selected date to the screen that opened the dialog
    private void saveItem(Calendar selectedTime){
        // get activity is for the screen that opened the dialog
        SaveDateListener activity = (SaveDateListener) getActivity();

        activity.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }

    // builds the layout of the dialog and sets up the calendar and the buttons
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        // create the dialogs layout from the select date xml
        View view = inflater.inflate(
                R.layout.select_date,
                container,
                false
        );

        getDialog().setTitle("Select Date");

        // start with today's date, so if the user taps select without choosing a date, today is used
        selectDate = Calendar.getInstance();

        CalendarView calendar = view.findViewById(R.id.calendarView);

        // will run every time the user taps a different day on the calendar
        // will update the selected date to the day that was tapped
        calendar.setOnDateChangeListener(
                (calendarView, year, month, dayOfMonth)->{
                    selectDate.set(year,month,dayOfMonth);
                }
        );

        // select button sends the date that is chosen back and closes the dialog
        Button selectButton = view.findViewById(R.id.buttonSelect);
        selectButton.setOnClickListener(v->{
            saveItem(selectDate);
        });

        // cancel button closes the dialog without sending a date
        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v->{
            getDialog().dismiss();
        });
        return view;
    }



}

