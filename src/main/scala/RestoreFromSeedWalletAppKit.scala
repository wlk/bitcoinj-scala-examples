package com.wlangiewicz

import java.io.File
import org.bitcoinj.kits.WalletAppKit
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.DeterministicSeed

/**
 * The following example shows you how to restore a HD wallet from a previously generated deterministic seed.
 * It uses WalletAppKit which provides a restoreWalletFromSeed function to load a wallet from a deterministic seed.
 * Usage:
 *    sbt "run-main com.wlangiewicz.RestoreFromSeedWalletAppKit"
 *
 */
object RestoreFromSeedWalletAppKit extends App {
  override def main(args: Array[String]): Unit = {
    val params = MainNetParams.get
    val dir = new File(".")
    val filePrefix = "restored"
    val walletAppkit: WalletAppKit = new WalletAppKit(params, dir, filePrefix)

    val seedCode = args mkString(" ")
    val passphrase = ""
    val creationTime = System.currentTimeMillis()

    val seed = new DeterministicSeed(seedCode, null, passphrase, creationTime)

    walletAppkit.restoreWalletFromSeed(seed)
  }
}




