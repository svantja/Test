package de.htwg.se.menschaergerdichnicht.model.fieldComponent.fieldBaseImpl

import de.htwg.se.menschaergerdichnicht.model.playerComponent.Player

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Anastasia on 01.05.17.
  */
case class TargetField(player: Player) {

  val targetField = new ArrayBuffer[Field]

  for (i <- 1 to 4) {
    targetField += new Field()
  }

  def isFull(player: Player): Boolean = {
    for (field <- player.target.targetField) {
      if (field.tokenId == -1) {
        return false
      }
    }
    true
  }
}