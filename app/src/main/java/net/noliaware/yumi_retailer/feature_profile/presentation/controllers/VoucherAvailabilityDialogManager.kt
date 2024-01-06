package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.DateTime.DATE_SOURCE_FORMAT
import net.noliaware.yumi_retailer.commun.DateTime.NUMERICAL_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.util.parseDateStringToFormat
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.commun.util.recordNonFatal
import net.noliaware.yumi_retailer.commun.util.toast
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherAmendAvailabilityView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherAmendAvailabilityView.VoucherAmendAvailabilityViewCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VoucherAvailabilityDialogManager(private val context: Context) {

    private val calendarInstance = Calendar.getInstance()
    private var minStartTimestamp = calendarInstance.timeInMillis
    private var minEndTimestamp = calendarInstance.timeInMillis

    fun displayDialogForUpdateAvailability(
        startDate: String?,
        endDate: String?,
        onUpdate: (String, String, String) -> Unit
    ) {
        MaterialAlertDialogBuilder(
            context,
            R.style.AlertDialog
        ).apply {
            val dialogView = LayoutInflater.from(context).inflate(
                R.layout.voucher_amend_availability_layout,
                null
            ) as VoucherAmendAvailabilityView

            dialogView.apply {
                setStartDate(startDate?.parseDateToFormat(NUMERICAL_DATE_FORMAT).orEmpty())
                setEndDate(endDate?.parseDateToFormat(NUMERICAL_DATE_FORMAT).orEmpty())
                callback = object : VoucherAmendAvailabilityViewCallback {
                    override fun onStartDateInputClicked() {
                        displayDatePicker(
                            startDate.orEmpty(),
                            minStartTimestamp
                        ) { newStartDate ->
                            calendarInstance.apply {
                                time = SimpleDateFormat(
                                    NUMERICAL_DATE_FORMAT,
                                    Locale.FRANCE
                                ).parse(newStartDate) ?: Date()
                                minEndTimestamp = calendarInstance.timeInMillis
                            }
                            dialogView.setStartDate(newStartDate)
                        }
                    }

                    override fun onEndDateInputClicked() {
                        // minEndTimestamp should not be before minStartTimestamp
                        displayDatePicker(
                            endDate.orEmpty(),
                            minEndTimestamp
                        ) { newEndDate ->
                            dialogView.setEndDate(newEndDate)
                        }
                    }
                }
            }

            setView(dialogView)

            setPositiveButton(R.string.send) { _, _ ->
                val newStartDate = dialogView.getStartDate().parseSelectedDateToStandardFormat()
                val newEndDate = dialogView.getEndDate().parseSelectedDateToStandardFormat()
                if (newEndDate >= newStartDate) {
                    val comment = dialogView.getUserComment()
                    onUpdate(newStartDate, newEndDate, comment)
                } else {
                    context.toast(R.string.start_date_before_end_date)
                }
            }
            setNegativeButton(R.string.cancel, null)
        }
        .show()
    }

    private fun String.parseSelectedDateToStandardFormat() = this.parseDateStringToFormat(
        NUMERICAL_DATE_FORMAT,
        DATE_SOURCE_FORMAT
    ).orEmpty()

    private fun displayDatePicker(
        date: String,
        minDateTimestamp: Long,
        selection: (String) -> Unit
    ) {
        val cldr = Calendar.getInstance().apply {
            try {
                time = SimpleDateFormat(
                    DATE_SOURCE_FORMAT,
                    Locale.getDefault()
                ).parse(date) ?: return
            } catch (e: ParseException) {
                e.recordNonFatal()
                return
            }
        }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selection(
                    context.getString(
                        R.string.selected_date_format,
                        day,
                        month + 1,
                        year
                    )
                )
            },
            cldr.get(Calendar.YEAR),
            cldr.get(Calendar.MONTH),
            cldr.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDateTimestamp
        }
        .show()
    }
}