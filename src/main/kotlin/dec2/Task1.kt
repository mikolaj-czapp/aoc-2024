package org.example.dec2

import org.example.InputReader
import kotlin.math.abs

fun main() {
    val inputReader = InputReader().getBufferedReader("dec2/input1.txt")

    var correctReports = 0
    inputReader.lines().forEach {
        val sanitized = sanitizeReport(it)
        if (isReportCorrect(sanitized)) {
            correctReports++
        } else if (isDampenedReportCorrect(sanitized)) {
            correctReports++
        }
    }
    println(correctReports)
}

private fun sanitizeReport(report: String): List<Int> = report.split(Regex("\\s+")).map { it.toInt() }

private fun isReportCorrect(report: List<Int>): Boolean = isReportMonotonic(report) && isReportGradual(report, 1, 3)

private fun isDampenedReportCorrect(report: List<Int>): Boolean {
    report.forEachIndexed { index, i ->
        if (isReportCorrect(dampReport(report, index))) {
            return true
        }
    }
    return false
}

private fun dampReport(
    report: List<Int>,
    position: Int,
): List<Int> = report.subList(0, position) + report.subList(position + 1, report.size)

private fun isReportMonotonic(report: List<Int>): Boolean {
    val monotonicityContext = MonotonicityContext()
    report.forEachIndexed { index, i ->
        if ((index + 1) < report.size) {
            if (report[index + 1] > i) {
                monotonicityContext.setState(MonotonicityState.INCREASING)
            }
            if (report[index + 1] < i) {
                monotonicityContext.setState(MonotonicityState.DECREASING)
            }
        }
    }
    return monotonicityContext.getStateChangedCount() <= 1
}

private fun isReportGradual(
    report: List<Int>,
    atLeast: Int,
    atMost: Int,
): Boolean {
    val sumOfDifferences = report.zipWithNext()
    return sumOfDifferences.all { abs(it.first - it.second) >= atLeast && abs(it.second - it.first) <= atMost }
}

private class MonotonicityContext {
    private var currentState: MonotonicityState = MonotonicityState.INITIAL
    private var stateChangedCount = 0

    fun setState(state: MonotonicityState) {
        if (currentState != state) {
            stateChangedCount++
        }
        currentState = state
    }

    fun getStateChangedCount(): Int = stateChangedCount
}

private enum class MonotonicityState {
    INITIAL,
    INCREASING,
    DECREASING,
}
