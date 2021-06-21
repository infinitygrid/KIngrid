package net.infinitygrid.ingrid.module.autorole

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.infinitygrid.ingrid.MainInfiniBot

class JoinListener : ListenerAdapter() {

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val member = event.member
        val guild = event.guild
        val role = guild.getRoleById(MainInfiniBot.instance.config.defaultRoleId)!!
        guild.addRoleToMember(member, role).queue()
    }

}