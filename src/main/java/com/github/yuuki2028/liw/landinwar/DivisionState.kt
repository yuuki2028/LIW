package com.github.yuuki2028.liw.landinwar

import java.nio.ByteBuffer

class DivisionState {
    fun toByteArray():ByteArray{
        val byteArray = ByteArray(1024)
        byteArray[0] = Personnel.toByte()
        byteArray[1] = MilitaryPower.toByte()
        byteArray[2] = Food.toByte()
        return byteArray
    }
    var Personnel = 10000
    var MilitaryPower = 10000
    var Food = 10000
}