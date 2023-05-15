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
import com.diyantech.dreamcricket.activity.AddPlayer
import com.diyantech.dreamcricket.adapter.PlayerAdapter
import com.diyantech.dreamcricket.databinding.FragmentPlayerBinding
import com.diyantech.dreamcricket.model.ModelPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerFragment : Fragment() {

    private var playerDataList = arrayListOf<ModelPlayer>()
    private lateinit var binding : FragmentPlayerBinding
    var appDatabase : AppDatabase?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater , container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerFloatBtn?.setOnClickListener {
            var intent  = Intent(requireContext(), AddPlayer::class.java)
            startActivity(intent)
//            showAddMatchDialog()
        }

//        playerDataList = ArrayList()
//        playerDataList.add(ModelPlayer("MS Dhoni","Chennai","Batsman"))
//        playerDataList.add(ModelPlayer("MS Dhoni","Chennai","Batsman"))
//        playerDataList.add(ModelPlayer("MS Dhoni","Chennai","Batsman"))
//        playerDataList.add(ModelPlayer("MS Dhoni","Chennai","Batsman"))
//        playerDataList.add(ModelPlayer("MS Dhoni","Chennai","Batsman"))
//
//
//        val adapter = PlayerAdapter(playerDataList)
//
//        binding.mplayerRvView?.adapter = adapter
//
//        binding.mplayerRvView?.layoutManager = LinearLayoutManager(requireContext())

    }
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{

            val userPlayerList = AppDatabase.getInstance(requireContext()).getPlayerDao().getAllPlayer()


            binding.mplayerRvView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PlayerAdapter().apply {
                    setPlayerData(userPlayerList)
                    setOnActionEditPlayerListner {
                        var intent  = Intent(requireContext(), AddPlayer::class.java)
                        intent.putExtra("DataPlayer",it)
                        startActivity(intent)
                    }
                }
            }
        }
    }

}
