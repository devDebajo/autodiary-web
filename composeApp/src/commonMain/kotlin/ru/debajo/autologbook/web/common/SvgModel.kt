package ru.debajo.autologbook.web.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import autodiary_web.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Immutable
internal data class SvgModel(val path: String)

@Composable
internal fun rememberSvgModel(path: String): SvgModel {
    return remember(path) { SvgModel(path) }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun rememberResUri(path: String): String {
    return remember(path) { Res.getUri(path) }
}
