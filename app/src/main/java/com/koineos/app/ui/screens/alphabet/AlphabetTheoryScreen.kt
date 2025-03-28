package com.koineos.app.ui.screens.alphabet

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koineos.app.R
import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.presentation.model.practice.alphabet.theory.AlphabetTheoryScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.theory.TheoryEntityUiState
import com.koineos.app.presentation.viewmodel.AlphabetTheoryViewModel
import com.koineos.app.ui.components.cards.CardPadding
import com.koineos.app.ui.components.cards.RegularCard
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.HeadlineLarge
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.components.core.NestedScreenScaffold
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.core.RegularText
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

@Composable
fun AlphabetTheoryScreen(
    onStartPractice: () -> Unit,
    onClose: () -> Unit,
    viewModel: AlphabetTheoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NestedScreenScaffold {
        AlphabetTheoryScreenContent(
            uiState = uiState,
            onStartPractice = onStartPractice,
            onClose = onClose
        )
    }
}

@Composable
private fun AlphabetTheoryScreenContent(
    uiState: AlphabetTheoryScreenUiState,
    onStartPractice: () -> Unit,
    onClose: () -> Unit
) {
    val listState = rememberLazyListState()
    val dividerValue by remember {
        derivedStateOf {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
            val totalScroll = if (firstVisibleItemIndex > 0) {
                firstVisibleItemIndex * 100 + firstVisibleItemOffset
            } else {
                firstVisibleItemOffset
            }
            (totalScroll.toFloat() / 40f).coerceIn(0f, 1f)
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
        topBar = {
            TheoryTopBar(
                dividerValue = dividerValue,
                onClose = onClose
            )
        },
        bottomBar = {
            TheoryBottomBar(
                onStartPractice = {
                    if (uiState is AlphabetTheoryScreenUiState.Loaded) {
                        onStartPractice()
                    }
                }
            )
        },
        containerColor = Colors.Surface
    ) { paddingValues ->
        when (uiState) {
            is AlphabetTheoryScreenUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Colors.Primary)
                }
            }

            is AlphabetTheoryScreenUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(Dimensions.paddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error Loading Content",
                        style = Typography.headlineMedium,
                        color = Colors.Error,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    Text(
                        text = uiState.message,
                        style = Typography.bodyLarge,
                        color = Colors.OnSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is AlphabetTheoryScreenUiState.Loaded -> {
                TheoryContent(
                    titleResId = uiState.titleResId,
                    introTextResId = uiState.introTextResId,
                    entities = uiState.entities,
                    listState = listState,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun TheoryTopBar(
    modifier: Modifier = Modifier,
    dividerValue: Float = 0f,
    onClose: () -> Unit
) {

    Surface(
        color = Colors.Surface,
        shadowElevation = (dividerValue * 4).dp
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimensions.paddingLarge,
                        end = Dimensions.paddingMedium,
                        top = Dimensions.paddingMedium,
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    style = Typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = Colors.TextTertiary,
                    text = "Learn the alphabet".uppercase()
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    IconComponent(
                        icon = AppIcon.Close,
                        contentDescription = "Close practice",
                        tint = Colors.OnSurface
                    )
                }
            }
            if (dividerValue > 0f) {
                HorizontalDivider(
                    thickness = (dividerValue * 1).dp,
                    color = Colors.RegularCardBorder
                )
            }
        }
    }
}

@Composable
fun TheoryBottomBar(
    onStartPractice: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Colors.Surface,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(thickness = 1.dp, color = Colors.RegularCardBorder)

            RegularButton(
                onClick = onStartPractice,
                text = "Start learning",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimensions.paddingLarge,
                        vertical = Dimensions.paddingLarge
                    )
            )
        }
    }
}

@Composable
private fun TheoryContent(
    @StringRes titleResId: Int,
    @StringRes introTextResId: Int,
    entities: List<TheoryEntityUiState>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingLarge),
        contentPadding = PaddingValues(
            top = Dimensions.paddingLarge,
            bottom = Dimensions.paddingXLarge
        ),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        item {
            HeadlineLarge(
                text = stringResource(id = titleResId),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            RegularText(
                text = stringResource(id = introTextResId),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
        }
        val displayEntities = entities.filter {
            !(it.entity is Letter && it.entity.name.contains("final sigma"))
        }
        items(displayEntities) { entityState ->
            TheoryEntityItem(entityState = entityState)
        }
    }
}

