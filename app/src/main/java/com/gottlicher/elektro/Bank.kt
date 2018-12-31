package com.gottlicher.elektro

import android.content.Context
import java.util.*

enum class Color(val color:Int) {
    RED(R.color.pl_red),
    BLUE(R.color.pl_blue),
    BLACK(R.color.pl_black),
    YELLOW(R.color.pl_yellow),
    GREEN(R.color.pl_green),
    PURPLE(R.color.pl_purple),
    DISABLED(R.color.pl_disabled)
}

data class Player(var money:Int, val name:String, val color:Color, val pastTransactions:ArrayDeque<Int>)


const val PrefKey = "PLAYERS"
const val PrefName = "NAME"
const val PrefColor = "COLOR"
const val PrefMoney = "MONEY"
const val MAX_PLAYERS = 6
class Bank (private var players: ArrayList<Player>, context: Context){

    init {
        if (players.isEmpty()) {
            players = ArrayList(6)
            val pref = context.getSharedPreferences(PrefKey, Context.MODE_PRIVATE)

            for (i in 0 until MAX_PLAYERS) {
                if (pref.contains("$PrefName$i")) {
                    val name = pref.getString("$PrefName$i", "${PrefName}i")
                    val money = pref.getInt("$PrefMoney$i", 50)
                    val color = pref.getInt("$PrefColor$i", Color.RED.ordinal)
                    players.add(Player(money, name, Color.values()[color], ArrayDeque(0)))
                }
            }
        }
    }

    fun writeToPref(context:Context){
        val pref = context.getSharedPreferences(PrefKey, Context.MODE_PRIVATE).edit()

        for (i in 0 until players.size) {
            pref.putString("$PrefName$i", players[i].name)
            pref.putInt("$PrefMoney$i", players[i].money)
            pref.putInt("$PrefColor$i", players[i].color.ordinal)
        }
        pref.apply()
    }

    fun getPlayer(index:Int):Player{
        if (index >= players.size){
            return Player(-1,"None", Color.DISABLED, ArrayDeque(0))
        }
        return players[index]
    }

    fun playerCt():Int{
        return players.size
    }

}