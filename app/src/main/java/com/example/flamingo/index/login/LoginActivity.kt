package com.example.flamingo.index.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.example.flamingo.base.activity.VVMBaseActivity
import com.example.flamingo.databinding.ActivityLoginBinding
import com.example.flamingo.utils.afterTextChanged
import com.example.flamingo.utils.getViewModel

class LoginActivity : VVMBaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override val viewModel: LoginViewModel by lazy { getViewModel() }

    override fun initStatusBarDarkFont() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()

        binding.run {

            tvUsername.afterTextChanged {
                viewModel.loginDataChanged(
                    tvUsername.text.toString(),
                    tvPassword.text.toString()
                )
            }

            tvPassword.apply {
                afterTextChanged {
                    viewModel.loginDataChanged(
                        tvUsername.text.toString(),
                        tvPassword.text.toString()
                    )
                }

                // 软键盘确认
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            viewModel.login(
                                tvUsername.text.toString(),
                                tvPassword.text.toString()
                            )
                    }
                    false
                }

                btLogin.setOnClickListener {
                    viewModel.login(tvUsername.text.toString(), tvPassword.text.toString())
                }
            }

        }
    }

    private fun observe() {
        viewModel.loginFormState.observe(this) {
            val loginState = it ?: return@observe

            // disable login button unless both username / password is valid
            binding.btLogin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.tvUsername.error = loginState.usernameError
            }
            if (loginState.passwordError != null) {
                binding.tvPassword.error = loginState.passwordError
            }
        }
        viewModel.result.observe(this) {
            if (it) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

}
