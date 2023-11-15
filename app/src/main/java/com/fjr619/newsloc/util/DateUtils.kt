package com.fjr619.newsloc.util


import android.content.Context
import com.fjr619.newsloc.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(@ApplicationContext val context: Context) {
  fun getString(id: Int) = context.getString(id)
  fun getString(id: Int, vararg args: Any) = context.getString(id, *args)
}
object DateUtils {
  fun getTimePassedInHourMinSec(resourceProvider: ResourceProvider, timePassedMs: Long): String {
    return when {
      timePassedMs < TimeUnit.MINUTES.toMillis(1) -> {
        resourceProvider.getString(
          R.string.d_seconds_ago,
          TimeUnit.MILLISECONDS.toSeconds(timePassedMs)
        )
      }

      timePassedMs < TimeUnit.HOURS.toMillis(1) -> {
        resourceProvider.getString(
          R.string.d_minutes_ago,
          TimeUnit.MILLISECONDS.toMinutes(timePassedMs)
        )
      }

      timePassedMs < TimeUnit.HOURS.toMillis(4) -> {
        val hours = TimeUnit.MILLISECONDS.toHours(timePassedMs)
        val minutes =
          TimeUnit.MILLISECONDS.toMinutes(timePassedMs - hours * TimeUnit.HOURS.toMillis(1))
        resourceProvider.getString(R.string.d_hours_ago, hours, minutes)
      }

      else -> ""
    }
  }
}
