package com.github.yuuki2028.liw.landinwar.Inventory

import com.github.yuuki2028.liw.landinwar.Division
import com.github.yuuki2028.liw.landinwar.Items.BlockCrick
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CDI{
    val CDI = Bukkit.createInventory(null,54,"師団作成")
    val divisions = arrayListOf<Division>()
    fun update(){
        var id = 0
        CDI.remove(Material.IRON_HORSE_ARMOR)
        for (division in divisions){
            CDI.addItem(division)
        }
    }
    fun removeDivision(item:ItemStack){
        for (division in divisions){
            var div:ItemStack = division
            if (div == item){
                divisions.remove(division)
                break
            }
        }
    }
    init{
        for (one in 0..5) {
            this.CDI.setItem(7+(one*9), BlockCrick)
        }
        val item = ItemStack(Material.NETHER_STAR)
        val meta = item.itemMeta
        meta!!.setDisplayName("師団作成")
        meta!!.lore = arrayListOf("師団を作成します","約一か月かかります")
        item.itemMeta = meta
        this.CDI.setItem(8,item)

    }
}