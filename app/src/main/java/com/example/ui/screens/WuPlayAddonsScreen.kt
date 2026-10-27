package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun WuPlayAddonsScreen(
    viewModel: StreamNovaViewModel = viewModel()
) {
    AddonManagerScreen(viewModel = viewModel)
}
