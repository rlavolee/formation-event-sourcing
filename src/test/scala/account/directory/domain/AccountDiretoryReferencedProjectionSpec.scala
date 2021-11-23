package account.directory.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AccountDiretoryReferencedProjectionSpec extends AnyFlatSpec with Matchers {

  "AccountDiretoryReferencedProjection" should "add id to the list when AccountDirectoryReferenced is consumed" in {
    val id = AccountDirectoryId(123L)

    implicit val repo = new AccountDirectoryReferencedProjectionRepositoryInMemory

    AccountDirectoryReferencedProjection.handleEvent(id, AccountDirectoryEvent.AccountDirectoryReferenced)

    repo.getContent() should contain(id)
  }

  it should "remove id from the list when AccountDirectoryActivated is consumed" in {
    val id = AccountDirectoryId(123L)

    implicit val repo = new AccountDirectoryReferencedProjectionRepositoryInMemory(List(AccountDirectoryId(123L), AccountDirectoryId(42L)))

    AccountDirectoryReferencedProjection.handleEvent(id, AccountDirectoryEvent.AccountDirectoryActivated)

    repo.getContent() should not contain(id)
  }

}
