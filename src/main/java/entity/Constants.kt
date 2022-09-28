package entity

enum class CellType {
    WHITE,
    BLACK,
    SOURCE,
    DESTINATION
}

enum class ResultType {
    SUCCESS,
    CUTOFF,
    FAILURE
}

enum class Direction {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

enum class SearchType {
    BFS,
    DLS,
    DLSR,
    IDS,
    IDSR,
    UCS,
    A_STAR
}
