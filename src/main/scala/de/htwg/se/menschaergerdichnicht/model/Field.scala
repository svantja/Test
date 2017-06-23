package de.htwg.se.menschaergerdichnicht.model

/**
  * Created by Anastasia on 01.05.17.
  */


case class Field() {

  var tokenId: Int = -1

  def setToken(token: Token) { this.tokenId = token.tokenId}

  def getToken(): Int = tokenId

  def removeToken() {this.tokenId = -1}

}
