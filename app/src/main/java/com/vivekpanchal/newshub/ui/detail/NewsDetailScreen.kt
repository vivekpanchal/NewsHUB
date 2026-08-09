package com.vivekpanchal.newshub.ui.detail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vivekpanchal.newshub.BuildConfig
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.ui.common.BannerAdView
import com.vivekpanchal.newshub.ui.common.PreviewSampleData
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import com.vivekpanchal.newshub.util.formatNewsDate
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    onBack: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NewsDetailEffect.ShowMessage ->
                    Toast.makeText(context, effect.messageResId, Toast.LENGTH_SHORT).show()
                is NewsDetailEffect.ShareUrl -> {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, effect.url)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, null))
                }
                is NewsDetailEffect.OpenUrlInBrowser -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                    context.startActivity(
                        Intent.createChooser(browserIntent, context.getString(R.string.intent_action_open)),
                    )
                }
            }
        }
    }

    NewsDetailContent(
        state = state,
        onBack = onBack,
        onToggleFavorite = { viewModel.setIntent(NewsDetailIntent.ToggleFavorite) },
        onShare = { viewModel.setIntent(NewsDetailIntent.Share) },
        onOpenInBrowser = { viewModel.setIntent(NewsDetailIntent.OpenInBrowser) },
        bannerAd = { BannerAdView(adUnitId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID, modifier = Modifier.padding(bottom = 16.dp)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsDetailContent(
    state: NewsDetailState,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onOpenInBrowser: () -> Unit,
    // Slotted out rather than called directly, so previews don't have to spin up a real AdView.
    bannerAd: @Composable () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            AsyncImage(
                model = state.article.imageUrl,
                contentDescription = stringResource(R.string.news_image_content_desc),
                placeholder = painterResource(R.drawable.news_placeholder),
                error = painterResource(R.drawable.error_news_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(280.dp),
            )

            Card(
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = state.article.headline,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    DetailLabelRow(
                        labelRes = R.string.date_label,
                        value = formatNewsDate(state.article.publishedAt),
                    )
                    DetailLabelRow(
                        labelRes = R.string.author_label,
                        value = state.article.authorName?.ifBlank { null }
                            ?: stringResource(R.string.no_author_message),
                    )

                    Text(
                        text = stringResource(R.string.description_label),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                    Text(
                        text = state.article.description?.ifBlank { null }
                            ?: stringResource(R.string.no_description_message),
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    Text(
                        text = state.article.newsSource?.ifBlank { null }
                            ?.let { stringResource(R.string.source_label) + " " + it }
                            ?: stringResource(R.string.no_source_available),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 12.dp).align(Alignment.End),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                DetailActionButton(
                    icon = if (state.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    labelRes = R.string.title_mark_favorite,
                    onClick = onToggleFavorite,
                )
                DetailActionButton(
                    icon = Icons.Filled.Share,
                    labelRes = R.string.title_share,
                    onClick = onShare,
                )
                DetailActionButton(
                    icon = Icons.Filled.OpenInBrowser,
                    labelRes = R.string.title_open_in_browser,
                    onClick = onOpenInBrowser,
                )
            }

            bannerAd()
        }
    }
}

@Composable
private fun DetailLabelRow(labelRes: Int, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(labelRes),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 4.dp),
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun DetailActionButton(
    icon: ImageVector,
    labelRes: Int,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = stringResource(labelRes))
        }
        Text(text = stringResource(labelRes), style = MaterialTheme.typography.labelSmall)
    }
}

@PreviewLightDark
@Composable
private fun NewsDetailContentPreview() {
    NewsHubPreviewSurface {
        NewsDetailContent(
            state = NewsDetailState(article = PreviewSampleData.standardArticle, isFavorite = false),
            onBack = {},
            onToggleFavorite = {},
            onShare = {},
            onOpenInBrowser = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun NewsDetailContentFavoritedPreview() {
    NewsHubPreviewSurface {
        NewsDetailContent(
            state = NewsDetailState(article = PreviewSampleData.breakingArticle, isFavorite = true),
            onBack = {},
            onToggleFavorite = {},
            onShare = {},
            onOpenInBrowser = {},
        )
    }
}
