# 🌤️ App de Clima Android - Tutorial Completo

## 📱 Descripción
Una aplicación Android nativa desarrollada en Kotlin que permite consultar el clima actual y el pronóstico de 5 días para cualquier ciudad del mundo usando la API de OpenWeatherMap.

## ✨ Características
- 🌡️ Consulta clima actual (temperatura, descripción, humedad, viento)
- 📅 Pronóstico extendido de 5 días
- 🔍 Búsqueda por nombre de ciudad
- 🌐 Datos en tiempo real desde OpenWeatherMap API
- 📱 Interfaz moderna con Material Design

## 🛠️ Tecnologías Utilizadas
- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Arquitectura:** MVVM (Repository Pattern)
- **Networking:** Retrofit2 + Gson
- **UI:** ViewBinding + RecyclerView
- **Async:** Coroutines
- **API:** OpenWeatherMap

---

# 🚀 Tutorial Paso a Paso

## 📋 Prerrequisitos
- Android Studio instalado (versión más reciente)
- Conocimientos básicos de Kotlin/Android
- Conexión a Internet
- Cuenta en OpenWeatherMap (gratuita)

---

## 1️⃣ CREAR EL PROYECTO

### Paso 1: Nuevo proyecto en Android Studio
1. Abre Android Studio
2. `File > New > New Project`
3. Selecciona **"Empty Views Activity"**
4. Configuración:
   - **Name:** `climaapp`
   - **Package name:** `com.sakhura.climaapp`
   - **Language:** Kotlin
   - **Minimum SDK:** API 24 (Android 7.0)
5. Clic en **"Finish"**

---

## 2️⃣ CONFIGURAR DEPENDENCIAS

### Paso 2: Editar build.gradle.kts (Module: app)
Reemplaza el contenido de `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sakhura.climaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sakhura.climaapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit y JSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle y ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### Paso 3: Sincronizar proyecto
- Clic en **"Sync Now"** cuando aparezca la notificación

---

## 3️⃣ OBTENER API KEY

### Paso 4: Registrarse en OpenWeatherMap
1. Ve a [https://openweathermap.org/api](https://openweathermap.org/api)
2. Clic en **"Sign Up"**
3. Completa el registro con tu email
4. Confirma tu email
5. Inicia sesión
6. Ve a [https://home.openweathermap.org/api_keys](https://home.openweathermap.org/api_keys)
7. **Copia tu API Key** (algo como: `1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p`)

⏰ **Nota:** La API key puede tomar hasta 10 minutos en activarse.

---

## 4️⃣ CONFIGURAR PERMISOS

### Paso 5: AndroidManifest.xml
Reemplaza `app/src/main/AndroidManifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Permisos para Internet -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Climaapp"
        android:usesCleartextTraffic="true"
        tools:targetApi="31">
        
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <activity
            android:name=".PronosticoActivity"
            android:exported="false" />
            
    </application>
</manifest>
```

---

## 5️⃣ CREAR ESTRUCTURA DE CARPETAS

### Paso 6: Crear paquetes
En `app/src/main/java/com/sakhura/climaapp/`, crear las siguientes carpetas:
- `api/`
- `model/`
- `repository/`
- `adapter/`

---

## 6️⃣ CREAR MODELOS DE DATOS

### Paso 7: ClimaResponse.kt
Crear `app/src/main/java/com/sakhura/climaapp/model/ClimaResponse.kt`:

```kotlin
package com.sakhura.climaapp.model

data class ClimaResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(val temp: Double, val humidity: Int)
data class Weather(val description: String, val icon: String)
data class Wind(val speed: Double)
```

### Paso 8: PronosticoResponse.kt
Crear `app/src/main/java/com/sakhura/climaapp/model/PronosticoResponse.kt`:

```kotlin
package com.sakhura.climaapp.model

data class PronosticoResponse(
    val list: List<PronosticoDia>
)

data class PronosticoDia(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)
```

---

## 7️⃣ CONFIGURAR RETROFIT

### Paso 9: RetrofitClient.kt
Crear `app/src/main/java/com/sakhura/climaapp/api/RetrofitClient.kt`:

```kotlin
package com.sakhura.climaapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

### Paso 10: ClimaApiService.kt
Crear `app/src/main/java/com/sakhura/climaapp/api/ClimaApiService.kt`:

```kotlin
package com.sakhura.climaapp.api

import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.model.PronosticoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {

    @GET("weather")
    suspend fun obtenerClimaActual(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): ClimaResponse

    @GET("forecast")
    suspend fun obtenerPronostico(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): PronosticoResponse
}
```

---

## 8️⃣ CREAR REPOSITORIO

### Paso 11: ClimaRepository.kt
Crear `app/src/main/java/com/sakhura/climaapp/repository/ClimaRepository.kt`:

