package me.sagiri.minecraft.ero

import kotlinx.coroutines.launch
import me.sagiri.minecraft.ero.loliapp.LoliApp
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class EroPlugin : JavaPlugin()  {
    companion object {

    }

    override fun onLoad() {
        super.onLoad()
        config.addDefault("r18", false)
        config.options().copyDefaults()
        saveDefaultConfig()
    }

    override fun onEnable() {
        super.onEnable()
        getCommand("get")?.setExecutor(EroCommandExecutor(this))
    }

    override fun onDisable() {
        super.onDisable()
    }
}

class EroCommandExecutor(var eroPlugin: EroPlugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        EroScope.launch {
            val response = LoliApp.get(
                r18 = if (eroPlugin.config.getBoolean("r18")) 1 else 0,
                tag = if (args.isNotEmpty()) args.toList().toTypedArray() else null
            )
            if (response != null && response.data.isNotEmpty()) {
                val imageData = response.data[0]
                sender.sendMessage("""
                    title:  ${imageData.title}
                    author: ${imageData.author}
                    pid:    ${imageData.pid}
                    tag:    ${imageData.tags.joinToString()}
                    url:    ${imageData.url}
                """.trimIndent())
            } else {
                sender.sendMessage("获取图片失败了")
            }
        }
        return true
    }
}