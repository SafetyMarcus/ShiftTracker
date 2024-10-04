package com.safetymarcus.shifttracker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.safetymarcus.shifttracker.models.ShiftType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewShift(
    startingMonth: Int,
    startingDate: Int,
    onConfirm: (Long?, ShiftType) -> Unit,
    onDismiss: () -> Unit
) {
    val initialDate by rememberInitialDate(startingMonth, startingDate)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Picker
    )
    var inputState by remember { mutableStateOf(Input.Date) }
    var shiftType by remember { mutableStateOf(ShiftType.EARLY) }
    val textForState by remember {
        derivedStateOf {
            when {
                inputState == Input.Date || shiftType == ShiftType.CUSTOM -> "Continue"
                else -> "Save"
            }
        }
    }

    DatePickerDialog(
        confirmButton = {
            Button(
                onClick = {
                    when (inputState) {
                        Input.Date -> inputState = Input.Type
                        Input.Type -> {
                            onConfirm( //TODO handle custom
                                datePickerState.selectedDateMillis,
                                shiftType
                            )
                        }

                        Input.Time -> TODO("Support custom types")
                    }
                }
            ) { Text(textForState) }
        },
        onDismissRequest = onDismiss
    ) {
        when (inputState) {
            Input.Date -> DatePicker(state = datePickerState)
            Input.Type -> Column {
                Button(onClick = { shiftType = ShiftType.EARLY }) { Text("Early") }
                Button(onClick = { shiftType = ShiftType.LATE }) { Text("Late") }
                Button(onClick = { shiftType = ShiftType.NIGHT }) { Text("Night") }
//                Button(onClick = { shiftType = ShiftType.CUSTOM }) { Text("Custom") }
            }

            Input.Time -> TODO("Support custom types")
        }
    }
}

@Composable
private fun rememberInitialDate(startingMonth: Int, startingDate: Int): MutableState<Instant> {
    //+1 for 0 start
    val month = "${startingMonth + 1}".padStart(2, '0')
    val day = "${startingDate + 1}".padStart(2, '0')
    //Z because date picker is in UTC
    return remember {
        mutableStateOf(
            LocalDateTime
                .parse("2024-$month-${day}T00:00:00")
                .toInstant(TimeZone.UTC)
        )
    }
}

enum class Input {
    Date, Type, Time
}