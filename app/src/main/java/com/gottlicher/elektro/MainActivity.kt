package com.gottlicher.elektro

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class MainActivity : AppCompatActivity() {

    private val bottomIcons = ArrayList<PlayerNavView>(0)
    private lateinit var bank:Bank
    var currPl:Int = 0

    var numberQ:Deque<Int> = ArrayDeque<Int>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startSession()

        bottomIcons.add(pl1_nav)
        bottomIcons.add(pl2_nav)
        bottomIcons.add(pl3_nav)
        bottomIcons.add(pl4_nav)
        bottomIcons.add(pl5_nav)
        bottomIcons.add(pl6_nav)

        for(i in 0 until MAX_PLAYERS){
            bottomIcons[i].onClick { onPlayerClick(i) }
        }

        btnZero.onClick { onNumberClick(0) }
        btnOne.onClick { onNumberClick(1) }
        btnTwo.onClick { onNumberClick(2) }
        btnThree.onClick { onNumberClick(3) }
        btnFour.onClick { onNumberClick(4) }
        btnFive.onClick { onNumberClick(5) }
        btnSix.onClick { onNumberClick(6) }
        btnSeven.onClick { onNumberClick(7) }
        btnEight.onClick { onNumberClick(8) }
        btnNine.onClick { onNumberClick(9) }
        btnPlusTen.onClick { onNumberClick(10) }
        btnPlusFifteen.onClick { onNumberClick(15) }

        btnPlus.onClick { onPlusClick() }
        btnUndo.onClick { onUndoClick() }
        btnAdd.onClick { onSubAddClick(true) }
        btnSub.onClick { onSubAddClick(false) }
    }

    private fun startSession() = GlobalScope.launch(Dispatchers.Main) {
        val hasSession = hasPreviousSession()
        val res = AsyncAlertDialog(R.string.restore_previous, null, if (hasSession) R.string.restore_previous else R.string.no_session, R.string.start_new, null).show(this@MainActivity)
        if (res == DialogResult.POSITIVE && hasSession){ //restore
            bank = async { Bank(ArrayList(0), this@MainActivity) }.await()
        } else {
            val list = ArrayList<Player>(0);
            do {
                val plRes = PlayerAsyncDialog().show(this@MainActivity)
                list.add(plRes.player)
            } while (!plRes.done && list.size < 6)
            bank = Bank(list, this@MainActivity)
        }
        for(i in 0 until MAX_PLAYERS) {
            if (i < bank.playerCt()){
                bottomIcons[i].money = bank.getPlayer(i).money
                bottomIcons[i].color = bank.getPlayer(i).color
            } else {
                bottomIcons[i].money = -1
                bottomIcons[i].color = Color.DISABLED
            }
        }
        onPlayerClick(0)

    }

    override fun onPause() {
        super.onPause()
        bank.writeToPref(this)
    }

    private fun onPlayerClick(plIndex:Int){
        currPl = plIndex
        setSelectedPlayer()
        drawTotalMoney()
        numberQ.clear()
        numberQ.addFirst(0)
        drawQ()
    }

    private fun onNumberClick(number:Int){
        val n = if(numberQ.any()) numberQ.removeFirst() else 0
        numberQ.addFirst(n * 10 + number)
        drawQ()
    }

    private fun onUndoClick(){
        if (numberQ.any()) {
            numberQ.removeFirst()
        }
        drawQ()
    }

    private fun onPlusClick(){
        numberQ.addFirst(0)
        drawQ()
    }

    private fun onSubAddClick(doAdd:Boolean) {
        val tran =  + (numberQ.sum() * if (doAdd) 1 else -1)

        while(bank.getPlayer(currPl).pastTransactions.size > 5){
            bank.getPlayer(currPl).pastTransactions.removeLast()
        }

        bank.getPlayer(currPl).pastTransactions.addFirst(tran)
        bank.getPlayer(currPl).money = bank.getPlayer(currPl).money + tran
        numberQ.clear()
        drawQ()
        drawTotalMoney()
    }

    private fun drawQ(){
        var text = ""
        for(num in numberQ){
            text += "${num}\n"
        }
        txtCurrOp.text = text
    }

    private fun hasPreviousSession():Boolean{
        return getSharedPreferences(PrefKey, Context.MODE_PRIVATE).contains("${PrefName}0")
    }

    private fun drawTotalMoney(){

        var trText = ""
        for(tr in bank.getPlayer(currPl).pastTransactions){
            trText += tr.toString() + "\n"
        }
        txtPast.text = trText
        txtTotal.text = "${bank.getPlayer(currPl).money}€"
        bottomIcons[currPl].money = bank.getPlayer(currPl).money
    }

    private fun setSelectedPlayer() {

        for (i in 0 until bank.playerCt()){
            bottomIcons[i].plSelected = currPl == i
            bottomIcons[i].color = bank.getPlayer(i).color
        }
        supportActionBar?.title = bank.getPlayer(currPl).name
    }
}
