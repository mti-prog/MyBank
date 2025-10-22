package com.geeks.mybank.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.model.AccountState
import com.geeks.mybank.data.network.AccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountsApi: AccountsApi
) : ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    fun loadAccounts() {
        accountsApi.getAccounts().handleAccountResponse(
            onSuccess = {
                _accounts.value = it
            }
        )
    }

    fun addAccount(account: Account) {
        accountsApi.addAccount(account).handleAccountResponse()
    }

    fun updateAccountFully(account: Account) {
        account.id?.let {
            accountsApi.updateAccount(it, account).handleAccountResponse()
        }
    }

    fun updateAccountPartially(id: String, isChecked: Boolean) {
        accountsApi.updateAccountPartially(id, AccountState(isChecked))
            .handleAccountResponse()
    }

    fun deleteAccount(id: String) {
        accountsApi.deleteAccount(id).handleAccountResponse()
    }

    private fun <T> Call<T>?.handleAccountResponse(
        onSuccess: (T) -> Unit = { loadAccounts() },
        onError: (String) -> Unit = {}
    ) {
        this?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val result = response.body()
                if (result != null && response.isSuccessful) {
                    onSuccess(result)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                onError(t.message.toString())
            }

        }
        )
    }

}