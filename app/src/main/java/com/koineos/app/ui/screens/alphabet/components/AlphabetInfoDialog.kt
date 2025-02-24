package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.koineos.app.presentation.model.AlphabetEntityUiState
import com.koineos.app.presentation.model.BreathingMarkUiState
import com.koineos.app.presentation.model.DiphthongUiState
import com.koineos.app.presentation.model.ImproperDiphthongUiState
import com.koineos.app.presentation.model.LetterUiState
import com.koineos.app.ui.components.core.RegularAssistChip
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * A dialog component for displaying detailed information about an alphabet entity.
 *
 * @param entityUiState The state of the alphabet entity to display
 * @param onDismiss The action to perform when the dialog is dismissed
 * @param modifier The modifier to apply to the dialog
 */
@Composable
fun AlphabetInfoDialog(
    entityUiState: AlphabetEntityUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Colors.Surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimensions.paddingXLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with title and close button
                DialogHeader(
                    title = when (entityUiState) {
                        is LetterUiState -> entityUiState.name.uppercase()
                        is DiphthongUiState -> "DIPHTHONG"
                        is ImproperDiphthongUiState -> "IMPROPER DIPHTHONG"
                        is BreathingMarkUiState -> "${entityUiState.name.uppercase()} BREATHING"
                    }
                )

                // Entity content
                when (entityUiState) {
                    is LetterUiState -> LetterContent(letter = entityUiState)
                    is DiphthongUiState -> DiphthongContent(diphthong = entityUiState)
                    is ImproperDiphthongUiState -> ImproperDiphthongContent(improperDiphthong = entityUiState)
                    is BreathingMarkUiState -> BreathingMarkContent(breathingMark = entityUiState)
                }
            }
        }
    }
}

@Composable
private fun DialogHeader(
    title: String,
) {
    Text(
        text = title,
        style = Typography.labelMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Colors.OnSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun LetterContent(letter: LetterUiState) {
    // Main letter display
    GreekSymbol(text = "${letter.uppercase} ${letter.lowercase}${if (letter.hasAlternateLowercase) " ${letter.alternateLowercase ?: ""}" else ""}")

    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

    // Transliteration and pronunciation
    PronunciationInfo(
        transliteration = letter.transliteration,
        pronunciation = letter.pronunciation
    )

    // Notes
    if (letter.notes != null) {
        ContentNotes(notes = letter.notes)
    }

    // Examples
    if (letter.examples.isNotEmpty()) {
        ExamplesSection(examples = letter.examples)
    }
}

@Composable
private fun DiphthongContent(diphthong: DiphthongUiState) {
    // Main diphthong display
    GreekSymbol(text = diphthong.symbol)

    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

    // Transliteration and pronunciation
    PronunciationInfo(
        transliteration = diphthong.transliteration,
        pronunciation = diphthong.pronunciation
    )

    // Notes
    if (diphthong.notes != null) {
        ContentNotes(notes = diphthong.notes)
    }

    // Examples
    if (diphthong.examples.isNotEmpty()) {
        ExamplesSection(examples = diphthong.examples)
    }
}

@Composable
private fun ImproperDiphthongContent(improperDiphthong: ImproperDiphthongUiState) {
    // Main improper diphthong display
    GreekSymbol(text = improperDiphthong.symbol)

    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

    // Transliteration and pronunciation
    PronunciationInfo(
        transliteration = improperDiphthong.transliteration,
        pronunciation = improperDiphthong.pronunciation
    )

    // Notes
    if (improperDiphthong.notes != null) {
        ContentNotes(notes = improperDiphthong.notes)
    }

    // Examples
    if (improperDiphthong.examples.isNotEmpty()) {
        ExamplesSection(examples = improperDiphthong.examples)
    }
}

@Composable
private fun BreathingMarkContent(breathingMark: BreathingMarkUiState) {
    // Main breathing mark display
    GreekSymbol(text = breathingMark.symbol)

    // Pronunciation
    if (breathingMark.pronunciation != "") {
        RegularAssistChip(
            text = breathingMark.pronunciation,
            backGroundColor = Colors.PrimaryContainer,
            textColor = Colors.OnPrimaryContainer
        )
    }

    // Notes
    if (breathingMark.notes != null) {
        ContentNotes(notes = breathingMark.notes)
    }

    // Examples
    if (breathingMark.examples.isNotEmpty()) {
        ExamplesSection(examples = breathingMark.examples)
    }
}

@Composable
private fun GreekSymbol(text: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontFamily = KoineFont, fontWeight = FontWeight.Bold)) {
                append(text)
            }
        },
        fontSize = 60.sp,
        color = Colors.Primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PronunciationInfo(transliteration: String, pronunciation: String) {
    RegularAssistChip(
        text = "$transliteration · /$pronunciation/",
        backGroundColor = Colors.PrimaryContainer,
        textColor = Colors.OnPrimaryContainer
    )
}

