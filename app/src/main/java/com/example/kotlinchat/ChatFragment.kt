package com.example.kotlinchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinchat.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Any


class ChatFragment : Fragment() {
    private var _binding : FragmentChatBinding? = null
    private val binding get () = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    val chats = ArrayList<chats>()
    private lateinit var adapter : chatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         db = Firebase.firestore
        auth = FirebaseAuth.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        val view = binding.root

        adapter = chatAdapter()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter


        binding.sentButton.setOnClickListener {
            auth.currentUser?.let {
                val user = it.email
                val cahttext = binding.chatText.text.toString()
                val data = FieldValue.serverTimestamp()

                val dataPut = HashMap<String,kotlin.Any>()
                dataPut.put("text",cahttext)
                dataPut.put("user",user!!)
                dataPut.put("time",data)
                db.collection("Chats").add(dataPut).addOnSuccessListener {
                    // veri yuklendi
                    binding.chatText.setText("")
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                    binding.chatText.setText("")
                }
            }

        }

        db.collection("Chats").orderBy("time",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if (value.isEmpty){
                       Toast.makeText(requireContext(),"mesaj yok",Toast.LENGTH_LONG).show()
                    }else{
                        val snapshots = value
                        chats.clear()
                        for (snapshot in snapshots){
                            val text = snapshot.get("text") as String
                            val user = snapshot.get("user") as String
                            //val data = snapshot.get("time") as String
                            val currentChats = chats(user,text)
                            chats.add(currentChats)
                            adapter.chatList = chats
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }

        return  view
     }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}