package com.digitalsamurai.automaton.sample.core.views

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun MyTextField(
    value: String,
    modifier: Modifier = Modifier,
    textFieldName: String = "TextField",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    enabled: Boolean = true,
    placeholder: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        enabled = enabled,
        isError = isError,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged({ focusState ->
        }),
        placeholder = placeholder,
        visualTransformation = visualTransformation
    )
}