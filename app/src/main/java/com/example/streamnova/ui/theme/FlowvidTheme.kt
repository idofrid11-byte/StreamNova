package com.example.streamnova.ui.theme

import androidx.compose.runtime.Composable
import com.example.ui.theme.FlowvidDarkColorScheme as BaseColorScheme
import com.example.ui.theme.FlowvidTheme as BaseFlowvidTheme

val FlowvidDarkColorScheme = BaseColorScheme

@Composable
fun FlowvidTheme(content: @Composable () -> Unit) {
    BaseFlowvidTheme(content = content)
}
