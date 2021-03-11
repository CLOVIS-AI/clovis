package clovis.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clovis.core.api.Profile
import clovis.core.api.User

@Suppress("FunctionName")
@Composable
fun HomeScreen() {
	MaterialTheme {
		Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
			UserView(User(5, Profile("Test 1", "test@email.com")))
		}
	}
}
