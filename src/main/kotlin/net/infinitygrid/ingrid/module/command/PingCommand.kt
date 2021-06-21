package net.infinitygrid.ingrid.module.command

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class PingCommand : InfiniCommand("ping", "Evaluates the ping from the bot to Discord's servers") {

    override fun execute(event: SlashCommandEvent) {
        val time = System.currentTimeMillis()
        event.reply("Testing...").setEphemeral(true).flatMap {
            event.hook.editOriginalFormat("Ping: ${System.currentTimeMillis() - time}ms")
        }.queue()
    }

}