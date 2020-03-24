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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.Application
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.AuthFragmentBinding
import javax.inject.Inject

/*
 *  I'm really conflicted about the strategy here. It seems like far too much for a single fragment, but breaking
 *  it up into six different ones seems redundant and wasteful.
 */

class AuthFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: AuthFragmentBinding

    private var currentStage = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)
        Application.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)

        setUpPhoneEditTexts()
        setUpButtons()

        val currentStage = viewModel.selectStage()
        goTo(currentStage)

        return binding.root
    }

    /*
     *      GO TO STAGE X
     */

    private fun goTo(stage: Int) {
        when (stage) {
            1 -> goToStage1()
            2 -> goToStage2()
            3 -> goToStage3()
            4 -> goToStage4()
            5 -> goToStage5()
            6 -> goToStage6()
        }
    }

    private fun goToStage1() {
        currentStage = 1
        hideEverything()

        showPhoneNumber()
        enablePhoneEditTexts()

        binding.primaryTextBlock.visibility = View.VISIBLE
        binding.primaryTextBlock.setText(R.string.enter_phone_number)

        binding.stage1CenterButton.visibility = View.VISIBLE
    }

    private fun goToStage2() {
        currentStage = 2
        hideEverything()
        showPhoneNumber()
        disablePhoneEditTexts()

        binding.primaryTextBlock.visibility = View.VISIBLE
        binding.secondaryTextBlock.visibility = View.VISIBLE
        binding.primaryTextBlock.setText(R.string.enter_phone_number)
        binding.secondaryTextBlock.setText(R.string.may_we_send_sms)

        binding.stage2LeftButton.visibility = View.VISIBLE
        binding.stage2RightButton.visibility = View.VISIBLE
    }

    private fun goToStage3() {
        currentStage = 3
        hideEverything()

        binding.secondaryTextBlock.visibility = View.VISIBLE
        binding.secondaryTextBlock.setText(R.string.code_is_coming)
        binding.verificationCodeEdittext.visibility = View.VISIBLE

        binding.stage3LeftButton.visibility = View.VISIBLE
        binding.stage3RightButton.visibility = View.VISIBLE
    }

    private fun goToStage4() {
        currentStage = 4
        hideEverything()

        binding.secondaryTextBlock.visibility = View.VISIBLE
        binding.secondaryTextBlock.setText(R.string.choose_display_name)
        binding.usernameEdittext.visibility = View.VISIBLE
        binding.verificationCodeEdittext.visibility = View.INVISIBLE      // This is to keep the secondary_block in the right place

        binding.stage4CenterButton.visibility = View.VISIBLE
    }

    private fun goToStage5() {
        currentStage = 5
        hideEverything()

        binding.secondaryTextBlock.visibility = View.VISIBLE
        binding.secondaryTextBlock.text = getString(R.string.nice_to_meet_you, viewModel.username)

        binding.stage56CenterButton.visibility = View.VISIBLE
        viewModel.userIsLoggedIn()
    }

    private fun goToStage6() {
        currentStage = 6
        hideEverything()

        binding.secondaryTextBlock.visibility = View.VISIBLE
        binding.secondaryTextBlock.text = getString(R.string.welcome_back, viewModel.username)

        binding.stage56CenterButton.visibility = View.VISIBLE
        viewModel.userIsLoggedIn()
    }

    private fun hideEverything() {
        binding.phoneTextview1.visibility = View.GONE
        binding.phoneTextview2.visibility = View.GONE
        binding.phoneTextview3.visibility = View.GONE
        binding.phoneEdittext1.visibility = View.GONE
        binding.phoneEdittext2.visibility = View.GONE
        binding.phoneEdittext3.visibility = View.GONE

        binding.primaryTextBlock.visibility = View.GONE
        binding.secondaryTextBlock.visibility = View.GONE

        binding.usernameEdittext.visibility = View.GONE
        binding.verificationCodeEdittext.visibility = View.GONE

        binding.stage1CenterButton.visibility = View.GONE
        binding.stage2LeftButton.visibility = View.GONE
        binding.stage2RightButton.visibility = View.GONE
        binding.stage3LeftButton.visibility = View.GONE
        binding.stage3RightButton.visibility = View.GONE
        binding.stage4CenterButton.visibility = View.GONE
        binding.stage56CenterButton.visibility = View.GONE
    }

    private fun showPhoneNumber() {
        binding.phoneTextview1.visibility = View.VISIBLE
        binding.phoneTextview2.visibility = View.VISIBLE
        binding.phoneTextview3.visibility = View.VISIBLE
        binding.phoneEdittext1.visibility = View.VISIBLE
        binding.phoneEdittext2.visibility = View.VISIBLE
        binding.phoneEdittext3.visibility = View.VISIBLE
    }


    private fun enablePhoneEditTexts() {
        binding.phoneEdittext1.isEnabled = true
        binding.phoneEdittext2.isEnabled = true
        binding.phoneEdittext3.isEnabled = true
    }

    private fun disablePhoneEditTexts() {
        binding.phoneEdittext1.isEnabled = false
        binding.phoneEdittext2.isEnabled = false
        binding.phoneEdittext3.isEnabled = false
        binding.phoneEdittext1.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))        // Risky? How do I know the context won't be null? Because it has to be attached to an activity for a user to click the button?
        binding.phoneEdittext2.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.phoneEdittext3.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
    }

    /*
     *      PHONE EDIT TEXT SETUP
     */

    private var digitDeletedFlag = false
    private var userIsNotMakingChanges = false
    private var prevEditText1CursorPosition: Int? = null
    private var prevEditText2CursorPosition: Int? = null

    private fun setUpPhoneEditTexts() {
        binding.phoneEdittext1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (binding.phoneEdittext1.text.length == 3) {
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
                prevEditText1CursorPosition = binding.phoneEdittext1.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.phoneEdittext2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (binding.phoneEdittext2.selectionEnd == 0) {
                    moveCursorBackTo(1)
                }
                if (binding.phoneEdittext2.text.length == 4) {
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
                prevEditText2CursorPosition = binding.phoneEdittext2.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.phoneEdittext3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (userIsNotMakingChanges) {
                    return
                }
                if (binding.phoneEdittext3.selectionEnd == 0) {
                    moveCursorBackTo(2)
                }
                if (binding.phoneEdittext3.text.length == 5) {
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
            binding.phoneEdittext2.requestFocus()
            binding.phoneEdittext2.setSelection(prevEditText1CursorPosition!! - 2)
        } else if (editTextNum == 2 && prevEditText2CursorPosition!! > 2) {
            binding.phoneEdittext3.requestFocus()
            binding.phoneEdittext3.setSelection(prevEditText2CursorPosition!! - 3)
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
            1 -> binding.phoneEdittext1
            2 -> binding.phoneEdittext2
            3 -> binding.phoneEdittext3
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
            1 -> binding.phoneEdittext2
            2 -> binding.phoneEdittext3
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
                "5" + binding.phoneEdittext1.text.toString() + binding.phoneEdittext2.text.toString() + binding.phoneEdittext3.text.toString()
            if (phoneNumber == "500") {         // FOR TESTING ONLY
                viewModel.nukeData()
                if (it.findNavController().currentDestination?.id == R.id.authFragment) {
                    findNavController().navigate(R.id.action_authFragment_to_welcomeFragment)
                }
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
        return binding.phoneEdittext1.text.length == 2 && binding.phoneEdittext2.text.length == 3 && binding.phoneEdittext3.text.length == 4
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
            if (viewModel.codeIsValid(binding.verificationCodeEdittext.text.toString())) {
                viewModel.validatePhoneNumber()
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
            val username = binding.usernameEdittext.text.toString().trim()
            // Testing-only code starts here
            if (username == "0000") {
                viewModel.nukeData()
                binding.usernameEdittext.text.clear()
                if (it.findNavController().currentDestination?.id == R.id.authFragment) {
                    findNavController().navigate(R.id.action_authFragment_to_welcomeFragment)
                }
            } else {
                // Testing-only code ends here
                when {
                    username.length < 4 -> insistOnLongerUsername()
                    !viewModel.nameIsAvailable(username) -> insistOnAvailableUsername(username)
                    else -> {
                        viewModel.saveUsername(username)
                        goToStage5()
                    }
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

