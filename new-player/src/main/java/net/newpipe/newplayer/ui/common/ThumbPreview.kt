package net.newpipe.newplayer.ui.common

/* NewPlayer
 *
 * @author Christian Schabesberger
 *
 * Copyright (C) NewPipe e.V. 2024 <code(at)newpipe-ev.de>
 *
 * NewPlayer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPlayer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPlayer.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.graphics.BitmapFactory
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import net.newpipe.newplayer.R
import net.newpipe.newplayer.ui.seeker.SeekerDefaults
import net.newpipe.newplayer.ui.theme.VideoPlayerTheme
import net.newpipe.newplayer.uiModel.NewPlayerUIState

private const val BOX_PADDING = 4

@OptIn(UnstableApi::class)
@Composable

/** @hide */
internal fun ThumbPreview(
    modifier: Modifier = Modifier,
    uiState: NewPlayerUIState,
    thumbSize: Dp = SeekerDefaults.ThumbRadius * 2,
    additionalStartPaddingPxls: Int = 0,
    additionalEndPaddingPxls: Int = 0,
    previewHeight: Dp = 60.dp,
) {

    val thumbSizePxls = with(LocalDensity.current) { thumbSize.toPx() }
    val boxPaddingPxls = with(LocalDensity.current) { BOX_PADDING.dp.toPx() }

    var sliderBoxWidth by remember {
        mutableIntStateOf(-1)
    }

    val aspectRatio = if (uiState.currentSeekPreviewThumbnail != null) {
        uiState.currentSeekPreviewThumbnail.width.toFloat() /
                uiState.currentSeekPreviewThumbnail.height.toFloat()
    } else {
        16f / 9f
    }

    val previewBoxWidthPxls = with(LocalDensity.current) { (previewHeight * aspectRatio).toPx() }

    val previewPosition = additionalStartPaddingPxls + thumbSizePxls / 2 +
            ((sliderBoxWidth - additionalEndPaddingPxls - additionalStartPaddingPxls - thumbSizePxls)
                    * uiState.seekerPosition)

    val edgeCorrectedPreviewPosition =
        if (previewPosition < (previewBoxWidthPxls / 2 + boxPaddingPxls))
            0
        else if ((sliderBoxWidth - (previewBoxWidthPxls / 2 + boxPaddingPxls)) < previewPosition)
            sliderBoxWidth - previewBoxWidthPxls - 2 * boxPaddingPxls
        else
            previewPosition - (previewBoxWidthPxls / 2 + boxPaddingPxls)


    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { rect ->
                sliderBoxWidth = rect.size.width
            }
    ) {
        AnimatedVisibility(
            visible = uiState.seekPreviewVisible && uiState.currentSeekPreviewThumbnail != null,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(400)),
        ) {
            // this allows the current thumbnail to remain when animated visibility is being hidden
            var lastAvailableImage by remember {
                mutableStateOf(uiState.currentSeekPreviewThumbnail)
            }
            if (uiState.currentSeekPreviewThumbnail != null) {
                lastAvailableImage = uiState.currentSeekPreviewThumbnail
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentSize()
                    .offset { IntOffset(edgeCorrectedPreviewPosition.toInt(), 0) },
            ) {
                uiState.currentSeekPreviewChapter?.chapterTitle?.let { chapterTitle ->
                    Text(
                        text = chapterTitle,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier.width(previewHeight * aspectRatio)
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(BOX_PADDING.dp)
                        .height((2 * BOX_PADDING).dp + previewHeight)
                        .aspectRatio(aspectRatio),
                    elevation = CardDefaults.cardElevation(BOX_PADDING.dp)
                ) {
                    lastAvailableImage?.let {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            bitmap = it,
                            contentDescription = stringResource(id = R.string.seek_thumb_preview)
                        )
                    }
                }
            }
        }


        /* This is a little helper block that helps place the thumbnail correctly relative to the
        thumb of the seeker. This is only there for debug reasons.
        Surface(
            modifier = Modifier
                .size(10.dp, 10.dp)
                .offset { IntOffset(previewPosition.toInt(), 200) }, color = Color.Blue
        ) {
        }
        */
    }
}

 @OptIn(UnstableApi::class)
@Preview(device = "spec:width=1080px,height=600px,dpi=440")
@Composable
private fun ThumbPreviewPreview() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    var startOffset by remember { mutableIntStateOf(0) }
    var endOffset by remember { mutableIntStateOf(0) }

    var thumbDown by remember { mutableStateOf(false) }

    val previewThumbnail = null
//        BitmapFactory.decodeResource(LocalContext.current.resources, R.mipmap.thumbnail_preview)


    VideoPlayerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        ) {
            ThumbPreview(
                uiState = NewPlayerUIState.DUMMY.copy(
                    seekerPosition = sliderPosition,
                    seekPreviewVisible = thumbDown,
                    currentSeekPreviewThumbnail = previewThumbnail?.asImageBitmap()
                ), additionalStartPaddingPxls = startOffset, additionalEndPaddingPxls = endOffset,
                thumbSize = 20.dp // see handle width
            )

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Left", modifier = Modifier.onGloballyPositioned {
                    startOffset = it.size.width
                })
                Slider(modifier = Modifier.weight(1f), value = sliderPosition, onValueChange = {
                    thumbDown = true
                    sliderPosition = it
                }, onValueChangeFinished = { thumbDown = false })
                Text(text = "R", modifier = Modifier.onGloballyPositioned {
                    endOffset = it.size.width
                })
            }
        }
    }
}
