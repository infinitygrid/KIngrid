package net.infinitygrid.ingrid.module.autovoice

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.infinitygrid.ingrid.MainInfiniBot

class VoiceChannelListener : ListenerAdapter() {

    val bot = MainInfiniBot.instance

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        if (event is GuildVoiceMoveEvent) return
        val voiceChannelList = bot.config.autoVoiceChannelIds
        val joinedChannel = event.channelJoined
        val leftChannel = event.channelLeft
        val entity = event.entity
        if (joinedChannel != null && voiceChannelList.contains(joinedChannel.idLong)) {
            createVoiceChannel(entity, joinedChannel.parent!!)
        } else if (leftChannel != null && !voiceChannelList.contains(leftChannel.idLong)) {
            deleteIfChannelEmpty(leftChannel)
        }
    }

    override fun onGuildVoiceMove(event: GuildVoiceMoveEvent) {
        val voiceChannelList = bot.config.autoVoiceChannelIds
        val leftChannel = event.channelLeft
        val joinedChannel = event.channelJoined
        if (!voiceChannelList.contains(leftChannel.idLong)) deleteIfChannelEmpty(leftChannel)
        if (voiceChannelList.contains(joinedChannel.idLong)) createVoiceChannel(event.entity, joinedChannel.parent!!)
    }

    private fun createVoiceChannel(member: Member, category: Category) {
        val username = if (member.nickname != null) { member.nickname } else { member.effectiveName }
        val guild = bot.guild
        guild.createVoiceChannel("$username's Channel", category).queue { createdVoiceChannel ->
            createdVoiceChannel.putPermissionOverride(member).grant(
                Permission.MANAGE_CHANNEL,
                Permission.MANAGE_PERMISSIONS,
                Permission.VOICE_MOVE_OTHERS
            ).queue {
                guild.moveVoiceMember(member, createdVoiceChannel).queue()
            }
        }
    }

    private fun deleteIfChannelEmpty(voiceChannel: VoiceChannel) {
        val permanent = bot.config.permanentVoiceChannelIds.contains(voiceChannel.idLong)
        if (voiceChannel.members.size == 0 && !permanent) voiceChannel.delete().queue()
    }

}