@Composable
private fun TheoryEntityItem(
    entityState: TheoryEntityUiState
) {
    // Check if this is a sigma letter
    val isSigma = entityState.entity is Letter &&
            entityState.entity.name.contains("sigma") &&
            !entityState.entity.name.contains("final")

    // Hardcoded final sigma lowercase character
    val finalSigmaLowercase = "ς"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left card with entity
        RegularCard(
            contentPadding = CardPadding.Large,
            onClick = {},
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val displayText = when {
                    // Special case for sigma: show both variants
                    isSigma -> {
                        val letter = entityState.entity as Letter
                        "${letter.uppercase} ${letter.lowercase} $finalSigmaLowercase"
                    }
                    // Regular cases
                    entityState.entity is Letter -> "${entityState.entity.uppercase} ${entityState.entity.lowercase}"
                    entityState.entity is Diphthong -> entityState.entity.lowercase
                    entityState.entity is ImproperDiphthong -> entityState.entity.lowercase
                    entityState.entity is BreathingMark -> entityState.entity.symbol
                    entityState.entity is AccentMark -> entityState.entity.symbol
                    else -> ""
                }

                Text(
                    text = displayText,
                    style = (if (isSigma) Typography.displaySmall else Typography.displayMedium).copy(
                        fontFamily = KoineFont,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Colors.Primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(Dimensions.spacingLarge))

        // Right content with entity info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
            horizontalAlignment = Alignment.Start
        ) {
            // Transliteration or name
            val topLineText = when (val entity = entityState.entity) {
                is Letter -> entity.transliteration
                is Diphthong -> entity.transliteration
                is ImproperDiphthong -> entity.transliteration
                is BreathingMark -> "${entity.name} breathing"
                is AccentMark -> "${entity.name} accent"
            }

            Text(
                text = topLineText,
                style = Typography.titleLarge,
                color = Colors.Primary,
                fontWeight = FontWeight.Bold
            )

            // Pronunciation info
            Text(
                text = entityState.pronunciation,
                style = Typography.bodyMedium,
                color = Colors.OnSurface
            )

            // Relevant info (if exists)
            if (entityState.relevantInfo.isNotBlank()) {
                Text(
                    text = entityState.relevantInfo,
                    style = Typography.bodySmall,
                    color = Colors.TextSecondary
                )
            }
        }
    }
}

