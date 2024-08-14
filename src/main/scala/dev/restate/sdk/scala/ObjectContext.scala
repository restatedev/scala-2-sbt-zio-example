package dev.restate.sdk.scala

import dev.restate.sdk.common.StateKey
import dev.restate.sdk.common.syscalls.{SyscallCallback, Syscalls}
import zio._

import java.util.concurrent.CompletableFuture

class ObjectContext(syscalls: Syscalls) extends SharedObjectContext(syscalls) {

  def clear(key: StateKey[_]): ZIO[Any, Throwable, Unit] = ZIO.fromCompletableFuture {
    val deferredFut = new CompletableFuture[Void]
    syscalls.clear(key.name(), SyscallCallback.completingFuture(deferredFut))
    deferredFut
  }.map(_ => {
    ()
  })

  def clearAll(): ZIO[Any, Throwable, Unit] = ZIO.fromCompletableFuture {
    val deferredFut = new CompletableFuture[Void]
    syscalls.clearAll(SyscallCallback.completingFuture(deferredFut))
    deferredFut
  }.map(_ => {
    ()
  })

  def set[T](key: StateKey[T], value: T): ZIO[Any, Throwable, Unit] = ZIO.fromCompletableFuture {
    val deferredFut = new CompletableFuture[Void]
    // TODO handle serialization error
    syscalls.set(key.name(), key.serde().serializeToByteBuffer(value), SyscallCallback.completingFuture(deferredFut))
    deferredFut
  }.map(_ => {
    ()
  })

}
