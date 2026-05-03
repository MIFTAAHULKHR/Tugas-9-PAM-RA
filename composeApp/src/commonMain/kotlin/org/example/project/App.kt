package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
// ✅ HAPUS: import androidx.compose.ui.graphics.Color  (tidak dipakai)
// ✅ HAPUS: import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
// ✅ HAPUS @Preview dari sini — Preview tidak tersedia di commonMain
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SummarizationScreen()
        }
    }
}

@Composable
fun SummarizationScreen(
    viewModel: SummarizationViewModel = viewModel { SummarizationViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "AI Content Summarizer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter content to summarize") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
            placeholder = { Text("Paste your long text here...") }
        )

        Button(
            onClick = { viewModel.summarizeText(inputText) },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is SummarizationUiState.Loading && inputText.isNotBlank()
        ) {
            if (uiState is SummarizationUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Summarizing...")
            } else {
                Text("Summarize with Gemini")
            }
        }

        when (val state = uiState) {
            is SummarizationUiState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Summary:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(text = state.summary)
                    }
                }
            }
            is SummarizationUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            else -> {}
        }

        if (uiState !is SummarizationUiState.Idle) {
            TextButton(onClick = {
                viewModel.reset()
                inputText = ""
            }) {
                Text("Clear All")
            }
        }
    }
}