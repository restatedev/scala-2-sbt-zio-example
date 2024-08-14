package dev.restate.sdk.scala

import dev.restate.sdk.common.Serde
import scalapb.json4s.JsonFormat

class ScalaJSONPBSerde[T <: scalapb.GeneratedMessage](implicit val messageCompanion: scalapb.GeneratedMessageCompanion[T]) extends Serde[T] {
  override def serialize(value: T): Array[Byte] = JsonFormat.toJsonString(value).getBytes

  override def deserialize(value: Array[Byte]): T = JsonFormat.fromJsonString[T](new String(value))

  override def contentType(): String = "application/json"
}
