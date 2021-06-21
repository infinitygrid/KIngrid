package net.infinitygrid.ingrid.module.command

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction
import net.infinitygrid.ingrid.MainInfiniBot

class EditCommand : InfiniCommand("toggleeditmode", "Toggles edit mode") {

    companion object {
        const val editRoleId = 817109177934544956L
    }

    private val bot = MainInfiniBot.instance

    override fun execute(event: SlashCommandEvent) {
        val member = event.member!!
        val editRole = event.jda.getRoleById(editRoleId)!!
        if (member.user.idLong != bot.config.operatorId) return
        var message = "Role successfully "
        val restAction: AuditableRestAction<Void> = if (member.roles.contains(editRole)) {
            message += "revoked."
            bot.guild.removeRoleFromMember(member, editRole)

        } else {
            message += "granted."
            bot.guild.addRoleToMember(member, editRole)
        }
        restAction.queue {
            event.reply(message).setEphemeral(true).queue()
        }
    }

}