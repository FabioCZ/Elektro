package com.gottlicher.elektro

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.Spinner
import kotlinx.coroutines.CompletableDeferred
import java.util.*

data class PlayerResult (val done:Boolean, val player:Player)

class PlayerAsyncDialog {
    lateinit var editText: EditText
    lateinit var colorPicker: Spinner

    suspend fun show(context: Context): PlayerResult {

        val completion = CompletableDeferred<PlayerResult> ()
        val builder = AlertDialog.Builder (context)
        builder.setView(R.layout.dialog_player_picker)
            .setPositiveButton(R.string.add) { _, _ -> completion.complete(PlayerResult(false, getPlayer()))}
            .setNegativeButton(R.string.done) { _, _ -> completion.complete(PlayerResult(true, getPlayer()))}

        val dialog = builder.create()
        dialog.show()
        setUpViews(dialog)
        return completion.await()
    }

    private fun getPlayer():Player{
       return Player(50,editText.text.toString(),getColor(colorPicker.selectedItem.toString()), ArrayDeque(0))
    }

    private fun getColor(name:String):Color{
        when(name){
            "Red" -> return Color.RED
            "Blue" -> return Color.BLUE
            "Yellow" -> return Color.YELLOW
            "Green" -> return Color.GREEN
            "Black" -> return Color.BLACK
            "Purple" -> return Color.PURPLE
        }
        return Color.RED
    }

    fun setUpViews(dialog:Dialog){
        editText = dialog.findViewById(R.id.editName)
        colorPicker = dialog.findViewById(R.id.colorSpinner)
    }
}