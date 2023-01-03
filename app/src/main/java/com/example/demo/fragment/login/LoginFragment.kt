package com.example.demo.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.demo.R
import com.example.demo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private var visible: Boolean = false
private lateinit var database: FirebaseDatabase
private lateinit var auth: FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        database = FirebaseDatabase.getInstance()
        auth = Firebase.auth
        _binding!!.loginButton.setOnClickListener { checkLogin() }
        _binding!!.cancelButton.setOnClickListener { goBack() }

        _binding!!.editTextPassword.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= _binding!!.editTextPassword.getRight() - _binding!!.editTextPassword.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    turnVisibility()
                    return@OnTouchListener true
                }
            }
            false
        })
        return view
    }

    private fun turnVisibility() {
        if (!visible) {
            _binding!!.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            _binding!!.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_on,
                0
            );
            visible = true;
        } else {
            _binding!!.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            _binding!!.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_off,
                0
            );
            visible = false;
        }
    }

    private fun checkLogin() {
        val email = _binding!!.editTextEmail.text.toString()
        val password = _binding!!.editTextPassword.text.toString()
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showErrorMsg("Complete los campos")
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        findNavController().navigate(R.id.goToHomeFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        showErrorMsg("Email y/o contraseña incorrectos")
                    }
                }
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.main_fragment)
    }

    private fun showErrorMsg(message: String) {
        _binding?.loginFailedTextView?.text = message
        _binding?.loginFailedTextView?.visibility = View.VISIBLE

        var fadeOutAnimationObject = AlphaAnimation(1f, 0f)
        fadeOutAnimationObject.setDuration(2000)
        Handler(Looper.getMainLooper()).postDelayed({
            _binding?.loginFailedTextView?.startAnimation(fadeOutAnimationObject)
            _binding?.loginFailedTextView?.visibility = View.INVISIBLE
        }, 1000)

    }
}