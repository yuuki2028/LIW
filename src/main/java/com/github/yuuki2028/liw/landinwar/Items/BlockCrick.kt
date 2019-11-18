package com.github.yuuki2028.liw.landinwar.Items

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object BlockCrick :ItemStack(Material.WHITE_STAINED_GLASS_PANE){
    init {
        val meta = this.itemMeta
        meta!!.setDisplayName("***")
        this.itemMeta = meta
    }
}