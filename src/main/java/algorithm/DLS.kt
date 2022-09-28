package algorithm

import entity.*
import java.util.ArrayList
import java.util.Stack

class DLS(override val board: Board, private var limit: Int) : AbstractAlgorithm {

    override fun search(): Result {
        val nodeStack = Stack<State>()
        val exploredSet = ArrayList<State>()
        var cost = -1
        nodeStack.add(State(board.source))

        while (!nodeStack.isEmpty()) {
            if (limit >= 0) {
                val current = nodeStack.pop()
                cost++
                if (board.isGoal(current)) {
                    return Result(
                        SearchType.DLS,
                        ResultType.SUCCESS,
                        getPath(current),
                        cost,
                        exploredSet.map { it.cell })
                } else {
                    exploredSet.add(current)
                    nodeStack.addAll(board.getActions(current).reversed())
                    limit--
                }
            } else {
                return Result(SearchType.DLS, ResultType.CUTOFF, null, cost, exploredSet.map { it.cell })
            }
        }
        return Result(SearchType.DLS, ResultType.FAILURE, null, cost, exploredSet.map { it.cell })
    }
}