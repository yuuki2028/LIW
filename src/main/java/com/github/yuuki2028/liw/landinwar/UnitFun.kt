package com.github.yuuki2028.liw.landinwar

import com.github.yuuki2028.liw.landinwar.LandInWar.Companion.PC
import com.github.yuuki2028.liw.landinwar.LandInWar.Companion.red
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.util.BlockIterator
import org.bukkit.util.Vector
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object UnitFun {
    private val faces = arrayListOf(BlockFace.EAST,BlockFace.NORTH,BlockFace.SOUTH,BlockFace.WEST)
    fun shortestPath(l1:Location,l2:Location):ArrayList<Location>{
        val b = BlockIterator(l1.world!!,l1.toVector(), genVec(l1,l2),0.0,l1.distance(l2).toInt())
        val a = arrayListOf<Location>()
        for (block in b){
            a.add(block.location)
        }
        return a
    }
    fun genVec(a: Location, b: Location): Vector {
        val dX = a.x - b.x
        val dY = a.y - b.y
        val dZ = a.z - b.z
        val yaw = atan2(dZ, dX)
        val pitch = atan2(sqrt(dZ * dZ + dX * dX), dY) + Math.PI
        val x = sin(pitch) * cos(yaw)
        val y = sin(pitch) * sin(yaw)
        val z = cos(pitch)
        return Vector(x, z, y)
    }
    fun arrivalPlace(loc:Location,player:Player,villager: Villager):Boolean{
        when(loc.block.getMetadata("PLACE")[0].value()){
            PlaceState.NULL->{
                loc.block.setMetadata("PLACE",FixedMetadataValue(Bukkit.getPluginManager().getPlugin("LandInWar")!!,PlaceState.WAIT))
                return true
            }
            PlaceState.WAIT->{
                if (PC[player.uniqueId]!!.name!=villager.name) {
                    loc.block.setMetadata("PLACE", FixedMetadataValue(Bukkit.getPluginManager().getPlugin("LandInWar")!!, PlaceState.VATTLE))
                    return false
                }
                else{
                    loc.block.setMetadata("PLACE", FixedMetadataValue(Bukkit.getPluginManager().getPlugin("LandInWar")!!, PlaceState.WAIT))
                    return true
                }
            }
            PlaceState.VATTLE->{
                loc.block.setMetadata("PLACE",FixedMetadataValue(Bukkit.getPluginManager().getPlugin("LandInWar")!!,PlaceState.VATTLE))
                return true
            }
        }
        return false
    }
    fun getDivisionformLocation(loc:Location):ArrayList<Villager>{
        val stand =  loc.world!!.spawnEntity(loc,EntityType.ARMOR_STAND) as ArmorStand
        stand.isVisible = false
        stand.isSmall = true
        stand.isCollidable = false
        val villagerList = arrayListOf<Villager>()
        for (entity in stand.getNearbyEntities(0.1,0.1,0.1)){
            if (entity.type == EntityType.VILLAGER){
                villagerList.add(entity as Villager)
            }
        }
        return villagerList
    }
    fun isblockconectone(start:Block,finish:Block,befor:BlockFace,blocks:MutableList<Location>):Boolean{
        var bool = false
        val iFace = faces.clone() as ArrayList<BlockFace>
        iFace.remove(befor)
        if (start.type == finish.type) {
            for (face in iFace) {
                val now = start.getRelative(face)
                if (now.location == finish.location){return true}
                if (now.type == finish.type) if (!blocks.contains(now.location)){
                    blocks.add(start.location)
                    if (isblockconectone(now,finish,face.oppositeFace,blocks)){bool = true}
                }
            }
        }
        return bool
    }
}
