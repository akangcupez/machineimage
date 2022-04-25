package com.akangcupez.imagemachine.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.akangcupez.imagemachine.utils.Helper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 22:40
 */
class CalendarDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var sListener: OnDateChangedListener? = null

    companion object {
        private const val EXT_DATA = "ext_date_data"
        @JvmStatic
        fun newInstance(date: String) = CalendarDialog().apply {
            arguments = bundleOf(
                EXT_DATA to date
            )
        }
    }

    fun setOnDateChangedListener(onDateSetListener: OnDateChangedListener?) {
        sListener = onDateSetListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"))
        var year = c[Calendar.YEAR]
        var month = c[Calendar.MONTH]
        var day = c[Calendar.DAY_OF_MONTH]
        arguments?.let {
            val mDate = it.getString(EXT_DATA)
            mDate?.let { mdt ->
                Helper.getValidDate(mdt)?.let { date ->
                    val df: DateFormat =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    df.timeZone = TimeZone.getTimeZone("GMT+7")
                    val dt = df.format(date)

                    year = Helper.formatDateTime(dt, "yyyy").toInt()
                    month = Helper.formatDateTime(dt, "MM").toInt() - 1
                    day = Helper.formatDateTime(dt, "dd").toInt()
                }
            }
        }
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        try {
            val date = year.toString() + "/" + (month + 1) + "/" + day
            val df1: DateFormat = SimpleDateFormat("yyyy/M/d", Locale.getDefault())
            df1.timeZone = TimeZone.getTimeZone("GMT+7")
            val dt = df1.parse(date)
            if (dt != null) {
                val df2: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                df2.timeZone = TimeZone.getTimeZone("GMT+7")
                sListener?.onDateChanged(df2.format(dt))
            }
        } catch (e: Exception) {
            sListener?.onDateChanged(null)
        }
    }

    interface OnDateChangedListener {
        fun onDateChanged(date: String?)
    }
}