package account.directory.domain

sealed trait AccountDirectoryEvent
object AccountDirectoryEvent {
  case object AccountDirectoryReferenced extends AccountDirectoryEvent
  case object AccountDirectoryActivated extends AccountDirectoryEvent
  case object AccountDirectoryDeactivated extends AccountDirectoryEvent
}
