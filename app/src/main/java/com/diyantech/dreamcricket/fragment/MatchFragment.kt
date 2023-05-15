package com.diyantech.dreamcricket.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diyantech.dreamcricket.AppDatabase
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.activity.AddMatch
import com.diyantech.dreamcricket.adapter.MatchAdapter
import com.diyantech.dreamcricket.databinding.FragmentMatchBinding
import com.diyantech.dreamcricket.model.ModelMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatchFragment : Fragment() {
    private var matchDataList = arrayListOf<ModelMatch>()
    var appDatabase : AppDatabase?=null
    private lateinit var binding : FragmentMatchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchBinding.inflate(inflater , container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.floatAddPlus?.setOnClickListener {
            var intent  = Intent(requireContext(), AddMatch::class.java)
            startActivity(intent)
//            showAddMatchDialog()
        }

//        matchDataList = ArrayList()
//        matchDataList.add(ModelMatch("World Cup", "RCB", "KOL", "07:30"))
//        matchDataList.add(ModelMatch("World Cup", "RCB", "KOL", "07:30"))
//        matchDataList.add(ModelMatch("World Cup", "RCB", "KOL", "07:30"))
//        matchDataList.add(ModelMatch("World Cup", "RCB", "KOL", "07:30"))
//        matchDataList.add(ModelMatch("World Cup", "RCB", "KOL", "07:30"))

//        val adapter = MatchAdapter(matchDataList)
//
//        binding.mRecycleview?.adapter = adapter
//
//        binding.mRecycleview?.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{
            val userList = AppDatabase.getInstance(requireContext()).getMatchDao().getAllMatch()
            Log.d(TAG, "onResume: "+ userList.size)

            binding.mRecycleview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = MatchAdapter().apply {
                    setData(userList)
                    setOnActionEditListner {
                        var intent  = Intent(requireContext(), AddMatch::class.java)
                        intent.putExtra("Data",it)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}