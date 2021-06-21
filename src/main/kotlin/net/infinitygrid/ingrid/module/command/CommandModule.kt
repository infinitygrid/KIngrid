package net.infinitygrid.ingrid.module.command

import net.infinitygrid.ingrid.module.BotModule

class CommandModule : BotModule() {

    override fun onEnable() {
        registerCommand(PingCommand(), EditCommand())
    }

}