@Preview(name = "Theory Screen - Full Screen with Button", showSystemUi = true)
@Composable
fun TheoryScreenFullPreview() {
    KoineosTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding(),
            topBar = {
                TheoryTopBar(
                    dividerValue = 0f,
                    onClose = {}
                )
            },
            bottomBar = {
                TheoryBottomBar(
                    onStartPractice = {}
                )
            },
            containerColor = Colors.Surface
        ) { paddingValues ->
            TheoryContent(
                titleResId = R.string.theory_title_letters_batch_1,
                introTextResId = R.string.theory_intro_letters_batch_1,
                entities = listOf(
                    TheoryEntityUiState(
                        entity = Letter(
                            id = "letter_0",
                            order = 1,
                            name = "alpha",
                            uppercase = "Α",
                            lowercase = "α",
                            transliteration = "a",
                            pronunciation = "ah",
                            examples = listOf("ἀγάπη", "ἄνθρωπος", "ἀλήθεια"),
                            notesResId = R.string.note_alpha,
                            masteryLevel = 0f
                        ),
                        pronunciation = "Pronounced as 'a' in 'father'",
                        relevantInfo = ""
                    ),
                    TheoryEntityUiState(
                        entity = Letter(
                            id = "letter_1",
                            order = 2,
                            name = "beta",
                            uppercase = "Β",
                            lowercase = "β",
                            transliteration = "b",
                            pronunciation = "b",
                            examples = listOf("βασιλεία", "βίβλος", "βαπτίζω"),
                            notesResId = R.string.note_beta,
                            masteryLevel = 0f
                        ),
                        pronunciation = "Pronounced as 'b' in 'boy'",
                        relevantInfo = ""
                    ),
                    TheoryEntityUiState(
                        entity = Letter(
                            id = "letter_4",
                            order = 5,
                            name = "epsilon",
                            uppercase = "Ε",
                            lowercase = "ε",
                            transliteration = "e",
                            pronunciation = "eh",
                            examples = listOf("ἐκκλησία", "ἔργον", "εἰρήνη"),
                            notesResId = R.string.note_epsilon,
                            masteryLevel = 0f
                        ),
                        pronunciation = "Short 'e' sound, as in 'pet'",
                        relevantInfo = ""
                    ),
                    TheoryEntityUiState(
                        entity = Letter(
                            id = "letter_2",
                            order = 3,
                            name = "gamma",
                            uppercase = "Γ",
                            lowercase = "γ",
                            transliteration = "g",
                            pronunciation = "g",
                            examples = listOf("γῆ", "γράφω", "γίνομαι"),
                            notesResId = R.string.note_gamma,
                            masteryLevel = 0f
                        ),
                        pronunciation = "Pronounced as 'g' in 'got'",
                        relevantInfo = "Before γ, κ, χ, or ξ, pronounced as 'ng'"
                    )
                ),
                listState = rememberLazyListState(),
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Preview(name = "Theory Screen - Letters Batch 1 (4 items)", showBackground = true)
@Composable
fun TheoryScreenLettersBatch1Preview() {
    val letterEntities = listOf(
        TheoryEntityUiState(
            entity = Letter(
                id = "letter_0",
                order = 1,
                name = "alpha",
                uppercase = "Α",
                lowercase = "α",
                transliteration = "a",
                pronunciation = "ah",
                examples = listOf("ἀγάπη", "ἄνθρωπος", "ἀλήθεια"),
                notesResId = R.string.note_alpha,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced as 'a' in 'father'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Letter(
                id = "letter_1",
                order = 2,
                name = "beta",
                uppercase = "Β",
                lowercase = "β",
                transliteration = "b",
                pronunciation = "b",
                examples = listOf("βασιλεία", "βίβλος", "βαπτίζω"),
                notesResId = R.string.note_beta,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced as 'b' in 'boy'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Letter(
                id = "letter_4",
                order = 5,
                name = "epsilon",
                uppercase = "Ε",
                lowercase = "ε",
                transliteration = "e",
                pronunciation = "eh",
                examples = listOf("ἐκκλησία", "ἔργον", "εἰρήνη"),
                notesResId = R.string.note_epsilon,
                masteryLevel = 0f
            ),
            pronunciation = "Short 'e' sound, as in 'pet'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Letter(
                id = "letter_2",
                order = 3,
                name = "gamma",
                uppercase = "Γ",
                lowercase = "γ",
                transliteration = "g",
                pronunciation = "g",
                examples = listOf("γῆ", "γράφω", "γίνομαι"),
                notesResId = R.string.note_gamma,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced as 'g' in 'got'",
            relevantInfo = "Before γ, κ, χ, or ξ, pronounced as 'ng'"
        )
    )

    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryContent(
                titleResId = R.string.theory_title_letters_batch_1,
                introTextResId = R.string.theory_intro_letters_batch_1,
                entities = letterEntities,
                listState = rememberLazyListState(),
            )
        }
    }
}

@Preview(name = "Theory Screen - Diphthongs Batch 1 (4 items)", showBackground = true)
@Composable
fun TheoryScreenDiphthongsBatch1Preview() {
    val diphthongEntities = listOf(
        TheoryEntityUiState(
            entity = Diphthong(
                id = "diphthong_0",
                order = 1,
                lowercase = "αι",
                transliteration = "ai",
                pronunciation = "eye",
                examples = listOf("καί", "αἷμα", "παιδίον"),
                notesResId = R.string.note_diphthong_ai,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced like 'ai' in 'aisle'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Diphthong(
                id = "diphthong_1",
                order = 2,
                lowercase = "ει",
                transliteration = "ei",
                pronunciation = "ay",
                examples = listOf("εἰμί", "εἰς", "εἶπεν"),
                notesResId = R.string.note_diphthong_ei,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced like 'ay' in 'pay'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Diphthong(
                id = "diphthong_2",
                order = 3,
                lowercase = "οι",
                transliteration = "oi",
                pronunciation = "oy",
                examples = listOf("οἶκος", "οἶνος", "ποιέω"),
                notesResId = R.string.note_diphthong_oi,
                masteryLevel = 0f
            ),
            pronunciation = "Pronounced like 'oi' in 'coin'",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = Diphthong(
                id = "diphthong_3",
                order = 4,
                lowercase = "υι",
                transliteration = "ui",
                pronunciation = "ui",
                examples = listOf("υἱός", "υἱοθεσία"),
                notesResId = R.string.note_diphthong_ui,
                masteryLevel = 0f
            ),
            pronunciation = "Rare diphthong, pronounced as 'wee' in 'week'",
            relevantInfo = ""
        )
    )

    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryContent(
                titleResId = R.string.theory_title_diphthongs_batch_1,
                introTextResId = R.string.theory_intro_diphthongs_batch_1,
                entities = diphthongEntities,
                listState = rememberLazyListState()
            )
        }
    }
}

@Preview(name = "Theory Screen - Improper Diphthongs (3 items)", showBackground = true)
@Composable
fun TheoryScreenImproperDiphthongsPreview() {
    val improperDiphthongEntities = listOf(
        TheoryEntityUiState(
            entity = ImproperDiphthong(
                id = "improper_diphthong_0",
                order = 1,
                lowercase = "ᾳ",
                transliteration = "aᵢ",
                pronunciation = "ah",
                examples = listOf("σοφίᾳ", "δόξᾳ", "ἡμέρᾳ"),
                notesResId = R.string.note_improper_diphthong_ai,
                masteryLevel = 0f
            ),
            pronunciation = "Alpha + iota subscript",
            relevantInfo = "Pronounced like alpha"
        ),
        TheoryEntityUiState(
            entity = ImproperDiphthong(
                id = "improper_diphthong_1",
                order = 2,
                lowercase = "ῃ",
                transliteration = "ēᵢ",
                pronunciation = "ay",
                examples = listOf("ζωῇ", "ἀγάπῃ", "ψυχῇ"),
                notesResId = R.string.note_improper_diphthong_ei,
                masteryLevel = 0f
            ),
            pronunciation = "Eta + iota subscript",
            relevantInfo = "Pronounced like eta"
        ),
        TheoryEntityUiState(
            entity = ImproperDiphthong(
                id = "improper_diphthong_2",
                order = 3,
                lowercase = "ῳ",
                transliteration = "ōᵢ",
                pronunciation = "oh",
                examples = listOf("λόγῳ", "κυρίῳ", "θεῷ"),
                notesResId = R.string.note_improper_diphthong_oi,
                masteryLevel = 0f
            ),
            pronunciation = "Omega + iota subscript",
            relevantInfo = "Pronounced like omega"
        )
    )

    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryContent(
                titleResId = R.string.theory_title_improper_diphthongs,
                introTextResId = R.string.theory_intro_improper_diphthongs,
                entities = improperDiphthongEntities,
                listState = rememberLazyListState()
            )
        }
    }
}

