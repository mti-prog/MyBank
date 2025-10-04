package com.geeks.mybank.demain.presenter

import com.geeks.mybank.data.model.Account
import com.google.android.filament.View

class AccountPresenter(private val view: AccountContracts.View) : AccountContracts.Presenter {
    override fun loadAccounts() {
        val testMockList = arrayListOf<Account>()
        testMockList.add(Account(
            name = "O!Bank",
            balance = 1000,
            currency = "KGS"
        ))
        testMockList.add(Account(
            name = "Mbank",
            balance = 3000,
            currency = "RUB"
        ))
        testMockList.add(Account(
            name = "BakaiBank",
            balance = 5000,
            currency = "USA"
        ))
        view.showAccounts(testMockList)
    }
}