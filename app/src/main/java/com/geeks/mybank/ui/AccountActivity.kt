package com.geeks.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks.mybank.R
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.databinding.ActivityMainBinding
import com.geeks.mybank.databinding.AddDialogBinding
import com.geeks.mybank.ui.viewModel.AccountViewModel
import com.geeks.mybank.ui.adapter.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountAdapter
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        subscribeToLiveData()
        binding.fabAddItem.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.accounts.observe(this) {
            adapter.submitList(it)

        }
    }


    private fun showAddDialog() {
        val binding = AddDialogBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            AlertDialog.Builder(this@AccountActivity)
                .setTitle("Adding new account")
                .setView(root)
                .setPositiveButton("Add") { _, _ ->
                    val account = Account(
                        name = etName.text.toString(),
                        balance = etBalance.text.toString().toInt(),
                        currency = etCurrency.text.toString()
                    )
                    viewModel.addAccount(account)
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isActive ->
                viewModel.updateAccountPartially(id, isActive)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        rec.layoutManager =
            LinearLayoutManager(this@AccountActivity, LinearLayoutManager.VERTICAL, false)
        rec.adapter = adapter
    }

    private fun showDeleteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this account?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteAccount(id)
            }
            .setNegativeButton("Cancel") { _, _ ->

            }
            .show()
    }

    private fun showEditDialog(account: Account) {
        val binding = AddDialogBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            account.run {
                etName.setText(name)
                etBalance.setText(balance.toString())
                etCurrency.setText(currency)
                AlertDialog.Builder(this@AccountActivity)
                    .setTitle("Editing account")
                    .setView(root)
                    .setPositiveButton("Edit") { _, _ ->
                        val updatedAccount = account.copy(
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )
                        viewModel.updateAccountFully(updatedAccount)
                    }
                    .show()
            }
        }
    }
}