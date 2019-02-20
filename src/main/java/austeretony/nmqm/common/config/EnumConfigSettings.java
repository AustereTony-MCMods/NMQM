package austeretony.nmqm.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum EnumConfigSettings {

    EXTERNAL_CONFIG(0, "main", "external_config", true),
    SERVER_ONLY(0, "main", "server_only"),
    CUSTOM_UPDATE_CHECKER(0, "main", "custom_update_checker"),
    MODE(1, "main", "list_mode"),
    AUTOSAVE(0, "settings", "enable_autosave");

    public final int type;//0 - boolean, 1 - int

    public final String configSection, configKey;

    public final boolean exclude;

    private boolean isEnabled;

    private int intValue;

    EnumConfigSettings(int type, String configSection, String configKey, boolean... exclude) {
        this.type = type;
        this.configSection = configSection;
        this.configKey = configKey;
        this.exclude = exclude.length > 0 ? exclude[0] : false;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public int getIntValue() {
        return this.intValue;
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(this.configSection).getAsJsonObject().get(this.configKey);
    }

    public void initByType(JsonObject jsonObject) {
        switch (this.type) {
        case 0:
            this.isEnabled = this.getValue(jsonObject).getAsBoolean();
            break;
        case 1:
            this.intValue = this.getValue(jsonObject).getAsInt();
            break;
        }
    }

    public static void initAll(JsonObject config) {
        for (EnumConfigSettings enumSetting : values())
            if (!enumSetting.exclude)
                enumSetting.initByType(config);
    }
}
