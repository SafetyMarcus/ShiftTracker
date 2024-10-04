package com.safetymarcus.shifttracker.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val Small = 8.dp

val Medium = 16.dp

val Large = 32.dp

@Composable
fun SmallSpacer() = Spacer(Modifier.size(Small))

@Composable
fun MediumSpacer() = Spacer(Modifier.size(Medium))

@Composable
fun LargeSpacer() = Spacer(Modifier.size(Large))