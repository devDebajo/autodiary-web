package ru.debajo.autologbook.web

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autodiary_web.composeapp.generated.resources.Res
import autodiary_web.composeapp.generated.resources.app_screenshot_1_description
import autodiary_web.composeapp.generated.resources.app_screenshot_2_description
import autodiary_web.composeapp.generated.resources.app_screenshot_3_description
import autodiary_web.composeapp.generated.resources.app_screenshot_4_description
import autodiary_web.composeapp.generated.resources.app_screenshot_5_description
import autodiary_web.composeapp.generated.resources.ruda
import autodiary_web.composeapp.generated.resources.subtitle
import autodiary_web.composeapp.generated.resources.title
import coil3.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import ru.debajo.autologbook.web.common.LocalImageLoader
import ru.debajo.autologbook.web.common.rememberImageLoader
import ru.debajo.autologbook.web.common.rememberResUri
import ru.debajo.autologbook.web.common.rememberSvgModel
import ru.debajo.autologbook.web.theme.AppTheme

private val AppScreenshots: List<String> = listOf(
    "files/app-screenshot-1.png",
    "files/app-screenshot-2.png",
    "files/app-screenshot-3.png",
    "files/app-screenshot-4.png",
    "files/app-screenshot-5.png",
)

private val AppScreenshotsDescriptions: List<StringResource> = listOf(
    Res.string.app_screenshot_1_description,
    Res.string.app_screenshot_2_description,
    Res.string.app_screenshot_3_description,
    Res.string.app_screenshot_4_description,
    Res.string.app_screenshot_5_description,
)

@Composable
internal fun App() = AppTheme {
    val defaultTextStyle = LocalTextStyle.current
    val rudaFont = Font(Res.font.ruda)
    val rudaFontFamily = remember(rudaFont) { FontFamily(rudaFont) }
    val fontStyle = remember(defaultTextStyle, rudaFontFamily) {
        defaultTextStyle.copy(fontFamily = rudaFontFamily)
    }
    CompositionLocalProvider(
        LocalImageLoader provides rememberImageLoader(),
        LocalTextStyle provides fontStyle
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxHeight = maxHeight
            if (maxWidth >= maxHeight) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FirstPane(65.dp)
                    Spacer(Modifier.size(80.dp))
                    SecondPane(height = maxHeight * 0.75f)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FirstPane(45.dp)
                    Spacer(Modifier.size(30.dp))
                    SecondPane(height = 530.dp)
                }
            }
        }
    }
}

@Composable
private fun FirstPane(
    buttonHeight: Dp,
    modifier: Modifier = Modifier,
) {
    var columnWidth by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
            .onSizeChanged { columnWidth = it.width }
    ) {
        Text(
            text = stringResource(Res.string.title),
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.size(12.dp))
        Text(
            text = stringResource(Res.string.subtitle),
            fontSize = 20.sp,
        )
        Spacer(Modifier.size(30.dp))
        val uriHandler = LocalUriHandler.current
        StoresButtons(
            modifier = Modifier
                .width(columnWidth.toDp()),
            buttonHeight = buttonHeight,
            onClick = { uriHandler.openUri(it) }
        )
    }
}

@Composable
private fun Int.toDp(): Dp {
    return with(LocalDensity.current) {
        toDp()
    }
}

