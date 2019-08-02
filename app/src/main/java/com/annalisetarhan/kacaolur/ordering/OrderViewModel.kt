package com.annalisetarhan.kacaolur.ordering

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.annalisetarhan.kacaolur.R

// I suppose I'm making the decision to pass application to my viewModels.
// I thought viewModels weren't supposed to know about context.
// But now it's okay because I'm not storing a reference, so not causing a memory leak.
// So eff the whole concept??
// Or is it okay because I'm passing application (context) not activity context?

class OrderViewModel(application: Application): AndroidViewModel(application) {

    fun setItemInfo(itemName: String, itemDescription: String) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        val editor = sharedPrefs.edit()
        editor.putString("item_name", itemName)
        editor.putString("item_description", itemDescription)
        editor.apply()
    }
}

