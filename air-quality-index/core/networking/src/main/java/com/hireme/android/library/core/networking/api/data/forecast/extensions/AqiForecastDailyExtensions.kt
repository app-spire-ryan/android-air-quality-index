package com.hireme.android.library.core.networking.api.data.forecast.extensions

import com.hireme.android.library.core.networking.api.data.forecast.AqiForecastDaily
import com.hireme.android.library.core.aqi.data.ForecastReading

/**
 * Converts [AqiForecastDaily] into a 3 day(day before/day after) forecast [List] of [ForecastReading] for ozone.
 *
 * @param day - String representation of the current day to reference the forecast.
 * @return [List] of [ForecastReading].
 */
internal fun AqiForecastDaily.toOzoneForecast(day: String): List<ForecastReading> {

    val ozoneToday = this.ozone.firstOrNull { it.day.contentEquals(day) }

    val readings = mutableListOf<ForecastReading>()

    if (ozoneToday == null) return readings

    val dayIndex = ozone.indexOf(ozoneToday)

    val readingToday = ForecastReading(ozoneToday.day, ozoneToday.max, ozoneToday.min, ozoneToday.average)

    // Make sure the current day isn't first index to avoid reading a non existent 'before' reading
    if (dayIndex != 0) {

        val ozoneBefore = ozone[dayIndex - 1]
        readings.add(ForecastReading(ozoneBefore.day, ozoneBefore.max, ozoneBefore.min, ozoneBefore.average))
    }

    readings.add(readingToday)

    // Make sure the current day isn't last index to avoid reading a non existent 'after' reading
    if (dayIndex != ozone.lastIndex) {

        val ozoneAfter = ozone[dayIndex + 1]
        readings.add(ForecastReading(ozoneAfter.day, ozoneAfter.max, ozoneAfter.min, ozoneAfter.average))
    }

    return readings
}

/**
 * Converts [AqiForecastDaily] into a 3 day(day before/day after) forecast [List] of [ForecastReading] for pm10.
 *
 * @param day - String representation of the current day to reference the forecast.
 * @return [List] of [ForecastReading].
 */
internal fun AqiForecastDaily.toParticulateMatter10Forecast(day: String): List<ForecastReading> {

    val pm10Today = this.particulateMatter10.firstOrNull { it.day.contentEquals(day) }

    // TODO: Update with exception message. Words n' stuff
    if (pm10Today == null) throw IllegalArgumentException("")

    val readings = mutableListOf<ForecastReading>()

    val dayIndex = particulateMatter10.indexOf(pm10Today)

    val readingToday = ForecastReading(pm10Today.day, pm10Today.max, pm10Today.min, pm10Today.average)

    // Make sure the current day isn't first index to avoid reading a non existent 'before' reading
    if (dayIndex != 0) {

        val pm10Before = particulateMatter10[dayIndex - 1]
        readings.add(ForecastReading(pm10Before.day, pm10Before.max, pm10Before.min, pm10Before.average))
    }

    readings.add(readingToday)

    // Make sure the current day isn't last index to avoid reading a non existent 'after' reading
    if (dayIndex != particulateMatter10.lastIndex) {

        val pm10After = particulateMatter10[dayIndex + 1]
        readings.add(ForecastReading(pm10After.day, pm10After.max, pm10After.min, pm10After.average))
    }

    return readings
}

/**
 * Converts [AqiForecastDaily] into a 3 day(day before/day after) forecast [List] of [ForecastReading] for pm25.
 *
 * @param day - String representation of the current day to reference the forecast.
 * @return [List] of [ForecastReading].
 */
internal fun AqiForecastDaily.toParticulateMatter2Point5Forecast(day: String): List<ForecastReading> {

    val pm25Today = this.particulateMatter2Point5.firstOrNull { it.day.contentEquals(day) }

    // TODO: Update with exception message. Words n' stuff
    if (pm25Today == null) throw IllegalArgumentException("")

    val readings = mutableListOf<ForecastReading>()

    val dayIndex = particulateMatter2Point5.indexOf(pm25Today)

    val readingToday = ForecastReading(pm25Today.day, pm25Today.max, pm25Today.min, pm25Today.average)

    // Make sure the current day isn't first index to avoid reading a non existent 'before' reading
    if (dayIndex != 0) {

        val pm25Before = particulateMatter2Point5[dayIndex - 1]
        readings.add(ForecastReading(pm25Before.day, pm25Before.max, pm25Before.min, pm25Before.average))
    }

    readings.add(readingToday)

    // Make sure the current day isn't last index to avoid reading a non existent 'after' reading
    if (dayIndex != particulateMatter2Point5.lastIndex) {

        val pm25After = particulateMatter2Point5[dayIndex + 1]
        readings.add(ForecastReading(pm25After.day, pm25After.max, pm25After.min, pm25After.average))
    }

    return readings
}

/**
 * Converts [AqiForecastDaily] into a 3 day(day before/day after) forecast [List] of [ForecastReading] for uvi.
 *
 * @param day - String representation of the current day to reference the forecast.
 * @return [List] of [ForecastReading].
 */
internal fun AqiForecastDaily.toUviForecast(day: String): List<ForecastReading> {

    val uviToday = this.ultravioletIndex.firstOrNull { it.day.contentEquals(day) }

    // TODO: Update with exception message. Words n' stuff
    if (uviToday == null) throw IllegalArgumentException("")

    val readings = mutableListOf<ForecastReading>()

    val dayIndex = ultravioletIndex.indexOf(uviToday)

    val readingToday = ForecastReading(uviToday.day, uviToday.max, uviToday.min, uviToday.average)

    // Make sure the current day isn't first index to avoid reading a non existent 'before' reading
    if (dayIndex != 0) {

        val uviBefore = ultravioletIndex[dayIndex - 1]
        readings.add(ForecastReading(uviBefore.day, uviBefore.max, uviBefore.min, uviBefore.average))
    }

    readings.add(readingToday)

    // Make sure the current day isn't last index to avoid reading a non existent 'after' reading
    if (dayIndex != ultravioletIndex.lastIndex) {

        val uviAfter = ultravioletIndex[dayIndex + 1]
        readings.add(ForecastReading(uviAfter.day, uviAfter.max, uviAfter.min, uviAfter.average))
    }

    return readings
}