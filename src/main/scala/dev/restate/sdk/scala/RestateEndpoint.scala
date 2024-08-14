package dev.restate.sdk.scala

import dev.restate.sdk.common.syscalls.ServiceDefinition
import dev.restate.sdk.http.vertx.RestateHttpEndpointBuilder
import zio._

class RestateEndpoint(val service: ServiceDefinition[Any]) {

  def start: ZIO[Any, Throwable, Int] = ZIO.fromCompletionStage({
    RestateHttpEndpointBuilder.builder()
      .bind(service)
      .build()
      .listen(9080).toCompletionStage
  }).map({
    _.actualPort()
  })

}
