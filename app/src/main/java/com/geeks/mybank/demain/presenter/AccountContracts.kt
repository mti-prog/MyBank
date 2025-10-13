package com.geeks.mybank.demain.presenter

import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.model.AccountState

interface AccountContracts {
    interface View{
        fun showAccounts(accountList : List<Account>)
    }
    interface Presenter{
        fun loadAccounts()
        fun addAccount(account: Account)
        fun updateAccountFully(account: Account)
        fun updateAccountPartially(id: String, isChecked: Boolean)
        fun deleteAccount(id: String)

    }
}