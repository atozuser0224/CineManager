package org.gang.cinemanager.playermanage

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.heartbeat.coroutines.Suspension
import kotlinx.coroutines.launch
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class Catureable(val entity : NPC,val player: Player) {
    val hashmap : MutableList<Location> = mutableListOf()

    var isCapture = false
    fun capture() {
        HeartbeatScope().launch {
            val suspension = Suspension()
            var tick = 1
            while (isCapture){
                hashmap.add(player.location)
                suspension.delay(50L)
            }
        }
    }
    fun replay() {
        val time = (hashmap.size/0.8f).toInt()
        val diffs = ArrayList<Double>()
        val travelTimes = ArrayList<Int>()

        var totalDiff = 0.0

        for (i in 0 until hashmap.size - 1) {
            val s = hashmap[i]
            val n = hashmap[i + 1]
            val diff = positionDifference(s, n)
            totalDiff += diff
            diffs.add(diff)
        }

        for (i in diffs.indices) {
            val d = diffs[i]
            travelTimes.add((d / totalDiff * time).toInt())
        }

        val tps = ArrayList<Location>()
        val w: World = entity.entity.world

        for (i in 0 until hashmap.size - 1) {
            val s = hashmap[i]
            val n = hashmap[i + 1]
            val t = travelTimes[i]

            val moveX = n.x - s.x
            val moveY = n.y - s.y
            val moveZ = n.z - s.z
            val movePitch = n.pitch - s.pitch

            val yawDiff = Math.abs(n.yaw - s.yaw)
            var c = 0.0F

            if (yawDiff <= 180.0) {
                c = if (s.yaw < n.yaw) yawDiff else -yawDiff
            } else {
                c = if (s.yaw < n.yaw) -(360.0f - yawDiff) else 360.0f - yawDiff
            }

            val d = c / t

            for (x in 0 until t) {
                val l = Location(w, s.x + moveX / t * x, s.y + moveY / t * x,
                    s.z + moveZ / t * x, (s.yaw + d * x).toFloat(), (s.pitch + movePitch / t * x).toFloat())
                tps.add(l)
            }
        }
        entity.teleport(tps[0], PlayerTeleportEvent.TeleportCause.PLUGIN)
        HeartbeatScope().launch {
            val suspension = Suspension()
            tps.forEach {
                entity.navigator.setTarget(it)
                suspension.delay(50L)
            }

        }
    }
}