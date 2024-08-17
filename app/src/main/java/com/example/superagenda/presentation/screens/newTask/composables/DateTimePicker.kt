package com.example.superagenda.presentation.screens.newTask.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.util.Calendar

@Composable
fun DateTimePicker(
   context: Context,
   initialDateTime: LocalDateTime,
   onDateTimePicked: (LocalDateTime) -> Unit
) {
   val now = Calendar.getInstance()

   // Show DatePicker first
   DatePickerDialog(
      context,
      { _, year, month, dayOfMonth ->
         // Once a date is picked, update LocalDateTime
         val updatedDateTime = initialDateTime
            .withYear(year)
            .withMonth(month + 1) // Month is 0-indexed
            .withDayOfMonth(dayOfMonth)

         // Then show TimePicker
         TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
               // Once time is picked, update LocalDateTime
               val finalDateTime = updatedDateTime
                  .withHour(hourOfDay)
                  .withMinute(minute)

               // Return the updated LocalDateTime
               onDateTimePicked(finalDateTime)
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true // Use 24-hour format
         ).show()

      },
      now.get(Calendar.YEAR),
      now.get(Calendar.MONTH),
      now.get(Calendar.DAY_OF_MONTH)
   ).show()
}