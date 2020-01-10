package com.annalisetarhan.kacaolur.authing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
        setUpButtons()

        val currentStage = viewModel.selectStage()
        goTo(currentStage)
    }

    /*
     *      GO TO STAGE X
     */

    private fun goTo(currentStage: Int) {
        when (currentStage) {
            1 -> goToStage1()
            2 -> goToStage2()
            3 -> goToStage3()
            4 -> goToStage4()
            5 -> goToStage5()
            6 -> goToStage6()
        }
    }

    private fun goToStage1() {
        hideEverything()

        showPhoneNumber()
        enablePhoneEditTexts()

        primary_text_block.visibility = View.VISIBLE
        primary_text_block.setText(R.string.enter_phone_number)

        stage1_center_button.visibility = View.VISIBLE
    }

    private fun goToStage2() {
        hideEverything()

        showPhoneNumber()
        disablePhoneEditTexts()

        primary_text_block.visibility = View.VISIBLE
        secondary_text_block.visibility = View.VISIBLE
        primary_text_block.setText(R.string.enter_phone_number)
        secondary_text_block.setText(R.string.may_we_send_sms)

        stage2_left_button.visibility = View.VISIBLE
        stage2_right_button.visibility = View.VISIBLE
    }

    private fun goToStage3() {
        hideEverything()

        secondary_text_block.visibility = View.VISIBLE
        secondary_text_block.setText(R.string.code_is_coming)

        verification_code_edittext.visibility = View.VISIBLE

        stage3_left_button.visibility = View.VISIBLE
        stage3_right_button.visibility = View.VISIBLE
    }

    private fun goToStage4() {
        hideEverything()

        primary_text_block.visibility = View.VISIBLE
        primary_text_block.setText(R.string.choose_display_name)

        username_edittext.visibility = View.VISIBLE

        stage4_center_button.visibility = View.VISIBLE
    }

    private fun goToStage5() {
        hideEverything()

        primary_text_block.visibility = View.VISIBLE
        primary_text_block.text = getString(R.string.nice_to_meet_you, viewModel.username)

        stage56_center_button.visibility = View.VISIBLE
    }

    private fun goToStage6() {
        hideEverything()

        primary_text_block.visibility = View.VISIBLE
        primary_text_block.text = getString(R.string.welcome_back, viewModel.username)

        stage56_center_button.visibility = View.VISIBLE
    }

    private fun hideEverything() {
        phone_textview_1.visibility = View.GONE
        phone_textview_2.visibility = View.GONE
        phone_textview_3.visibility = View.GONE
        phone_edittext_1.visibility = View.GONE
        phone_edittext_2.visibility = View.GONE
        phone_edittext_3.visibility = View.GONE

        primary_text_block.visibility = View.GONE
        secondary_text_block.visibility = View.GONE

        username_edittext.visibility = View.GONE
        verification_code_edittext.visibility = View.GONE

        stage1_center_button.visibility = View.GONE
        stage2_left_button.visibility = View.GONE
        stage2_right_button.visibility = View.GONE
        stage3_left_button.visibility = View.GONE
        stage3_right_button.visibility = View.GONE
        stage4_center_button.visibility = View.GONE
        stage56_center_button.visibility = View.GONE
    }

    private fun showPhoneNumber() {
        phone_textview_1.visibility = View.VISIBLE
        phone_textview_2.visibility = View.VISIBLE
        phone_textview_3.visibility = View.VISIBLE
        phone_edittext_1.visibility = View.VISIBLE
        phone_edittext_2.visibility = View.VISIBLE
        phone_edittext_3.visibility = View.VISIBLE
    }


    private fun enablePhoneEditTexts() {
        phone_edittext_1.isEnabled = true
        phone_edittext_2.isEnabled = true
        phone_edittext_3.isEnabled = true
    }

    private fun disablePhoneEditTexts() {
        phone_edittext_1.isEnabled = false
        phone_edittext_2.isEnabled = false
        phone_edittext_3.isEnabled = false
        phone_edittext_1.setTextColor(ContextCompat.getColor(context!!, R.color.primaryTextColor))        // Risky? How do I know the context won't be null? Because it has to be attached to an activity for a user to click the button?
        phone_edittext_2.setTextColor(ContextCompat.getColor(context!!, R.color.primaryTextColor))
        phone_edittext_3.setTextColor(ContextCompat.getColor(context!!, R.color.primaryTextColor))
    }

    /*
     *      PHONE EDIT TEXT SETUP
     */

    private var digitDeletedFlag = false
    private var userIsNotMakingChanges = false
    private var prevEditText1CursorPosition: Int? = null
    private var prevEditText2CursorPosition: Int? = null

    private fun setUpPhoneEditTexts() {
        phone_edittext_1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (phone_edittext_1.text.length == 3) {
                    val lastDigit = chopOffLastDigit(1)
                    pushDigitToNextEditText(lastDigit, 2)
                    moveCursorIfNecessary(1)
                }
                if (digitDeletedFlag) {
                    pullDigitFromNextEditText(1)
                    digitDeletedFlag = false
                }
                prevEditText1CursorPosition = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (after < count && !userIsNotMakingChanges) {
                    digitDeletedFlag = true
                }
                prevEditText1CursorPosition = phone_edittext_1.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phone_edittext_2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (phone_edittext_2.selectionEnd == 0) {
                    moveCursorBackTo(1)
                }
                if (phone_edittext_2.text.length == 4) {
                    val lastDigit = chopOffLastDigit(2)
                    pushDigitToNextEditText(lastDigit, 3)
                    moveCursorIfNecessary(2)
                }
                if (digitDeletedFlag) {
                    pullDigitFromNextEditText(2)
                    digitDeletedFlag = false
                }
                prevEditText2CursorPosition = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (after < count && !userIsNotMakingChanges) {
                    digitDeletedFlag = true
                }
                prevEditText2CursorPosition = phone_edittext_2.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phone_edittext_3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (phone_edittext_3.selectionEnd == 0) {
                    moveCursorBackTo(2)
                }
                if (phone_edittext_3.text.length == 5) {
                    chopOffLastDigit(3)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun chopOffLastDigit(editTextNum: Int): Char {
        val editText = getEditText(editTextNum)
        val maxLength = getMaxLength(editTextNum)
        val lastDigit = editText.text[maxLength]

        userIsNotMakingChanges = true
        editText.text.delete(maxLength, maxLength + 1)
        userIsNotMakingChanges = false

        return lastDigit
    }

    private fun pushDigitToNextEditText(digit: Char, nextEditTextNum: Int) {
        val nextEditText = getEditText(nextEditTextNum)
        val nextNextEditTextNum = getNextEditTextNum(nextEditTextNum)
        val maxLength = getMaxLength(nextEditTextNum)

        userIsNotMakingChanges = true
        var bumpedDigit: Char? = null
        if (nextEditText.text.length == maxLength) {
            bumpedDigit = nextEditText.text[maxLength - 1]
            nextEditText.text.delete(maxLength - 1, maxLength)
        }
        nextEditText.text.insert(0, digit.toString())
        if (bumpedDigit != null && nextNextEditTextNum != null) {
            pushDigitToNextEditText(bumpedDigit, nextNextEditTextNum)
        }
        userIsNotMakingChanges = false
    }

    private fun moveCursorBackTo(prevEditTextNum: Int) {
        val prevEditText = getEditText(prevEditTextNum)
        prevEditText.requestFocus()
        prevEditText.setSelection(prevEditText.length())
    }

    private fun moveCursorIfNecessary(editTextNum: Int) {
        if (editTextNum == 1 && prevEditText1CursorPosition!! > 1) {
            phone_edittext_2.requestFocus()
            phone_edittext_2.setSelection(prevEditText1CursorPosition!! - 2)
        } else if (editTextNum == 2 && prevEditText2CursorPosition!! > 2) {
            phone_edittext_3.requestFocus()
            phone_edittext_3.setSelection(prevEditText2CursorPosition!! - 3)
        }
    }

    private fun pullDigitFromNextEditText(editTextNum: Int) {
        val editText = getEditText(editTextNum)
        val maxLength = getMaxLength(editTextNum)
        val nextEditTextNum = getNextEditTextNum(editTextNum)
        val nextEditText = getNextEditText(editTextNum)

        if (editText.text.length != maxLength - 1) {
            return
        }
        if (nextEditText?.text.isNullOrEmpty()) {
            return
        }

        userIsNotMakingChanges = true
        val pulledDigit = nextEditText!!.text[0]
        editText.text.append(pulledDigit)
        if (editText.selectionEnd == editText.text.length) {
            editText.setSelection(editText.selectionEnd - 1)
        }
        nextEditText.text.delete(0, 1)
        nextEditText.setText(nextEditText.text.toString())
        pullDigitFromNextEditText(nextEditTextNum!!)
        userIsNotMakingChanges = false
    }

    private fun getEditText(editTextNum: Int): EditText {
        return when (editTextNum) {
            1 -> phone_edittext_1
            2 -> phone_edittext_2
            3 -> phone_edittext_3
            else -> error("invalid editTextNum")
        }
    }

    private fun getMaxLength(editTextNum: Int): Int {
        return when (editTextNum) {
            1 -> 2
            2 -> 3
            3 -> 4
            else -> error("invalid editTextNum")
        }
    }

    private fun getNextEditTextNum(editTextNum: Int): Int? {
        return when (editTextNum) {
            1 -> 2
            2 -> 3
            3 -> null
            else -> error("invalid editTextNum")
        }
    }

    private fun getNextEditText(editTextNum: Int): EditText? {
        return when (editTextNum) {
            1 -> phone_edittext_2
            2 -> phone_edittext_3
            3 -> null
            else -> error("invalid editTextNum")
        }
    }

    /*
     *      BUTTON SETUP
     */

    private fun setUpButtons() {
        setUpStage1CenterButton()
        setUpStage2LeftButton()
        setUpStage2RightButton()
        setUpStage3LeftButton()
        setUpStage3RightButton()
        setUpStage4CenterButton()
        setUpStage56CenterButton()
    }

    private fun setUpStage1CenterButton() {
        binding.stage1CenterButton.setOnClickListener {
            val phoneNumber =
                "5" + phone_edittext_1.text.toString() + phone_edittext_2.text.toString() + phone_edittext_3.text.toString()
            if (phoneNumber == "500") {         // FOR TEXTING ONLY
                viewModel.nukeData()
            }
            if (phoneNumberIsValid()) {
                viewModel.savePhoneNumber(phoneNumber)
                goToStage2()
            } else {
                insistOnPhoneNumber()
            }
        }
    }

    private fun phoneNumberIsValid(): Boolean {
        return phone_edittext_1.text.length == 2 && phone_edittext_2.text.length == 3 && phone_edittext_3.text.length == 4
    }

    private fun insistOnPhoneNumber() {
        val toast = Toast.makeText(context, "Please enter a valid phone number.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

    private fun setUpStage2LeftButton() {
        binding.stage2LeftButton.setOnClickListener {
            goToStage1()
        }
    }

    private fun setUpStage2RightButton() {
        binding.stage2RightButton.setOnClickListener {
            goToStage3()
        }
    }

    private fun setUpStage3LeftButton() {
        binding.stage3LeftButton.setOnClickListener {
            goToStage1()
        }
    }

    private fun setUpStage3RightButton() {
        binding.stage3RightButton.setOnClickListener {
            if (viewModel.codeIsValid(verification_code_edittext.text.toString())) {
                goTo(viewModel.selectStage())
            } else {
                insistOnCode()
            }
        }
    }

    private fun insistOnCode() {
        val toast = Toast.makeText(context, getString(R.string.please_enter_code), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

    private fun setUpStage4CenterButton() {
        binding.stage4CenterButton.setOnClickListener {
            if (username_edittext.text.toString() == "0000") {      // FOR TESTING ONLY
                viewModel.nukeData()
                goToStage1()
            } else {
                when {
                    username_edittext.text.length < 4 -> insistOnLongerUsername()
                    !viewModel.nameIsAvailable(username_edittext.text.toString()) -> insistOnAvailableUsername(
                        username_edittext.text.toString()
                    )
                    else -> goToStage5()
                }
            }
        }
    }

    private fun insistOnLongerUsername() {
        val toast = Toast.makeText(context, getString(R.string.choose_longer_username), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

    private fun insistOnAvailableUsername(username: String) {
        val toast = Toast.makeText(context, getString(R.string.sorry_username_taken, username), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

    private fun setUpStage56CenterButton() {
        binding.stage56CenterButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.authFragment) {
                findNavController().navigate(R.id.action_authFragment_to_orderFragment)
            }
        }
    }
}

