package me.sagiri.minecraft.ero

import kotlinx.coroutines.launch
import me.sagiri.minecraft.ero.loliapp.LoliApp
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.java.JavaPlugin

class EroPlugin : JavaPlugin()  {
    companion object {

    }

    override fun onLoad() {
        super.onLoad()
        config.addDefault("r18", false)
        config.options().copyDefaults()
        saveConfig()
    }

    override fun onEnable() {
        super.onEnable()
        getCommand("get")?.setExecutor(EroCommandExecutor(this))
    }

    override fun onDisable() {
        super.onDisable()
    }
}

class EroCommandExecutor : CommandExecutor {
    lateinit var eroPlugin: EroPlugin

    constructor(eroPlugin: EroPlugin) {
        this.eroPlugin = eroPlugin
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val playerGetEroPicture = PlayerGetEroPictureEvent(sender, true)
            Bukkit.getPluginManager().callEvent(playerGetEroPicture)
        }
        EroScope.launch {
            val url = LoliApp.get(if (eroPlugin.config.getBoolean("r18")) 2 else 1)!!.data[0].url
            sender.sendMessage(url)
            if(sender is Player) {
                val playerGetEroPictureEvented = PlayerGetEroPictureEventEd(sender, true, url)
                Bukkit.getPluginManager().callEvent(playerGetEroPictureEvented)
            }
        }
        return true
    }
}

class PlayerGetEroPictureEvent : PlayerEvent {
    companion object {
        private val HANDLERS = HandlerList()
    }
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    constructor(player : Player) : super(player) {

    }

    constructor(player: Player, async : Boolean) : super(player, async) {

    }
}

class PlayerGetEroPictureEventEd : PlayerEvent {
    lateinit var url: String
    companion object {
        private val HANDLERS = HandlerList()
    }
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    constructor(player : Player, url : String) : super(player) {
        this.url = url
    }

    constructor(player: Player, async : Boolean, url: String) : super(player, async) {
        this.url = url
    }
}