package account.directory.domain

import infra.repository.EventStoreInMemory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AccountDirectoryCommandHandlerSpec extends AnyFlatSpec with Matchers {

  "AccountDirectoryCommandHandler" should "display updated AccountDirectoryReferencedProjection when ReferenceAccount is sent" in {
    val id = AccountDirectoryId(42L)
    val command = AccountDirectoryCommand.ReferenceAccount
    implicit val repo = new AccountDirectoryReferencedProjectionRepositoryInMemory
    implicit val pubSub = new PubSubInMemory
    implicit val eventStore = new EventStoreInMemory
    pubSub.registerHandler(AccountDirectoryReferencedProjection.handleEvent)

    AccountDirectoryCommandHandler.handle(id, command)

    repo.getContent() should contain(id)
  }

}
