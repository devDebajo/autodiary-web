package ru.debajo.autologbook.web.common

import autodiary_web.composeapp.generated.resources.Res
import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import okio.Buffer
import org.jetbrains.compose.resources.ExperimentalResourceApi

internal class SvgModelFetcher(
    private val svgModel: SvgModel,
    private val options: Options,
) : Fetcher {

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun fetch(): FetchResult {
        val bytes = Res.readBytes(svgModel.path)
        return SourceFetchResult(
            source = ImageSource(
                source = Buffer().apply { write(bytes) },
                fileSystem = options.fileSystem,
            ),
            mimeType = "image/svg+xml",
            dataSource = DataSource.MEMORY,
        )
    }

    class Factory : Fetcher.Factory<SvgModel> {
        override fun create(
            data: SvgModel,
            options: Options,
            imageLoader: ImageLoader,
        ): Fetcher {
            return SvgModelFetcher(data, options)
        }
    }
}
