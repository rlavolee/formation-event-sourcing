package account.directory.domain

trait AccountDirectoryReferencedProjectionRepository {

  private[domain] def add(id: AccountDirectoryId, l: List[AccountDirectoryId]): List[AccountDirectoryId] =
    l :+ id

  def addIo(id: AccountDirectoryId): Unit

  private[domain] def remove(id: AccountDirectoryId, l: List[AccountDirectoryId]): List[AccountDirectoryId] =
    l.filterNot(_ == id)

  def removeIo(id: AccountDirectoryId): Unit

}

class AccountDirectoryReferencedProjectionRepositoryInMemory(initalList: List[AccountDirectoryId] = List.empty[AccountDirectoryId]) extends AccountDirectoryReferencedProjectionRepository {

  private var inMemoryAccountDirectoryList: List[AccountDirectoryId] = initalList

  override def addIo(id: AccountDirectoryId): Unit =
    inMemoryAccountDirectoryList = add(id, inMemoryAccountDirectoryList)

  override def removeIo(id: AccountDirectoryId): Unit =
    inMemoryAccountDirectoryList = remove(id, inMemoryAccountDirectoryList)

  def getContent(): List[AccountDirectoryId] = inMemoryAccountDirectoryList
}

object AccountDirectoryReferencedProjection {

  def handleEvent(id: AccountDirectoryId, event: AccountDirectoryEvent)(implicit repository: AccountDirectoryReferencedProjectionRepository): Unit =
    event match {
      case AccountDirectoryEvent.AccountDirectoryReferenced => repository.addIo(id)
      case AccountDirectoryEvent.AccountDirectoryActivated => repository.removeIo(id)
      case AccountDirectoryEvent.AccountDirectoryDeactivated => ()
    }

}
