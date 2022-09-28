package entity

data class Cell(val row: Int, val column: Int, val type: CellType) {
    override fun equals(other: Any?): Boolean {
        return other is Cell && other.row == row && other.column == column && other.type == type
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        result = 31 * result + type.hashCode()
        return result
    }
}