package com.wlangiewicz

import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.store.MemoryBlockStore
import org.bitcoinj.wallet.DeterministicSeed

/**
 * DumpWallet loads a serialized wallet and prints information about what it contains.
 * It contains public and private keys and addresses.
 *
 * Usage:
 *    sbt "run-main com.wlangiewicz.ImportFromSeedCLI seed"
 *
 */
object ImportFromSeedCLI extends App {
  def performDump(seedCode: String): Unit = {
    val params = MainNetParams.get
    val chainStore: MemoryBlockStore = new MemoryBlockStore(params)
    val chain: BlockChain = new BlockChain(params, chainStore)

    val seed: DeterministicSeed = new DeterministicSeed(seedCode, null, "", System.currentTimeMillis())

    val wallet = Wallet.fromSeed(params, seed)

    for( i <- 1 to 100){
      wallet.freshReceiveAddress()
    }


    Console.println(wallet.toString(true, false, false, null))

    val peers: PeerGroup = new PeerGroup(params, chain)
    peers.addPeerDiscovery(new DnsDiscovery(params))
    peers.setUseLocalhostPeerWhenPossible(true)

    chain.addWallet(wallet)
    peers.addWallet(wallet)
    peers.setBloomFilterFalsePositiveRate(0.0)

    peers.startAsync
    peers.awaitRunning()
    peers.downloadBlockChain()

    Console.println(wallet.toString(true, false, false, null))
  }

  override def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      Console.println("Usage: scala ImportFromSeedCLI seed")
    }
    else {
      performDump(args(0))
    }
  }
}
