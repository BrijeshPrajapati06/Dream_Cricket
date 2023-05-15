package com.diyantech.dreamcricket.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.diyantech.dreamcricket.AppDatabase
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.databinding.ActivityAddTeamBinding
import com.diyantech.dreamcricket.model.ModelPlayer
import com.diyantech.dreamcricket.model.ModelTeam
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class AddTeam : AppCompatActivity(R.layout.activity_add_team) {

    private lateinit var binding: ActivityAddTeamBinding
    private var modelTeam: ModelTeam? = null
    private lateinit var PERMISSIONS: Array<String>
    var SelectPicture: Int = 200
    private var playerlist = listOf<ModelPlayer>()
    var appDatabase: AppDatabase? = null
    var selectImgUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.red)

        setupListenersTeam()

        PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        modelTeam = intent.getSerializableExtra("TeamData") as? ModelTeam

        if (modelTeam == null) binding.btnSaveOrUpdate.text = "Save Team Data"
        else {
            binding.btnSaveOrUpdate.text = "Update Team"
            binding.etName.setText(modelTeam?.team.toString())
            binding.autoCompletePlayerName.setText(modelTeam?.total.toString())

        }
        dropDownTextView()

        binding.btnSaveOrUpdate.setOnClickListener {
            Toast.makeText(this, "saveTeam", Toast.LENGTH_SHORT).show()
            saveTeamData()
        }

    }

    private fun setupListenersTeam() {
        binding.etName.addTextChangedListener(TextFieldValidation(binding.etName))
        binding.autoCompletePlayerName.addTextChangedListener(TextFieldValidation(binding.autoCompletePlayerName))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.et_Name -> {
                    validTeamName()
                }
                R.id.auto_complete_playerName -> {
                    validSelectPlayer()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            binding.txtTeamName.error = null
            binding.txtTeamName.isErrorEnabled = false
            binding.txtSelectPlayerName.error = null
            binding.txtSelectPlayerName.isErrorEnabled = false
        }

    }


    private fun saveTeamData() {
        var teamname = binding.etName.text.toString()
        var playername = binding.autoCompletePlayerName.text.toString()

        Glide.with(this)
            .load(modelTeam?.teamPhoto)
            .into(binding.circleProfile)


        if (validTeamName() && validSelectPlayer()) {
            lifecycleScope.launch {

                if (modelTeam == null) {
                    var modelTeam = ModelTeam(team = teamname, total = playername, teamPhoto = binding.circleProfile.toString())
//                appDatabase!!.getTeamDao().addTeamUser(modelTeam)
                    AppDatabase.getInstance(this@AddTeam).getTeamDao().addTeamUser(modelTeam)
                    finish()
                } else {
                    var t = ModelTeam(teamname, playername,binding.circleProfile.toString())
                    t.id = modelTeam?.id ?: 0
                    AppDatabase.getInstance(this@AddTeam).getTeamDao().updateTeamUser(t)
                    finish()
                }

            }
        }


    }


    private fun validSelectPlayer(): Boolean {
        if (binding.autoCompletePlayerName.text.toString().trim().isEmpty()) {
            binding.txtSelectPlayerName.error = "Required Choose Player Title"
            binding.autoCompletePlayerName.requestFocus()
            return false
        } else {
            binding.txtSelectPlayerName.isErrorEnabled = false
        }
        return true
    }

    private fun validTeamName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.txtTeamName.error = "Required Team Name"
            binding.etName.requestFocus()
            return false
        } else {
            binding.txtTeamName.isErrorEnabled = false
        }
        return true
    }


    private fun dropDownTextView() {
        val item = arrayOf(
            "Virat Kohli",
            "MS Dhoni",
            "Jadeja",
            "Rohit Sharma",
            "Sachin Tendulkar",
            "Arjun Tendulkar",
            "M Shami",
            "Gill",
            "Green",
            "Hardik Pandya",
            "W Shaha",
            "I Kishan",
            "S Yadav",
            "Rashid-Khan"
        )


//        playerList = appDatabase!!.getPlayerDao().getAllPlayer()
//
//        val arrayList: ArrayList<String> = ArrayList()
//        for (i in 0 until playerList!!.size) {
//            val objects: String = playerList!!.get(i).playername
//            arrayList.add(objects)
//        }
        val arrayList: ArrayList<String> = ArrayList()
        lifecycleScope.launch {
            playerlist = AppDatabase.getInstance(this@AddTeam).getPlayerDao().getAllPlayer()
//

            for (i in 0 until playerlist.size) {
                val objects: String = playerlist.get(i).playername
                arrayList.add(objects)
            }
        }



        Log.e("TAG", "dropDownTextView: " + arrayList as List<String?>)
        val adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, arrayList as List<String?>)
        binding.autoCompletePlayerName.setAdapter(adapterItems)

        binding.autoCompletePlayerName.setOnItemClickListener(object :
            AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                addChip(item1)
                Log.d("TAG", "onItemClick: " + item1)
            }

        })

        binding.igCircleEditProfile.setOnClickListener {
            selectImage()
        }
    }

    private fun addChip(item1: String) {
        val chip = Chip(this)
        chip.text = item1
        chip.closeIconTint?.getColorForState(FOCUSED_STATE_SET,getColor(R.color.red))
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(chip)
        }

        binding.chipGroup.addView(chip)
    }

//    private fun addChip(text : String) {
//        val chip = Chip(this)
//        chip.text = text
//
//        chip.isCloseIconVisible = true
//
//        chip.setOnCloseIconClickListener {
//            binding.chipGroup.removeView(chip)
//        }
//
//        binding.chipGroup.addView(chip)
//    }

    private fun selectImage() {
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
//                    binding.circleProfile.setImageURI(selectedImageUri)
//                }
//            }
//        }
        if (requestCode == SelectPicture && resultCode == RESULT_OK && data != null && data.data != null) {
            selectImgUri = data.data
            try {
                var bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, selectImgUri)
                binding.circleProfile.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

}

