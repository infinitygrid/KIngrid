package net.infinitygrid.ingrid.module.dashboard

import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.infinitygrid.ingrid.MainInfiniBot
import java.util.concurrent.TimeUnit

class DashboardListener (private val messageId: Long,
                         private val roleMap: Map<String, Role>) : ListenerAdapter() {

    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.messageIdLong != messageId) return
        var channel = event.channel
        var reactionEmote = event.reactionEmote.toString().replace("RE:", "")
        reactionEmote = if (reactionEmote.startsWith("U+")) {
            reactionEmote.uppercase()
        } else {
            val split = reactionEmote.split('(')
            val name = split[0]
            val id = split[1].replace(")", "")
            "$name:$id"
        }
        val user = event.user
        channel.sendMessage("${user.asMention} Handling request...").queue { message ->
            event.reaction.removeReaction(user).queue {
                if (!roleMap.containsKey(reactionEmote)) return@queue
                val guild = MainInfiniBot.instance.guild
                val role = guild.getRoleById(roleMap[reactionEmote]!!.idLong)!!
                val member = event.member
                var action = ""
                val restAction = if (member.roles.contains(role)) {
                    action += "removed"
                    guild.removeRoleFromMember(member, role)
                } else {
                    action += "assigned"
                    guild.addRoleToMember(member, role)
                }
                restAction.queue {
                    message.editMessage("${user.asMention} Your role was successfully $action!").queue {
                        it.delete().queueAfter(3, TimeUnit.SECONDS)
                    }
                }
            }
        }
    }

}