package com.koineos.app.ui.utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**
 * Provider for string resources
 */
interface StringProvider {
    /**
     * Provides a string resource
     *
     * @param resId The resource identifier
     * @param formatArgs The format arguments
     *
     * @return The string data associated with the resource
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    /**
     * Provides an array of string resources
     *
     * @param resId The resource identifier
     *
     * @return The string array associated with the resource
     */
    fun getStringArray(@ArrayRes resId: Int): Array<String>


    /**
     * Provides a quantity string resource
     *
     * @param resId The resource identifier
     * @param quantity The quantity
     * @param formatArgs The format arguments
     *
     * @return The string data associated with the resource
     */
    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String
}

/**
 * Default Android implementation of [StringProvider]
 *
 * @param context The application context
 */
class AndroidStringProvider(private val context: Context) : StringProvider {
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    override fun getStringArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(resId, quantity, *formatArgs)
    }
}