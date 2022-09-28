package algorithm

import entity.*

class DLSR(override val board: Board, private val limit: Int) : AbstractAlgorithm {
    private val exploredSet = ArrayList<State>()
    private var cost = 0

    override fun search(): Result {
        return recursiveDLS(State(board.source), limit)
    }

    private fun recursiveDLS(state: State, limit: Int): Result {
        when {
            board.isGoal(state) -> {
                return Result(SearchType.DLSR, ResultType.SUCCESS, getPath(state), cost, exploredSet.map { it.cell })
            }
            limit == 0 -> {
                return Result(SearchType.DLSR, ResultType.CUTOFF, null, cost, exploredSet.map { it.cell })
            }
            else -> {
                var cutoffOccurred = false
                exploredSet.add(state)
                cost++
                board.getActions(state).forEach {
                    val result = recursiveDLS(it, limit - 1)
                    if (result.type == ResultType.CUTOFF) {
                        cutoffOccurred = true
                    } else if (result.type != ResultType.FAILURE) {
                        return result
                    }
                }
                return if (cutoffOccurred) {
                    Result(SearchType.DLSR, ResultType.CUTOFF, null, cost, exploredSet.map { it.cell })
                } else {
                    Result(SearchType.DLSR, ResultType.FAILURE, null, cost, exploredSet.map { it.cell })
                }
            }
        }
    }
}