package com.example.flamingo.index.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.example.flamingo.base.activity.VVMBaseActivity
import com.example.flamingo.databinding.ActivityLoginBinding
import com.example.flamingo.utils.afterTextChanged
import com.example.flamingo.utils.getViewModel
import splitties.views.onClick

class LoginActivity : VVMBaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override val viewModel: LoginViewModel by lazy { getViewModel() }

    private var loginOrRegister = true

    override fun initStatusBarDarkFont() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        observe()

        binding.run {
            etUsername.afterTextChanged {
                viewModel.loginDataChanged(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }
            etPassword.apply {
                afterTextChanged {
                    viewModel.loginDataChanged(
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                }
                // 软键盘确认
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            if (loginOrRegister) {
                                viewModel.login(
                                    etUsername.text.toString(),
                                    etPassword.text.toString()
                                )
                            } else {
                                viewModel.register(
                                    etUsername.text.toString(),
                                    etPassword.text.toString()
                                )
                            }
                    }
                    false
                }
            }
            btLogin.setOnClickListener {
                if (loginOrRegister) {
                    viewModel.login(
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                } else {
                    viewModel.register(
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun initView() {
        setUI()
        binding.tvAnother.onClick {
            loginOrRegister = !loginOrRegister
            setUI()
        }
    }

    private fun setUI() {
        if (loginOrRegister) {
            binding.tvAnother.text = "去注册"
            binding.btLogin.text = "登录"
            binding.etPassword.setImeActionLabel("登录", EditorInfo.IME_ACTION_DONE)
        } else {
            binding.tvAnother.text = "去登录"
            binding.btLogin.text = "注册"
            binding.etPassword.setImeActionLabel("注册", EditorInfo.IME_ACTION_DONE)
        }
    }

    private fun observe() {
        viewModel.loginFormState.observe(this) {
            val loginState = it ?: return@observe

            // disable login button unless both username / password is valid
            binding.btLogin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.etUsername.error = loginState.usernameError
            }
            if (loginState.passwordError != null) {
                binding.etPassword.error = loginState.passwordError
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
