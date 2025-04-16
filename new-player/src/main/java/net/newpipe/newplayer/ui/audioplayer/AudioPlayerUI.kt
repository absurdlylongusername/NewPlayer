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


package net.newpipe.newplayer.ui.audioplayer

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import net.newpipe.newplayer.R
import net.newpipe.newplayer.ui.common.NewPlayerSeeker
import net.newpipe.newplayer.ui.common.ThumbPreview
import net.newpipe.newplayer.ui.common.Thumbnail
import net.newpipe.newplayer.ui.common.getInsets
import net.newpipe.newplayer.ui.common.getLocale
import net.newpipe.newplayer.ui.common.getTimeStringFromMs
import net.newpipe.newplayer.ui.seeker.SeekerDefaults
import net.newpipe.newplayer.ui.selection_ui.ChapterSelectUI
import net.newpipe.newplayer.ui.selection_ui.ReorderableStreamItemsList
import net.newpipe.newplayer.ui.selection_ui.StreamSelectUI
import net.newpipe.newplayer.ui.theme.VideoPlayerTheme
import net.newpipe.newplayer.uiModel.InternalNewPlayerViewModel
import net.newpipe.newplayer.uiModel.NewPlayerUIState
import net.newpipe.newplayer.uiModel.NewPlayerViewModelDummy
import net.newpipe.newplayer.uiModel.UIModeState


private val UI_ENTER_ANIMATION = fadeIn(tween(200))
private val UI_EXIT_ANIMATION = fadeOut(tween(200))

@Composable

/** @hide */
internal fun lightAudioControlButtonColorScheme() = ButtonDefaults.buttonColors().copy(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onSurface,
    disabledContainerColor = MaterialTheme.colorScheme.background
)

@OptIn(UnstableApi::class)
@Composable

