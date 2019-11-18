package com.github.yuuki2028.liw.landinwar.Inventory

import com.github.yuuki2028.liw.landinwar.Items.BlockCrick
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack

class DI (intientity:Villager){
    val DIC = Bukkit.createInventory(null,54,"師団操作")
    val villager = intientity
    init {
        for (one in 0..5) {
            this.DIC.setItem(7+(one*9), BlockCrick)
        }
        val stick = ItemStack(Material.STICK)
        val stickmeta = stick.itemMeta!!
        stickmeta.setDisplayName("指揮棒")
        stickmeta.lore = arrayListOf("${villager.location.x.toInt()},${villager.location.z.toInt()}")
        stick.itemMeta = stickmeta
        DIC.setItem(8,stick)

    }
}