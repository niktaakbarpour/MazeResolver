package entity

class Result(
    val searchType: SearchType,
    val type: ResultType,
    val solution: List<State>? = null,
    val searchCost: Int = 0,
    exploredSet: List<Cell>? = null,
) {
    var duration: Long = 0
    val exploredSet = ArrayList<Cell>()

    init {
        exploredSet?.forEach {
            if (!this.exploredSet.contains(it)) {
                this.exploredSet.add(it)
            }
        }
    }

    fun getPathLength() = (solution?.size ?: Int.MAX_VALUE) - 1
    fun getExtendedNodeCount() = exploredSet.size
}