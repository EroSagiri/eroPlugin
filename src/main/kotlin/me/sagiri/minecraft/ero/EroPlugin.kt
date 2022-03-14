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
    var eroPlugin: EroPlugin

    constructor(eroPlugin: EroPlugin) {
        this.eroPlugin = eroPlugin
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        EroScope.launch {
            val url = LoliApp.get(if (eroPlugin.config.getBoolean("r18")) 2 else 1)!!.data[0].url
            sender.sendMessage(url)
        }
        return true
    }
}