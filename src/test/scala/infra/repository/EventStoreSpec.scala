package infra.repository

import account.directory.domain.AccountDirectoryEvent.{AccountDirectoryActivated, AccountDirectoryDeactivated, AccountDirectoryReferenced}
import account.directory.domain.AccountDirectoryId
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EventStoreSpec  extends AnyFlatSpec with Matchers {

  "EventStore" should "return all stored events for a given account" in {
    val accountId = AccountDirectoryId(12L)
    val events = List(AccountDirectoryReferenced, AccountDirectoryActivated)
    val eventStore = new EventStoreInMemory()

    eventStore.store(accountId, Version(0L), events)
    eventStore.store(AccountDirectoryId(42L), Version(0L), List(AccountDirectoryReferenced))
    eventStore.store(accountId, Version(2L), List(AccountDirectoryDeactivated))

    eventStore.filter(accountId) shouldEqual (events :+ AccountDirectoryDeactivated)
  }

  "EventStore" should "return StorageResult in error when amount stored events is different from version provided at storage" in {
    val accountId = AccountDirectoryId(12L)
    val events = List(AccountDirectoryReferenced, AccountDirectoryActivated)
    val knownVersion = Version(1L)
    val eventStore = new EventStoreInMemory()

    val storageResult = eventStore.store(accountId, knownVersion, events)

    storageResult match {
      case StorageResult.ERR => succeed
      case _ => fail("Storage should return StorageResult in error")
    }
  }
}
