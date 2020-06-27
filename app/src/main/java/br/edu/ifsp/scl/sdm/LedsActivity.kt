package br.edu.ifsp.scl.sdm

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import br.edu.ifsp.scl.sdm.BluetoothConstants.BLUETOOTH_DEVICE_EXTRA
import br.edu.ifsp.scl.sdm.LedsActivity.Cor.AMARELO
import br.edu.ifsp.scl.sdm.LedsActivity.Cor.INDEFINIDO
import br.edu.ifsp.scl.sdm.LedsActivity.Cor.VERDE
import br.edu.ifsp.scl.sdm.LedsActivity.Cor.VERMELHO

class LedsActivity : AppCompatActivity() {

    private object Cor {
        const val INDEFINIDO = -1
        const val AMARELO    = 0
        const val VERMELHO   = 1
        const val VERDE      = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leds)

        /* Recuperando dispositivo bt da intent */
        intent.getParcelableExtra<BluetoothDevice>(BLUETOOTH_DEVICE_EXTRA)?.let {
            /*
            Instanciando o ViewModel
             */
        }
    }

    fun onClickSwitch(view: View) {
        val led = when (view.id) {
            R.id.ledAmareloSw -> AMARELO
            R.id.ledVermelhoSw -> VERMELHO
            R.id.ledVerdeSw -> VERDE
            else -> INDEFINIDO
        }

        /*
        Envia comando bt para o ViewModel
         */
    }
}
