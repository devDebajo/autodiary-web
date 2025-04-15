package ru.debajo.autologbook.web.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.svg.SvgDecoder

internal val LocalImageLoader: ProvidableCompositionLocal<ImageLoader> = staticCompositionLocalOf {
    error("No ImageLoader")
}

@Composable
internal fun rememberImageLoader(): ImageLoader {
    return remember {
        ImageLoader.Builder(PlatformContext.INSTANCE)
            .components {
                add(SvgModelFetcher.Factory())
                add(SvgDecoder.Factory(renderToBitmap = false))
            }
            .build()
    }
}