import algorithm.*
import entity.Result
import utils.Utils
import utils.Utils.Companion.getDigits
import utils.Utils.Companion.print
import utils.Utils.Companion.sortResults
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val scanner = Scanner(System.`in`)
    Utils.getColoredText("MAZE RESOLVER!\n", Utils.ANSI_BLUE).print()
    Utils.getColoredText(
        "You Can Enter Maze By Import A File Or Generate It Randomly,\nWhich One Do You Prefer(1:File, 2:Random): ",
        Utils.ANSI_YELLOW
    ).print()
    val board = when (scanner.nextInt()) {
        1 -> {
            Utils.getColoredText("Enter The File Path:\n", Utils.ANSI_RED).print()
            val path = scanner.next()
            Utils.readBoardFromFile(path)
        }
        2 -> {
            Utils.getColoredText("Enter The Percent Of White Houses (0-100): ", Utils.ANSI_RED).print()
            val percent = scanner.nextInt()
            if (percent < 0 || percent > 100) {
                throw Exception("Error: Illegal Value")
            }
            Utils.generateRandomTable(percent)
        }
        else -> throw Exception("Error: Illegal Value")
    }
    Utils.getColoredText(
        "Available Search Algorithm:\n\t1:BFS\n\t2:DLS\n\t3:IDS\n\t4:UCS\n\t5:A*\nEnter A Number Which Contains The Number Of Preferred Algorithm?: ",
        Utils.ANSI_YELLOW
    ).print()
    val selectedAlgorithms = scanner.nextInt().getDigits()
    var limit = 0
    if (selectedAlgorithms.contains(2)) {
        Utils.getColoredText("Enter Limit For DLS Algorithm: ", Utils.ANSI_YELLOW).print()
        limit = scanner.nextInt()
        if (limit < 0) {
            throw Exception("Error: Illegal Value")
        }
    }
    var recursive = false
    if (selectedAlgorithms.contains(2) || selectedAlgorithms.contains(3)) {
        Utils.getColoredText("Use Recursive Methods For DLS/IDS?(y,n): ", Utils.ANSI_RED).print()
        recursive = when (scanner.next()) {
            "y" -> true
            "n" -> false
            else -> throw Exception("Error: Illegal Value")
        }
    }

    val results = ArrayList<Result>()
    selectedAlgorithms.forEach {
        val searcher = when (it) {
            1 -> BFS(board)
            2 -> if (recursive) DLSR(board, limit) else DLS(board, limit)
            3 -> IDS(board, recursive)
            4 -> UCS(board)
            5 -> AStar(board)
            else -> throw Exception("Error: Illegal Value")
        }
        results.add(searcher.execute())
    }
    results.sortResults()
    Utils.printResults(board, results)
}