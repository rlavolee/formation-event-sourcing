package account.directory.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AccountDirectorySpec extends AnyFlatSpec with Matchers {

  "AccountDirectory" should "reference account" in {
    val expected = List(AccountDirectoryEvent.AccountDirectoryReferenced)
    val sut = AccountDirectory.handleCommandInit(AccountDirectoryCommand.ReferenceAccount)

    sut shouldBe expected
  }

  it should "active account" in {
    val expected = List(AccountDirectoryEvent.AccountDirectoryActivated)
    val account = AccountDirectory.applyEvents(List(AccountDirectoryEvent.AccountDirectoryReferenced))

    val sut = AccountDirectory.handleCommand(AccountDirectoryCommand.ActivateAccount, account)

    sut shouldBe expected
  }

  it should "deactivate account" in {
    val expected = List(AccountDirectoryEvent.AccountDirectoryDeactivated)
    val account = AccountDirectory.applyEvents(List(AccountDirectoryEvent.AccountDirectoryReferenced))

    val sut = AccountDirectory.handleCommand(AccountDirectoryCommand.DeactivateAccount, account)

    sut shouldBe expected
  }

  it should "not generate two AccountDirectoryDeactivated events in a row" in {
    val expected = Nil
    val account = AccountDirectory.applyEvents(List(AccountDirectoryEvent.AccountDirectoryReferenced, AccountDirectoryEvent.AccountDirectoryActivated, AccountDirectoryEvent.AccountDirectoryDeactivated))
    val sut = AccountDirectory.handleCommand(AccountDirectoryCommand.DeactivateAccount, account)

    sut shouldBe expected
  }

}
