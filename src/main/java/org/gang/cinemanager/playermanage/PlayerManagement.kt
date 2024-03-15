package org.gang.cinemanager.playermanage

import com.sun.tools.javac.code.Kinds.KindSelector.VAL
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.heartbeat.coroutines.Suspension
import kotlinx.coroutines.launch
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.gang.cinemanager.CinematicManager

class PlayerManagement(val player : Player) {
    val hashmap : MutableList<Location> = mutableListOf()

    var isCapture = false
    val npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.name);
    var npcs : MutableMap<String, Catureable> = mutableMapOf<String, Catureable> ()

    init {
        npcs["test"] = Catureable(npc,player)
    }
}

val Player.cineManager : PlayerManagement? get() = CinematicManager.players[this]

val CommandSender.toPlayer : Player get() = this as Player

fun positionDifference(cLoc: Location, eLoc: Location): Double {
    val cX = cLoc.x
    val cY = cLoc.y
    val cZ = cLoc.z

    val eX = eLoc.x
    val eY = eLoc.y
    val eZ = eLoc.z

    var dX = eX - cX
    if (dX < 0.0) {
        dX = -dX
    }
    var dZ = eZ - cZ
    if (dZ < 0.0) {
        dZ = -dZ
    }
    val dXZ = Math.hypot(dX, dZ)

    var dY = eY - cY
    if (dY < 0.0) {
        dY = -dY
    }
    val dXYZ = Math.hypot(dXZ, dY)

    return dXYZ
}
