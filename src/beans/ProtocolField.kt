package beans

object ProtocolField {
    /**
     * pcOnline PC online
     *
     *
     * */
    const val pcOnline : Byte      = 0x00.toByte()
    const val phoneOnline : Byte   = 0x01.toByte()
    const val onlineSuccess : Byte = 0x02.toByte()
    const val onlineFailed : Byte  = 0x03.toByte()
    const val offline : Byte       = 0x04.toByte()

    /**
     *  File operation Fields
     * */

    const val fileGet =        0x10.toByte()
    const val fileGetBig =     0x11.toByte()
    const val fileGetOK =      0x12.toByte()
    const val fileGetHead =    0x13.toByte()
    const val fileGetBody =    0x14.toByte()
    const val fileGetEnd =     0x15.toByte()
    const val fileGetStop =    0x16.toByte()
    const val fileGetContinue =0x17.toByte()


    const val fileSend =        0x30.toByte()
    const val fileSendBig =     0x31.toByte()
    const val fileSendOK =      0x32.toByte()
    const val fileSendHead =    0x33.toByte()
    const val fileSendBody =    0x34.toByte()
    const val fileSendEnd =     0x35.toByte()
    const val fileSendStop =    0x36.toByte()
    const val fileSendContinue =0x37.toByte()
    /**
     *  Command operation Fields
     * */

    const val command =            0x20.toByte()
    const val commandreturn =      0x21.toByte()
    const val cmdScreenGetOk =     0x22.toByte()
    const val cmdScreenBody =      0x23.toByte()



    /**
     *
     */
    const val realtimescreen= 0xff.toByte()
}