package utils

import entity.*
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class Utils {
    companion object {
        const val ANSI_RESET = "\u001B[0m"
        const val ANSI_BLACK = "\u001B[30m"
        const val ANSI_RED = "\u001B[31m"
        const val ANSI_GREEN = "\u001B[32m"
        const val ANSI_YELLOW = "\u001B[33m"
        const val ANSI_BLUE = "\u001B[34m"
        const val ANSI_PURPLE = "\u001B[35m"
        const val ANSI_CYAN = "\u001B[36m"

        fun readBoardFromFile(path: String): Board {
            val inputStream = FileInputStream(File(path))
            val scanner = Scanner(inputStream)
            val board = ArrayList<ArrayList<Cell>>()
            var row = 0
            while (scanner.hasNextLine()) {
                val rowCells = ArrayList<Cell>()
                var column = 0
                scanner.nextLine().forEach {
                    val type = when (it) {
                        'w' -> CellType.WHITE
                        'b' -> CellType.BLACK
                        's' -> CellType.SOURCE
                        'd' -> CellType.DESTINATION
                        else -> throw IllegalArgumentException("FILES CONTAINS ILLEGAL VALUE($it) IN($row $column)")
                    }
                    rowCells.add(Cell(row, column++, type))
                }
                if (rowCells.size != Board.BOARD_SIZE) {
                    throw Exception("COLUMN SIZE IS ILLEGAL")
                }
                row++
                board.add(rowCells)
            }
            if (board.size != Board.BOARD_SIZE) {
                throw Exception("ROW SIZE IS ILLEGAL")
            }
            return Board(board)
        }

        fun generateRandomTable(percent: Int): Board {
            val random = Random()
            var sourceRowRandom = 0
            var sourceColumnRandom = 0
            var destinationRowRandom = 0
            var destinationColumnRandom = 0
            while (sourceRowRandom == destinationRowRandom && sourceColumnRandom == destinationColumnRandom) {
                sourceRowRandom = random.nextInt(Board.BOARD_SIZE)
                sourceColumnRandom = random.nextInt(Board.BOARD_SIZE)
                destinationRowRandom = random.nextInt(Board.BOARD_SIZE)
                destinationColumnRandom = random.nextInt(Board.BOARD_SIZE)
            }
            val board = ArrayList<ArrayList<Cell>>()
            for (i in 0 until Board.BOARD_SIZE) {
                val row = ArrayList<Cell>()
                for (j in 0 until Board.BOARD_SIZE) {
                    val cellType = if (i == sourceRowRandom && j == sourceColumnRandom) {
                        CellType.SOURCE
                    } else if (i == destinationRowRandom && j == destinationColumnRandom) {
                        CellType.DESTINATION
                    } else {
                        if (random.nextInt(100) < percent) CellType.WHITE else CellType.BLACK
                    }
                    row.add(Cell(i, j, cellType))
                }
                board.add(row)
            }

            return Board(board)
        }

        fun ArrayList<Result>.sortResults() {
            val success = filter { it.type == ResultType.SUCCESS }.sortedByDescending { it.getPathLength() }
            val cutOff = filter { it.type == ResultType.CUTOFF }.sortedBy { it.searchCost }
            val failed = filter { it.type == ResultType.FAILURE }.sortedBy { it.duration }
            clear()
            addAll(success)
            addAll(cutOff)
            addAll(failed)
        }

        fun printResults(board: Board, results: ArrayList<Result>) {
            println(getResultTable(results))
            results.forEach {
                println(getResultDetail(board, it))
            }
        }

        fun getColoredText(text: String, color: String): String {
            return color + text + ANSI_RESET
        }

        fun String.print() {
            print(this)
        }

        fun Int.getDigits(): List<Int> {
            var number = this
            val digits = ArrayList<Int>()
            while (number != 0) {
                digits.add(number % 10)
                number /= 10
            }
            return digits.reversed()
        }

        private fun getDirectionSymbol(direction: Direction): String {
            return when (direction) {
                Direction.TOP -> "^"
                Direction.RIGHT -> ">"
                Direction.BOTTOM -> "v"
                Direction.LEFT -> "<"
            }
        }

        private fun getResultTable(result: ArrayList<Result>): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ")
                .append("\n")
            stringBuilder.append(
                String.format(
                    "|\t\t|%7s|%15s|%15s|%15s|%21s|\n",
                    "STATUS",
                    "PATH LENGTH",
                    "SEARCH TIME(ms)",
                    "SEARCH COST",
                    "EXTENDED NODE COUNT"
                )
            )
            stringBuilder.append("ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ")
                .append("\n")
            result.forEach {
                stringBuilder.append(
                    String.format(
                        "|%7s|%16s|%15s|%15s|%15s|%21s|\n",
                        it.searchType,
                        getColoredResultType(it.type),
                        if (it.type == ResultType.SUCCESS) it.getPathLength() else "",
                        it.duration,
                        it.searchCost,
                        it.getExtendedNodeCount()
                    )
                )
            }
            stringBuilder.append("ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ")
                .append("\n\n")
            return stringBuilder.toString()
        }

        private fun getResultDetail(board: Board, result: Result): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(getColoredText("SEARCH ALGORITHM: ${result.searchType}\n", ANSI_YELLOW))
            stringBuilder.append(getColoredText("RESULT: ", ANSI_YELLOW))
            stringBuilder.append(getColoredResultType(result.type))
            stringBuilder.append("\n")
            stringBuilder.append(
                getColoredText(
                    "PATH LENGTH: ${if (result.type == ResultType.SUCCESS) result.getPathLength() else ""}\n",
                    ANSI_YELLOW
                )
            )
            stringBuilder.append(getColoredText("SEARCH TIME: ${result.duration} ms\n", ANSI_YELLOW))
            stringBuilder.append(getColoredText("SEARCH COST: ${result.searchCost}\n", ANSI_YELLOW))
            stringBuilder.append(
                getColoredText(
                    "EXTENDED NODE COUNT: ${result.getExtendedNodeCount()}\n\n",
                    ANSI_YELLOW
                )
            )

            board.board.forEach { row ->
                row.forEach { cell ->
                    stringBuilder.append("|")
                    if (result.solution != null &&
                        result.solution.map { it.cell }.contains(cell) &&
                        cell.type != CellType.SOURCE &&
                        cell.type != CellType.DESTINATION
                    ) {
                        val state = result.solution.find { it.cell == cell }
                        stringBuilder.append(getColoredText(getDirectionSymbol(state!!.direction!!), ANSI_GREEN))
                    } else if (result.exploredSet.contains(cell) &&
                        cell.type != CellType.SOURCE &&
                        cell.type != CellType.DESTINATION
                    ) {
                        stringBuilder.append(getColoredText(".", ANSI_RED))
                    } else {
                        stringBuilder.append(
                            when (cell.type) {
                                CellType.BLACK -> "#"
                                CellType.SOURCE -> getColoredText("S", ANSI_GREEN)
                                CellType.DESTINATION -> getColoredText("D", ANSI_GREEN)
                                else -> " "
                            }
                        )
                    }
                }
                stringBuilder.append("|")
                stringBuilder.append("\n")
            }
            return stringBuilder.toString()
        }

        private fun getColoredResultType(resultType: ResultType): String {
            return getColoredText(
                resultType.name, when (resultType) {
                    ResultType.SUCCESS -> ANSI_GREEN
                    ResultType.CUTOFF -> ANSI_YELLOW
                    ResultType.FAILURE -> ANSI_RED
                }
            )
        }
    }
}