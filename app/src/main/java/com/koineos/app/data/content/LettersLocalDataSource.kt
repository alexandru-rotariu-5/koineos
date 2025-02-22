package com.koineos.app.data.content

import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.data.content.dto.LettersResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Singleton
class LettersLocalDataSource @Inject constructor() {

    private val letters = listOf(
        LetterDto(
            id = "letter_0",
            order = 1,
            name = "alpha",
            uppercase = "Α",
            lowercase = "α",
            transliteration = "a",
            pronunciation = "ah"
        ),
        LetterDto(
            id = "letter_1",
            order = 2,
            name = "beta",
            uppercase = "Β",
            lowercase = "β",
            transliteration = "b",
            pronunciation = "b"
        ),
        LetterDto(
            id = "letter_2",
            order = 3,
            name = "gamma",
            uppercase = "Γ",
            lowercase = "γ",
            transliteration = "g",
            pronunciation = "g"
        ),
        LetterDto(
            id = "letter_3",
            order = 4,
            name = "delta",
            uppercase = "Δ",
            lowercase = "δ",
            transliteration = "d",
            pronunciation = "d"
        ),
        LetterDto(
            id = "letter_4",
            order = 5,
            name = "epsilon",
            uppercase = "Ε",
            lowercase = "ε",
            transliteration = "e",
            pronunciation = "eh"
        ),
        LetterDto(
            id = "letter_5",
            order = 6,
            name = "zeta",
            uppercase = "Ζ",
            lowercase = "ζ",
            transliteration = "z",
            pronunciation = "z"
        ),
        LetterDto(
            id = "letter_6",
            order = 7,
            name = "eta",
            uppercase = "Η",
            lowercase = "η",
            transliteration = "ē",
            pronunciation = "ay"
        ),
        LetterDto(
            id = "letter_7",
            order = 8,
            name = "theta",
            uppercase = "Θ",
            lowercase = "θ",
            transliteration = "th",
            pronunciation = "th"
        ),
        LetterDto(
            id = "letter_8",
            order = 9,
            name = "iota",
            uppercase = "Ι",
            lowercase = "ι",
            transliteration = "i",
            pronunciation = "ee"
        ),
        LetterDto(
            id = "letter_9",
            order = 10,
            name = "kappa",
            uppercase = "Κ",
            lowercase = "κ",
            transliteration = "k",
            pronunciation = "k"
        ),
        LetterDto(
            id = "letter_10",
            order = 11,
            name = "lambda",
            uppercase = "Λ",
            lowercase = "λ",
            transliteration = "l",
            pronunciation = "l"
        ),
        LetterDto(
            id = "letter_11",
            order = 12,
            name = "mu",
            uppercase = "Μ",
            lowercase = "μ",
            transliteration = "m",
            pronunciation = "m"
        ),
        LetterDto(
            id = "letter_12",
            order = 13,
            name = "nu",
            uppercase = "Ν",
            lowercase = "ν",
            transliteration = "n",
            pronunciation = "n"
        ),
        LetterDto(
            id = "letter_13",
            order = 14,
            name = "xi",
            uppercase = "Ξ",
            lowercase = "ξ",
            transliteration = "x",
            pronunciation = "ks"
        ),
        LetterDto(
            id = "letter_14",
            order = 15,
            name = "omicron",
            uppercase = "Ο",
            lowercase = "ο",
            transliteration = "o",
            pronunciation = "oh"
        ),
        LetterDto(
            id = "letter_15",
            order = 16,
            name = "pi",
            uppercase = "Π",
            lowercase = "π",
            transliteration = "p",
            pronunciation = "p"
        ),
        LetterDto(
            id = "letter_16",
            order = 17,
            name = "rho",
            uppercase = "Ρ",
            lowercase = "ρ",
            transliteration = "r",
            pronunciation = "r"
        ),
        LetterDto(
            id = "letter_17",
            order = 18,
            name = "sigma",
            uppercase = "Σ",
            lowercase = "σ",
            transliteration = "s",
            pronunciation = "s"
        ),
        LetterDto(
            id = "letter_18",
            order = 19,
            name = "final sigma",
            uppercase = "Σ",
            lowercase = "ς",
            transliteration = "s",
            pronunciation = "s"
        ),
        LetterDto(
            id = "letter_19",
            order = 20,
            name = "tau",
            uppercase = "Τ",
            lowercase = "τ",
            transliteration = "t",
            pronunciation = "t"
        ),
        LetterDto(
            id = "letter_20",
            order = 21,
            name = "upsilon",
            uppercase = "Υ",
            lowercase = "υ",
            transliteration = "y",
            pronunciation = "oo"
        ),
        LetterDto(
            id = "letter_21",
            order = 22,
            name = "phi",
            uppercase = "Φ",
            lowercase = "φ",
            transliteration = "ph",
            pronunciation = "f"
        ),
        LetterDto(
            id = "letter_22",
            order = 23,
            name = "chi",
            uppercase = "Χ",
            lowercase = "χ",
            transliteration = "ch",
            pronunciation = "kh"
        ),
        LetterDto(
            id = "letter_23",
            order = 24,
            name = "psi",
            uppercase = "Ψ",
            lowercase = "ψ",
            transliteration = "ps",
            pronunciation = "ps"
        ),
        LetterDto(
            id = "letter_24",
            order = 25,
            name = "omega",
            uppercase = "Ω",
            lowercase = "ω",
            transliteration = "ō",
            pronunciation = "oh"
        )
    )

    fun getAllLetters(): Flow<LettersResponse> = flowOf(LettersResponse(letters))

    fun getLetterById(id: String): Flow<LetterDto?> =
        flowOf(letters.find { it.id == id })

    fun getLettersByRange(fromOrder: Int, toOrder: Int): Flow<List<LetterDto>> =
        flowOf(letters.filter { it.order in fromOrder..toOrder }.sortedBy { it.order })
}