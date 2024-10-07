package com.safetymarcus.shifttracker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.safetymarcus.shifttracker.models.ShiftType
import kotlinx.datetime.Instant

@Composable
fun NewShift(
    date: Instant,
    onConfirm: (Long?, ShiftType) -> Unit,
    onDismiss: () -> Unit
) {
    val inputState by remember { mutableStateOf(Input.Type) }
    var shiftType by remember { mutableStateOf(ShiftType.EARLY) }
    val textForState by remember {
        derivedStateOf {
            when(shiftType) {
                ShiftType.CUSTOM -> "Continue"
                else -> "Save"
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column {
            when (inputState) {
                Input.Type -> {
                    Button(onClick = { shiftType = ShiftType.EARLY }) { Text("Early") }
                    Button(onClick = { shiftType = ShiftType.LATE }) { Text("Late") }
                    Button(onClick = { shiftType = ShiftType.NIGHT }) { Text("Night") }
//                Button(onClick = { shiftType = ShiftType.CUSTOM }) { Text("Custom") }
                }

                Input.Time -> TODO("Support custom types")
            }

            Button(
                onClick = {
                    when (inputState) {
                        Input.Type -> {
                            onConfirm( //TODO handle custom
                                date.toEpochMilliseconds(), // TODO convert to instant passthrough
                                shiftType,
                            )
                        }

                        Input.Time -> TODO("Support custom types")
                    }
                }
            ) { Text(textForState) }
        }
    }
}

enum class Input {
    Type, Time
}