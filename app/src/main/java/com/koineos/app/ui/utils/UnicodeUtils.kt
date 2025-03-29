package com.koineos.app.ui.utils

import java.text.Normalizer

/**
 * Utility functions for working with Unicode characters.
 */
object UnicodeUtils {
    fun formatMarkWithPlaceholder(markSymbol: String): String {
        // Unicode character for dotted circle placeholder
        val placeholder = "\u25CC" // ◌

        // Map of breathing and accent marks to their combining form
        val markMap = mapOf(
            "᾽" to "\u0313", // Smooth breathing to combining comma above
            "῾" to "\u0314", // Rough breathing to combining reversed comma above
            "´" to "\u0301", // Acute accent to combining acute accent
            "`" to "\u0300", // Grave accent to combining grave accent
            "῀" to "\u0311"  // Circumflex to combining inverted breve
        )

        // Get the combining form of the mark, or return the original with placeholder if not found
        val combiningMark = markMap[markSymbol] ?: return "$placeholder$markSymbol"

        // Combine placeholder with the mark and normalize
        return Normalizer.normalize(
            placeholder + combiningMark,
            Normalizer.Form.NFC
        )
    }
}