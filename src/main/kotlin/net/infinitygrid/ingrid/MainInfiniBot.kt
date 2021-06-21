package net.infinitygrid.ingrid

import net.dv8tion.jda.api.entities.Activity
import net.infinitygrid.ingrid.module.autorole.AutoMemberModule
import net.infinitygrid.ingrid.module.autovoice.AutoVoiceModule
import net.infinitygrid.ingrid.module.command.CommandModule
import net.infinitygrid.ingrid.module.dashboard.DashboardModule

class MainInfiniBot : InfiniBot("config.json") {

    companion object {
        lateinit var instance: MainInfiniBot
    }

    override fun whenConnected() {
        instance = this
        registerModule(AutoVoiceModule(), CommandModule(), DashboardModule(), AutoMemberModule())
        jda.presence.activity = Activity.watching("\u200B")
    }

}
