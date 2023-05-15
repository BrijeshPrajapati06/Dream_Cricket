package com.diyantech.dreamcricket.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diyantech.dreamcricket.AppDatabase
import com.diyantech.dreamcricket.activity.AddTeam
import com.diyantech.dreamcricket.adapter.TeamAdapter
import com.diyantech.dreamcricket.databinding.FragmentTeamBinding
import com.diyantech.dreamcricket.model.ModelTeam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamFragment : Fragment() {

    private var teamDataList = arrayListOf<ModelTeam>()
    private lateinit var binding : FragmentTeamBinding
    var appDatabase : AppDatabase?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.teamFloatBtn?.setOnClickListener {
            var intent  = Intent(requireContext(), AddTeam::class.java)
            startActivity(intent)
//            showAddMatchDialog()
        }

//        teamDataList = ArrayList()
//        teamDataList.add(ModelTeam("RCB","11"))
//        teamDataList.add(ModelTeam("RCB","11"))
//        teamDataList.add(ModelTeam("RCB","11"))
//        teamDataList.add(ModelTeam("RCB","11"))
//        teamDataList.add(ModelTeam("RCB","11"))
//        teamDataList.add(ModelTeam("RCB","11"))
//
//        val adapter = TeamAdapter(teamDataList)
//
//        binding.mRvView?.adapter = adapter
//
//        binding.mRvView?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userTeamList = AppDatabase.getInstance(requireContext()).getTeamDao().getAllTeam()

            binding.mRvView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TeamAdapter().apply {
                    setTeamData(userTeamList)
                    setOnActionEditTeamListner {
                        var intent  = Intent(requireContext(), AddTeam::class.java)
                        intent.putExtra("TeamData",it)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}