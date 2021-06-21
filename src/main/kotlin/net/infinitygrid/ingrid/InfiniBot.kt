package net.infinitygrid.ingrid

import com.google.common.io.Resources
import com.google.gson.Gson
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.infinitygrid.ingrid.module.command.InfiniCommand
import net.infinitygrid.ingrid.module.BotModule
import net.infinitygrid.ingrid.pojo.InfiniBotConfig

abstract class InfiniBot(resourceConfigFile: String) {

    lateinit var jda: JDA
    var config: InfiniBotConfig
    lateinit var guild: Guild
    var generalCommandHandler: GeneralCommandHandler
    private var moduleList = mutableListOf<BotModule>()

    abstract fun whenConnected()

    init {
        @Suppress("UnstableApiUsage")
        config = Gson().fromJson(Resources.getResource(resourceConfigFile)!!.readText(), InfiniBotConfig::class.java)
        generalCommandHandler = GeneralCommandHandler()
        connect {
            jda = it
            guild = it.getGuildById(config.guildId)!!
            guild.upsertCommand("ping", "test").queue()
            it.addEventListener(generalCommandHandler)
            whenConnected()
        }
    }

    private fun connect(r: (JDA) -> Unit) {
        val jda = JDABuilder.createDefault(config.token,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()
            .awaitReady()
        r.invoke(jda)
    }

    fun registerCommand(vararg infiniCommands: InfiniCommand) {
        infiniCommands.forEach { infiniCommand ->
            guild.upsertCommand(CommandData(infiniCommand.name, infiniCommand.description)).queue {
                generalCommandHandler.commandList.add(infiniCommand)
            }
        }
    }

    fun registerModule(vararg modules: BotModule) {
        modules.forEach {
            moduleList.add(it)
            it.start()
        }
    }

}
