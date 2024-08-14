import dev.restate.sdk.scala.RestateEndpoint
import zio._

object Main extends ZIOAppDefault {
  def run = for {
    port <- new RestateEndpoint(new AddressBookService().toServiceDefinition).start
    _ <- Console.printLine(s"Started AddressBook on port ${port}")
    _ <- ZIO.never
  } yield ()
}
