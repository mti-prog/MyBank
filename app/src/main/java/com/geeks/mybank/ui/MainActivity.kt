package com.geeks.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks.mybank.R
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.databinding.ActivityMainBinding
import com.geeks.mybank.databinding.AddDialogBinding
import com.geeks.mybank.demain.presenter.AccountContracts
import com.geeks.mybank.demain.presenter.AccountPresenter
import com.geeks.mybank.ui.adapter.AccountAdapter

class MainActivity : AppCompatActivity(), AccountContracts.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountAdapter
    private lateinit var presenter: AccountPresenter

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
        presenter = AccountPresenter(this)

        binding.fabAddItem.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val binding = AddDialogBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Adding new account")
                .setView(root)
                .setPositiveButton("Add") { _, _ ->
                    val account = Account(
                        name = etName.text.toString(),
                        balance = etBalance.text.toString().toInt(),
                        currency = etCurrency.text.toString()
                    )
                    presenter.addAccount(account)
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isActive ->
                presenter.updateAccountPartially(id, isActive)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        rec.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        rec.adapter = adapter
    }

    private fun showDeleteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this account?")
            .setPositiveButton("Delete") { _, _ ->
                presenter.deleteAccount(id)
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
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Editing account")
                    .setView(root)
                    .setPositiveButton("Edit") { _, _ ->
                        val updatedAccount = account.copy(
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )
                        presenter.updateAccountFully(updatedAccount)
                    }
                    .show()
            }
        }
    }


    override fun showAccounts(accountList: List<Account>) {
        adapter.submitList(accountList)
    }
}