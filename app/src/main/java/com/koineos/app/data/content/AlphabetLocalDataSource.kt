package com.koineos.app.data.content

import com.koineos.app.data.content.dto.BreathingMarkDto
import com.koineos.app.data.content.dto.DiphthongDto
import com.koineos.app.data.content.dto.ImproperDiphthongDto
import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.R
import com.koineos.app.data.content.dto.AccentMarkDto
import com.koineos.app.data.content.dto.AlphabetResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source for the Alphabet feature
 */
@Singleton
class AlphabetLocalDataSource @Inject constructor() {
    private val letters = listOf(
        LetterDto(
            id = "letter_0",
            order = 1,
            name = "alpha",
            uppercase = "Α",
            lowercase = "α",
            transliteration = "a",
            pronunciation = "ah",
            examples = listOf("ἀγάπη", "ἄνθρωπος", "ἀλήθεια"),
            notesResId = R.string.note_alpha
        ),
        LetterDto(
            id = "letter_1",
            order = 2,
            name = "beta",
            uppercase = "Β",
            lowercase = "β",
            transliteration = "b",
            pronunciation = "b",
            examples = listOf("βασιλεία", "βίβλος", "βαπτίζω"),
            notesResId = R.string.note_beta
        ),
        LetterDto(
            id = "letter_2",
            order = 3,
            name = "gamma",
            uppercase = "Γ",
            lowercase = "γ",
            transliteration = "g",
            pronunciation = "g",
            examples = listOf("γῆ", "γράφω", "γίνομαι"),
            notesResId = R.string.note_gamma
        ),
        LetterDto(
            id = "letter_3",
            order = 4,
            name = "delta",
            uppercase = "Δ",
            lowercase = "δ",
            transliteration = "d",
            pronunciation = "d",
            examples = listOf("δοῦλος", "διδάσκαλος", "δικαιοσύνη"),
            notesResId = R.string.note_delta
        ),
        LetterDto(
            id = "letter_4",
            order = 5,
            name = "epsilon",
            uppercase = "Ε",
            lowercase = "ε",
            transliteration = "e",
            pronunciation = "eh",
            examples = listOf("ἐκκλησία", "ἔργον", "εἰρήνη"),
            notesResId = R.string.note_epsilon
        ),
        LetterDto(
            id = "letter_5",
            order = 6,
            name = "zeta",
            uppercase = "Ζ",
            lowercase = "ζ",
            transliteration = "z",
            pronunciation = "z",
            examples = listOf("ζωή", "ζητέω", "ζῆλος"),
            notesResId = R.string.note_zeta
        ),
        LetterDto(
            id = "letter_6",
            order = 7,
            name = "eta",
            uppercase = "Η",
            lowercase = "η",
            transliteration = "ē",
            pronunciation = "ay",
            examples = listOf("ἡμέρα", "ἤδη", "ἦλθον"),
            notesResId = R.string.note_eta
        ),
        LetterDto(
            id = "letter_7",
            order = 8,
            name = "theta",
            uppercase = "Θ",
            lowercase = "θ",
            transliteration = "th",
            pronunciation = "th",
            examples = listOf("θεός", "θάνατος", "θέλημα"),
            notesResId = R.string.note_theta
        ),
        LetterDto(
            id = "letter_8",
            order = 9,
            name = "iota",
            uppercase = "Ι",
            lowercase = "ι",
            transliteration = "i",
            pronunciation = "ee",
            examples = listOf("Ἰησοῦς", "ἵνα", "ἱερόν"),
            notesResId = R.string.note_iota
        ),
        LetterDto(
            id = "letter_9",
            order = 10,
            name = "kappa",
            uppercase = "Κ",
            lowercase = "κ",
            transliteration = "k",
            pronunciation = "k",
            examples = listOf("καρδία", "κόσμος", "κύριος"),
            notesResId = R.string.note_kappa
        ),
        LetterDto(
            id = "letter_10",
            order = 11,
            name = "lambda",
            uppercase = "Λ",
            lowercase = "λ",
            transliteration = "l",
            pronunciation = "l",
            examples = listOf("λόγος", "λαός", "λαμβάνω"),
            notesResId = R.string.note_lambda
        ),
        LetterDto(
            id = "letter_11",
            order = 12,
            name = "mu",
            uppercase = "Μ",
            lowercase = "μ",
            transliteration = "m",
            pronunciation = "m",
            examples = listOf("μαθητής", "μάρτυς", "μένω"),
            notesResId = R.string.note_mu
        ),
        LetterDto(
            id = "letter_12",
            order = 13,
            name = "nu",
            uppercase = "Ν",
            lowercase = "ν",
            transliteration = "n",
            pronunciation = "n",
            examples = listOf("νόμος", "ναός", "νῦν"),
            notesResId = R.string.note_nu
        ),
        LetterDto(
            id = "letter_13",
            order = 14,
            name = "xi",
            uppercase = "Ξ",
            lowercase = "ξ",
            transliteration = "x",
            pronunciation = "ks",
            examples = listOf("ξένος", "ξύλον", "ξηρός"),
            notesResId = R.string.note_xi
        ),
        LetterDto(
            id = "letter_14",
            order = 15,
            name = "omicron",
            uppercase = "Ο",
            lowercase = "ο",
            transliteration = "o",
            pronunciation = "oh",
            examples = listOf("ὁδός", "οἶκος", "ὀφθαλμός"),
            notesResId = R.string.note_omicron
        ),
        LetterDto(
            id = "letter_15",
            order = 16,
            name = "pi",
            uppercase = "Π",
            lowercase = "π",
            transliteration = "p",
            pronunciation = "p",
            examples = listOf("πίστις", "πνεῦμα", "προφήτης"),
            notesResId = R.string.note_pi
        ),
        LetterDto(
            id = "letter_16",
            order = 17,
            name = "rho",
            uppercase = "Ρ",
            lowercase = "ρ",
            transliteration = "r",
            pronunciation = "r",
            examples = listOf("ῥῆμα", "ῥαββί", "ῥίζα"),
            notesResId = R.string.note_rho
        ),
        LetterDto(
            id = "letter_17",
            order = 18,
            name = "sigma",
            uppercase = "Σ",
            lowercase = "σ",
            transliteration = "s",
            pronunciation = "s",
            examples = listOf("σῶμα", "σοφία", "λόγος"),
            notesResId = R.string.note_sigma
        ),
        LetterDto(
            id = "letter_18",
            order = 19,
            name = "final sigma",
            uppercase = "Σ",
            lowercase = "ς",
            transliteration = "s",
            pronunciation = "s",
            examples = listOf("Χριστός", "θεός", "ἄνθρωπος"),
            notesResId = R.string.note_final_sigma
        ),
        LetterDto(
            id = "letter_19",
            order = 20,
            name = "tau",
            uppercase = "Τ",
            lowercase = "τ",
            transliteration = "t",
            pronunciation = "t",
            examples = listOf("τέκνον", "τόπος", "τιμή"),
            notesResId = R.string.note_tau
        ),
        LetterDto(
            id = "letter_20",
            order = 21,
            name = "upsilon",
            uppercase = "Υ",
            lowercase = "υ",
            transliteration = "y",
            pronunciation = "oo",
            examples = listOf("ὕδωρ", "υἱός", "ὑπέρ"),
            notesResId = R.string.note_upsilon
        ),
        LetterDto(
            id = "letter_21",
            order = 22,
            name = "phi",
            uppercase = "Φ",
            lowercase = "φ",
            transliteration = "ph",
            pronunciation = "f",
            examples = listOf("φῶς", "φωνή", "φόβος"),
            notesResId = R.string.note_phi
        ),
        LetterDto(
            id = "letter_22",
            order = 23,
            name = "chi",
            uppercase = "Χ",
            lowercase = "χ",
            transliteration = "ch",
            pronunciation = "kh",
            examples = listOf("χάρις", "Χριστός", "χείρ"),
            notesResId = R.string.note_chi
        ),
        LetterDto(
            id = "letter_23",
            order = 24,
            name = "psi",
            uppercase = "Ψ",
            lowercase = "ψ",
            transliteration = "ps",
            pronunciation = "ps",
            examples = listOf("ψυχή", "ψευδοπροφήτης", "ψαλμός"),
            notesResId = R.string.note_psi
        ),
        LetterDto(
            id = "letter_24",
            order = 25,
            name = "omega",
            uppercase = "Ω",
            lowercase = "ω",
            transliteration = "ō",
            pronunciation = "oh",
            examples = listOf("ὥρα", "ὡς", "ὤμος"),
            notesResId = R.string.note_omega
        )
    )

