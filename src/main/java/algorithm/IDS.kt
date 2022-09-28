package algorithm

import entity.*

class IDS(override val board: Board, private val useRecursive: Boolean) : AbstractAlgorithm {
    private val searchType: SearchType = if (useRecursive) {
        SearchType.IDSR
    } else {
        SearchType.IDS
    }

    override fun search(): Result {
        var cost = 0
        val exploredSet = ArrayList<Cell>()
        for (limit in 0 until Int.MAX_VALUE) {
            val result = (if (useRecursive) {
                DLSR(board, limit)
            } else {
                DLS(board, limit)
            }).execute()
            result.exploredSet.forEach {
                if (!exploredSet.contains(it)) {
                    exploredSet.add(it)
                }
            }
            cost += result.searchCost
            if (result.type != ResultType.CUTOFF) {
                return Result(searchType, result.type, result.solution, cost, exploredSet)
            }
        }
        return Result(searchType, ResultType.FAILURE, null, cost, exploredSet)
    }
}