package account.directory.domain

import infra.repository.Version
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PubSubInMemorySpec extends AnyFlatSpec with Matchers {

  "PubSubInMemory" should "store events when publish event" in {
    val id = AccountDirectoryId(42L)
    val event = AccountDirectoryEvent.AccountDirectoryReferenced

    val expectedStore = List((id, event))

    val sut = new PubSubInMemory
    sut.publish(id, Version(0L), event)

    sut.getEvents() shouldEqual expectedStore
  }

  it should "call handlers when publish event" in {
    val id = AccountDirectoryId(42L)
    val event = AccountDirectoryEvent.AccountDirectoryReferenced

    var eventPublished = List.empty[(AccountDirectoryId, AccountDirectoryEvent)]
    def basicEventHandler(id: AccountDirectoryId, event: AccountDirectoryEvent): Unit =
      eventPublished = eventPublished :+ (id, event)

    val sut = new PubSubInMemory
    sut.registerHandler(basicEventHandler)
    sut.publish(id, Version(0), event)

    eventPublished shouldEqual List((id, event))
  }

}
