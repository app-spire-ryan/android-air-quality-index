package com.hireme.android.library.core.android.extensions

/**
 * Converts a [Throwable] to an [Exception].
 *
 * @return [Exception]
 */
fun Throwable.toException(): Exception = Exception(this)