import codegen.BaseAddressBookService
import dev.restate.sdk.common.StateKey
import dev.restate.sdk.scala.{ObjectContext, SharedObjectContext}
import tutorial.addressbook.{AddressBook, Person}
import zio._

class AddressBookService extends BaseAddressBookService {

  private val ADDRESS_BOOK_KEY = StateKey.of("addressBook", Serdes.ADDRESS_BOOK)

  override def add(ctx: ObjectContext, person: Person): ZIO[Any, Throwable, Unit] = for {
    addressBook <- ctx.get(ADDRESS_BOOK_KEY)
    newAddressBook <- ZIO.succeed(
      addressBook
        .getOrElse(AddressBook.defaultInstance)
        .addPeople(person)
    )
    _ <- ctx.set(ADDRESS_BOOK_KEY, newAddressBook)
  } yield ()

  override def get(ctx: SharedObjectContext): ZIO[Any, Throwable, AddressBook] =
    ctx
      .get(ADDRESS_BOOK_KEY)
      .map(_.getOrElse(AddressBook.defaultInstance))
}
