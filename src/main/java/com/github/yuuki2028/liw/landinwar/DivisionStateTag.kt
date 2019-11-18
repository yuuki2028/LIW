package com.github.yuuki2028.liw.landinwar

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

import java.nio.ByteBuffer
import java.util.UUID

object DivisionStateTag : PersistentDataType<ByteArray, DivisionState> {

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<DivisionState> {
        return DivisionState::class.java
    }
    override fun toPrimitive(complex: DivisionState, context: PersistentDataAdapterContext): ByteArray {
        return complex.toByteArray()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): DivisionState {
        val Personnel = primitive[0].toInt()
        val MilitaryPower = primitive[1].toInt()
        val Food = primitive[2].toInt()
        val a = DivisionState()
        a.Personnel = Personnel
        a.MilitaryPower = MilitaryPower
        a.Food = Food
        return a
    }
}
