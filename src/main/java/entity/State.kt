package entity

data class State(val cell: Cell, val parent: State? = null) {
    var direction: Direction? = null

    fun setDirection() {
        if (parent != null) {
            direction = if (parent.cell.row == cell.row) {
                if (cell.column > parent.cell.column) {
                    Direction.RIGHT
                } else {
                    Direction.LEFT
                }
            } else {
                if (cell.row > parent.cell.row) {
                    Direction.BOTTOM
                } else {
                    Direction.TOP
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is State && other.cell == cell
    }

    override fun hashCode(): Int {
        return cell.hashCode()
    }
}