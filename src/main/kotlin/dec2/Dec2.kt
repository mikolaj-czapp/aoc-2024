package org.example.dec2

import org.example.InputReader
import java.io.BufferedReader
import kotlin.math.abs

fun main() {
    val inputReaderTask1 = InputReader().getBufferedReader("dec2/test1.txt")
    println("Correct reports: ${Dec2.task1(inputReaderTask1)}")

    val inputReaderTask2 = InputReader().getBufferedReader("dec2/test1.txt")
    println("Correct reports: ${Dec2.task2(inputReaderTask2)}")
}

class Dec2 {
    companion object {
        fun task1(inputReader: BufferedReader): Int {
            var correctReports = 0
            inputReader.lines().forEach {
                val sanitized = sanitizeReport(it)
                if (isReportCorrect(sanitized)) {
                    correctReports++
                }
            }
            return correctReports
        }

        fun task2(inputReader: BufferedReader): Int {
            var correctReports = 0
            inputReader.lines().forEach {
                val sanitized = sanitizeReport(it)
                if (isReportCorrect(sanitized)) {
                    correctReports++
                } else if (isDampenedReportCorrect(sanitized)) {
                    correctReports++
                }
            }
            return correctReports
        }

        private enum class MonotonicityState {
            INITIAL,
            INCREASING,
            DECREASING,
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
    }
}
