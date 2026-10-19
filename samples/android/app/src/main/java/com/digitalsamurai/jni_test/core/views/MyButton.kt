package com.digitalsamurai.automaton.sample.core.views

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    buttonName: String,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier.testTag(buttonName),
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.wrapContentSize().align(Alignment.CenterVertically),
                    trackColor = MaterialTheme.colorScheme.background
                )
            } else {
                content()
            }
        },
    )
}