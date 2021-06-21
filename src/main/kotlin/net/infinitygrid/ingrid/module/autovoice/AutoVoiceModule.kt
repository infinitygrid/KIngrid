package net.infinitygrid.ingrid.module.autovoice

import net.infinitygrid.ingrid.module.BotModule

class AutoVoiceModule : BotModule() {

    override fun onEnable() {
        registerListener(VoiceChannelListener())
    }

}