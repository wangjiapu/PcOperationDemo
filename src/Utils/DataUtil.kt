package Utils

import kotlin.experimental.and

object  DataUtil {
    @JvmStatic
     fun getType(byte: Byte):Int{
        var b = byte and 0xF0.toByte()
        when(b){
            0x00.toByte() -> return 0
            0x10.toByte() -> return 1
            0X20.toByte() -> return 2
        }
        return -1
    }
}