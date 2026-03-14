package qwq.qwqifya.qwqifyaClient.utils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static qwq.qwqifya.qwqifyaClient.QwQifyaClient.modId;

@Config(name = modId)
public class ClientConfig implements ConfigData {
    public boolean clickEventEnabled = true;
    public boolean checkEntityData = false;
    public boolean checkBlockEntityData = false;
    public boolean checkPotionEffects = true;
}
