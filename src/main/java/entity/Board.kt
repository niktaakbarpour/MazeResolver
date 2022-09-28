package entity

import java.lang.Exception
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Board(val board: ArrayList<ArrayList<Cell>>) {
    lateinit var source: Cell
    lateinit var destination: Cell

    init {
        board.forEach { row ->
            row.forEach { cell ->
                if (cell.type == CellType.SOURCE) {
                    source = cell
                } else if (cell.type == CellType.DESTINATION) {
                    destination = cell
                }
            }
        }
        if (!(::source.isInitialized && ::destination.isInitialized)) {
            throw Exception("SOURCE OR DESTINATION IS NOT INITIALIZED")
        }
    }

    fun getActions(state: State, shouldReorder: Boolean = false): List<State> {
        val children = ArrayList<State>()
        val cell = state.cell
        fun addIfNotBlack(child: Cell) {
            if (child.type != CellType.BLACK && child != state.parent?.cell) {
                children.add(State(child, state))
            }
        }
        //top
        if (cell.row > 0) {
            board[cell.row - 1][cell.column].also {
                addIfNotBlack(it)
            }
        }
        //right
        if (cell.column + 1 < BOARD_SIZE) {
            board[cell.row][cell.column + 1].also {
                addIfNotBlack(it)
            }
        }
        //bottom
        if (cell.row + 1 < BOARD_SIZE) {
            board[cell.row + 1][cell.column].also {
                addIfNotBlack(it)
            }
        }
        //left
        if (cell.column > 0) {
            board[cell.row][cell.column - 1].also {
                addIfNotBlack(it)
            }
        }
        if (shouldReorder) {
            children.shuffle()
        }
        return children
    }

    fun isGoal(state: State): Boolean {
        return state.cell == destination
    }

    fun calculateHeuristic(cell: Cell): Double {
        val x = (cell.row - destination.row).toDouble().pow(2)
        val y = (cell.column - destination.column).toDouble().pow(2)
        return sqrt(x + y)
    }

    companion object {
        const val BOARD_SIZE = 20
    }
}