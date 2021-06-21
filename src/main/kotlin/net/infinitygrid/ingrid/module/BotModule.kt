package net.infinitygrid.ingrid.module

import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.infinitygrid.ingrid.MainInfiniBot
import net.infinitygrid.ingrid.module.command.InfiniCommand

abstract class BotModule {

    private val listenerList = mutableListOf<ListenerAdapter>()
    private val commandList = mutableListOf<InfiniCommand>()

    fun start() {
        onEnable()
    }

    private val bot = MainInfiniBot.instance

    fun shutdown() {
        listenerList.forEach { bot.jda.removeEventListener(it) }
        onDisable()
    }

    open fun onEnable() {}
    open fun onDisable() {}

    fun registerListener(vararg listeners: ListenerAdapter) {
        bot.jda.addEventListener(*listeners)
        listeners.forEach { listenerList.add(it) }
    }

    fun registerCommand(vararg infiniCommands: InfiniCommand) {
        infiniCommands.forEach { infiniCommand ->
            bot.guild.upsertCommand(CommandData(infiniCommand.name, infiniCommand.description)).queue {
                bot.generalCommandHandler.commandList.add(infiniCommand)
                commandList.add(infiniCommand)
            }
        }
    }

}