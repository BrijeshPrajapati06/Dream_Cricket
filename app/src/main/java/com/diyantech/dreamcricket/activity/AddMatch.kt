package com.diyantech.dreamcricket.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diyantech.dreamcricket.AppDatabase
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.databinding.ActivityAddMatchBinding
import com.diyantech.dreamcricket.model.ModelMatch
import com.diyantech.dreamcricket.model.ModelTeam
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddMatch : AppCompatActivity(R.layout.activity_add_match) {

    private lateinit var binding: ActivityAddMatchBinding
    private var modelMatch : ModelMatch? = null
    var appDatabase : AppDatabase? = null
    private var teamList = listOf<ModelTeam>()


    val cal : Calendar = Calendar.getInstance()
    var date: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.red)

        setupListeners()

        modelMatch = intent.getSerializableExtra("Data") as? ModelMatch

        if (modelMatch == null)binding.btnSaveOrUpdate.text = "Save Data"
        else {
            binding.btnSaveOrUpdate.text = "Update"
            binding.etTitle.setText(modelMatch?.title.toString())
            binding.etDate.setText(modelMatch?.date.toString())
            binding.etTime.setText(modelMatch?.time.toString())
            binding.autoCompleteTxtView.setText(modelMatch?.teamone.toString())
            binding.autoCompleteTxtViewTwo.setText(modelMatch?.teamtwo.toString())
        }

        dropDownTextView()
        dropDownTextViewTwo()



        binding.btnSaveOrUpdate.setOnClickListener {
            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show()
            saveData()
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }
        }
        // create an OnDateSetListener
        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                updateTimeInView()
            }
        }


        binding.etDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
               val a= DatePickerDialog(
                    this@AddMatch,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)

                )
                a.getDatePicker().setMinDate(Date().time)
                    a.show();
            }
        })

        binding.etTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                TimePickerDialog(this@AddMatch,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
               true).show()
            }
        })
    }

    private fun setupListeners() {
        binding.etTitle.addTextChangedListener(TextFieldValidation(binding.etTitle))
        binding.etDate.addTextChangedListener(TextFieldValidation(binding.etDate))
        binding.etTime.addTextChangedListener(TextFieldValidation(binding.etTime))
        binding.autoCompleteTxtView.addTextChangedListener(TextFieldValidation(binding.autoCompleteTxtView))
        binding.autoCompleteTxtViewTwo.addTextChangedListener(TextFieldValidation(binding.autoCompleteTxtViewTwo))

    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.et_title -> {
                    validateMatchTitle()
                }
                R.id.et_date -> {
                    validMatchDate()
                }
                R.id.et_time -> {
                    validMatchTime()
                }
                R.id.auto_complete_txtView -> {
                    validTeamOne()
                }
                R.id.auto_complete_txtView_two -> {
                    validTeamTwo()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            binding.txtMatchTitle.error = null
            binding.txtMatchTitle.isErrorEnabled = false
        }

    }


    private fun saveData() {



        var name = binding.etTitle.text.toString()
        var teamone = binding.autoCompleteTxtView.text.toString()
        var teamtwo = binding.autoCompleteTxtViewTwo.text.toString()
        var textDate = binding.etDate.text.toString()
        var textTime = binding.etTime.text.toString()


//        validateMatchTitle()
//        validMatchDate()
//        validMatchTime()
//        validTeamOne()
//        validTeamTwo()
        if(validateMatchTitle()&&validMatchDate()&&validMatchTime()&&validTeamOne()&&validTeamTwo()){
            lifecycleScope.launch{

                if (modelMatch == null){
                    var modelMatch = ModelMatch(date =textDate  , teamone = teamone, teamtwo = teamtwo, title =name, time = textTime)
//                AppDatabase(this@AddMatch).getMatchDao().addUser(modelMatch)
                    AppDatabase.getInstance(this@AddMatch).getMatchDao().addUser(modelMatch)
                    finish()
                }else{

                    var m = ModelMatch(name,teamone,teamtwo,textDate,textTime)
                    m.id = modelMatch?.id ?: 0
//                AppDatabase(this@AddMatch).getMatchDao().updateUser(m)
                    AppDatabase.getInstance(this@AddMatch).getMatchDao().updateUser(m)
                    finish()
                }

            }
        }

    }

    private fun dropDownTextViewTwo() {
        val item = arrayOf("GUJARAT","MUMBAI","SHRI LANKA","PAKISTAN","NEW ZEALAND","DELHI","HYDERABAD",
            "CHENNAI","KOLKATA","BANGLORE","RAJASTHAN","PUNJAB")

        val arrayList: ArrayList<String> = ArrayList()
        lifecycleScope.launch {
            teamList = AppDatabase.getInstance(this@AddMatch).getTeamDao().getAllTeam()
//

            for (i in 0 until teamList.size) {
                val objects: String = teamList.get(i).team
                arrayList.add(objects)
            }
        }

        val adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, arrayList as List<String?>)

        binding.autoCompleteTxtViewTwo.setAdapter(adapterItems)

        binding.autoCompleteTxtViewTwo.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(this@AddMatch, "item "+ item1, Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun dropDownTextView() {
        val item = arrayOf("GUJARAT","MUMBAI","SHRI LANKA","PAKISTAN","NEW ZEALAND","DELHI","HYDERABAD",
        "CHENNAI","KOLKATA","BANGLORE","RAJASTHAN","PUNJAB")

        val arrayList: ArrayList<String> = ArrayList()
        lifecycleScope.launch {
            teamList = AppDatabase.getInstance(this@AddMatch).getTeamDao().getAllTeam()
//

            for (i in 0 until teamList.size) {
                val objects: String = teamList.get(i).team
                arrayList.add(objects)
            }
        }


        val adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, arrayList as List<String?>)
            binding.autoCompleteTxtView.setAdapter(adapterItems)

        binding.autoCompleteTxtView.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(this@AddMatch, "item "+ item1, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val sdfdate =  sdf.format(cal.getTime())
        binding.etDate.setText(sdfdate)
    }
    private fun updateTimeInView() {
        val sdftime = SimpleDateFormat("hh:mm")
        try {
            date = sdftime.parse("07:00")
        } catch (e: ParseException) {
        }
        val time =  sdftime.format(cal.time)
        binding.etTime.setText(time)
    }
       private fun validTeamTwo(): Boolean {
        if (binding.autoCompleteTxtViewTwo.text.toString().trim().isEmpty()) {
            binding.autoCompleteTxtViewTwo.error = "Required Match Title"
            binding.autoCompleteTxtViewTwo.requestFocus()
            return false
        }else {
            binding.txtSelectTeamTwo.isErrorEnabled = false
        }
        return true
    }

    private fun validTeamOne(): Boolean {
        if (binding.autoCompleteTxtView.text.toString().trim().isEmpty()) {
            binding.autoCompleteTxtView.error = "Required Match Title"
            binding.autoCompleteTxtView.requestFocus()
            return false
        }else {
            binding.txtSelectTeamOne.isErrorEnabled = false
        }
        return true
    }

    private fun validMatchTime(): Boolean {
        if (binding.etTime.text.toString().trim().isEmpty()){
            binding.txtTime.error = "Please Select Date"
            binding.txtTime.requestFocus()
            return false
        }else{

        }
        return true
    } private fun validMatchDate(): Boolean {
        if (binding.etDate.text.toString().trim().isEmpty()){
            binding.txtDate.error = "Please Select Date"
            binding.txtDate.requestFocus()
            return false
        }else{

        }
        return true
    }

    private fun validateMatchTitle(): Boolean  {
        if (binding.etTitle.text.toString().trim().isEmpty()) {
            binding.txtMatchTitle.error = "Required Match Title"
            binding.etTitle.requestFocus()
            return false
        }else {
            binding.txtMatchTitle.isErrorEnabled = false
        }
        return true
    }
}