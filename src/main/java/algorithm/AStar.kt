package algorithm

import entity.Board
import entity.State

class AStar(board: Board) : UCS(board) {
    override fun calculateF(state: State): Double {
        val g = getPath(state).size
        return g + board.calculateHeuristic(state.cell)
    }
}