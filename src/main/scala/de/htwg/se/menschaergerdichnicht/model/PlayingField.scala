package de.htwg.se.menschaergerdichnicht.model

import scala.collection.mutable.ArrayBuffer

class PlayingField() {

  val playingField = new ArrayBuffer[Field]

  for (i <- 1 to 40) {
    playingField += new Field()
  }

  def getField(id: Int): Field = playingField(id)

  def moveToken(token: Token, num: Int): Unit = {
    token.position._1.tokenId = -1

    //token.setPosition(token.position.)
  }

  def moveToStart(token: Token): Unit = {
    token.position._1.tokenId = -1
    token.getPlayer().playerId match {
      case 0 => token.setPosition(playingField(0),0)
      case 1 => token.setPosition(playingField(10), 0)
      case 2 => token.setPosition(playingField(20), 0)
      case 3 => token.setPosition(playingField(30), 0)
      case _ => token.position._1.tokenId = token.tokenId
    }
  }
}

