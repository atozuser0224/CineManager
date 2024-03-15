package org.gang.cinemanager

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.gang.cinemanager.playermanage.PlayerManagement

object CinematicManager{
    val players = hashMapOf<Player , PlayerManagement>()


}