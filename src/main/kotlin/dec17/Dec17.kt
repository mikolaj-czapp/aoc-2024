package org.example.dec17

import org.example.InputReader
import kotlin.math.pow

fun main() {
    val inputString = InputReader().readFile("dec17/input.txt")
    println("Result: ${Dec17.task1(inputString)}")
}

typealias Pointer = Int

class Dec17 {
    companion object {
        private val output: MutableList<ULong> = mutableListOf()

        fun task1(string: String) {
            val calculator = parseInput(string)
            calculator.executeProgram()
            println(output.joinToString(","))
        }

        private fun parseInput(input: String): ProblemCalculator {
            val registersAndProgram = input.split(Regex("\n\n"))
            val registers =
                registersAndProgram[0]
                    .split(Regex("\n"))
                    .map {
                        Regex("Register [A-C]: (\\d+)")
                            .find(it)
                            ?.groupValues
                            ?.get(1)
                            ?.toULong()
                    }
            val program =
                registersAndProgram[1]
                    .split(":")[1]
                    .trim()
                    .split(Regex(","))
                    .map { it.toULong() }
            return ProblemCalculator(
                Registry(
                    registers.get(0)!!,
                    registers.get(1)!!,
                    registers.get(2)!!,
                ),
                program,
            )
        }

        private data class ProblemCalculator(
            val registry: Registry,
            val program: List<ULong>,
        ) {
            fun executeProgram() {
                var pointer = 0
                while (pointer < program.size) {
                    val instruction = Instruction.fromDigit(program[pointer])
                    pointer = instruction.execute(program[pointer + 1], registry, pointer)
                }
            }
        }

        private enum class Instruction(
            val opcode: ULong,
        ) {
            adv(0UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.a /= 2.0.pow(getComboOperand(operand, registry).toDouble()).toULong()
                    return pointer + 2
                }
            },
            bxl(1UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.b = registry.b xor operand
                    return pointer + 2
                }
            },
            bst(2UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.b = getComboOperand(operand, registry) % 8UL
                    return pointer + 2
                }
            },
            jnz(3UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer = if (registry.a == 0UL) pointer + 2 else operand.toInt()
            },
            bxc(4UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.b = registry.b xor registry.c
                    return pointer + 2
                }
            },
            Out(5UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    output.add(getComboOperand(operand, registry) % 8UL)
                    return pointer + 2
                }
            },
            bdv(6UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.b = registry.a / 2.0.pow(getComboOperand(operand, registry).toDouble()).toULong()
                    return pointer + 2
                }
            },
            cdv(7UL) {
                override fun execute(
                    operand: ULong,
                    registry: Registry,
                    pointer: Pointer,
                ): Pointer {
                    registry.c = registry.a / 2.0.pow(getComboOperand(operand, registry).toDouble()).toULong()
                    return pointer + 2
                }
            }, ;

            abstract fun execute(
                operand: ULong,
                registry: Registry,
                pointer: Pointer,
            ): Pointer

            companion object {
                fun fromDigit(digit: ULong): Instruction = entries.first { it.opcode == digit }
            }
        }

        private fun getComboOperand(
            operand: ULong,
            registry: Registry,
        ): ULong =
            when (operand) {
                in 0UL..3UL -> operand
                4UL -> registry.a
                5UL -> registry.b
                6UL -> registry.c
                else -> throw IllegalArgumentException("Unknown operand: $operand")
            }

        private data class Registry(
            var a: ULong,
            var b: ULong,
            var c: ULong,
        )
    }
}
