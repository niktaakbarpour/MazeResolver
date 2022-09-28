package algorithm

import entity.*

open class UCS(override val board: Board) : AbstractAlgorithm {

    override fun search(): Result {
        val searchType = if (this is AStar) {
            SearchType.A_STAR
        } else {
            SearchType.UCS
        }
        val initialState = State(board.source)
        val frontier = ArrayList<Pair<State, Double>>()
        insertToFrontier(initialState, frontier)
        val exploredSet = ArrayList<State>()
        var cost = 0
        while (frontier.isNotEmpty()) {
            val currentState = frontier.removeFirst()
            if (board.isGoal(currentState.first)) {
                return Result(
                    searchType,
                    ResultType.SUCCESS,
                    getPath(currentState.first),
                    cost,
                    exploredSet.map { state -> state.cell })
            }
            cost++
            exploredSet.add(currentState.first)
            board.getActions(currentState.first).forEach { state ->
                if (!exploredSet.contains(state) && !frontier.map { it.first }.contains(state)) {
                    insertToFrontier(state, frontier)
                } else if (frontier.map { it.first }.contains(state)) {
                    val oldState = frontier.find { it.first == state }!!
                    val newCost = calculateF(state)
                    if (newCost < oldState.second) {
                        frontier.remove(oldState)
                        insertToFrontier(state, frontier)
                    }
                }
            }
        }
        return Result(searchType, ResultType.FAILURE, null, cost, exploredSet.map { it.cell })
    }

    private fun insertToFrontier(state: State, frontier: ArrayList<Pair<State, Double>>) {
        frontier.add(Pair(state, calculateF(state)))
        frontier.sortBy { it.second }
    }

    open fun calculateF(state: State): Double {
        return getPath(state).size.toDouble()
    }
}