package com.wlangiewicz

import java.io.File
import java.math.BigInteger
import java.net.InetAddress

import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.{MainNetParams, TestNet3Params}
import org.bitcoinj.store.MemoryBlockStore
import org.bitcoinj.wallet.DeterministicSeed

/**
 * This example shows how to solve the challenge Hal posted here:<p>
 *
 * <a href="http://www.bitcoin.org/smf/index.php?topic=3638.0">http://www.bitcoin.org/smf/index.php?topic=3638
 * .0</a><p>
 *
 * in which a private key with some coins associated with it is published. The goal is to import the private key,
 * claim the coins and then send them to a different address.
 *
 *  Usage:
 *    sbt "run-main com.wlangiewicz.PrivateKeys sourceAddress destinationAddress"
 *  Usage example:
 *     sbt "run-main com.wlangiewicz.PrivateKeys KxtJogX5u9ucMiQfLYdbDx4XBCfmER58LaXfLkfvSYHGxRpsA49h 17G3mZYjuNPhW267feDuVg3TmPBe6dEtEp"
 */
object PrivateKeys extends App {
  override def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      Console.println("First arg should be private key in Base58 format. Second argument should be address to send to.")
    }
    else {
      val sourceAddress = args(0)
      val destinationAddress = args(1)
      forwardCoins(sourceAddress, destinationAddress)
    }
  }

  def addressToKey(params: NetworkParameters, sourceAddress: String): ECKey = {
    sourceAddress match {
      case _ if sourceAddress.length == 51 || sourceAddress.length == 52 => new DumpedPrivateKey(params, sourceAddress).getKey
      case _ => ECKey.fromPrivate(Base58.decodeToBigInteger(sourceAddress))
    }
  }

  def forwardCoins(sourceAddress: String, destinationAddress: String): Unit = {
    val params: NetworkParameters = MainNetParams.get
    val key = addressToKey(params, sourceAddress)

    Console.println("Address from private key is: " + key.toAddress(params).toString)

    // And the address ...
    val destination: Address = new Address(params, destinationAddress)

    // Import the private key to a fresh wallet.
    val wallet: Wallet = new Wallet(params)
    wallet.importKey(key)

    // Find the transactions that involve those coins.
    val blockStore: MemoryBlockStore = new MemoryBlockStore(params)
    val chain: BlockChain = new BlockChain(params, wallet, blockStore)
    val peerGroup: PeerGroup = new PeerGroup(params, chain)
    peerGroup.addPeerDiscovery(new DnsDiscovery(params))
    peerGroup.startAsync
    peerGroup.downloadBlockChain()
    peerGroup.stopAsync

    // And take them!
    Console.println("Claiming " + wallet.getBalance.toFriendlyString)
    wallet.sendCoins(peerGroup, destination, wallet.getBalance)

    // Wait a few seconds to let the packets flush out to the network (ugly).
    Thread.sleep(5000)
    System.exit(0)
  }
}
