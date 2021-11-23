package account.directory.domain

import infra.repository.Version

trait PubSub {

  private[domain] def store(id: AccountDirectoryId, event: AccountDirectoryEvent, eventsStore: List[(AccountDirectoryId, AccountDirectoryEvent)])
  : List[(AccountDirectoryId, AccountDirectoryEvent)] =
    eventsStore :+ (id, event)

  def publish(id: AccountDirectoryId, knownVersion: Version, event: AccountDirectoryEvent): Unit

  // case class id list events
  //def publish()

  private def notify(event: (AccountDirectoryId, AccountDirectoryEvent), eventHandler: (AccountDirectoryId, AccountDirectoryEvent) => Unit): Unit =
    event match {
      case (id, e) => eventHandler(id, e)
    }

  private[domain] def notifiyAll(event: (AccountDirectoryId, AccountDirectoryEvent)): Unit =
    eventHandlers.foreach(notify(event, _))

  private var eventHandlers: List[(AccountDirectoryId, AccountDirectoryEvent) => Unit] = List.empty[(AccountDirectoryId, AccountDirectoryEvent) => Unit]
  def registerHandler(handler: (AccountDirectoryId, AccountDirectoryEvent) => Unit): Unit =
    eventHandlers = eventHandlers :+ handler
}

class PubSubInMemory extends PubSub {

  private var inMemoryStore = List.empty[(AccountDirectoryId, AccountDirectoryEvent)]

  override def publish(id: AccountDirectoryId, knownVersion: Version, event: AccountDirectoryEvent): Unit = {
    inMemoryStore = store(id, event, inMemoryStore)
    notifiyAll((id, event))
  }

  def getEvents(): List[(AccountDirectoryId, AccountDirectoryEvent)] = inMemoryStore
}
