package com.geeks.mybank.demain.presenter

import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.model.AccountState
import com.geeks.mybank.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPresenter(private val view: AccountContracts.View) : AccountContracts.Presenter {

    override fun loadAccounts() {
        ApiClient.accountsApi.getAccounts().enqueue(object : Callback<List<Account>> {
            override fun onResponse(
                call: Call<List<Account>?>,
                response: Response<List<Account>?>
            ) {
                if (response.isSuccessful) view.showAccounts(response.body() ?: emptyList())
            }

            override fun onFailure(
                call: Call<List<Account>?>,
                t: Throwable
            ) {

            }
        })
    }

    override fun addAccount(account: Account) {
        ApiClient.accountsApi.addAccount(account).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                responseSuccessful(response)
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {

            }

        })
    }

    override fun updateAccountFully(account: Account) {
        account.id?.let {
            ApiClient.accountsApi.updateAccount(it, account).enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit?>,
                    response: Response<Unit?>
                ) {
                    responseSuccessful(response)
                }

                override fun onFailure(call: Call<Unit?>, t: Throwable) {

                }

            })
        }
    }

    override fun updateAccountPartially(id: String, isChecked: Boolean) {
        ApiClient.accountsApi.updateAccountPartially(id, AccountState(isChecked)).enqueue(object  : Callback<Unit>{
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                responseSuccessful(response)
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
            }

        })
    }

    override fun deleteAccount(id: String) {
        ApiClient.accountsApi.deleteAccount(id).enqueue(object : Callback<Unit>{
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                responseSuccessful(response)
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
            }

        })
    }


    fun responseSuccessful(response: Response<Unit?>){
        if (response.isSuccessful) loadAccounts()
        else response.code()

    }

}