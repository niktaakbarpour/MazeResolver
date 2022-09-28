package algorithm

import entity.Board
import entity.Result
import entity.State
import java.util.*
import kotlin.collections.ArrayList

interface AbstractAlgorithm {
    val board: Board
    fun search(): Result

    fun execute(): Result {
        val startDate = Date().time
        val result = search()
        val endDate = Date().time
        result.duration = endDate - startDate
        return result
    }

    fun getPath(goalState: State): List<State> {
        val solution = ArrayList<State>()
        goalState.setDirection()
        solution.add(goalState)
        var currentState = goalState
        while (currentState.parent != null) {
            currentState = currentState.parent!!
            currentState.setDirection()
            solution.add(currentState)
        }
        return solution
    }
}