package com.vivekpanchal.newshub.domain.model

import androidx.annotation.DrawableRes
import com.vivekpanchal.newshub.R

data class UserInterest(
    val name: String,
    @DrawableRes val imageResId: Int,
)

object Categories {
    const val POLITICS = "Politics"
    const val SPORTS = "Sports"
    const val TECHNOLOGY = "Technology"
    const val ENTERTAINMENT = "Entertainment"
    const val SCIENCE = "Science"
    const val FASHION = "Fashion"
    const val TRAVEL = "Travel"
    const val STARTUPS = "Start Ups"

    val ALL: List<UserInterest> = listOf(
        UserInterest(POLITICS, R.drawable.politics),
        UserInterest(ENTERTAINMENT, R.drawable.entertainment),
        UserInterest(FASHION, R.drawable.fashion),
        UserInterest(SCIENCE, R.drawable.science),
        UserInterest(TECHNOLOGY, R.drawable.technology),
        UserInterest(STARTUPS, R.drawable.startup),
        UserInterest(TRAVEL, R.drawable.travel),
        UserInterest(SPORTS, R.drawable.sports),
    )

    val DEFAULT_CHOICES = listOf(TECHNOLOGY, STARTUPS, TRAVEL)
}
