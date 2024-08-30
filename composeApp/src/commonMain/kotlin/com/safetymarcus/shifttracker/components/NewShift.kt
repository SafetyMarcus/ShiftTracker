package com.safetymarcus.shifttracker.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewShift(
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    DatePickerDialog(
        confirmButton = {
            Button(
                onClick = { onConfirm(state.selectedDateMillis) }
            ) { Text("Save") }
        },
        onDismissRequest = onDismiss
    ) { DatePicker(state = state) }
}