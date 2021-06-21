package net.infinitygrid.ingrid.module.autorole

import net.infinitygrid.ingrid.module.BotModule

class AutoMemberModule : BotModule() {

    override fun onEnable() {
        registerListener(JoinListener())
    }

}