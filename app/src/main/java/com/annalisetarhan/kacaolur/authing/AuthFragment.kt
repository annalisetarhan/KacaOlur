package com.annalisetarhan.kacaolur.authing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.AuthFragmentBinding
import kotlinx.android.synthetic.main.auth_fragment.*

class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: AuthFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpPhoneEditTexts()
    }

    private var digitDeletedFlag = false
    private var userIsNotMakingChanges = false
    private var prevEditText2CursorPosition: Int? = null
    private var prevEditText1CursorPosition: Int? = null

    private fun setUpPhoneEditTexts() {
        phoneEditText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) { return }
                // If editText is full and cursor is after last digit, jump to the next one
                /*if (!digitDeletedFlag && phoneEditText1.selectionEnd == 3) {
                    Log.d("phoneEditText1", "afterTextChanged")
                    phoneEditText2.requestFocus()
                }*/
                if (phoneEditText1.text.length == 3) {
                    Log.d("phoneEditText1", "if (phoneEditText1.text.length == 3)")
                    val lastDigit = phoneEditText1.text[2]
                    userIsNotMakingChanges = true
                    phoneEditText1.text.delete(2, 3)
                    userIsNotMakingChanges = false
                    pushDigitToFront(lastDigit, 2)
                    if (prevEditText1CursorPosition == 2) {
                        phoneEditText2.requestFocus()
                        phoneEditText2.setSelection(0)
                    } else if (prevEditText1CursorPosition == 3){
                        phoneEditText2.requestFocus()
                        phoneEditText2.setSelection(1)
                    }
                }
                if (digitDeletedFlag) {
                    Log.d("phoneEditText1", "if (digitDeletedFlag)")
                    pullDigitToEnd(1)
                    digitDeletedFlag = false
                }
                prevEditText1CursorPosition = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (after < count && !userIsNotMakingChanges) {
                    Log.d("phoneEditText1", "beforeTextChanged, if (after < count)")
                    digitDeletedFlag = true
                }
                prevEditText1CursorPosition = phoneEditText1.selectionStart
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phoneEditText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) { return }
                if (phoneEditText2.selectionEnd == 0) {
                    Log.d("phoneEditText2", "if (phoneEditText2.selectionEnd == 0)")
                    phoneEditText1.requestFocus()
                    phoneEditText1.setSelection(phoneEditText1.length())
                }
                // If editText is full and cursor is after last digit, jump to the next one
                /*if (!digitDeletedFlag && phoneEditText2.selectionEnd == 4) {
                    Log.d("phoneEditText2", "if (!digitDeletedFlag && )")
                    phoneEditText3.requestFocus()
                    phoneEditText3.setSelection(phoneEditText3.text.length)
                }*/
                if (phoneEditText2.text.length == 4) {
                    Log.d("phoneEditText2", "if (phoneEditText2.text.length == 4)")
                    val lastDigit = phoneEditText2.text[3]
                    userIsNotMakingChanges = true
                    phoneEditText2.text.delete(3, 4)
                    userIsNotMakingChanges = false
                    pushDigitToFront(lastDigit, 3)
                    if (prevEditText2CursorPosition == 3) {
                        phoneEditText3.requestFocus()
                        phoneEditText3.setSelection(0)
                    } else if (prevEditText2CursorPosition == 4){
                        phoneEditText3.requestFocus()
                        phoneEditText3.setSelection(1)
                    }
                }
                if (digitDeletedFlag) {
                    Log.d("phoneEditText2", "if (digitDeletedFlag)")
                    pullDigitToEnd(2)
                    digitDeletedFlag = false
                }
                prevEditText2CursorPosition = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (after < count && !userIsNotMakingChanges) {
                    Log.d("phoneEditText2", "beforeTextChanged, if (after < count)")
                    digitDeletedFlag = true
                }
                prevEditText2CursorPosition = phoneEditText2.selectionStart
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phoneEditText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) { return }
                if (phoneEditText3.selectionEnd == 0) {
                    Log.d("phoneEditText3", "if (phoneEditText3.selectionEnd == 0)")
                    phoneEditText2.requestFocus()
                    phoneEditText2.setSelection(phoneEditText2.length())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun pushDigitToFront(digit: Char, editTextNum: Int) {
        Log.d("pushDigitToFront", "pushDigitToFront, editTextNum: " + editTextNum )
        userIsNotMakingChanges = true
        Log.d("userIsNotMakingChanges", "true")
        val editText =
            when (editTextNum) {
                2 -> phoneEditText2
                3 -> phoneEditText3
                else -> error("invalid editTextNum")
            }
        val nextEditTextNum =
            when (editTextNum) {
                2 -> 3
                3 -> null
                else -> error("invalid editTextNum")
            }
        val maxLength =
            when (editTextNum) {
                2 -> 3
                3 -> 4
                else -> error("invalid editTextNum")
            }
        var bumpedDigit: Char? = null
        if (editText.text.length == maxLength) {
            bumpedDigit = editText.text[maxLength - 1]
            editText.text.delete(maxLength - 1, maxLength)
        }
        editText.text.insert(0, digit.toString())
        if (bumpedDigit != null && nextEditTextNum != null) {
            pushDigitToFront(bumpedDigit, nextEditTextNum)
        }
        userIsNotMakingChanges = false
        Log.d("userIsNotMakingChanges", "false")
    }

    private fun pullDigitToEnd(editTextNum: Int) {
        Log.d("pullDigitToEnd", "pullDigitToEnd, editTextNum: " + editTextNum)
        userIsNotMakingChanges = true
        Log.d("userIsNotMakingChanges", "true")
        val editText =
            when(editTextNum) {
                1 -> phoneEditText1
                2 -> phoneEditText2
                3 -> phoneEditText3
                else -> error("invalid editTextNum")
            }
        val maxLength =
            when (editTextNum) {
                1 -> 2
                2 -> 3
                3 -> 4
                else -> error("invalid editTextNum")
            }
        if (editText.text.length != maxLength - 1) {
            userIsNotMakingChanges = false
            Log.d("userIsNotMakingChanges", "false")
            return
        }
        val nextEditText =
            when (editTextNum) {
                1 -> phoneEditText2
                2 -> phoneEditText3
                3 -> null
                else -> error("invalid editTextNum")
            }
        val nextEditTextNum =
            when (editTextNum) {
                1 -> 2
                2 -> 3
                3 -> null
                else -> error("invalid editTextNum")
            }

        if (!nextEditText?.text.isNullOrEmpty()) {
            val pulledDigit = nextEditText!!.text[0]
            editText.text.append(pulledDigit)
            if (editText.selectionEnd == editText.text.length && editText.text.isNotEmpty()) {    // WHAT THE ACTUAL FUCK IS THIS
                editText.setSelection(editText.selectionEnd - 1)
            }
            nextEditText.text.delete(0, 1)                  // WHAT THE ACTUAL FUCK IS THIS99
            nextEditText.setText(nextEditText.text.toString())      // WHAT THE ACTUAL FUCK IS THIS
            pullDigitToEnd(nextEditTextNum!!)
        }
        userIsNotMakingChanges = false
        Log.d("userIsNotMakingChanges", "false")
    }
}

