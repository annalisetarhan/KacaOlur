package com.annalisetarhan.kacaolur.ordering

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.OrderFragmentBinding
import kotlinx.android.synthetic.main.order_fragment.*
import java.io.File
import java.io.IOException

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: OrderFragmentBinding

    // TODO: Investigate this
    val PERMISSION_REQUEST_CODE = 101
    val REQUEST_IMAGE_CAPTURE = 101
    val REQUEST_TAKE_PHOTO = 101

    private lateinit var photoPath: String
    private lateinit var photoUri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel.orderSubmitted) {
            sterilizeFragment()
        } else {
            setUpNormalFragment()
        }
    }

    private fun sterilizeFragment() {
        setItemName()
        setItemDescription()
        addPicture()
        binding.attachPictureButton.visibility = View.GONE
        setUpSterileKacaButton()
    }

    private fun setItemName() {
        val itemName = viewModel.getItemName()
        binding.itemNameEditText.isEnabled = false
        binding.itemNameEditText.hint = itemName
    }

    private fun setItemDescription() {
        val itemDescription = viewModel.getItemDescription()
        binding.itemDescriptionEditText.isEnabled = false
        binding.itemDescriptionEditText.hint = itemDescription
    }

    private fun setUpSterileKacaButton() {
        binding.kacaButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.orderFragment) {
                findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
            }
        }
    }

    private fun setUpNormalFragment() {
        setUpAttachPictureButton()      // Consider disabling this for phones that don't have a camera (do those exist?)
        setUpKacaButton()
        addPicture()
    }

    private fun addPicture() {
        if (viewModel.hasPicture) {
            val photoUri = viewModel.getPhotoUri()
            imageView.setImageURI(photoUri)
        }
    }

    /*
     *      ATTACH PICTURE BUTTON
     */

    private fun setUpAttachPictureButton() {
        binding.attachPictureButton.setOnClickListener {
            tryToGetPicture()
        }
    }

    private fun tryToGetPicture() {
        if (checkPermissions()) {
            takePicture()
        } else {
            requestPermission()
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(context!!, CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context!!, READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE, CAMERA), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
                }
                takePicture()
            }
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile = try {
                    createImageFile()
                } catch (e: IOException) {
                    println("An error occurred while taking photo")
                    null
                }
                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        context!!, "com.annalisetarhan.kacaolur.fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "item_picture", ".jpg", storageDir
        ).apply {
            photoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(photoUri)
            binding.attachPictureButton.text = getString(R.string.retake_picture)
            viewModel.savePhoto(photoUri)
        }
    }


    /*
     *      KACA BUTTON
     */

    private fun setUpKacaButton() {
        binding.kacaButton.setOnClickListener {
            val itemName = item_name_edit_text.text.toString().trim()
            val itemDescription = item_description_edit_text.text.toString().trim()
            val order = Order(itemName, itemDescription)

            if (itemName == "") {
                insistOnItemName()
            } else if (itemName == "0000") {        // FOR TESTING ONLY
                viewModel.nukeData()
                findNavController().navigate(R.id.action_orderFragment_to_welcomeFragment)
            } else {
                viewModel.acceptOrder(order)
                if (it.findNavController().currentDestination?.id == R.id.orderFragment) {
                    findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
                }
            }
        }
    }

    private fun insistOnItemName() {
        val toast = Toast.makeText(context, R.string.what_do_you_need, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }
}
