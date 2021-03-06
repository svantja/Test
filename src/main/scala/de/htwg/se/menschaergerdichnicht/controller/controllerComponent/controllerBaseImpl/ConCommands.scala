package de.htwg.se.menschaergerdichnicht.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.menschaergerdichnicht.model.fieldComponent.fieldBaseImpl.Dice
import de.htwg.se.menschaergerdichnicht.util.Command
import de.htwg.se.menschaergerdichnicht.controller.controllerComponent.GameState._
import de.htwg.se.menschaergerdichnicht.model.playerComponent.playerBaseImpl.Player

import scala.util.{Success, Try}

/**
  * Created by Anastasia on 06.06.17.
  */
case class AddPlayer(name: String, c: Controller) extends Command {
  val player = Player(name, 0)

  override def action(): Try[_] = {
    if (c.gameState == NONE || c.gameState == PREPARE) {
      if(c.players.players.length < 4) {
        c.players = c.players.addPlayer(player)
        println("Spieler " + name + " wurde hinzugefuegt")
        c.gameState = PREPARE
        c.tui.update
      }else {
        println("Es existieren bereits 4 Spieler")
      }
    } else{println("Spiel wurde bereits gestarted")}
    Success()
  }

  override def undo(): Try[_] = {
    c.players = c.players.removePlayer()
    c.message = "Geloeschter Spieler: " + name
    c.gameState = PREPARE
    c.tui.update
    Success()
  }
}

case class ChooseToken(tokenId: Int, c: Controller) extends Command {
  val player = c.players.getCurrentPlayer
  val token = player.getTokenById(tokenId)
  val dice = Dice()


  override def action(): Try[_] = {
    println(c.players.getCurrentPlayer)

    if (player.getDiced() == 6) {
      if (token.counter == 0) {
        c.playingField.moveToStart(token)
        println("Moved Token" + tokenId + " to start")
        player.setDiced(0)
      } else {
        c.playingField.moveToken(token, player.getDiced(), c.players)
        println("Moved Token" + tokenId + " " + player.getDiced() + " fields")
        player.setDiced(0)
      }
    } else {
      c.playingField.moveToken(token, player.getDiced(), c.players)
      println("Moved Token" + tokenId + " " + player.getDiced() + " fields")
      player.setDiced(0)
      c.players = c.players.nextPlayer()
    }
    c.gameState = ONGOING
    c.tui.update
    Success()
  }

  override def undo(): Try[_] = ???

}

case class Play(c: Controller) extends Command {
  val dice = Dice()
  //c.gui.players = c.players
  //c.gui.repaint()
  //println("alle payers..." + c.gui.players)

  override def action(): Try[_] = {
    //while (true)
    if(c.gameState != DICED){
      c.gameState == ONGOING
      val player = c.players.getCurrentPlayer
      if (!player.getFinished()) {
        val num = dice.rollDice(c.players.getCurrentPlayer)
        if (num == 0) {
          player.setDiced(num)
          println("Cannot move, next player.")
          c.players = c.players.nextPlayer()
          println(c.players.getCurrentPlayer)
          c.gameState == ONGOING

        } else {
          if (player.house.isFull(player)) {
            player.setDiced(num)
            println("Choose token to move")
            println(num + "diced" + player.getDiced())
            println("avaiable tokens: " + player.getAvailableTokens())
            c.gameState = DICED
          } else {
            player.setDiced(num)

            if (num == 6) {
              println("Choose token to move")
              println(num + "diced" + player.getDiced())
              println("avaiable tokens: " + player.getAvailableTokens())
              c.gameState = DICED
            } else {
              if (player.getAvailableTokens().length == 0) {
                println("Cannot move, must dice a 6")
                c.gameState = ONGOING
                c.players = c.players.nextPlayer()
              }else {
                println("Choose token to move")
                println(num + "diced" + player.getDiced())
                println("avaiable tokens: " + player.getAvailableTokens())
                c.gameState = DICED
              }
            }
          }
        }
      }
    }
    else {
      println("Player must move before dicing again!")
    }


    //c.gameState = ONGOING
    c.tui.update
    Success()
  }

  override def undo(): Try[_] = {
    println("Undo")
    c.gameState = ONGOING
    Success()
  }
}
