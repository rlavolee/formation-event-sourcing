package account.directory.domain

import scala.annotation.tailrec

import infra.repository.Version

sealed trait AccountDirectoryStatus
object AccountDirectoryStatus {
  case object Activated extends AccountDirectoryStatus
  case object Deactivated extends AccountDirectoryStatus
  case object Referenced extends AccountDirectoryStatus
}

case class AccountDirectory(status: AccountDirectoryStatus)
object AccountDirectory {

  private[domain] def handleCommandInit(c: AccountDirectoryCommand): List[AccountDirectoryEvent] = c match {
    case AccountDirectoryCommand.ReferenceAccount => List(AccountDirectoryEvent.AccountDirectoryReferenced)
    case _ => Nil
  }

  private[domain] def handleCommand(c: AccountDirectoryCommand, a: AccountDirectory): List[AccountDirectoryEvent]
  = c match {
    case AccountDirectoryCommand.ActivateAccount => List(AccountDirectoryEvent.AccountDirectoryActivated)
    case AccountDirectoryCommand.DeactivateAccount =>
      a.status match {
        case AccountDirectoryStatus.Activated | AccountDirectoryStatus.Referenced => List(AccountDirectoryEvent.AccountDirectoryDeactivated)
        case AccountDirectoryStatus.Deactivated => Nil
      }
  }

  def handle(c: AccountDirectoryCommand, a: AccountDirectory): List[AccountDirectoryEvent] =
    c match {
      case AccountDirectoryCommand.ReferenceAccount => handleCommandInit(c)
      case AccountDirectoryCommand.ActivateAccount => handleCommand(c, a)
      case AccountDirectoryCommand.DeactivateAccount => handleCommand(c, a)
    }

  val empty = AccountDirectory(AccountDirectoryStatus.Referenced)

  @tailrec
  private def applyEvents(events: List[AccountDirectoryEvent], a: AccountDirectory = empty): AccountDirectory =
    events match {
      case ::(head, tail) => head match {
        case AccountDirectoryEvent.AccountDirectoryReferenced => applyEvents(tail, a)
        case AccountDirectoryEvent.AccountDirectoryActivated => applyEvents(tail, a.copy(status = AccountDirectoryStatus.Activated))
        case AccountDirectoryEvent.AccountDirectoryDeactivated => applyEvents(tail, a.copy(status = AccountDirectoryStatus.Deactivated))
      }
      case Nil => a
    }

  def applyEvents(events: List[AccountDirectoryEvent]): AccountDirectory =
    applyEvents(events, empty)


}
