package com.geeks.mybank.data.network

import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.model.AccountState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountsApi {
    @GET("accounts")
    fun getAccounts(): Call<List<Account>>

    @POST("accounts")
    fun addAccount(@Body account: Account): Call<Unit>

    @PUT("accounts/{id}")
    fun updateAccount(
        @Path("id") id: String,
        @Body account: Account): Call<Unit>

    @PATCH("accounts/{id}")
    fun updateAccountPartially(
        @Path("id") id: String,
        @Body account: AccountState): Call<Unit>

    @DELETE("accounts/{id}")
    fun deleteAccount(
        @Path("id"
        ) id: String
    ): Call<Unit>
}