@Composable
private fun SecondPane(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val buttonOffset = 10.dp
    val buttonSize = 56.dp
    val maxWidth = (buttonOffset * 2) + (buttonSize * 2) + calculatePhoneWidth(height)
    val controller = rememberPagerStateController()
    val currentPage by controller.currentPage.collectAsState()
    Column(
        modifier = modifier.width(maxWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val previousButtonVisible = currentPage != 0
            PagerButton(
                modifier = Modifier.padding(end = buttonOffset).alpha(if (previousButtonVisible) 1f else 0f),
                imageVector = Icons.Default.ArrowBack,
                enabled = previousButtonVisible,
                onClick = { controller.previousPage() },
            )
            PhonePager(
                height = height,
                controller = controller,
            )
            val nextButtonVisible = currentPage != controller.pagerState.pageCount - 1
            PagerButton(
                modifier = Modifier.padding(start = buttonOffset).alpha(if (nextButtonVisible) 1f else 0f),
                imageVector = Icons.Default.ArrowForward,
                enabled = nextButtonVisible,
                onClick = { controller.nextPage() },
            )
        }

        Spacer(Modifier.height(30.dp))
        AnimatedContent(
            targetState = AppScreenshotsDescriptions[currentPage]
        ) { description ->
            Text(
                maxLines = 3,
                minLines = 3,
                text = stringResource(description),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun calculatePhoneWidth(height: Dp): Dp {
    return height / 2.113f
}

@Composable
private fun PhonePager(
    height: Dp,
    controller: PagerStateController,
    modifier: Modifier = Modifier,
) {
    val width = calculatePhoneWidth(height)
    val shape = remember { RoundedCornerShape(17) }
    BoxWithConstraints(
        modifier = modifier
            .size(width, height)
            .hover()
    ) {
        val actualWidth = maxWidth
        val actualHeight = actualWidth * 2.113f
        Spacer(
            modifier = Modifier
                .align(Alignment.Center)
                .size(actualWidth, actualHeight)
                .shadow(20.dp, shape = shape),
        )

        HorizontalPager(
            modifier = Modifier
                .align(Alignment.Center)
                .height(actualHeight * 0.97f)
                .padding(horizontal = 2.dp)
                .clip(shape)
                .background(Color(0xFF121414)),
            userScrollEnabled = false,
            state = controller.pagerState,
        ) { index ->
            val path = AppScreenshots[index]
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(),
                    model = rememberResUri(path),
                    imageLoader = LocalImageLoader.current,
                    contentDescription = null,
                )
            }
        }

        AsyncImage(
            modifier = Modifier.matchParentSize(),
            model = rememberResUri("files/phone-mockup.webp"),
            imageLoader = LocalImageLoader.current,
            contentDescription = null,
        )
    }
}

@Composable
private fun PagerButton(
    imageVector: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.2f))
    ) {
        Icon(
            imageVector = imageVector,
            tint = Color.White,
            contentDescription = null,
        )
    }
}

private fun Modifier.hover(enabled: Boolean = true): Modifier {
    return composed {
        val hovered = remember { mutableStateOf(false) }
        val hoverInteractionSource = remember { MutableInteractionSource() }
        LaunchedEffect(hoverInteractionSource, hovered) {
            hoverInteractionSource.interactions.filterIsInstance<HoverInteraction>().collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hovered.value = true
                    is HoverInteraction.Exit -> hovered.value = false
                }
            }
        }
        val scale by animateFloatAsState(
            targetValue = if (enabled && hovered.value) 1.05f else 1.0f,
            animationSpec = tween(500),
        )

        hoverable(hoverInteractionSource, enabled).scale(scale)
    }
}

@Composable
private fun StoresButtons(
    buttonHeight: Dp,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        StoreButton(
            modifier = Modifier.height(buttonHeight).alpha(0.5f),
            enabled = false,
            path = "files/google-play-soon-badge.svg",
            onClick = { }
        )
        Spacer(Modifier.size(16.dp))
        StoreButton(
            modifier = Modifier.height(buttonHeight),
            path = "files/rustore-badge.svg",
            onClick = { onClick("https://www.rustore.ru/catalog/app/ru.debajo.autologbook.androidApp") }
        )
    }
}

@Composable
private fun StoreButton(
    path: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier
            .hover(enabled)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = null,
                indication = null,
            ),
        model = rememberSvgModel(path),
        imageLoader = LocalImageLoader.current,
        contentDescription = null,
    )
}

@Composable
private fun rememberPagerStateController(): PagerStateController {
    val pagerState = rememberPagerState { AppScreenshots.size }
    val coroutineScope = rememberCoroutineScope()
    return remember(pagerState, coroutineScope) {
        PagerStateController(pagerState, coroutineScope)
    }
}

@Stable
internal class PagerStateController(
    val pagerState: PagerState,
    private val coroutineScope: CoroutineScope,
) {
    private val _currentPage: MutableStateFlow<Int> = MutableStateFlow(0)
    private var autoTimerJob: Job? = null
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    init {
        scheduleNextPage()

        coroutineScope.launch {
            currentPage.collect { index ->
                autoTimerJob?.cancel()
                if (index == pagerState.currentPage - 1 || index == pagerState.currentPage + 1) {
                    pagerState.animateScrollToPage(index, animationSpec = tween(1000))
                } else {
                    pagerState.scrollToPage(index)
                }
                scheduleNextPage()
            }
        }
    }

    fun previousPage() {
        if (!pagerState.isScrollInProgress) {
            moveNext(-1)
        }
    }

    fun nextPage() {
        if (!pagerState.isScrollInProgress) {
            moveNext(1)
        }
    }

    private fun moveNext(delta: Int) {
        _currentPage.value = (_currentPage.value + delta + pagerState.pageCount) % pagerState.pageCount
    }

    private fun scheduleNextPage() {
        autoTimerJob?.cancel()
        autoTimerJob = coroutineScope.launch {
            delay(5000)
            moveNext(1)
        }
    }
}
