package alserdar.fanos.sign_in

import alserdar.fanos.Home
import alserdar.fanos.Launcher
import alserdar.fanos.R
import alserdar.fanos.databinding.ActivitySiginBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SiginActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySiginBinding
    private lateinit var auth: FirebaseAuth
    private var RC_SIGN_IN = 2120
    private lateinit var googleSignInClient: GoogleSignInClient
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "fanos"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_sigin)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sigin)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.signWithGoogle.setOnClickListener { clickMe(it) }
    }

    fun clickMe(view :View?)
    {
        binding.apply {

            invalidateAll()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

            Toast.makeText(baseContext, "Clicked" , Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                Toast.makeText(baseContext, "sigin succes" , Toast.LENGTH_SHORT).show()
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {

                Toast.makeText(baseContext, "sigin failed" , Toast.LENGTH_SHORT).show()
                var launcher = Intent(this , Launcher::class.java)
                startActivity(launcher)
                finish()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "task succes" , Toast.LENGTH_SHORT).show()
                    val createUser = hashMapOf(
                        "userName" to user!!.displayName,
                        "email" to user.email,
                        "phoneNumber" to user.phoneNumber)


                    db.collection("UserInformation" ).document(user.uid)
                        .set(createUser)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
                            Toast.makeText(baseContext, "firebase succes and build user" , Toast.LENGTH_SHORT).show()

                            var launcher = Intent(this , Home::class.java)
                            startActivity(launcher)
                            finish()
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)
                            Toast.makeText(baseContext, "firebase failed" , Toast.LENGTH_SHORT).show()

                            var launcher = Intent(this , Launcher::class.java)
                            startActivity(launcher)
                            finish()
                        }

                } else {

                    Toast.makeText(baseContext, "task failed" , Toast.LENGTH_SHORT).show()

                    var launcher = Intent(this , Launcher::class.java)
                    startActivity(launcher)
                    finish()
                }
            }
    }
}
