package com.saibabui.spotar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.saibabui.spotar.data.TfObjectDetectionClassifier
import com.saibabui.spotar.domain.Classification
import com.saibabui.spotar.presentation.CameraPreview
import com.saibabui.spotar.presentation.ObjectDetectionImageAnalyzer
import com.saibabui.spotar.ui.theme.SpotARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotARTheme {
                var classification by remember {
                    mutableStateOf(emptyList<Classification>())
                }

                val analyzer = remember {
                    ObjectDetectionImageAnalyzer(
                        classifier = TfObjectDetectionClassifier(
                            context = applicationContext,
                            threshold = .6f,
                            maxResults = 5,
                        ),
                        onResults = {
                            classification = it
                        })
                }
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }
                Surface {

                    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                        Box(modifier = Modifier.fillMaxSize()){
                            CameraPreview(
                                controller = controller,
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize()
                            )

                            Text(
                                text = classification.firstOrNull()?.label ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)
                            )
                        }


                        Log.d(
                            "classification",
                            classification.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotARTheme {
        Greeting("Android")
    }
}