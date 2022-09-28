package algorithm

import entity.*
import kotlin.collections.ArrayList

class BFS(override val board: Board) : AbstractAlgorithm {

    override fun search(): Result {
        val initialState = State(board.source)
        val frontier = ArrayList<State>()
        val exploredSet = ArrayList<State>()
        var cost = 0
        if (board.isGoal(initialState)) {
            return Result(SearchType.BFS, ResultType.SUCCESS, getPath(initialState), cost, exploredSet.map { it.cell })
        }
        frontier.add(initialState)
        while (frontier.isNotEmpty()) {
            val currentState = frontier.removeFirst()
            cost++
            exploredSet.add(currentState)
            board.getActions(currentState).forEach {
                if (!exploredSet.contains(it) && !frontier.contains(it)) {
                    if (board.isGoal(it)) {
                        return Result(
                            SearchType.BFS,
                            ResultType.SUCCESS,
                            getPath(it),
                            cost,
                            exploredSet.map { state -> state.cell })
                    }
                    frontier.add(it)
                }
            }
        }
        return Result(SearchType.BFS, ResultType.FAILURE, null, cost, exploredSet.map { it.cell })
    }
}