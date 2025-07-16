import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sakhura.climaapp.api.RetrofitClient
import com.sakhura.climaapp.databinding.ActivityMainBinding
import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.repository.ClimaRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repo = ClimaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuscar.setOnClickListener {
            val ciudad = binding.etCiudad.text.toString()
            if (ciudad.isNotEmpty()) {
                obtenerClima(ciudad)
            }
        }

        binding.btnPronostico.setOnClickListener {
            val ciudad = binding.etCiudad.text.toString()
            val intent = Intent(this, PronosticoActivity::class.java)
            intent.putExtra("ciudad", ciudad)
            startActivity(intent)
        }
    }

    private fun obtenerClima(ciudad: String) {
        lifecycleScope.launch {
            try {
                val clima: ClimaResponse = repo.obtenerClima(ciudad)
                binding.tvResultado.text = "Ciudad: ${clima.name}\nTemp: ${clima.main.temp}°C\nDesc: ${clima.weather[0].description}"
            } catch (e: Exception) {
                binding.tvResultado.text = "Error: ${e.localizedMessage}"
            }
        }
    }
}