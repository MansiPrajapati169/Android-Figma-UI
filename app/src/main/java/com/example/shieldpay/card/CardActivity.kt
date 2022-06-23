package com.example.shieldpay.card

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.shieldpay.Data
import com.example.shieldpay.R
import com.example.shieldpay.basesetup.BaseActivity
import com.example.shieldpay.databinding.ActivityCardBinding
import com.example.shieldpay.home.BottomNavigationActivity
import com.example.shieldpay.home.CardAdapter
import com.example.shieldpay.onboarding.setStatusBarColorWhite
import com.example.shieldpay.roomdatabase.NotesActivity
import com.example.shieldpay.webservices.http.AuthenticationViewModel

class CardActivity : BaseActivity<ActivityCardBinding, AuthenticationViewModel>(
    ActivityCardBinding::inflate,
    AuthenticationViewModel::class.java
), View.OnClickListener {
    // Overridden Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.click = this
        binding.let {
            Data.setToolBar(this, it.toolbarTitle, it.toolbar, getString(R.string.your_cards))
        }
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
        window.apply {
            setStatusBarColorWhite()
        }
        setUpCards()
    }

    // Class Method
    private fun setUpCards() {
        binding.rvCards.adapter = CardAdapter(Data.cardDataTwo(this))
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.btnAddCreditCard.id -> startActivity(Intent(this, NotesActivity::class.java))
        }
    }

}