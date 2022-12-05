package gecko10000.servermurderer;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerMurderer extends JavaPlugin {

    public void onEnable() {
        new CommandHandler(this);
    }

}
