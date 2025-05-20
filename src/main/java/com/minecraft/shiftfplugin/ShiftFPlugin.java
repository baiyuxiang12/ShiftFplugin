package com.minecraft.shiftfplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShiftFPlugin extends JavaPlugin implements Listener, CommandExecutor {

    private FileConfiguration config;
    private String commandToExecute;
    private final Map<UUID, Boolean> playerSneakingMap = new HashMap<>();
    private final Map<UUID, Long> lastFPressMap = new HashMap<>();
    private final long PRESS_THRESHOLD = 500; // 按F的有效时间窗口（毫秒）

    @Override
    public void onEnable() {
        // 保存默认配置
        saveDefaultConfig();
        // 加载配置
        loadConfig();
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        // 注册命令
        getCommand("shiftf").setExecutor(this);
        
        getLogger().info("ShiftFPlugin 已启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("ShiftFPlugin 已禁用！");
    }

    private void loadConfig() {
        // 重新加载配置
        reloadConfig();
        config = getConfig();
        
        // 获取配置中的命令
        commandToExecute = config.getString("command", "say 你按下了Shift+F！");
        
        getLogger().info("配置已加载，当前命令: " + commandToExecute);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        
        // 更新玩家潜行状态
        playerSneakingMap.put(player.getUniqueId(), event.isSneaking());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // 检查玩家是否有权限使用此功能
        if (!player.hasPermission("shiftfplugin.use")) {
            return;
        }
        
        // 检查是否按下了F键（主手交互）
        if (event.getAction().name().contains("F")) {
            // 记录按下F键的时间
            lastFPressMap.put(playerId, System.currentTimeMillis());
            
            // 检查玩家是否同时按下了Shift键
            Boolean isSneaking = playerSneakingMap.getOrDefault(playerId, false);
            
            if (isSneaking) {
                // 执行配置的命令
                executeCommand(player);
            }
        }
    }

    private void executeCommand(Player player) {
        // 以玩家身份执行命令
        player.performCommand(commandToExecute);
        
        // 发送反馈消息
        if (config.getBoolean("show-feedback", true)) {
            player.sendMessage("§a已执行命令: §e" + commandToExecute);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("shiftf")) {
            // 检查权限
            if (!sender.hasPermission("shiftfplugin.admin")) {
                sender.sendMessage("§c你没有权限使用此命令！");
                return true;
            }
            
            // 处理reload子命令
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                loadConfig();
                sender.sendMessage("§a配置已重新加载！当前命令: §e" + commandToExecute);
                return true;
            }
            
            // 显示帮助信息
            sender.sendMessage("§6===== ShiftFPlugin 帮助 =====");
            sender.sendMessage("§a/shiftf reload §7- 重新加载配置文件");
            return true;
        }
        
        return false;
    }
}
