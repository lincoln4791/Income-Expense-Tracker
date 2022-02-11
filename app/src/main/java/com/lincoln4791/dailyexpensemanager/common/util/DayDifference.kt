package com.itmedicus.patientaid.utils

import java.util.*

class DayDifference {
    companion object{
        fun getDaysDifference(fromDate: Date?, toDate: Date?): Int {
            return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24)).toInt()
        }
    }
}