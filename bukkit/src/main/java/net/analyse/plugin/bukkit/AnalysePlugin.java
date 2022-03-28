package net.analyse.plugin.bukkit;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import net.analyse.plugin.bukkit.commands.AnalyseCommand;
import net.analyse.plugin.bukkit.event.ServerHeartbeatEvent;
import net.analyse.plugin.bukkit.listener.PlayerActivityListener;
import net.analyse.plugin.bukkit.util.Config;
import net.analyse.sdk.AnalyseSDK;
import net.analyse.sdk.exception.ServerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPooled;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static net.analyse.plugin.bukkit.util.EncryptUtil.generateEncryptionKey;

public class AnalysePlugin extends JavaPlugin {
    private final Map<UUID, Date> activeJoinMap = new TCustomHashMap<>(new IdentityHashingStrategy<>());
    private final Map<UUID, String> playerDomainMap = new TCustomHashMap<>(new IdentityHashingStrategy<>());

    private AnalyseSDK core = null;

    private boolean setup;
    private boolean papiHooked;

    private String serverToken;
    private String encryptionKey;
    private JedisPooled redis = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        serverToken = getConfig().getString("server.token");
        encryptionKey = getConfig().getString("encryption-key");

        setup = serverToken != null && !serverToken.isEmpty();

        papiHooked = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

        getCommand("analyse").setExecutor(new AnalyseCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerActivityListener(this), this);

        ServerHeartbeatEvent serverHeartBeatEvent = new ServerHeartbeatEvent(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, serverHeartBeatEvent, 0, 20 * 10);

        if (Config.ADVANCED_MODE) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> this.redis = new JedisPooled(Config.REDIS_HOST, Config.REDIS_PORT));
        }

        if (encryptionKey == null || encryptionKey.isEmpty()) {
            encryptionKey = generateEncryptionKey(64);
            getConfig().set("encryption-key", encryptionKey);
            getLogger().info("Generated encryption key.");
            saveConfig();
        }

        if (!setup) {
            getLogger().info("Hey! I'm not yet set-up, please run the following command:");
            getLogger().info("/analyse setup <server-token>");
        } else {
            core = new AnalyseSDK(serverToken, encryptionKey);
            try {
                getLogger().info("Linked Analyse to " + core.getServer().getName() + ".");
            } catch (ServerNotFoundException e) {
                getLogger().warning("The server linked no longer exists.");
            }
        }

        debug("Successfully booted!");
        debug("- Debug Enabled.");
        debug("- Enabled Stats: " + String.join(", ", Config.ENABLED_STATS));
        debug("- Excluded Players: " + String.join(", ", Config.EXCLUDED_PLAYERS));
        debug("- Min Session: " + Config.MIN_SESSION_DURATION);
        debug("- Advanced Mode: " + Config.ADVANCED_MODE);
    }

    @Override
    public void onDisable() {
        activeJoinMap.clear();
        playerDomainMap.clear();
    }

    public Map<UUID, Date> getActiveJoinMap() {
        return activeJoinMap;
    }

    public Map<UUID, String> getPlayerDomainMap() {
        return playerDomainMap;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    public String parse(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public boolean isPapiHooked() {
        return papiHooked;
    }

    public AnalyseSDK getCore() {
        return core;
    }

    public AnalyseSDK setup(String token) {
        core = new AnalyseSDK(token, encryptionKey);
        return core;
    }

    public JedisPooled getRedis() {
        return redis;
    }

    public void debug(String message) {
        if(Config.DEBUG) getLogger().info("DEBUG: " + message);
    }
}