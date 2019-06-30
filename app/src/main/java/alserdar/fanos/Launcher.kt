package alserdar.fanos

import alserdar.fanos.sign_in.SiginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

class Launcher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        Handler().postDelayed(
            {
                val yeah:Boolean =  FirebaseAuth.getInstance().currentUser != null

                if (yeah)
                {
                    val i = Intent(this , Home::class.java)
                    startActivity(i)
                }else
                {
                    val i = Intent(this , SiginActivity::class.java)
                    startActivity(i)
                }


            },2000
        )
    }
}
