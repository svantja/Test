package de.htwg.se.menschaergerdichnicht.model.fieldComponent.fieldBaseImpl

import javax.inject.Inject

import de.htwg.se.menschaergerdichnicht.model.fieldComponent.{FieldInterface, PlayingInterface}
import de.htwg.se.menschaergerdichnicht.model.playerComponent.{PlayerInterface, PlayersInterface, TokenInterface}

import scala.collection.mutable.ArrayBuffer

case class PlayingField @Inject() () extends PlayingInterface {

  val playingField = new ArrayBuffer[FieldInterface]

  for (i <- 1 to 40) {
    playingField += Field()
  }

  def getField(id: Int): FieldInterface = playingField(id)

  def moveToken(token: TokenInterface, num: Int, players: PlayersInterface): Unit = {
    if (token.counter + num >= 41) {
      val move = (token.counter + num) - 40
      println("move to target??" + move)
      moveToTarget(token, move)
    } else {
      val oldPosition = token.getPosition()._2
      var newPosition = oldPosition + num
      if (newPosition > 39) {
        newPosition = newPosition - 39
      }
      token.position._1.tokenId = -1

      if (playingField(newPosition).tokenId == -1) {
        token.setPosition((playingField(newPosition), newPosition))
        playingField(newPosition).setToken(token)
      } else {
        val toBeKicked = playingField(newPosition).tokenId
        if (kickToken(toBeKicked, token.getPlayer(), players)) {
          token.setPosition((playingField(newPosition), newPosition))
          playingField(newPosition).setToken(token)
        }
      }
    }
    token.setCounter(token.getCounter() + num)
  }

  def kickToken(tokenId: Int, player: PlayerInterface, players: PlayersInterface): Boolean = {
    for (p <- players.getAllPlayer) {
      for (token <- p.getTokens()) {
        if (token.tokenId == tokenId) {
          if (token.getPlayer() != player) {
            val player = token.getPlayer()
            val free = player.house.house(0)
            free.setToken(token)
            token.setPosition((free, 0))
            token.setCounter(0)
            return true
          }
        }
      }
    }
    return false
  }

  def moveToTarget(token: TokenInterface, i: Int): Unit = {
    val player = token.getPlayer()
    if (!token.getFinished()) {
      if (i <= 3) {
        if (token.counter + i <= 44) {
          val target = player.target.targetField(i)
          if (target.tokenId == -1) {
            target.setToken(token)
            token.position._1.tokenId = -1
            token.setPosition(target, i)
            print(token.getPosition() + "position im finish")
            token.setFinished(true)
            if (player.target.isFull(player)) {
              player.setFinished(true)
            }
          }
        }
      }
    }
  }

  def moveToStart(token: TokenInterface): Unit = {
    token.position._1.tokenId = -1
    token.getPlayer().playerId match {
      case 1 => token.setPosition(playingField(0),0); token.setCounter(1);
      case 2 => token.setPosition(playingField(10), 10); token.setCounter(1);
      case 3 => token.setPosition(playingField(20), 20); token.setCounter(1);
      case 4 => token.setPosition(playingField(30), 30); token.setCounter(1);
      case _ => token.position._1.tokenId = token.tokenId
    }
  }
}

