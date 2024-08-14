package codegen

import dev.restate.sdk.common.syscalls.{HandlerDefinition, HandlerSpecification, ServiceDefinition}
import dev.restate.sdk.common.{HandlerType, Serde, ServiceType}
import dev.restate.sdk.scala.{HandlerRunner, ObjectContext, ScalaJSONPBSerde, SharedObjectContext}
import tutorial.addressbook.{AddressBook, Person}
import zio._

import scala.jdk.CollectionConverters._

abstract class BaseAddressBookService {
  object Serdes {
    val ADDRESS_BOOK = new ScalaJSONPBSerde[AddressBook]
    val PERSON = new ScalaJSONPBSerde[Person]
    val VOID: Serde[Void] = Serde.VOID
  }

  def add(ctx: ObjectContext, person: Person): ZIO[Any, Throwable, Unit]

  def get(ctx: SharedObjectContext): ZIO[Any, Throwable, AddressBook]

  def toServiceDefinition: ServiceDefinition[Any] = ServiceDefinition.of(
    "AddressBook",
    ServiceType.VIRTUAL_OBJECT,
    List(
      HandlerDefinition.of(
        HandlerSpecification.of("add", HandlerType.EXCLUSIVE, Serdes.PERSON, Serdes.VOID),
        new HandlerRunner((ctx: ObjectContext, person: Person) => {
          this.add(ctx, person).map(_ => {
            null.asInstanceOf[Void]
          })
        })
      ),
      HandlerDefinition.of(
        HandlerSpecification.of("get", HandlerType.SHARED, Serdes.VOID, Serdes.ADDRESS_BOOK),
        new HandlerRunner((ctx: ObjectContext, _: Void) => this.get(ctx))
      )
    ).asJavaCollection.asInstanceOf[java.util.Collection[HandlerDefinition[_, _, Any]]]
  )
}
