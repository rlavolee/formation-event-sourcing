package infra.repository

import java.util.concurrent.ConcurrentMap

import account.directory.domain.{AccountDirectoryEvent, AccountDirectoryId}
import scala.collection.concurrent.TrieMap
import scala.concurrent.blocking

trait EventStore {

  def store(accountId: AccountDirectoryId, knownVersion: Version, events: List[AccountDirectoryEvent]): StorageResult

  def filter(accountId: AccountDirectoryId): List[AccountDirectoryEvent]
}

class EventStoreInMemory(var eventsById: TrieMap[AccountDirectoryId, List[AccountDirectoryEvent] ] = TrieMap.empty) extends EventStore {

  override def filter(accountId: AccountDirectoryId) : List[AccountDirectoryEvent] = {
    eventsById.getOrElse(accountId, List.empty)
  }

  override def store(accountId: AccountDirectoryId, knownVersion: Version, eventsToAdd: List[AccountDirectoryEvent]): StorageResult = {
    blocking {
      if (eventsById.getOrElse(accountId, List.empty).size != knownVersion.value) {
        StorageResult.ERR
      } else {
        eventsById.updateWith(accountId) {
          case Some(value) => Some(value ++ eventsToAdd)
          case None => Some(eventsToAdd)
        }
        StorageResult.ACK
      }
    }

  }

}
