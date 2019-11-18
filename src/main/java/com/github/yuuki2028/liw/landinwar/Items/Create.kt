package com.github.yuuki2028.liw.landinwar.Items

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Create: ItemStack(Material.DIAMOND_HORSE_ARMOR){
    init {
        var meta = this.itemMeta
        meta!!.setDisplayName("師団作成")
        this.itemMeta = meta
    }
}