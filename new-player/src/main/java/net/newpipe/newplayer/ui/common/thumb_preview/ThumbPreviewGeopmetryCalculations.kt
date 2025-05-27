package net.newpipe.newplayer.ui.common.thumb_preview

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import net.newpipe.newplayer.uiModel.NewPlayerUIState

/** @hide */
internal data class ThumbnailGeometry(
    val aspectRatio: Float, val edgeCorrectedPreviewPosition: Int
)

/** @hide */
@OptIn(UnstableApi::class)
@Composable
internal fun calculateThumbnailPreviewGeometry(
    uiState: NewPlayerUIState,
    thumbSize: Dp,
    previewHeight: Dp,
    sliderBoxWidth: Int,
    additionalStartPaddingPxls: Int,
    additionalEndPaddingPxls: Int,
): ThumbnailGeometry {
    val thumbSizePxls = with(LocalDensity.current) { thumbSize.toPx() }
    val boxPaddingPxls = with(LocalDensity.current) { PREVIEW_BOX_PADDING.dp.toPx() }

    val aspectRatio = if (uiState.currentSeekPreviewThumbnail != null) {
        uiState.currentSeekPreviewThumbnail.width.toFloat() / uiState.currentSeekPreviewThumbnail.height.toFloat()
    } else {
        16f / 9f
    }

    val previewBoxWidthPxls = with(LocalDensity.current) { (previewHeight * aspectRatio).toPx() }

    val previewPosition =
        additionalStartPaddingPxls + thumbSizePxls / 2 + ((sliderBoxWidth - additionalEndPaddingPxls - additionalStartPaddingPxls - thumbSizePxls) * uiState.seekerPosition)

    val edgeCorrectedPreviewPosition =
        if (previewPosition < (previewBoxWidthPxls / 2 + boxPaddingPxls)) 0
        else if ((sliderBoxWidth - (previewBoxWidthPxls / 2 + boxPaddingPxls)) < previewPosition) sliderBoxWidth - previewBoxWidthPxls - 2 * boxPaddingPxls
        else previewPosition - (previewBoxWidthPxls / 2 + boxPaddingPxls)

    return ThumbnailGeometry(
        aspectRatio, edgeCorrectedPreviewPosition.toInt()
    )
}