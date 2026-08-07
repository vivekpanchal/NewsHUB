package com.vivekpanchal.newshub.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Categories
import com.vivekpanchal.newshub.ui.theme.NewsHubPrimary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OnboardingScreen(
    onNavigateToMain: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                OnboardingEffect.NavigateToMain -> onNavigateToMain()
                is OnboardingEffect.ShowMessage ->
                    Toast.makeText(context, effect.messageResId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewsHubPrimary),
    ) {
        Text(
            text = stringResource(R.string.splash_screen_choose_interests),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
        ) {
            items(Categories.ALL) { interest ->
                val isSelected = state.selectedChoices.contains(interest.name)
                Card(
                    onClick = { viewModel.setIntent(OnboardingIntent.ToggleChoice(interest.name)) },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.padding(8.dp).aspectRatio(1f),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(interest.imageResId),
                            contentDescription = stringResource(R.string.content_description_user_interest_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (isSelected) Color.Black.copy(alpha = 0.6f) else NewsHubPrimary.copy(alpha = 0.35f),
                                ),
                        )
                        Text(
                            text = interest.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }

        Text(
            text = if (state.selectedChoices.isEmpty()) {
                stringResource(R.string.splash_screen_no_choices_selected)
            } else {
                state.selectedChoices.joinToString("   ")
            },
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )
        Text(
            text = "${state.remainingChoices} ${stringResource(R.string.numOfChoices)}",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )

        Button(
            onClick = { viewModel.setIntent(OnboardingIntent.ConfirmChoices) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(stringResource(R.string.splash_screen_next_btn))
        }
    }
}
