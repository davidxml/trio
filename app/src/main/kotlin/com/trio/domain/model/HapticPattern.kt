package com.trio.domain.model

data class HapticPattern(
    val durations: LongArray,
    val amplitudes: IntArray,
    val repeatIndex: Int = -1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HapticPattern) return false
        return durations.contentEquals(other.durations) &&
                amplitudes.contentEquals(other.amplitudes) &&
                repeatIndex == other.repeatIndex
    }

    override fun hashCode(): Int {
        var result = durations.contentHashCode()
        result = 31 * result + amplitudes.contentHashCode()
        result = 31 * result + repeatIndex
        return result
    }
}
