package com.wlangiewicz

import java.net.InetAddress
import java.util.concurrent.Future
import org.bitcoinj.core._
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.{MainNetParams, TestNet3Params}
import org.bitcoinj.store.{MemoryBlockStore, BlockStore}
import org.bitcoinj.utils.BriefLogFormatter

/**
 * Downloads the block given a block hash from the network and prints it out.
 *
 * Usage:
 *    sbt "run-main com.wlangiewicz.FetchBlock <blockHash>"
 *
 * Example:
 *    sbt "run-main com.wlangiewicz.FetchBlock 000000000000000016603a15ec1538514af4ba5db4001a6449edcab57d9cc64e"
 *
 */
object FetchBlock extends App {
  override def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      Console.println("Usage: sbt \"run-main com.wlangiewicz.FetchBlock <blockHash>\"")
    }
    else {
      performFetch(args(0))
    }
  }

  def performFetch(blockHashString: String): Unit ={
    BriefLogFormatter.init()
    Console.println("Connecting to node")
    val params: NetworkParameters = MainNetParams.get

    val blockStore: BlockStore  = new MemoryBlockStore(params)
    val chain: BlockChain  = new BlockChain(params, blockStore)
    val peerGroup: PeerGroup  = new PeerGroup(params, chain)
    peerGroup.startAsync()
    peerGroup.awaitRunning()
    peerGroup.addPeerDiscovery(new DnsDiscovery(params))

    Console.println("Waiting for peers...")

    peerGroup.waitForPeers(1).get()
    val peer: Peer  = peerGroup.getConnectedPeers().get(0)

    val blockHash: Sha256Hash  = new Sha256Hash(blockHashString)
    val future: Future[Block]  = peer.getBlock(blockHash)

    Console.println("Waiting for node to send us the requested block: " + blockHash)

    val block: Block = future.get()
    Console.println(block)
    peerGroup.stopAsync()
  }
}
