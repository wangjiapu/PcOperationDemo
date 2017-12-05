package beans

import java.util.*

data class PackByteArray (public val flag : Byte ,val len : Short, public val body:ByteArray?){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackByteArray

        if (flag != other.flag) return false
        if (!Arrays.equals(body, other.body)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = flag.toInt()
        result = 31 * result + (body?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}