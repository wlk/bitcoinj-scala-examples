This is a collection of different bitcoinj examples rewritten to scala.
Most of them are not very interesting or insightful but I did this just as an exercise.
Some examples work on test network and require running a full node on localhost, others use main Bitcoin network.

##Examples so far:
1. DumpWallet
2. BackupToMnemonicSeed
3. DoubleSpend
4. FetchBlockTestNet
5. FetchBlock
6. PrintPeers (not complete)
7. ForwardingService
8. RestoreFromSeedManual
9. RestoreFromSeedWalletAppKit
10. KitTestNet
11. KitMainNet
12. PrivateKeys
13. SendRequest

##Logging:
To disable logging simply comment out this line in `build.sbt`:

```
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"
```

At the moment I'm not sure how to do this more easily configurable.

##Usage:

1. At the moment scala examples work with bitcoinj version 0.12 - you might need to change that in the future
2. This is how you run examples:  
```
sbt "run-main package.name.class.name parameters"
```  
for example:  
```
sbt "run-main com.wlangiewicz.DumpWallet /home/w/bitcoinj/examples/forwarding-service.wallet"
```  

3. This is example output from DumpWallet example:  

```
w@virtualbox ~/dump-wallet (master) $ sbt "run-main com.wlangiewicz.DumpWallet /home/w/bitcoinj/examples/forwarding-service.wallet"
[info] Loading project definition from /home/w/dump-wallet/project
[info] Set current project to bitcoinj-examples-scala (in build file:/home/w/dump-wallet/)
[info] Running com.wlangiewicz.DumpWallet /home/w/bitcoinj/examples/forwarding-service.wallet
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Wallet containing 0 BTC (available: 0 BTC) in:
  0 pending transactions
  0 unspent transactions
  0 spent transactions
  0 dead transactions
Last seen best block: 321404 (Thu Sep 18 23:08:47 CEST 2014): 000000000000000003a63a6d9ab2086919b34aaaa4f9d59f5213228c1d68f295

Keys:
Seed as words: stay power divert sock shine base vault canvas kite bid sick prize
Seed as hex:   55c909c735442ff43881cc4833299b5d12a03098f9c4255e035a20dea0335da29875fcb19cc88849b429a7733c52425741ec1400923896294a1b724c82eee0e8
Seed birthday: 1411049292  [Thu Sep 18 16:08:12 CEST 2014]
Key to watch:  xpub68VEFNrNHtueGhybm9Rv7hET4KetvrnPmVnvHoGt3Vr5qeagjbAMRXzc2RtBAR91bJ7ZvYuGwMJuiMKEJtDsohyiaNHHt9n5XpN5ge2nSYk
  addr:1AZtkCyPcjmVX91bg2smXREd6Co9LvNtQL  hash160:68f1a328166dd76e1b214f8daf81c53088a3b285  (M/0H/0/0)
  DeterministicKey{pub=0367f08bf4fc532290cab2c15c5961ce56d94015682c5fd4347b61dfb33a00b5d0, priv=31b16fc53dcbbe82ec03c30c06ccdbe84e52a72e94fe89b57109d46160e9c636, isEncrypted=false}
  addr:1AvXCvVjnd2ynPQvRJ3SpsdaEmFXyY6bJp  hash160:6cd864719a9910e188d0320eb6e56e7487becea0  (M/0H/0/1)
  DeterministicKey{pub=0205ba05fd5b250c9c96a34e8ca136726b516fab967d94dbc74228ebd30deb4771, priv=00d5e2d63c7ab8d05339f8661ad40fe422a369f9c9a2e6467fd53ed1f936bd7be0, isEncrypted=false}
  addr:1G5mV5hYodvRBLnVf6WGVELJdL7GkqRUGC  hash160:a570af219fa0581089dfcfcb9b2b879e751051d2  (M/0H/0/2)
  DeterministicKey{pub=03d8ec1e09698c9311920675a15914641f1fe3fbc437228c1277bceadb95cb0910, priv=00b1da0d142b522adcca7ec064dfdad482e77e4da1171d89726537a8ca094f7e8b, isEncrypted=false}
  addr:1NGK3nhS2yWGQae4J2HHkyzK5H9n8SrZ68  hash160:e93ff257e5f737fcc2e8f08315d19115447f2008  (M/0H/1/0)
  DeterministicKey{pub=02596f67e6f7e6d86f159c7e69d836a78463dbaa77838ad72db047713dd3ae0540, priv=3616c205121d38bd0b2a1d531af66eb5dda7ba3ce6b62d484a2f115a22746adb, isEncrypted=false}

[success] Total time: 2 s, completed Oct 3, 2014 4:58:15 PM
```

Sample output from KitTestNet example (1BTC was send using testnet faucet):  

```
sbt "run-main com.wlangiewicz.KitTestNet"
[info] Loading project definition from /home/w/dump-wallet/project
[info] Set current project to bitcoinj-examples-scala (in build file:/home/w/dump-wallet/)
[info] Compiling 1 Scala source to /home/w/dump-wallet/target/scala-2.11/classes...
[warn] there was one deprecation warning; re-run with -deprecation for details
[warn] one warning found
[info] Running com.wlangiewicz.KitTestNet 
send money to: miHmDQv93X2Wz4XBmex4YyC8gzigKFVDVo
-----> coins resceived: 0bcc1275e9e4f921fc675749c543b13edadf067a7beba44d5ea11c956c408058
received: 100000000
-----> confidence changed: 0bcc1275e9e4f921fc675749c543b13edadf067a7beba44d5ea11c956c408058
new block depth: 0
-----> confidence changed: 0bcc1275e9e4f921fc675749c543b13edadf067a7beba44d5ea11c956c408058
new block depth: 0
```

This is output from running SendRequest (when everything went OK):  

```
sbt "run-main com.wlangiewicz.SendRequest"
[info] Loading project definition from /home/w/dump-wallet/project
[info] Set current project to bitcoinj-examples-scala (in build file:/home/w/dump-wallet/)
[info] Compiling 1 Scala source to /home/w/dump-wallet/target/scala-2.11/classes...
[warn] there was one deprecation warning; re-run with -deprecation for details
[warn] one warning found
[info] Running com.wlangiewicz.SendRequest 
Send money to: muuwSEgE4nh375c9x29bXfThUCtvAHLAsw
coins sent. transaction hash: ad5e8ae0721565efd282d6630f5ffe9ff37bee068474368411e9e3032b865c11
```

This is output from running SendRequest (when everything we don't have enough founds):  

```
sbt "run-main com.wlangiewicz.SendRequest"
[info] Loading project definition from /home/w/dump-wallet/project
[info] Set current project to bitcoinj-examples-scala (in build file:/home/w/dump-wallet/)
[info] Compiling 1 Scala source to /home/w/dump-wallet/target/scala-2.11/classes...
[warn] there was one deprecation warning; re-run with -deprecation for details
[warn] one warning found
[info] Running com.wlangiewicz.SendRequest 
Send money to: msEDq1aybTVp3ZZby7kC9RbGBvXji6R7Qu
Not enough coins in your wallet. Missing 9010000 satoshis are missing (including fees)
Send money to: msEDq1aybTVp3ZZby7kC9RbGBvXji6R7Qu
```