package net.infinitygrid.ingrid.pojo;

import java.util.List;

public class InfiniBotConfig {

    public String token;
    public long operatorId;
    public long guildId;
    public List<Long> autoVoiceChannelIds;
    public List<Long> permanentVoiceChannelIds;
    public long dashboardChannelId;
    public long defaultRoleId;
    public Dashboard dashboard;

}
