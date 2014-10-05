package com.wlangiewicz
import java.io.File

import com.google.common.util.concurrent.{FutureCallback, ListenableFuture, Futures}
import org.bitcoinj.core.Wallet.BalanceType
import org.bitcoinj.core._
import org.bitcoinj.kits.WalletAppKit
import org.bitcoinj.params.TestNet3Params


/**
 * The following example shows you how to create a SendRequest to send coins from a wallet to a given address.
 *
 * Usage example:
 *    sbt "run-main com.wlangiewicz.SendRequest"
 */
object SendRequest extends App {
  override def main(args: Array[String]): Unit = {
    // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
    val params: NetworkParameters = TestNet3Params.get
    val kit: WalletAppKit = new WalletAppKit(params, new File("."), "sendrequest-example")
    kit.startAsync
    kit.awaitRunning
    Console.println("Send money to: " + kit.wallet.currentReceiveAddress.toString)

    // How much coins do we want to send?
    // The Coin class represents a monetary Bitcoin value.
    // We use the parseCoin function to simply get a Coin instance from a simple String.
    val value: Coin = Coin.parseCoin("0.09")

    // To which address you want to send the coins?
    // The Address class represents a Bitcoin address.
    val to: Address = new Address(params, "mupBAFeT63hXfeeT4rnAUcpKHDkz1n4fdw")

    // There are different ways to create and publish a SendRequest. This is probably the easiest one.
    // Have a look at the code of the SendRequest class to see what's happening and what other options you have: https://bitcoinj.github.io/javadoc/0.11/com/google/bitcoin/core/Wallet.SendRequest.html
    //
    // Please note that this might raise a InsufficientMoneyException if your wallet has not enough coins to spend.
    // When using the testnet you can use a faucet (like the http://faucet.xeno-genesis.com/) to get testnet coins.
    // In this example we catch the InsufficientMoneyException and register a BalanceFuture callback that runs once the wallet has enough balance.
    try {
      val result: Wallet.SendResult = kit.wallet.sendCoins(kit.peerGroup, to, value)
      Console.println("coins sent. transaction hash: " + result.tx.getHashAsString)
    }
    catch {
      case e: InsufficientMoneyException => {
        Console.println("Not enough coins in your wallet. Missing " + e.missing.getValue + " satoshis are missing (including fees)")
        Console.println("Send money to: " + kit.wallet.currentReceiveAddress.toString)
        val balanceFuture: ListenableFuture[Coin] = kit.wallet.getBalanceFuture(value, BalanceType.AVAILABLE)
        val callback: FutureCallback[Coin] = new FutureCallback[Coin] {
          def onSuccess(balance: Coin) {
            Console.println("coins arrived and the wallet now has enough balance")
          }

          def onFailure(t: Throwable) {
            Console.println("something went wrong")
          }
        }
        Futures.addCallback(balanceFuture, callback)
      }
    }

    // shutting down
    //kit.stopAsync();
    //kit.awaitTerminated();
  }
}
