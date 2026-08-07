package com.vivekpanchal.newshub.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.formatNewsDate

@Composable
fun NewsListItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = stringResource(R.string.news_image_content_desc),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.error_news_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        // colorStops uses fractions (0f-1f) of the gradient's bounds, unlike
                        // startY/endY which are absolute pixels - use stops so the fade starts
                        // 35% down the card regardless of its measured height.
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                0.35f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.75f),
                            ),
                        ),
                    ),
            )
            Text(
                text = article.headline,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            )
            Text(
                text = formatNewsDate(article.publishedAt),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 8.dp),
            )
        }
    }
}
