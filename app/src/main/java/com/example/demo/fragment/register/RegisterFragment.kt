package com.example.demo.fragment.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.demo.R
import com.example.demo.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private var visible: Boolean = false
private lateinit var databaseReference: DatabaseReference
private lateinit var database: FirebaseDatabase
private lateinit var auth: FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseReference = database.reference.child("Users")

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        _binding!!.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.main_fragment)
        }

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

        _binding!!.editTextPassword2.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= _binding!!.editTextPassword2.getRight() - _binding!!.editTextPassword2.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    turnVisibility()
                    return@OnTouchListener true
                }
            }
            false
        })

        _binding!!.registerButton.setOnClickListener { checkSamePassword() }

        return view
    }

    private fun checkSamePassword() {
        val pass1 = _binding!!.editTextPassword.text.toString()
        val pass2 = _binding!!.editTextPassword2.text.toString()
        if (pass1 != pass2) {
            showErrorMsg()
        } else {
            createNewAccount()
        }
    }

    private fun showErrorMsg() {
        _binding?.loginFailedTextView?.visibility = View.VISIBLE

        var fadeOutAnimationObject = AlphaAnimation(1f, 0f)
        fadeOutAnimationObject.setDuration(2000)
        Handler(Looper.getMainLooper()).postDelayed({
            _binding?.loginFailedTextView?.startAnimation(fadeOutAnimationObject)
            _binding?.loginFailedTextView?.visibility = View.INVISIBLE
        }, 1000)

    }

    private fun turnVisibility() {
        if (!visible) {
            _binding!!.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
            _binding!!.editTextPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());

            _binding!!.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_on,
                0
            )
            _binding!!.editTextPassword2.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_on,
                0
            )
            visible = true;
        } else {
            _binding!!.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            _binding!!.editTextPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

            _binding!!.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_off,
                0
            )
            _binding!!.editTextPassword2.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.visible_off,
                0
            )
            visible = false;
        }
    }

    private fun createNewAccount() {
        val firstName = _binding!!.editTextFirstName.text.toString()
        val lastName = _binding!!.editTextLastName.text.toString()
        val email = _binding!!.editTextEmail.text.toString()
        val password = _binding!!.editTextPassword.text.toString()

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
        ) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.main_fragment)
                        Toast.makeText(
                            activity, "Registro exitoso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            activity, "Error en la autenticación.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } else {
            Toast.makeText(activity, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyEmail(user: FirebaseUser) {
        activity?.let {
            user.sendEmailVerification()
                .addOnCompleteListener(it) {
                    //Verificamos que la tarea se realizó correctamente
                        task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            it,
                            "Email " + user.getEmail(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            it,
                            "Error al verificar el correo ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}







