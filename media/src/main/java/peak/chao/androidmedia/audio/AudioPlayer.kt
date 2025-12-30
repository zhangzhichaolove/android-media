package peak.chao.androidmedia.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Audio Player Composable component.
 * 
 * Provides a compact audio player with customizable controller UI.
 * 
 * @param state AudioPlayerState created via rememberAudioPlayerState()
 * @param modifier Modifier for the container
 * @param controller Custom controller composable, uses default if null
 */
@Composable
fun AudioPlayer(
    state: AudioPlayerState,
    modifier: Modifier = Modifier,
    controller: (@Composable (AudioPlayerState) -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (controller != null) {
                controller(state)
            } else {
                AudioController(state = state)
            }
        }
    }
}
