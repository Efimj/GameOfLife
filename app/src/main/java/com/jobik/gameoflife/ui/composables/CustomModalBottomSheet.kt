package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    state: SheetState,
    onCancel: () -> Unit = {},
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    dragHandle: @Composable() (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable() (ColumnScope.() -> Unit)
) {
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onCancel,
        shape = shape,
        dragHandle = dragHandle,
        contentWindowInsets = { windowInsets },
        properties = properties
    ) {
        content()
    }
}