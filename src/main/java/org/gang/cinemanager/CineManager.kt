package org.gang.cinemanager

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.units.qual.t
import org.gang.cinemanager.CinematicManager.players
import org.gang.cinemanager.playermanage.PlayerManagement
import org.gang.cinemanager.playermanage.cineManager
import org.gang.cinemanager.playermanage.toPlayer
import xyz.icetang.lib.kommand.getValue
import xyz.icetang.lib.kommand.kommand


class CineManager : JavaPlugin() , Listener {
    companion object{
        lateinit var plugin : CineManager
    }

    override fun onLoad() {
        plugin = this
    }
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this)
        kommand {
            register("cinema"){
                then("capture"){
                    then("who" to string().apply {
                        suggests {ctx->
                            suggest(players[ctx.source.player]?.npcs?.map { it.key }?:listOf())
                        }
                    }){
                        executes {
                            val who : String by it
                            sender.toPlayer.cineManager?.let{
                                it.isCapture = !it.isCapture
                                sender.sendMessage("[ CINEMATIC ] CAPTURE MODE = ${if (it.isCapture) ChatColor.GREEN else ChatColor.RED}${it.isCapture}")
                                it.npcs[who]?.capture()
                            }
                        }
                        then("replay"){
                            executes {
                                val who : String by it
                                sender.toPlayer.cineManager?.let{
                                    it.npcs[who]?.replay()
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    override fun onDisable() {

    }
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        players[e.player] = PlayerManagement(e.player)
    }
    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        event
    }
}
