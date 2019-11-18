package com.github.yuuki2028.liw.landinwar

import com.github.yuuki2028.liw.landinwar.Inventory.DI
import com.github.yuuki2028.liw.landinwar.Items.Create
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.awt.Color.*
import java.util.*

class LandInWar : JavaPlugin(),Listener {

    override fun onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this)
        logger.info("正常にLIWが起動しました")
    }
    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("正常にLIWが終了しました")
    }
    @EventHandler
    fun crick(event:PlayerInteractEvent){
        val item = event.item
        if (item != null) if((event.action == Action.RIGHT_CLICK_AIR) or (event.action == Action.RIGHT_CLICK_BLOCK)){
            if (item.hasItemMeta()){
                if(item.itemMeta!!.hasDisplayName()){
                    when(item.itemMeta!!.displayName){
                        "再帰的接続確認2D(OP専用)"->{
                            if (event.player.isOp) {
                                if (event.action == Action.RIGHT_CLICK_BLOCK) {
                                    if (!item.itemMeta!!.hasLore()) {
                                        val meta = event.item!!.itemMeta!!
                                        val lore = arrayListOf("${event.clickedBlock!!.location.x}","${event.clickedBlock!!.location.z}")
                                        meta.lore = lore
                                        item.itemMeta = meta
                                        event.player.playSound(event.player.location,Sound.ENTITY_PLAYER_LEVELUP,1f,1f)
                                    }
                                    else{
                                        event.player.playSound(event.player.location,Sound.ENTITY_PLAYER_LEVELUP,1f,1f)
                                        val meta = event.item!!.itemMeta!!
                                        val start = Location(event.player.world, meta.lore?.get(0)!!.toDouble(),event.clickedBlock!!.location.y,meta.lore?.get(1)!!.toDouble()).block
                                        val finish = event.clickedBlock!!
                                        meta.lore = arrayListOf()
                                        item.itemMeta = meta
                                        event.player.sendMessage(UnitFun.isblockconectone(start,finish,BlockFace.SELF, mutableListOf()).toString())
                                    }
                                }
                            }
                        }
                        "師団作成"->{
                            event.player.openInventory(PC[event.player.uniqueId]!!.CDI.CDI)
                        }
                        "師団(未配置)"->{
                            if (event.action == Action.RIGHT_CLICK_BLOCK) {
                                if (event.clickedBlock!!.type == when(PC[event.player.uniqueId]!!.name){
                                    "赤"-> Material.RED_STAINED_GLASS
                                    "青"-> Material.BLUE_STAINED_GLASS
                                    "緑"-> Material.GREEN_STAINED_GLASS
                                    "黒"-> Material.BLACK_STAINED_GLASS
                                    else -> Material.WHITE_STAINED_GLASS
                                }){
                                    val loc = Location(event.player.world,event.clickedBlock!!.x.toDouble(),event.clickedBlock!!.y+1.3,event.clickedBlock!!.z.toDouble())
                                    val piece = event.player.world.spawnEntity(loc,EntityType.VILLAGER) as Villager
                                    piece.profession = Villager.Profession.NITWIT
                                    piece.isCustomNameVisible = true
                                    piece.customName = PC[event.player.uniqueId]!!.name
                                    piece.setAI(false)
                                    piece.isCollidable = false
                                    piece.isInvulnerable = true
                                    piece.setMetadata("DIVISION",FixedMetadataValue(this, item.itemMeta!!.persistentDataContainer.get(NamespacedKey(this,"DIVISION"),DivisionStateTag)))
                                    DIs[piece.uniqueId] = DI(piece)
                                    item.amount -= 1
                                }
                            }
                        }
                        "指揮棒"->{
                            if (event.action == Action.RIGHT_CLICK_BLOCK) {
                                if (event.item != null)if (event.item!!.hasItemMeta()) if (event.item!!.itemMeta!!.hasDisplayName()) if (event.item!!.itemMeta!!.hasLore()){
                                    val meta = event.item!!.itemMeta!!
                                    val lore = arrayListOf(event.item!!.itemMeta!!.lore!![0],"${event.clickedBlock!!.location.x},${event.clickedBlock!!.location.z}")
                                    meta.lore = lore
                                    event.item!!.itemMeta = meta
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    fun ecrick(event:PlayerInteractEntityEvent){
        if (event.rightClicked.type == EntityType.VILLAGER){
            if (event.rightClicked.hasMetadata("DIVISION")) {
                var item = event.player.inventory.itemInMainHand
                if (item.hasItemMeta()) if (item.itemMeta!!.hasDisplayName())if (item.itemMeta!!.hasLore()){
                    when(item.itemMeta!!.displayName){
                        "指揮棒"->{
                            if (item.itemMeta!!.lore!!.size >= 2){
                                val xz1 = item.itemMeta!!.lore!![0]
                                val x1 = xz1.split(",")[0].toDouble()
                                val z1 = xz1.split(",")[1].toDouble()
                                val xz2 = item.itemMeta!!.lore!![1]
                                val x2 = xz2.split(",")[0].toDouble()
                                val z2 = xz2.split(",")[1].toDouble()
                                val l1 = Location(event.player.world,x1,5.0,z1)
                                val l2 = Location(event.player.world,x2,5.0,z2)
                                val la = UnitFun.shortestPath(l1,l2)
                                val villager = event.rightClicked as Villager
                                var id = 0
                                id = scheduler.scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("LandInWar")!!,{
                                    if (la.size >= 1){
                                        val lb = la[0].clone()
                                        if (UnitFun.arrivalPlace(lb,event.player,villager)) {
                                            la.removeAt(0)
                                            villager.teleport(lb)
                                        }
                                        else{
                                            val a = villager.getMetadata("DIVISION")[0].value() as DivisionState
                                            for(villagerB in UnitFun.getDivisionformLocation(lb)){
                                                val b = villagerB.getMetadata("DIVISION")[0].value() as  DivisionState
                                            }
                                        }
                                        val lc = lb.clone()
                                        lc.y -= 1
                                        lc.block.type = when(PC[event.player.uniqueId]!!.name){
                                            "赤"-> Material.RED_STAINED_GLASS
                                            "青"-> Material.BLUE_STAINED_GLASS
                                            "緑"-> Material.GREEN_STAINED_GLASS
                                            "黒"-> Material.BLACK_STAINED_GLASS
                                            else -> Material.WHITE_STAINED_GLASS
                                        }
                                    }
                                    else{ scheduler.cancelTask(id)}
                                },0L,20L)
                            }
                        }
                    }
                }
                else{
                    val div = event.rightClicked.getMetadata("DIVISION")[0].value() as DivisionState
                    if (DIs.containsKey(event.rightClicked.uniqueId)){
                        val di = DIs[event.rightClicked.uniqueId]!!
                        event.player.openInventory(di.DIC)
                    }
                }
            }
        }
    }
    @EventHandler
    fun icrick(event: InventoryClickEvent){
        if (event.view.title == "師団作成") {
            event.isCancelled = true
            if (event.currentItem != null) if (event.currentItem!!.hasItemMeta()) if (event.currentItem!!.itemMeta!!.hasDisplayName()){
                when (event.currentItem!!.itemMeta!!.displayName) {
                    "師団作成"->{
                        if (0<=PC[event.whoClicked.uniqueId]!!.Money-1000000) {
                            PC[event.whoClicked.uniqueId]!!.CDI.divisions.add(Division())
                            PC[event.whoClicked.uniqueId]!!.Money -= 1000000
                        }
                    }
                    "師団(未配置)"->{
                        if (event.isLeftClick) {
                            if (0 <= PC[event.whoClicked.uniqueId]!!.Dominion - 10000) {
                                val item = event.currentItem!!
                                event.whoClicked.inventory.addItem(item.clone())
                                PC[event.whoClicked.uniqueId]!!.CDI.divisions.removeAt(event.slot)
                                item.amount = 0
                                PC[event.whoClicked.uniqueId]!!.Dominion -= 10000
                            }
                        }
                    }
                }
            }
        }
        else if (event.view.title == "師団操作"){
            event.isCancelled = true
            if (event.currentItem != null) if (event.currentItem!!.hasItemMeta()) if (event.currentItem!!.itemMeta!!.hasDisplayName()) {
                when (event.currentItem!!.itemMeta!!.displayName) {
                    "指揮棒" -> {
                        val stick = event.currentItem!!.clone()
                        event.whoClicked.inventory.addItem(stick)
                        event.whoClicked.closeInventory()
                    }
                }
            }
        }
    }
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (cmd.name.equals("LIW", ignoreCase = true)) {
            if (args.isEmpty()) {
                sender.sendMessage(ChatColor.DARK_RED.toString() + "コマンドの引数が足りません!!")
                return false
            }
            val sp = sender as Player
            when(args[0]){
                "start"->{
                    game = true
                    var year = 0
                    var time = 360
                    for (player in Bukkit.getOnlinePlayers()){
                        bar.addPlayer(player)
                        for (item in handsitem) {
                            player.inventory.addItem(item)
                        }
                    }
                    var id = 0
                    id = scheduler.scheduleSyncRepeatingTask(this,{
                        time -= 1
                        red.CDI.update()
                        blue.CDI.update()
                        green.CDI.update()
                        black.CDI.update()
                        bar.setTitle("${year}年${time}日")
                        if (time <= 0){
                            year += 1
                            time = 360
                        }
                        if (!game){ scheduler.cancelTask(id)}
                    },0L,20L)
                }
                "add"->{
                    when(args[1]){
                        "red"->{PC[sp.uniqueId] = red}
                        "blue"->{PC[sp.uniqueId] = blue}
                        "green"->{PC[sp.uniqueId] = green}
                        "black"->{PC[sp.uniqueId] = black}
                    }
                }
                "setup"->{
                    val sloc = Location(sender.world,0.0,5.0,0.0)
                    var iloc  = sloc.clone()
                    for(x in 0 until 100){
                        for(z in 0 until 100){
                            iloc.x = x.toDouble()
                            iloc.z = z.toDouble()
                            iloc.block.type = Material.WHITE_STAINED_GLASS
                            iloc.block.setMetadata("PLACE",FixedMetadataValue(this,PlaceState.NULL))
                        }
                    }
                }
            }
        }
        return false
    }
    companion object{
        var game = false
        val scheduler: BukkitScheduler = Bukkit.getScheduler()
        val red = Country("赤")
        val blue = Country("青")
        val green = Country("緑")
        val black = Country("黒")
        var bar = Bukkit.getServer().createBossBar(ChatColor.AQUA.toString()+"LIW", BarColor.RED, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY)
        val handsitem = arrayListOf(Create)
        val PC = hashMapOf<UUID,Country>()
        val DIs = hashMapOf<UUID,DI>()
    }
  }
