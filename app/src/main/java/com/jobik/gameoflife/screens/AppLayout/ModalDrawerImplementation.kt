package com.jobik.gameoflife.screens.AppLayout

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue

interface ModalDrawer {
    val drawerState: DrawerState
}

object ModalDrawerImplementation : ModalDrawer {
    override val drawerState = DrawerState(initialValue = DrawerValue.Closed)
}