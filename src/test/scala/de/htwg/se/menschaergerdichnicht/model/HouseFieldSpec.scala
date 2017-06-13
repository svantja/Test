package de.htwg.se.menschaergerdichnicht.model

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by svloeger on 13.06.2017.
  */
class HouseFieldSpec extends FlatSpec with Matchers{
  val player = Player("test", 0)
  "A House" should "have a Player" in{
    House(player)
  }

  "A House.house" should "be ArrayBuffer" in{
    House(player).house
  }

  "A House.house" should "have a length of 4" in{
    House(player).house.length
  }

  "A House" should "return true" in{
    player.house.isFull(player)
  }
  it should "return false" in{
    player.house.house(0).removeToken()
    player.house.isFull(player)
  }

}
