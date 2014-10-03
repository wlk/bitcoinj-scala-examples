package com.wlangiewicz

import java.io.File
import org.bitcoinj.core._
import org.bitcoinj.kits.WalletAppKit
import org.bitcoinj.params.RegTestParams
import org.bitcoinj.utils.{Threading, BriefLogFormatter}

import org.bitcoinj.core.Coin._;

/**
 * This is a little test app that waits for a coin on a local regtest node, then  generates two transactions that double
 * spend the same output and sends them. It's useful for testing double spend codepaths but is otherwise not something
 * you would normally want to do.
 *
 * Usage:
 *    sbt "run-main com.wlangiewicz.DoubleSpend"
 */
object DoubleSpend extends App {
  override def main(args: Array[String]): Unit = {
    BriefLogFormatter.init();
    val params: RegTestParams = RegTestParams.get
    val kit: WalletAppKit = new WalletAppKit(params, new File("."), "doublespend")
    kit.connectToLocalHost()
    kit.setAutoSave(false)
    kit.startAsync()
    kit.awaitRunning()

    Console.println(kit.wallet())

    kit.wallet().getBalanceFuture(COIN, Wallet.BalanceType.AVAILABLE).get

    val tx1 = kit.wallet().createSend(new Address(params, "muYPFNCv7KQEG2ZLM7Z3y96kJnNyXJ53wm"), CENT)
    val tx2 = kit.wallet().createSend(new Address(params, "muYPFNCv7KQEG2ZLM7Z3y96kJnNyXJ53wm"), CENT.add(SATOSHI.multiply(10)))

    val peer: Peer = kit.peerGroup().getConnectedPeers().get(0)

    peer.addEventListener(new AbstractPeerEventListener() {
      override def onPreMessageReceived(peer: Peer, m: Message): Message ={
        Console.err.println("Got a message!" + m)
        m
      }
    }, Threading.SAME_THREAD )

    peer.sendMessage(tx1);
    peer.sendMessage(tx2);

    Thread.sleep(5000);
    kit.stopAsync();
    kit.awaitTerminated();
  }
}
