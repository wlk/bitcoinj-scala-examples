package com.wlangiewicz

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.google.common.collect.Lists
import com.google.common.util.concurrent.{Futures, ListenableFuture}
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.net.NioClientManager
import org.bitcoinj.net.discovery.{DnsDiscovery, PeerDiscoveryException}
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.utils.BriefLogFormatter

/**
 * Prints a list of IP addresses obtained from DNS.
 *
 * Usage:
 * sbt "run-main com.wlangiewicz.PrintPeers"
 *
 */
object PrintPeers extends App {
  var dnsPeers = Array[InetSocketAddress]()

  private def printElapsed(start: Long) {
    val now = System.currentTimeMillis()
    val diff = (now - start) / 1000.0
    val toPrint = f"Took $diff%.2f seconds"
    Console.println(toPrint)
  }

  private def printPeers(addresses: Array[InetSocketAddress]) {
    val output = addresses.map((a: InetSocketAddress) => f"${a.getAddress.getHostAddress}%s:${a.getPort}%d") mkString ("\n")
    Console.println(output)
  }

  @throws[PeerDiscoveryException]("problem contacting DNS")
  private def printDNS() {
    val start = System.currentTimeMillis()
    val dns: DnsDiscovery = new DnsDiscovery(MainNetParams.get())
    dnsPeers = dns.getPeers(10, TimeUnit.SECONDS)
    printPeers(dnsPeers)
    printElapsed(start)
  }

  override def main(args: Array[String]): Unit = {
    BriefLogFormatter.init
    Console.println("=== DNS ===")
    printDNS
    Console.println("=== Version/chain heights ===")

    val addrs = dnsPeers.map(_.getAddress)

    System.out.println("Scanning " + addrs.length + " peers:")

    val params: NetworkParameters = MainNetParams.get()
    val lock: Object = new Object()

    val bestHeight = Array(1L)



    //TODO translate to scala:
    /*
    val futures = new Array[ListenableFuture](3)
      val futures : List[ListenableFuture[Void]] = Lists.newArrayList


  val clientManager : NioClientManager = new NioClientManager


import scala.collection.JavaConversions._
for (addr <- addrs) {
  val address : InetSocketAddress = new InetSocketAddress(addr, params.getPort)
  val peer : Peer = new Peer(params, new VersionMessage(params, 0), null, new PeerAddress(address))
  val future : SettableFuture[Void] = SettableFuture.create
peer.addEventListener(new AbstractPeerEventListener {
override   def onPeerConnected(p : Peer, peerCount : Int){
  val ver : VersionMessage = peer.getPeerVersionMessage
  val nodeHeight : Long = ver.bestHeight
lock synchronized {
  val diff : Long = bestHeight(0) - nodeHeight
if (diff > 0) {
System.out.println("Node is behind by " + diff + " blocks: " + addr)
}
else if (diff == 0) {
System.out.println("Node " + addr + " has " + nodeHeight + " blocks")
bestHeight(0) = nodeHeight
}
else if (diff < 0) {
System.out.println("Node is ahead by " + Math.abs(diff) + " blocks: " + addr)
bestHeight(0) = nodeHeight
}
}
future.set(null)
peer.close
}
override   def onPeerDisconnected(p : Peer, peerCount : Int){
if (!future.isDone) System.out.println("Failed to talk to " + addr)
future.set(null)
}
})
clientManager.openConnection(address, peer)
futures.add(future)
}


// Wait for every tried connection to finish.


Futures.successfulAsList(futures).get
     */

  }
}
