package com.wlangiewicz

import java.io.File

import org.bitcoinj.core.{Wallet, NetworkParameters}
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.wallet.DeterministicSeed

object BackupToMnemonicSeed extends App {
  override def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      performRandomBackup
    }
    else {
      performBackupFromFile(args(0))
    }
  }

  def performRandomBackup {
    val params: NetworkParameters = TestNet3Params.get
    val wallet: Wallet = new Wallet(params)
    performBackup(wallet)
  }

  def performBackupFromFile(path: String) {
    val wallet: Wallet = Wallet.loadFromFile(new File(path))
    performBackup(wallet)
  }

  def performBackup(wallet: Wallet): Unit = {
    val seed: DeterministicSeed = wallet.getKeyChainSeed

    Console.println("seed: " + seed.toString())
    Console.println("creation time: " + seed.getCreationTimeSeconds)

    val words = seed.getMnemonicCode
    Console.println("mnemonicCode: " + words )
  }
}
