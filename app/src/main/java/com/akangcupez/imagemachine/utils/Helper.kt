package com.akangcupez.imagemachine.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 21:48
 */
@Suppress("unused")
object Helper {

    fun currentTimestamp(): Long {
        return Date().time / 1000
    }

    fun dateMillisToString(milis: Long): String {
        return dateMillisToString(milis, "MMMM d yyyy, h:mm:ss a")
    }

    fun dateMillisToString(milis: Long, format: String): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        val dt = Date(milis * 1000)
        return df.format(dt)
    }

    fun dateStringToMillis(date: String, format: String) : Long {
        val df = SimpleDateFormat(format, Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("GMT+7")
        val rs = df.parse(date)
        if (rs != null) {
            return rs.time / 1000
        }
        return -1
    }

    fun formatDateTime(date: String, format: String) : String {
        val dtf: DateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dtf.timeZone = TimeZone.getTimeZone("GMT+7")
        return try {
            val dt = dtf.parse(date)
            val df: DateFormat =
                SimpleDateFormat(format, Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("GMT+7")
            df.format(dt!!)
        } catch (e: Exception) {
            ""
        }
    }

    fun getValidDate(s: String): Date? {
        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return try {
            df.timeZone = TimeZone.getTimeZone("GMT+7")
            df.parse(s)
        } catch (e: ParseException) {
            null
        }
    }

}