package com.example.threads

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.model.User
import com.example.threads.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    //initialising the firebase user
    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)

                //firstly we have to get data, then we can store it in sharedPref
                getData(auth.currentUser!!.uid, context)

            } else {
                _error.postValue(it.exception!!.message)
            }
        }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                SharedPref.storeData(
                    userData!!.email,
                    userData!!.name,
                    userData!!.bio,
                    userData!!.userName,
                    userData!!.imgUrl,
                    context
                )

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener {
                //Log.d("utk", "FAILURE: ${it.message}")
                _error.postValue(it.message)
            }
            .addOnSuccessListener {
                //Log.d("utk", it.user?.email.toString())
                //Log.d("utk", auth.currentUser?.email.toString())

                _firebaseUser.postValue(auth.currentUser)
                saveImage(
                    email,
                    password,
                    name,
                    bio,
                    userName,
                    imageUri,
                    auth.currentUser?.uid,
                    context
                )
            }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {

            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email, password, name, bio, userName, it.toString(), uid, context)
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imgUrl: String,
        uid: String?,
        context: Context
    ) {
        val userData = User(email, password, name, bio, userName, imgUrl, uid!!)

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                //if user is successfully registered, then store their data in shared preferences
                SharedPref.storeData(email, name, bio, userName, imgUrl, context)

            }.addOnFailureListener {

            }
    }

    fun userLogOut() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }

}