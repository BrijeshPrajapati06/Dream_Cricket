package com.diyantech.dreamcricket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diyantech.dreamcricket.AppDatabase
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.databinding.ActivityAddPlayerBinding
import com.diyantech.dreamcricket.lib.BitmapManager
import com.diyantech.dreamcricket.model.ModelPlayer
import com.diyantech.dreamcricket.player.PlayerDao
import kotlinx.coroutines.launch
import java.io.IOException


class AddPlayer : AppCompatActivity(R.layout.activity_add_player) {

    private lateinit var binding: ActivityAddPlayerBinding
    private var modelPlayer : ModelPlayer? = null
    var SelectPicture : Int = 200
    var appDatabase : AppDatabase? = null
    private var playerlist = listOf<ModelPlayer>()
    var playerDao : PlayerDao? = null
    var selectPlayerImgUri : Uri?=null
    var bitmapManager : BitmapManager? = null
    var bitmap: Bitmap? = null
    var context : Context? = null
    var lastUID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.red)

        setupListenersPlayer()
        
        modelPlayer = intent.getSerializableExtra("DataPlayer") as? ModelPlayer



        if (modelPlayer == null)binding.btnSaveOrUpdt.text = "Save Data Player"
        else {
            binding.btnSaveOrUpdt.text = "Update Player"
            binding.etTitlePlayer.setText(modelPlayer?.playername.toString())
            binding.autoCompleteTxtViewCountry.setText(modelPlayer?.teamname.toString())
            binding.autoSkills.setText(modelPlayer?.multiName.toString())
        }

        dropDownCountryTextView()
        dropDownSkills()


        binding.btnSaveOrUpdt.setOnClickListener {
            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show()
            savePlayerData()
        }

    }

    private fun setupListenersPlayer() {
        binding.etTitlePlayer.addTextChangedListener(TextFieldValidation(binding.etTitlePlayer))

    }

   inner class TextFieldValidation(private val view: View) : TextWatcher {
       override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       }

       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           // checking ids of each text field and applying functions accordingly.
           when (view.id) {
               R.id.et_title_player -> {
                   validPlayerName()
               }
               R.id.auto_complete_txtView_country -> {
                   validCountry()
               }
               R.id.autoSkills -> {
                   validSkills()
               }
           }
       }

       override fun afterTextChanged(s: Editable?) {
           binding.txtplayerName.error = null
           binding.txtplayerName.isErrorEnabled = false
           binding.txtSelectCountry.error = null
           binding.txtSelectCountry.isErrorEnabled = false
           binding.txtPlayerSkills.error = null
           binding.txtPlayerSkills.isErrorEnabled = false
       }

   }

    private fun validSkills() : Boolean{
        if (binding.autoSkills.text.toString().trim().isEmpty()) {
            binding.txtPlayerSkills.error = "Required Choose Player Title"
            binding.autoSkills.requestFocus()
            return false
        }else {
            binding.txtPlayerSkills.isErrorEnabled = false
        }
        return true
    }

    private fun savePlayerData() {
        var name = binding.etTitlePlayer.text.toString()
        var country = binding.autoCompleteTxtViewCountry.text.toString()
        var skills = binding.autoSkills.text.toString()
        var playerphoto =  selectPlayerImgUri


        if (validPlayerName() && validCountry() && validSkills()){
            lifecycleScope.launch{

                if (modelPlayer == null){
                    var modelPlayer = ModelPlayer(playername = name, teamname = country, multiName = skills, playerImg = playerphoto.toString())
//                appDatabase!!.getPlayerDao().addPlayerUser(modelPlayer)
                    AppDatabase.getInstance(this@AddPlayer).getPlayerDao().addPlayerUser(modelPlayer)
                    finish()
                }else{
                    var p = ModelPlayer(name,country,skills,playerphoto.toString())
                    p.id = modelPlayer?.id ?: 0
//                appDatabase!!.getPlayerDao().updatePlayerUser(p)
                    AppDatabase.getInstance(this@AddPlayer).getPlayerDao().updatePlayerUser(p)

                    finish()
                }

            }
        }
      
    }



    private fun validCountry(): Boolean {
        if (binding.autoCompleteTxtViewCountry.text.toString().trim().isEmpty()) {
            binding.txtSelectCountry.error = "Required Choose Player Title"
            binding.autoCompleteTxtViewCountry.requestFocus()
            return false
        }else {
            binding.txtSelectCountry.isErrorEnabled = false
        }
        return true
    }

    private fun validPlayerName(): Boolean {
        if (binding.etTitlePlayer.text.toString().trim().isEmpty()) {
            binding.txtplayerName.error = "Required Team Name"
            binding.etTitlePlayer.requestFocus()
            return false
        }else {
            binding.txtplayerName.isErrorEnabled = false
        }
        return true
    }


    private fun dropDownCountryTextView() {
        val item = arrayOf("India","China","Australia","Portugle","America","New Zealand")


        val adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, item)
        binding.autoCompleteTxtViewCountry.setAdapter(adapterItems)

        binding.autoCompleteTxtViewCountry.setOnItemClickListener(object :
            AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(this@AddPlayer, "item "+ item1, Toast.LENGTH_SHORT).show()
            }

        })
        binding.ivProfile.setOnClickListener {
            takeImage()
        }
    }

    private fun dropDownSkills() {
        val item = arrayOf("Wicket-Keepers","Batters","All-Rounders","Bowlers")

        val adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, item)
        binding.autoSkills.setAdapter(adapterItems)

        binding.autoSkills.setOnItemClickListener(object :
            AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(this@AddPlayer, "item "+ item1, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun takeImage() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SelectPicture)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (resultCode === RESULT_OK) {
//
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode === SelectPicture) {
//                // Get the url of the image from data
//                val selectedImageUri: Uri = data?.data!!
//                if (null != selectedImageUri) {
//                    // update the preview image in the layout
//                    binding.ivProfile.setImageURI(selectedImageUri)
//                }
//            }
//        }
        if(requestCode == SelectPicture && resultCode == RESULT_OK && data != null && data.data != null){
            selectPlayerImgUri = data.data
            try {
                var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectPlayerImgUri)
                binding.ivProfile.setImageBitmap(bitmap)
            }
            catch (e : IOException){
                e.printStackTrace()
            }
        }

    }


}