package com.github.yuuki2028.liw.landinwar

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.tags.ItemTagType
import org.bukkit.persistence.PersistentDataType
import javax.swing.text.html.HTML.Tag.P

class Division:ItemStack(Material.IRON_HORSE_ARMOR){
    var Personnel = 10000
    var MilitaryPower = 10000
    var food = 10000
    override fun clone(): Division {
        return this
    }
    init {
        var time = 31
        val meta = this.itemMeta!!
        meta.setDisplayName("師団作成中")
        meta.lore = arrayListOf("${time}日")
        this.itemMeta = meta
        var id = 0
        id = LandInWar.scheduler.scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("LandInWar")!!,{
            time -=1
            meta.lore = arrayListOf("${time}日")
            this.itemMeta = meta
            if (time<=0){
                meta.setDisplayName("師団(未配置)")
                meta.lore = arrayListOf("人員${Personnel}","軍事力${MilitaryPower}","食料${food}")
                meta.persistentDataContainer.set(NamespacedKey(Bukkit.getPluginManager().getPlugin("LandInWar")!!,"DIVISION"),DivisionStateTag,DivisionState())
                this.itemMeta = meta
                LandInWar.scheduler.cancelTask(id)
            }
        },0L,20L)
    }
}