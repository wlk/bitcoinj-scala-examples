package com.wlangiewicz

import java.io.File

import org.bitcoinj.core.{Wallet, NetworkParameters}
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.wallet.DeterministicSeed

/**
 * The following example shows you how to create a deterministic seed from a hierarchical deterministic wallet represented as a mnemonic code.
 * This seed can be used to fully restore your wallet. The RestoreFromSeed.scala example shows how to load the wallet from this seed.
 *
 * Usage:
 *    sbt "run-main com.wlangiewicz.BackupToMnemonicSeed /home/w/bitcoinj/examples/forwarding-service.wallet"
 *    to see specific wallet
 *    or
 *    sbt "run-main com.wlangiewicz.BackupToMnemonicSeed"
 *    to see randomly generated wallet
 *
 * In Bitcoin Improvement Proposal (BIP) 39 and BIP 32 describe the details about hierarchical deterministic wallets and mnemonic sentences
 * https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
 * https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki
 */
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
