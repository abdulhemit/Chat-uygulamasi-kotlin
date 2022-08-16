package com.example.kotlinchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kotlinchat.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
        auth.currentUser.let {
            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
          binding.SingButton.setOnClickListener {

              if(binding.EmailText.text == null && binding.PasswordText.text == null){
                  Toast.makeText(requireContext(),"inter user and password",Toast.LENGTH_LONG).show()
              }else{
                  auth.createUserWithEmailAndPassword(binding.EmailText.text.toString(),binding.PasswordText.text.toString()).addOnSuccessListener {
                      // kullanici kaydedildi
                      val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                      findNavController().navigate(action)
                      //Toast.makeText(requireContext(),"user saved",Toast.LENGTH_LONG).show()
                  }.addOnFailureListener {
                      // kullanici kaydedilirken hata olustu
                      Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                  }
              }


          }
        binding.loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.EmailText.text.toString(),binding.PasswordText.text.toString()).addOnSuccessListener {
                // kullanici giris
                val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                findNavController().navigate(action)
            }.addOnFailureListener {
            // kullanici girerken hata olustu
                Toast.makeText(requireContext(),it.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }
        }




        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}