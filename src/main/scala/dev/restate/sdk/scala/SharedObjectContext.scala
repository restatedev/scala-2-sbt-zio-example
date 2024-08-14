package dev.restate.sdk.scala

import dev.restate.sdk.common.StateKey
import dev.restate.sdk.common.syscalls.{Deferred, SyscallCallback, Syscalls}
import zio._

import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import scala.jdk.CollectionConverters._

class SharedObjectContext(syscalls: Syscalls) extends Context(syscalls) {

  def key: String = syscalls.objectKey()

  def get[T](key: StateKey[T]): ZIO[Any, Throwable, Option[T]] = for {
    deferred <- ZIO.fromCompletableFuture {
      val deferredFut = new CompletableFuture[Deferred[ByteBuffer]]
      syscalls.get(key.name(), SyscallCallback.completingFuture(deferredFut))
      deferredFut
    }
    _ <- ZIO.fromCompletableFuture {
      val fut = new CompletableFuture[Void]
      syscalls.resolveDeferred(deferred, SyscallCallback.completingFuture(fut))
      fut
    }
    result <- ZIO.succeed(deferred.toResult)
    z <-
      if (result.isEmpty)
        ZIO.succeed(None)
      else if (result.isSuccess)
        ZIO.succeed(Option(key.serde().deserialize(deferred.toResult.getValue)))
      else
        ZIO.fail(result.getFailure)

  } yield z

  def stateKeys: ZIO[Any, Throwable, Iterable[String]] = for {
    deferred <- ZIO.fromCompletableFuture {
      val deferredFut = new CompletableFuture[Deferred[java.util.Collection[String]]]
      syscalls.getKeys(SyscallCallback.completingFuture(deferredFut))
      deferredFut
    }
    _ <- ZIO.fromCompletableFuture {
      val fut = new CompletableFuture[Void]
      syscalls.resolveDeferred(deferred, SyscallCallback.completingFuture(fut))
      fut
    }
    result <- ZIO.succeed(deferred.toResult)
    z <-
      if (result.isSuccess)
        ZIO.succeed(result.getValue.asScala)
      else
        ZIO.fail(result.getFailure)
  } yield z

}
