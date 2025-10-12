package com.geeks.mybank.demain.presenter

import com.geeks.mybank.data.model.Account

interface AccountContracts {
    interface View{
        fun showAccounts(accountList : List<Account>)
    }
    interface Presenter{
        fun loadAccounts()
        fun addAccount(account: Account)
    }
}