package austeretony.nmqm.common.commands;

public enum EnumCommandNMQMArgs {

    HELP("help"),
    ENABLE("enable"),
    DISABLE("disable"),
    STATUS("status"),
    ENABLE_CONFIG("enable-conf"),
    DISABLE_CONFIG("disable-conf"),
    SETTINGS("settings"),
    LATEST("latest"),
    DENY("deny"),
    ALLOW("allow"),
    CLEAR_ALL("clear-all"),
    SAVE("save"),
    BACKUP("backup");

    public final String arg;

    EnumCommandNMQMArgs(String arg) {
        this.arg = arg;
    }

    public static EnumCommandNMQMArgs get(String strArg) {
        for (EnumCommandNMQMArgs arg : values())
            if (arg.arg.equals(strArg))
                return arg;
        return null;
    }

    @Override
    public String toString() {
        return this.arg;
    }
}