@Preview(name = "Theory Screen - Breathing Marks (2 items)", showBackground = true)
@Composable
fun TheoryScreenBreathingMarksPreview() {
    val breathingMarkEntities = listOf(
        TheoryEntityUiState(
            entity = BreathingMark(
                id = "breathing_0",
                order = 1,
                name = "rough",
                symbol = "῾",
                pronunciation = "h-",
                examples = listOf("ὁ", "ἡμεῖς", "ὑμεῖς", "ἅγιος"),
                notesResId = R.string.note_breathing_rough,
                masteryLevel = 0f
            ),
            pronunciation = "Adds an 'h' sound before vowels and rho",
            relevantInfo = ""
        ),
        TheoryEntityUiState(
            entity = BreathingMark(
                id = "breathing_1",
                order = 2,
                name = "smooth",
                symbol = "᾽",
                pronunciation = "",
                examples = listOf("ἐν", "ἀγάπη", "εἰρήνη", "ἰδού"),
                notesResId = R.string.note_breathing_smooth,
                masteryLevel = 0f
            ),
            pronunciation = "Does not affect pronunciation; used for orthographic purposes",
            relevantInfo = ""
        )
    )

    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryContent(
                titleResId = R.string.theory_title_breathing_marks,
                introTextResId = R.string.theory_intro_breathing_marks,
                entities = breathingMarkEntities,
                listState = rememberLazyListState()
            )
        }
    }
}

@Preview(name = "Theory Screen - Accent Marks (2 items)", showBackground = true)
@Composable
fun TheoryScreenAccentMarksPreview() {
    val accentMarkEntities = listOf(
        TheoryEntityUiState(
            entity = AccentMark(
                id = "accent_0",
                order = 1,
                name = "acute",
                symbol = "´",
                examples = listOf("λόγος", "ἀγάπη", "ἄνθρωπος"),
                notesResId = R.string.note_accent_acute,
                masteryLevel = 0f
            ),
            pronunciation = "Indicates a rising pitch",
            relevantInfo = "Appears on one of the last three syllables of a word"
        ),
        TheoryEntityUiState(
            entity = AccentMark(
                id = "accent_2",
                order = 2,
                name = "circumflex",
                symbol = "῀",
                examples = listOf("γῆ", "μῆνις", "δοῦλος"),
                notesResId = R.string.note_accent_circumflex,
                masteryLevel = 0f
            ),
            pronunciation = "Marks a rising-falling pitch and only appears on long vowels or diphthongs",
            relevantInfo = ""
        )
    )

    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryContent(
                titleResId = R.string.theory_title_accent_marks,
                introTextResId = R.string.theory_intro_accent_marks,
                entities = accentMarkEntities,
                listState = rememberLazyListState()
            )
        }
    }
}

@Preview(name = "Theory Entity Item Preview (Letter)", showBackground = true)
@Composable
fun TheoryEntityItemPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            TheoryEntityItem(
                entityState = TheoryEntityUiState(
                    entity = Letter(
                        id = "letter_1",
                        order = 2,
                        name = "beta",
                        uppercase = "Β",
                        lowercase = "β",
                        transliteration = "b",
                        pronunciation = "b",
                        examples = listOf("βασιλεία", "βίβλος", "βαπτίζω"),
                        notesResId = R.string.note_beta,
                        masteryLevel = 0f
                    ),
                    pronunciation = "Pronounced as 'b' in 'boy'",
                    relevantInfo = ""
                )
            )
        }
    }
}