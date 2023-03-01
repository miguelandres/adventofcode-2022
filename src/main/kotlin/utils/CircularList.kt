package utils

class CircularList<out T>(private val list: List<T>) : List<T> by list {
    /**
     * Get the value at the specified [index].
     *
     * If the [index] is negative it is interpreted as an offset from the end of
     * the list. If the [index] is positive and beyond the bounds of the underlying list,
     * it wraps around again from the start of the list.
     */
    override fun get(index: Int): T =
        list[index.safely()]

    /**
     * Get a [kotlin.collections.ListIterator] starting at the specified [index]
     *
     * If the [index] is negative it is interpreted as an offset from the end of
     * the list. If the [index] is positive and beyond the bounds of the underlying list,
     * it wraps around again from the start of the list.
     *
     * This iterator ends at the end of the list.
     */
    override fun listIterator(index: Int): ListIterator<T> =
        list.listIterator(index.safely())

    /**
     * Returns a circular iterator at the specified index, indices behave similarly to [listIterator] where a negative
     * index is an offset from the end of the list, and positive out-of-bounds indices wrap around, however the circular
     * iterator never stops providing values.
     *
     * If the circular iterator is asked for its index it will return unwrapped indices, just moving forwards or
     * backwards without adjusting to the boundaries
     */
    fun circularIterator(index: Int = 0): CircularIterator = CircularIterator(index.safely())

    /**
     * Returns a circular iterator at the specified index, indices behave similarly to [listIterator] where a negative
     * index is an offset from the end of the list, and positive out-of-bounds indices wrap around, however the circular
     * iterator never stops providing values.
     *
     * Unlike [circularIterator], it will keep the underlying indices within the bounds of the list.
     */
    fun circularIteratorWrappingIndices(index: Int = 0): CircularIteratorWrappingIndices =
        CircularIteratorWrappingIndices(index)

    open inner class CircularIterator(open var index: Int) : ListIterator<T> {
        override fun hasNext(): Boolean {
            return isNotEmpty()
        }

        override fun hasPrevious(): Boolean {
            return isNotEmpty()
        }

        override fun next(): T {
            val result = list[index.safely()]
            index++
            return result
        }

        override fun nextIndex(): Int {
            return index
        }

        override fun previous(): T {
            index = previousIndex()
            return list[index.safely()]
        }

        override fun previousIndex(): Int {
            return index - 1
        }
    }

    inner class CircularIteratorWrappingIndices(override var index: Int) : CircularIterator(index) {
        init {
            index = index.safely()
        }
        override fun nextIndex(): Int {
            return index
        }

        override fun previousIndex(): Int {
            return (index - 1).safely()
        }
    }

    /**
     * Returns a String representation of the object.
     */
    override fun toString(): String =
        list.toString()

    /**
     * Extension for Int to get the safe index, got this idea from https://github.com/tginsberg/cirkle, but I wanted to
     * modify it further
     */
    private fun Int.safely(): Int =
        if (this < 0) {
            (this % size + size) % size
        } else {
            this % size
        }
}

fun <T> List<T>.circular(): CircularList<T> = CircularList(this)
