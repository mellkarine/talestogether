package com.example.talestogether

package com.example.talestogether

import android.app.Application
import com.example.talestogether.data.AppContainer
import com.example.talestogether.data.AppDataContainer

// Minha classe Application para inicializar o DB (Singleton)
class TalesTogetherApplication : Application() {
    // Declaro o contêiner que será acessado pelas ViewModels.
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializo a instância única do meu DB e Repositório aqui.
        container = AppDataContainer(this)
    }
}
// CRÍTICO: Sua dupla precisa adicionar android:name=".TalesTogetherApplication" no AndroidManifest.xml.