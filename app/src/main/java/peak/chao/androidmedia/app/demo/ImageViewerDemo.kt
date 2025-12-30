package peak.chao.androidmedia.app.demo

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import peak.chao.androidmedia.image.ImageViewer
import peak.chao.androidmedia.image.rememberImageViewerState
import java.net.URL

/**
 * Demo screen for ImageViewer component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerDemo(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    // Load sample image
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                // Sample image URL
                val url = URL("https://picsum.photos/1920/1080")
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
                    ?.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    val imageState = rememberImageViewerState(bitmap)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("图片查看器") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ImageViewer(
                state = imageState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
