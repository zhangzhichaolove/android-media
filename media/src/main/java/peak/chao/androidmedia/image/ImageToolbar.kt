package peak.chao.androidmedia.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Default image toolbar with rotate, filter, and crop controls.
 * 
 * @param state ImageViewerState to control
 * @param showRotateButton Show rotate buttons
 * @param showFilterButton Show filter button
 * @param showCropButton Show crop button (not yet fully interactive)
 */
@Composable
fun ImageToolbar(
    state: ImageViewerState,
    showRotateButton: Boolean = true,
    showFilterButton: Boolean = true,
    showCropButton: Boolean = true
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black.copy(alpha = 0.7f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rotate Left
            if (showRotateButton) {
                ToolbarButton(
                    icon = Icons.Default.RotateLeft,
                    label = "左旋转",
                    onClick = { state.rotate90CCW() }
                )
            }
            
            // Rotate Right
            if (showRotateButton) {
                ToolbarButton(
                    icon = Icons.Default.RotateRight,
                    label = "右旋转",
                    onClick = { state.rotate90CW() }
                )
            }
            
            // Filter
            if (showFilterButton) {
                Box {
                    ToolbarButton(
                        icon = Icons.Default.Tune,
                        label = "滤镜",
                        onClick = { showFilterMenu = true }
                    )
                    
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        ImageFilter.entries.forEach { filter ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            when (filter) {
                                                ImageFilter.NONE -> "原图"
                                                ImageFilter.GRAYSCALE -> "黑白"
                                                ImageFilter.SEPIA -> "复古"
                                                ImageFilter.INVERT -> "反色"
                                            }
                                        )
                                        if (state.currentFilter == filter) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Selected",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    state.applyFilter(filter)
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Crop (placeholder - full crop UI would be more complex)
            if (showCropButton) {
                ToolbarButton(
                    icon = Icons.Default.Crop,
                    label = "裁剪",
                    onClick = { 
                        // TODO: Implement interactive crop UI
                        // For now, this is a placeholder
                    }
                )
            }
            
            // Reset
            ToolbarButton(
                icon = Icons.Default.Refresh,
                label = "重置",
                onClick = { state.resetToOriginal() }
            )
        }
    }
}

@Composable
private fun ToolbarButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}
