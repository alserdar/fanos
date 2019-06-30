package alserdar.fanos

import alserdar.fanos.databinding.ActivityHomeBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil


class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

    }
}
