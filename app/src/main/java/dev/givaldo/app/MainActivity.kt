package dev.givaldo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.givaldo.app.ui.theme.ApppretectionTheme
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.IntegrityResult

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApppretectionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Integrity") },
                            actions = {
                                IconButton(onClick = {
                                    viewModel.clearItems()
                                    Integrity.instance.startDetections {
                                        viewModel.onResult(it)
                                    }
                                }, content = {
                                    Icon(Icons.Default.Refresh, contentDescription = "refresh")
                                })
                            }
                        )
                    }
                ) { innerPadding ->
                    val state by viewModel.resultState.collectAsStateWithLifecycle()
                    HomeScreen(modifier = Modifier.padding(innerPadding), state)
                }
            }

        }
    }

}

@Composable
fun HomeScreen(
    modifier: Modifier,
    state: IntegrityCheckState,
) {
    when (state) {
        is IntegrityCheckState.Failure -> Text(text = state.exception.message.toString())
        is IntegrityCheckState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is IntegrityCheckState.Success -> {
            ValidationsResultList(
                modifier = modifier,
                result = state.result
            )
        }
    }
}


@Composable
fun ValidationsResultList(
    modifier: Modifier = Modifier,
    result: List<IntegrityResult>,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = result.sortedBy { it.validationType }) { item ->
            SecurityCheckItem(title = item.validationType.name, state = item.result)
        }
    }
}
