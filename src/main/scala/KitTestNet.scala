package com.wlangiewicz

import java.io.File
import org.bitcoinj.core._
import org.bitcoinj.kits.WalletAppKit
import org.bitcoinj.params.{MainNetParams, TestNet3Params}
import org.bitcoinj.script.Script

/**
 * The following example shows how to use the by bitcoinj provided WalletAppKit.
 * The WalletAppKit class wraps the boilerplate (Peers, BlockChain, BlockStorage, Wallet) needed to set up a new SPV bitcoinj app.
 *
 * In this example we also define a WalletEventListener class with implementors that are called when the wallet changes (for example sending/receiving money)
 *
 * Usage example:
 *    sbt "run-main com.wlangiewicz.KitTestNet"
 */
object KitTestNet extends App {
  override def main(args: Array[String]): Unit = {
    // First we configure the network we want to use.
    // The available options are:
    // - MainNetParams
    // - TestNet3Params
    // - RegTestParams
    // While developing your application you probably want to use the Regtest mode and run your local bitcoin network. Run bitcoind with the -regtest flag
    // To test you app with a real network you can use the testnet. The testnet is an alternative bitcoin network that follows the same rules as main network. Coins are worth nothing and you can get coins for example from http://faucet.xeno-genesis.com/
    //
    // For more information have a look at: https://bitcoinj.github.io/testing and https://bitcoin.org/en/developer-examples#testing-applications
    val params: NetworkParameters = TestNet3Params.get
    // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
    // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
    val kit: WalletAppKit = new WalletAppKit(params, new File("."), "walletappkit-testnet-example")
    // In case you want to connect with your local bitcoind tell the kit to connect to localhost.
    // You must do that in reg test mode.
    //kit.connectToLocalHost();

    //In main network mode you don't have to specify any hosts to connect to


    // Now we start the kit and sync the blockchain.
    // bitcoinj is working a lot with the Google Guava libraries. The WalletAppKit extends the AbstractIdleService. Have a look at the introduction to Guava services: https://code.google.com/p/guava-libraries/wiki/ServiceExplained
    kit.startAsync
    kit.awaitRunning
    // To observe wallet events (like coins received) we implement a EventListener class that extends the AbstractWalletEventListener bitcoinj then calls the different functions from the EventListener class
    val wListener: KitTestNet.WalletListener = new KitTestNet.WalletListener
    kit.wallet.addEventListener(wListener)
    // Ready to run. The kit syncs the blockchain and our wallet event listener gets notified when something happens.
    // To test everything we create and print a fresh receiving address. Send some coins to that address and see if everything works.
    Console.println("send money to: " + kit.wallet.freshReceiveAddress.toString)
    // Make sure to properly shut down all the running services when you manually want to stop the kit. The WalletAppKit registers a runtime ShutdownHook so we actually do not need to worry about that when our application is stopping.
    //System.out.println("shutting down again");
    //kit.stopAsync();
    //kit.awaitTerminated();
  }



  // The Wallet event listener its implementations get called on wallet changes.
  private class WalletListener extends AbstractWalletEventListener {
    override def onCoinsReceived(wallet: Wallet, tx: Transaction, prevBalance: Coin, newBalance: Coin) {
      Console.println("-----> coins resceived: " + tx.getHashAsString)
      Console.println("received: " + tx.getValue(wallet))
    }
    override def onTransactionConfidenceChanged(wallet: Wallet, tx: Transaction) {
      Console.println("-----> confidence changed: " + tx.getHashAsString)
      val confidence: TransactionConfidence = tx.getConfidence
      Console.println("new block depth: " + confidence.getDepthInBlocks)
    }
    override def onCoinsSent(wallet: Wallet, tx: Transaction, prevBalance: Coin, newBalance: Coin) {
      Console.println("coins sent")
    }
    override def onReorganize(wallet: Wallet) {
    }
    override def onWalletChanged(wallet: Wallet) {
    }
    def onKeysAdded(keys: List[ECKey]) {
      Console.println("new key added")
    }
    def onScriptsAdded(wallet: Wallet, scripts: List[Script]) {
      Console.println("new script added")
    }
  }

}