@Composable
private fun ContentNotes(notes: String) {
    Spacer(modifier = Modifier.height(Dimensions.paddingLarge))
    Text(
        text = notes,
        style = Typography.bodyMedium,
        color = Colors.OnSurface,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExamplesSection(examples: List<String>) {
    Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))
    Text(
        text = "EXAMPLES",
        style = Typography.labelSmall,
        color = Colors.OnSurfaceVariant,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            Dimensions.spacingMedium,
            Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy((-6).dp),
    ) {
        examples.forEach { example ->
            RegularAssistChip(
                text = example,
                textStyle = Typography.bodyMedium.copy(
                    fontFamily = KoineFont,
                    fontWeight = FontWeight.Bold
                ),
                textColor = Colors.OnPrimary,
                backGroundColor = Colors.Primary,
            )
        }
    }
}

@Composable
private fun MasteryProgressSection(masteryLevel: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "MASTERY PROGRESS",
            style = Typography.labelSmall,
            color = Colors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        LinearProgressIndicator(
            progress = { masteryLevel },
            modifier = Modifier.fillMaxWidth(),
            color = Colors.Primary,
            trackColor = Colors.PrimaryContainer,
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        Text(
            text = "${(masteryLevel * 100).toInt()}%",
            style = Typography.labelMedium,
            color = Colors.OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun LetterInfoDialogPreview() {
    val sampleLetter = LetterUiState(
        id = "letter_17",
        order = 18,
        name = "sigma",
        uppercase = "Σ",
        lowercase = "σ",
        transliteration = "s",
        pronunciation = "s",
        hasAlternateLowercase = true,
        alternateLowercase = "ς",
        examples = listOf("σῶμα", "σοφία", "λόγος"),
        notes = "Uses different forms based on position: σ within words, ς at the end of words",
        masteryLevel = 0.75f
    )

    KoineosTheme {
        AlphabetInfoDialog(
            entityUiState = sampleLetter,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun DiphthongInfoDialogPreview() {
    val sampleDiphthong = DiphthongUiState(
        id = "diphthong_0",
        order = 1,
        symbol = "αι",
        transliteration = "ai",
        pronunciation = "eye",
        componentLetters = "α + ι",
        examples = listOf("καί", "αἷμα", "παιδίον"),
        notes = "One of the most frequent diphthongs, pronounced like 'i' in 'pine'",
        masteryLevel = 0.45f
    )

    KoineosTheme {
        AlphabetInfoDialog(
            entityUiState = sampleDiphthong,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ImproperDiphthongInfoDialogPreview() {
    val sampleImproperDiphthong = ImproperDiphthongUiState(
        id = "improper_diphthong_0",
        order = 1,
        symbol = "ᾳ",
        transliteration = "āi",
        pronunciation = "ah",
        componentLetters = "α + iota subscript",
        examples = listOf("σοφίᾳ", "δόξᾳ", "ἡμέρᾳ"),
        notes = "Formed with alpha and iota subscript. Pronounced like alpha",
        masteryLevel = 0.6f
    )

    KoineosTheme {
        AlphabetInfoDialog(
            entityUiState = sampleImproperDiphthong,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun BreathingMarkInfoDialogPreview() {
    val sampleBreathingMark = BreathingMarkUiState(
        id = "breathing_0",
        order = 1,
        name = "rough",
        symbol = "῾",
        pronunciation = "h-",
        examples = listOf("ὁ", "ἡμεῖς", "ὑμεῖς", "ἅγιος"),
        notes = "Adds 'h' sound before vowels and rho. Must appear on every word beginning with a vowel or rho",
        masteryLevel = 0.9f
    )

    KoineosTheme {
        AlphabetInfoDialog(
            entityUiState = sampleBreathingMark,
            onDismiss = {}
        )
    }
}