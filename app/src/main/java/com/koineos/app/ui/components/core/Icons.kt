package com.koineos.app.ui.components.core

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koineos.app.R

sealed class AppIcon(val unselectedDrawableId: Int, val selectedDrawableId: Int) {

    // Bottom Navigation Bar
    data object Home : AppIcon(R.drawable.ic_home, R.drawable.ic_home_filled)
    data object Learn : AppIcon(R.drawable.ic_book, R.drawable.ic_open_book_filled)
    data object Alphabet : AppIcon(R.drawable.ic_alphabet, R.drawable.ic_alphabet)
    data object Read : AppIcon(R.drawable.ic_read, R.drawable.ic_read_filled)

    // General
    data object MoreHorizontal :
        AppIcon(R.drawable.ic_more_horizontal, R.drawable.ic_more_horizontal)

    data object MoreVertical : AppIcon(R.drawable.ic_more_vertical, R.drawable.ic_more_vertical)
    data object Search : AppIcon(R.drawable.ic_search, R.drawable.ic_search)
    data object Back : AppIcon(R.drawable.ic_back, R.drawable.ic_back)
    data object Menu : AppIcon(R.drawable.ic_menu, R.drawable.ic_menu)
    data object Add : AppIcon(R.drawable.ic_add, R.drawable.ic_add)
    data object Close : AppIcon(R.drawable.ic_close, R.drawable.ic_close)
    data object Check : AppIcon(R.drawable.ic_check, R.drawable.ic_check)

    // Learn
    data object Path : AppIcon(R.drawable.ic_path, R.drawable.ic_path)
    data object Vocabulary : AppIcon(R.drawable.ic_vocabulary, R.drawable.ic_vocabulary_filled)
    data object Practice : AppIcon(R.drawable.ic_practice, R.drawable.ic_practice)
    data object Book : AppIcon(R.drawable.ic_book, R.drawable.ic_book_filled)

    // Alphabet
    data object Alphabet2 : AppIcon(R.drawable.ic_alphabet_2, R.drawable.ic_alphabet_2)
    data object Alphabet3 : AppIcon(R.drawable.ic_alphabet_3, R.drawable.ic_alphabet_3)
}

@Composable
fun IconComponent(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    size: Dp = 24.dp,
    contentDescription: String? = "",
    isSelected: Boolean = false,
    tint: Color = LocalContentColor.current
) {
    Icon(
        modifier = modifier.size(size),
        painter = painterResource(id = if (isSelected) icon.selectedDrawableId else icon.unselectedDrawableId),
        contentDescription = contentDescription,
        tint = tint
    )
}