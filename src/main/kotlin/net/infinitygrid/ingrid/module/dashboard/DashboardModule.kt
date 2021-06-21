package net.infinitygrid.ingrid.module.dashboard

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote
import net.infinitygrid.ingrid.MainInfiniBot
import net.infinitygrid.ingrid.module.BotModule

class DashboardModule : BotModule() {

    private val bot = MainInfiniBot.instance
    private val textChannel: TextChannel = bot.jda.getTextChannelById(bot.config.dashboardChannelId)!!
    private val dashboardConfig = bot.config.dashboard

    override fun onEnable() {
        // constructMessagesByConfig()
        val roleMap = mutableMapOf<String, Role>()
        dashboardConfig.options.forEach { option ->
            val emojiId = option.emojiId
            val reactionEmote = if (emojiId != null) {
                ReactionEmote.fromCustom(bot.jda.getEmoteById(emojiId)!!).asReactionCode
            } else {
                ReactionEmote.fromUnicode(option.emojiUnicode, bot.jda).asReactionCode
            }
            roleMap[reactionEmote] = bot.jda.getRoleById(option.roleId)!!
        }
        registerListener(DashboardListener(bot.config.dashboard.messageId, roleMap))
    }

    private fun constructMessagesByConfig() {
        val bot = MainInfiniBot.instance
        val spacerEmote = MainInfiniBot.instance.jda.getEmoteById(850759812291100692)!!.asMention
        val titleEmbed = EmbedBuilder()
            .setImage(dashboardConfig.titleUrl)
            .setColor(Integer.parseInt(dashboardConfig.titleEmbedColor, 16))
            .build()
        val mainEmbedBuilder = EmbedBuilder()
            .setTitle("Notification Settings")
            .setDescription("React with their respective emoji to be notified about it and always stay up-to-date about the things you care about!")
            .setColor(Integer.parseInt(dashboardConfig.mainEmbedColor, 16))

        dashboardConfig.options.forEach { option ->
            val emoteId = option.emojiId
            var title = if (emoteId != null) {
                bot.jda.getEmoteById(option.emojiId)?.asMention
            } else {
                ":${option.emoji}:"
            }
            title += " ${option.name}"
            mainEmbedBuilder.addField(title, "$spacerEmote ${option.description}", false)
        }

        textChannel.sendMessage(titleEmbed).queue {
            textChannel.sendMessage(mainEmbedBuilder.build()).queue {
                dashboardConfig.options.forEach { option ->
                    val emojiId: Long? = option.emojiId
                    val restAction = if (emojiId != null) {
                        it.addReaction(bot.jda.getEmoteById(emojiId)!!)
                    } else {
                        it.addReaction(option.emojiUnicode)
                    }
                    restAction.queue()
                }
            }
        }

    }

}
