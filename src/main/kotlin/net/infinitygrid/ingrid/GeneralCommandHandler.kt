package net.infinitygrid.ingrid

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.infinitygrid.ingrid.module.command.InfiniCommand

class GeneralCommandHandler : ListenerAdapter() {

    val commandList = mutableListOf<InfiniCommand>()

    override fun onSlashCommand(event: SlashCommandEvent) {
        commandList.forEach { infiniCommand ->
            if (infiniCommand.name == event.name) {
                infiniCommand.execute(event)
            }
        }
    }

}
