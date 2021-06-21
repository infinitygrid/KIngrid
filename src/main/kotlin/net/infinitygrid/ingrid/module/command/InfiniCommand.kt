package net.infinitygrid.ingrid.module.command

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

abstract class InfiniCommand(val name: String, val description: String) {

    abstract fun execute(event: SlashCommandEvent)

}