package account.directory.domain

import infra.repository.{EventStore, Version}

object AccountDirectoryCommandHandler {

  def handle(id: AccountDirectoryId, command: AccountDirectoryCommand)(implicit pubsub: PubSub, eventStore: EventStore): Unit = {
    val history = eventStore.filter(id)
    val events = AccountDirectory.handle(command, AccountDirectory.applyEvents(history))
    events.foreach(pubsub.publish(id, Version(history.size), _))
  }

}
