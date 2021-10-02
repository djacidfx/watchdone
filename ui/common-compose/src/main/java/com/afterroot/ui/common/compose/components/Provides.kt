/*
 * 2021 AfterROOT
 */
package com.afterroot.ui.common.compose.components

import androidx.compose.runtime.compositionLocalOf
import com.afterroot.watchdone.data.model.LocalUser

val LocalCurrentUser = compositionLocalOf { LocalUser() }