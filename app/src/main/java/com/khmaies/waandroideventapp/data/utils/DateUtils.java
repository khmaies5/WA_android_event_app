package com.khmaies.waandroideventapp.data.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.khmaies.waandroideventapp.presentation.interfaces.TimePickerDialogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */
public class DateUtils {
    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils(){}

    public static String formatDate(String dateString) {

        try {
            // Define the input format pattern
            SimpleDateFormat inputFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
            Calendar calendar = Calendar.getInstance();

            // Parse the input date
            calendar.setTime(inputFormatter.parse(dateString));

            // Define the desired output format pattern
            SimpleDateFormat outputFormatter = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
            outputFormatter.setTimeZone(TimeZone.getDefault());

            // Format the parsed date to the desired output format

            return outputFormatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Calendar showDateTimePicker(Context context, Calendar date, TimePickerDialogListener listener) {

        final Calendar currentDate = Calendar.getInstance();

        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                if (listener != null) listener.onTimePicked(date);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

        return date;
    }
}