/** @hide */
internal fun AudioPlayerUI(
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState,
    isLandScape: Boolean
) {
    val insets = getInsets()

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = uiState.uiMode == UIModeState.AUDIO_CHAPTER_SELECT,
            enter = UI_ENTER_ANIMATION,
            exit = UI_EXIT_ANIMATION
        ) {
            ChapterSelectUI(viewModel = viewModel, uiState = uiState, shownInAudioPlayer = true)
        }

        AnimatedVisibility(
            visible = uiState.uiMode == UIModeState.AUDIO_STREAM_SELECT,
            enter = UI_ENTER_ANIMATION,
            exit = UI_EXIT_ANIMATION
        ) {
            StreamSelectUI(viewModel = viewModel, uiState = uiState, shownInAudioPlayer = true)
        }

        AnimatedVisibility(
            visible = uiState.uiMode == UIModeState.EMBEDDED_AUDIO,
            enter = UI_ENTER_ANIMATION,
            exit = UI_EXIT_ANIMATION
        ) {
            AudioPlayerEmbeddedUI(viewModel = viewModel, uiState = uiState)
        }

        AnimatedVisibility(
            uiState.uiMode == UIModeState.FULLSCREEN_AUDIO,
            enter = UI_ENTER_ANIMATION,
            exit = UI_EXIT_ANIMATION
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(insets),
            ) { innerPadding ->
                if (isLandScape) {
                    LandscapeLayout(
                        viewModel = viewModel,
                        uiState = uiState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp),
                    )
                } else {
                    PortraitLayout(
                        viewModel = viewModel,
                        uiState = uiState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun LandscapeLayout(
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {

            LandscapeCoverArtOrPlaylistUI(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                uiState = uiState,
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                AudioPlaybackController(viewModel = viewModel, uiState = uiState)
                ProgressUI(viewModel = viewModel, uiState = uiState)
                AudioBottomUI(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun PortraitLayout(
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        PortraitCoverArtOrPlaylistUI(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            viewModel = viewModel,
            uiState = uiState,
            isPlaylistView = true
        )
        AudioPlaybackController(viewModel = viewModel, uiState = uiState)
        ProgressUI(viewModel = viewModel, uiState = uiState)
        AudioBottomUI(viewModel, uiState)
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun PortraitCoverArtOrPlaylistUI(
    modifier: Modifier = Modifier,
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState,
    isPlaylistView: Boolean = false
) {

    if (isPlaylistView) {
        ReorderableStreamItemsList(
            modifier = modifier
                .fillMaxSize(),
            viewModel = viewModel,
            uiState = uiState
        )
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CoverArt(uiState = uiState)
            Spacer(modifier = Modifier.height(24.dp))
            TitleView(uiState = uiState)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun LandscapeCoverArtOrPlaylistUI(
    modifier: Modifier = Modifier,
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState,
    isPlaylistView: Boolean = false
) {
    Column(modifier = modifier) {
        TitleView(
            modifier = Modifier
                .fillMaxWidth(),
            uiState = uiState,
        )

        Spacer(modifier = Modifier.height(8.dp))

        CoverArt(
            uiState = uiState
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ProgressUI(
    modifier: Modifier = Modifier,
    viewModel: InternalNewPlayerViewModel,
    uiState: NewPlayerUIState
) {
    val locale = getLocale()!!

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(0.dp)
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true, align = Alignment.Bottom)
        ) {
            ThumbPreview(
                modifier = Modifier.offset(y = (-20).dp) /* We have this offset to make space for your thumb */,
                uiState = uiState,
                thumbSize = SeekerDefaults.ThumbRadius * 2,
                previewHeight = 120.dp
            )
        }

        NewPlayerSeeker(viewModel = viewModel, uiState = uiState)
        Row {
            Text(
                getTimeStringFromMs(
                    uiState.playbackPositionInMs,
                    getLocale() ?: locale
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                getTimeStringFromMs(
                    uiState.durationInMs,
                    getLocale() ?: locale
                )
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun TitleView(modifier: Modifier = Modifier, uiState: NewPlayerUIState) {
    Column(modifier = modifier) {
        Text(
            text = uiState.currentlyPlaying?.mediaMetadata?.title.toString(),
            maxLines = 1,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.basicMarquee(),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = uiState.currentlyPlaying?.mediaMetadata?.artist.toString(),
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.basicMarquee(),
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun CoverArt(modifier: Modifier = Modifier, uiState: NewPlayerUIState) {
    Box(modifier = modifier) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Thumbnail(
                modifier = Modifier.fillMaxWidth(),
                thumbnail = uiState.currentlyPlaying?.mediaMetadata?.artworkUri,
                contentDescription = stringResource(
                    id = R.string.stream_thumbnail
                ),
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Preview(device = "id:pixel_6", showSystemUi = true)
@Composable
private fun AudioPlayerUIPortraitPreview() {
    VideoPlayerTheme {
        AudioPlayerUI(
            viewModel = NewPlayerViewModelDummy(),
            uiState = NewPlayerUIState.DUMMY.copy(uiMode = UIModeState.FULLSCREEN_AUDIO),
            isLandScape = false
        )
    }
}

@OptIn(UnstableApi::class)
@Preview(device = "spec:parent=pixel_6,orientation=landscape", showSystemUi = true)
@Composable
private fun AudioPlayerUILandscapePreview() {
    VideoPlayerTheme {
        AudioPlayerUI(
            viewModel = NewPlayerViewModelDummy(),
            uiState = NewPlayerUIState.DUMMY.copy(uiMode = UIModeState.FULLSCREEN_AUDIO),
            isLandScape = true
        )
    }
}

@OptIn(UnstableApi::class)
@Preview(device = "spec:parent=pixel_6,orientation=portrait", showSystemUi = true)
@Composable
private fun AudioPlayerUIEmbeddedPreview() {
    VideoPlayerTheme {
        AudioPlayerUI(
            viewModel = NewPlayerViewModelDummy(),
            uiState = NewPlayerUIState.DUMMY.copy(uiMode = UIModeState.EMBEDDED_AUDIO),
            isLandScape = false
        )
    }
}