sealed interface AlphabetEntityDto {
    val id: String
    val order: Int
    val pronunciation: String
    val notesResId: Int?
}

data class LetterDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    override val notesResId: Int? = null
) : AlphabetEntityDto

data class DiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto

data class ImproperDiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto

data class BreathingMarkDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val symbol: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto