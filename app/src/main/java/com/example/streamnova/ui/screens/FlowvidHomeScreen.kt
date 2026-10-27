package com.example.streamnova.ui.screens

import androidx.compose.runtime.Composable
import com.example.data.model.MetaItem
import com.example.ui.screens.FlowvidCard as BaseFlowvidCard
import com.example.ui.screens.FlowvidHomeScreen as BaseFlowvidHomeScreen
import com.example.ui.screens.FlowvidStreamPickerModal as BaseFlowvidStreamPickerModal
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun FlowvidHomeScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit,
    onPlayDirect: (streamUrl: String, title: String) -> Unit = { _, _ -> },
    onSwitchToTvMode: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    BaseFlowvidHomeScreen(
        viewModel = viewModel,
        onItemClick = onItemClick,
        onPlayDirect = onPlayDirect,
        onSwitchToTvMode = onSwitchToTvMode,
        onSearchClick = onSearchClick
    )
}

@Composable
fun FlowvidCard(
    meta: MetaItem,
    onClick: () -> Unit,
    onStreamClick: () -> Unit = {}
) {
    BaseFlowvidCard(
        meta = meta,
        onClick = onClick,
        onStreamClick = onStreamClick
    )
}
