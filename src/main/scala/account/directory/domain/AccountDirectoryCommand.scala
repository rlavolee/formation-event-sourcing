package account.directory.domain

sealed trait AccountDirectoryCommand
object AccountDirectoryCommand {
  case object ReferenceAccount extends AccountDirectoryCommand
  case object ActivateAccount extends AccountDirectoryCommand
  case object DeactivateAccount extends AccountDirectoryCommand
}