    private val accentMarks = listOf(
        AccentMarkDto(
            id = "accent_0",
            order = 1,
            name = "acute",
            symbol = "´",
            examples = listOf("λόγος", "ἀγάπη", "ἄνθρωπος"),
            notesResId = R.string.note_accent_acute
        ),
        AccentMarkDto(
            id = "accent_2",
            order = 2,
            name = "circumflex",
            symbol = "῀",
            examples = listOf("γῆ", "μῆνις", "δοῦλος"),
            notesResId = R.string.note_accent_circumflex
        ),
        AccentMarkDto(
            id = "accent_1",
            order = 3,
            name = "grave",
            symbol = "`",
            examples = listOf("καὶ", "δὲ", "γὰρ"),
            notesResId = R.string.note_accent_grave
        )
    )


    private val diphthongs = listOf(
        DiphthongDto(
            id = "diphthong_0",
            order = 1,
            lowercase = "αι",
            transliteration = "ai",
            pronunciation = "eye",
            examples = listOf("καί", "αἷμα", "παιδίον"),
            notesResId = R.string.note_diphthong_ai
        ),
        DiphthongDto(
            id = "diphthong_1",
            order = 2,
            lowercase = "ει",
            transliteration = "ei",
            pronunciation = "ay",
            examples = listOf("εἰμί", "εἰς", "εἶπεν"),
            notesResId = R.string.note_diphthong_ei
        ),
        DiphthongDto(
            id = "diphthong_2",
            order = 3,
            lowercase = "οι",
            transliteration = "oi",
            pronunciation = "oy",
            examples = listOf("οἶκος", "οἶνος", "ποιέω"),
            notesResId = R.string.note_diphthong_oi
        ),
        DiphthongDto(
            id = "diphthong_3",
            order = 4,
            lowercase = "υι",
            transliteration = "ui",
            pronunciation = "ui",
            examples = listOf("υἱός", "υἱοθεσία"),
            notesResId = R.string.note_diphthong_ui
        ),
        DiphthongDto(
            id = "diphthong_4",
            order = 5,
            lowercase = "αυ",
            transliteration = "au",
            pronunciation = "ow",
            examples = listOf("αὐτός", "ταῦτα"),
            notesResId = R.string.note_diphthong_au
        ),
        DiphthongDto(
            id = "diphthong_5",
            order = 6,
            lowercase = "ευ",
            transliteration = "eu",
            pronunciation = "eh-oo",
            examples = listOf("εὑρίσκω", "εὐθύς"),
            notesResId = R.string.note_diphthong_eu
        ),
        DiphthongDto(
            id = "diphthong_6",
            order = 7,
            lowercase = "ηυ",
            transliteration = "ēu",
            pronunciation = "ay-oo",
            examples = listOf("ηὗρον", "ηὐχόμην"),
            notesResId = R.string.note_diphthong_hu
        ),
        DiphthongDto(
            id = "diphthong_7",
            order = 8,
            lowercase = "ου",
            transliteration = "ou",
            pronunciation = "oo",
            examples = listOf("οὐ", "οὖν", "οὗτος"),
            notesResId = R.string.note_diphthong_ou
        )
    )

