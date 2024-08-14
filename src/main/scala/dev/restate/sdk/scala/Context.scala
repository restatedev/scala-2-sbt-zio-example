package dev.restate.sdk.scala

import dev.restate.sdk.common.Request
import dev.restate.sdk.common.syscalls.Syscalls

class Context(protected val syscalls: Syscalls) {

  def request: Request = syscalls.request()

}
