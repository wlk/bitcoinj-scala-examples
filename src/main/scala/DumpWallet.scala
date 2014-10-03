package com.wlangiewicz

import java.io.File

import org.bitcoinj.core.Wallet

object DumpWallet extends App {


  def performDump(path: String): Unit = {
    val wallet: Wallet = Wallet.loadFromFile(new File(path))
    Console.println(wallet.toString(true, true, true, null))
  }

  override def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      Console.println("Usage: scala DumpWallet <path>")
    }
    else {
      performDump(args(0))
    }
  }
}