    private val improperDiphthongs = listOf(
        ImproperDiphthongDto(
            id = "improper_diphthong_0",
            order = 1,
            lowercase = "ᾳ",
            transliteration = "āi",
            pronunciation = "ah",
            examples = listOf("σοφίᾳ", "δόξᾳ", "ἡμέρᾳ"),
            notesResId = R.string.note_improper_diphthong_ai
        ),
        ImproperDiphthongDto(
            id = "improper_diphthong_1",
            order = 2,
            lowercase = "ῃ",
            transliteration = "ēi",
            pronunciation = "ay",
            examples = listOf("ζωῇ", "ἀγάπῃ", "ψυχῇ"),
            notesResId = R.string.note_improper_diphthong_ei
        ),
        ImproperDiphthongDto(
            id = "improper_diphthong_2",
            order = 3,
            lowercase = "ῳ",
            transliteration = "ōi",
            pronunciation = "oh",
            examples = listOf("λόγῳ", "κυρίῳ", "θεῷ"),
            notesResId = R.string.note_improper_diphthong_oi
        )
    )

    private val breathingMarks = listOf(
        BreathingMarkDto(
            id = "breathing_0",
            order = 1,
            name = "rough",
            symbol = "῾",
            pronunciation = "h-",
            examples = listOf("ὁ", "ἡμεῖς", "ὑμεῖς", "ἅγιος"),
            notesResId = R.string.note_breathing_rough
        ),
        BreathingMarkDto(
            id = "breathing_1",
            order = 2,
            name = "smooth",
            symbol = "᾽",
            pronunciation = "",
            examples = listOf("ἐν", "ἀγάπη", "εἰρήνη", "ἰδού"),
            notesResId = R.string.note_breathing_smooth
        )
    )

    /**
     * Returns a [Flow] of [AlphabetResponse] containing the local data source content
     */
    fun getAlphabetContent(): Flow<AlphabetResponse> = flowOf(
        AlphabetResponse(
            letters = letters,
            diphthongs = diphthongs,
            improperDiphthongs = improperDiphthongs,
            breathingMarks = breathingMarks,
            accentMarks = accentMarks
        )
    )
}