```kotlin
package com.sakhura.climaapp.repository

import com.sakhura.climaapp.api.ClimaApiService
import com.sakhura.climaapp.api.RetrofitClient
import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.model.PronosticoResponse

class ClimaRepository {
    private val api = RetrofitClient.instance.create(ClimaApiService::class.java)
    
    // 🔑 REEMPLAZA CON TU API KEY REAL
    private val apiKey = "TU_API_KEY_AQUI"

    suspend fun obtenerClima(ciudad: String): ClimaResponse {
        return try {
            if (apiKey == "TU_API_KEY_AQUI") {
                throw Exception("Por favor configura tu API key de OpenWeatherMap")
            }
            api.obtenerClimaActual(ciudad, apiKey)
        } catch (e: Exception) {
            throw Exception("Error al obtener el clima: ${e.message}")
        }
    }

    suspend fun obtenerPronostico(ciudad: String): PronosticoResponse {
        return try {
            if (apiKey == "TU_API_KEY_AQUI") {
                throw Exception("Por favor configura tu API key de OpenWeatherMap")
            }
            api.obtenerPronostico(ciudad, apiKey)
        } catch (e: Exception) {
            throw Exception("Error al obtener el pronóstico: ${e.message}")
        }
    }
}
```

**🚨 IMPORTANTE:** Reemplaza `"TU_API_KEY_AQUI"` con tu API key real de OpenWeatherMap.

---

## 9️⃣ CREAR LAYOUTS

### Paso 12: activity_main.xml
Reemplaza `app/src/main/res/layout/activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/layoutPrincipal"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".MainActivity">

    <EditText
        android:id="@+id/etCiudad"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Ingresa una ciudad"
        android:inputType="text" />

    <Button
        android:id="@+id/btnBuscar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Buscar Clima"
        android:layout_marginTop="8dp" />

    <TextView
        android:id="@+id/tvResultado"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Resultado aparecerá aquí"
        android:textSize="16sp"
        android:padding="12dp"
        android:layout_marginTop="8dp"
        android:background="#EEEEEE" />

    <Button
        android:id="@+id/btnPronostico"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Ver Pronóstico de 5 Días"
        android:layout_marginTop="8dp" />

</LinearLayout>
```

### Paso 13: activity_pronostico.xml
Crear `app/src/main/res/layout/activity_pronostico.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/layoutPronostico"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".PronosticoActivity">

    <TextView
        android:id="@+id/tvTituloPronostico"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Pronóstico de 5 días"
        android:textSize="20sp"
        android:textStyle="bold"
        android:layout_marginBottom="8dp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvPronostico"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

### Paso 14: item_dia_pronostico.xml
Crear `app/src/main/res/layout/item_dia_pronostico.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="12dp"
    android:background="@android:color/white"
    android:layout_marginBottom="8dp"
    android:elevation="2dp">

    <TextView
        android:id="@+id/tvFecha"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Fecha"
        android:textStyle="bold"
        android:textSize="16sp" />

    <TextView
        android:id="@+id/tvTemp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Temperatura" />

    <TextView
        android:id="@+id/tvDesc"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Descripción"
        android:textColor="@android:color/darker_gray" />

</LinearLayout>
```

---

## 🔟 CREAR ACTIVIDADES

### Paso 15: MainActivity.kt
Reemplaza `app/src/main/java/com/sakhura/climaapp/MainActivity.kt`:

```kotlin
package com.sakhura.climaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
            } else {
                binding.tvResultado.text = "Por favor ingresa una ciudad"
            }
        }

        binding.btnPronostico.setOnClickListener {
            val ciudad = binding.etCiudad.text.toString()
            if (ciudad.isNotEmpty()) {
                val intent = Intent(this, PronosticoActivity::class.java)
                intent.putExtra("ciudad", ciudad)
                startActivity(intent)
            } else {
                binding.tvResultado.text = "Por favor ingresa una ciudad"
            }
        }
    }

    private fun obtenerClima(ciudad: String) {
        binding.tvResultado.text = "Buscando..."
        
        lifecycleScope.launch {
            try {
                val clima: ClimaResponse = repo.obtenerClima(ciudad)
                binding.tvResultado.text = """
                    Ciudad: ${clima.name}
                    Temperatura: ${clima.main.temp}°C
                    Descripción: ${clima.weather[0].description}
                    Humedad: ${clima.main.humidity}%
                    Viento: ${clima.wind.speed} m/s
                """.trimIndent()
            } catch (e: Exception) {
                binding.tvResultado.text = "Error: ${e.localizedMessage}"
            }
        }
    }
}
```

### Paso 16: PronosticoActivity.kt
Crear `app/src/main/java/com/sakhura/climaapp/PronosticoActivity.kt`:

```kotlin
package com.sakhura.climaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakhura.climaapp.adapter.PronosticoAdapter
import com.sakhura.climaapp.databinding.ActivityPronosticoBinding
import com.sakhura.climaapp.repository.ClimaRepository
import kotlinx.coroutines.launch

class PronosticoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPronosticoBinding
    private val repo = ClimaRepository()
    private lateinit var adapter: PronosticoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPronosticoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ciudad = intent.getStringExtra("ciudad") ?: "Santiago"
        adapter = PronosticoAdapter()
        binding.rvPronostico.layoutManager = LinearLayoutManager(this)
        binding.rvPronostico.adapter = adapter

        lifecycleScope.launch {
            try {
                val pronostico = repo.obtenerPronostico(ciudad)
                adapter.actualizarLista(pronostico.list)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
```

---

## 1️⃣1️⃣ CREAR ADAPTER

### Paso 17: PronosticoAdapter.kt
Crear `app/src/main/java/com/sakhura/climaapp/adapter/PronosticoAdapter.kt`:

```kotlin
package com.sakhura.climaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.climaapp.R
import com.sakhura.climaapp.model.PronosticoDia

class PronosticoAdapter : RecyclerView.Adapter<PronosticoAdapter.ViewHolder>() {
    private val lista = mutableListOf<PronosticoDia>()

    fun actualizarLista(nueva: List<PronosticoDia>) {
        lista.clear()
        lista.addAll(nueva)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dia_pronostico, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PronosticoDia) {
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)
            val tvTemp = itemView.findViewById<TextView>(R.id.tvTemp)
            val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
            tvFecha.text = item.dt_txt
            tvTemp.text = "${item.main.temp}°C"
            tvDesc.text = item.weather[0].description
        }
    }
}
```

---

## 1️⃣2️⃣ CONFIGURAR API KEY

### Paso 18: Configurar tu API Key
En `ClimaRepository.kt`, encuentra esta línea:
```kotlin
private val apiKey = "TU_API_KEY_AQUI"
```

Y reemplázala con tu API key real:
```kotlin
private val apiKey = "1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p"
```

---

## 1️⃣3️⃣ COMPILAR Y PROBAR

### Paso 19: Limpiar y construir
1. `Build > Clean Project`
2. `Build > Rebuild Project`

### Paso 20: Ejecutar la app
1. Conecta un dispositivo Android o inicia un emulador
2. Clic en el botón **"Run"** (▶️)
3. Espera a que se instale la app

### Paso 21: Probar funcionalidades
1. **Probar clima actual:**
   - Ingresa una ciudad como "Santiago", "Madrid", "Buenos Aires"
   - Clic en "Buscar Clima"
   - Deberías ver temperatura, descripción, humedad y viento

2. **Probar pronóstico:**
   - Ingresa una ciudad
   - Clic en "Ver Pronóstico de 5 Días"
   - Deberías ver una lista con el pronóstico

---

## 🎯 FUNCIONALIDADES IMPLEMENTADAS

✅ **Clima actual:** Temperatura, descripción, humedad, viento  
✅ **Pronóstico extendido:** Lista de 5 días  
✅ **Búsqueda por ciudad:** Cualquier ciudad del mundo  
✅ **Interfaz intuitiva:** Material Design  
✅ **Manejo de errores:** Mensajes claros al usuario  
✅ **Navegación:** Entre pantallas  

---

## 🚨 PROBLEMAS COMUNES Y SOLUCIONES

### Error: "Por favor configura tu API key"
- ✅ Verifica que reemplazaste `"TU_API_KEY_AQUI"` con tu API key real
- ✅ La API key puede tomar hasta 10 minutos en activarse

### Error: Sin conexión a Internet
- ✅ Verifica permisos en AndroidManifest.xml
- ✅ Comprueba conexión a Internet del dispositivo

### Error: Ciudad no encontrada
- ✅ Prueba con nombres en inglés: "London", "Paris", "Tokyo"
- ✅ Verifica ortografía de la ciudad

### Error de compilación
- ✅ `Build > Clean Project`
- ✅ `Build > Rebuild Project`
- ✅ Verifica que todas las dependencias estén sincronizadas

---

## 🎊 ¡FELICITACIONES!

Has creado exitosamente una aplicación de clima funcional en Android. La app puede:
- Consultar clima actual de cualquier ciudad
- Mostrar pronóstico de 5 días
- Manejar errores de manera elegante
- Navegar entre pantallas

---

## 🔄 SIGUIENTES PASOS (Opcional)

### Funcionalidades adicionales que puedes implementar:
- 📍 **Geolocalización:** Detectar ubicación automáticamente
- 💾 **Base de datos:** Guardar ciudades favoritas
- 🎨 **Temas:** Modo oscuro/claro
- 🌡️ **Gráficos:** Mostrar tendencias de temperatura
- 🔔 **Notificaciones:** Alertas meteorológicas
- 🌍 **Mapas:** Visualización geográfica

---

## 📚 RECURSOS ADICIONALES

- [OpenWeatherMap API Documentation](https://openweathermap.org/api)
- [Android Developer Guide](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Retrofit Documentation](https://square.github.io/retrofit/)

---

**¡Tu app de clima está lista para usar! 🌟**
