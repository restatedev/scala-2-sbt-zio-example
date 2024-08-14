package dev.restate.sdk.scala

import dev.restate.sdk.common.syscalls.{HandlerSpecification, SyscallCallback, Syscalls}
import zio._

import java.nio.ByteBuffer

class HandlerRunner[REQ, RES](private val runner: (ObjectContext, REQ) => ZIO[Any, Throwable, RES]) extends dev.restate.sdk.common.syscalls.HandlerRunner[REQ, RES, Unit] {

  override def run(handlerSpecification: HandlerSpecification[REQ, RES], syscalls: Syscalls, options: Unit, callback: SyscallCallback[ByteBuffer]): Unit = {

    // Any context switching, if necessary, will be done by ResolvedEndpointHandler
    val ctx = new ObjectContext(syscalls)
    // Parse input
    val req: REQ = handlerSpecification.getRequestSerde.deserialize(syscalls.request.bodyBuffer)
    // TODO handle deserialization exception

    val runtime = Runtime.default
    Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.fork(
        this.runner(ctx, req).mapBoth(e => {
          callback.onCancel(e)
        }, t => {
          // TODO handle serialization exception
          callback.onSuccess(handlerSpecification.getResponseSerde.serializeToByteBuffer(t))
        })
      )
    }
  }
}

