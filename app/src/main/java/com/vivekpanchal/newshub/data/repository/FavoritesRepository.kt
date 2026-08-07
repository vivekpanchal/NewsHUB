package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.data.local.NewsHeadlineDao
import com.vivekpanchal.newshub.data.local.toDomain
import com.vivekpanchal.newshub.data.local.toEntity
import com.vivekpanchal.newshub.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface FavoritesRepository {
    fun getAllFavorites(): Flow<List<Article>>
    fun isFavorite(headline: String): Flow<Boolean>
    suspend fun addFavorite(article: Article)
    suspend fun removeFavorite(article: Article)
}

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dao: NewsHeadlineDao,
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<Article>> =
        dao.loadAllNewsHeadlines().map { entities -> entities.map { it.toDomain() } }

    override fun isFavorite(headline: String): Flow<Boolean> =
        dao.loadNewsByHeadline(headline).map { it != null }

    override suspend fun addFavorite(article: Article) {
        dao.insertNewsHeadline(article.toEntity())
    }

    override suspend fun removeFavorite(article: Article) {
        // Delete matches by primary key, so we must delete the entity actually stored in Room
        // (with its real id) rather than a freshly-mapped one whose id defaults to 0.
        val existing = dao.loadNewsByHeadline(article.headline).first() ?: return
        dao.deleteNewsHeadline(existing)
    }
}
