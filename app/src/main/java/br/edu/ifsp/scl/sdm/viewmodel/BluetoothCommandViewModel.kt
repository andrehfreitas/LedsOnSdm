package br.edu.ifsp.scl.sdm.viewmodel

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.ViewModel
import br.edu.ifsp.scl.sdm.BluetoothConstants.BLUETOOTH_SERVICE_UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class BluetoothCommandViewModel(val dispositivoBt: BluetoothDevice): ViewModel() {
    /* Socket Bluetooth com o dispositivo escolhido */
    private lateinit var bluetoothSocket: BluetoothSocket

    /* OutputStream obtido a partir do Socket para envio dos comandos */
    private lateinit var bluetoothOutputStream: DataOutputStream

    fun enviarComandoBluetooth(led: Int, ligado: Boolean) {
        val comando = preparaComando(led, ligado)

        /* Envia comando para dispositivo Bluetooth */
        GlobalScope.launch (Dispatchers.IO) {
            if (!::bluetoothSocket.isInitialized) {
                bluetoothSocket = dispositivoBt.createInsecureRfcommSocketToServiceRecord(
                    UUID.fromString(BLUETOOTH_SERVICE_UUID))
            }
            try {
                if (!bluetoothSocket.isConnected) {
                    bluetoothSocket.connect()
                    bluetoothOutputStream = DataOutputStream(bluetoothSocket.outputStream)
                }

                bluetoothOutputStream.write(comando.toByteArray())
                bluetoothOutputStream.flush()
            }
            catch (ioe: IOException) {
                ioe.printStackTrace()
                fechar()
            }
        }
    }

    private fun fechar() {
        /* Fechando Socket OutputStream */
        try {
            if (::bluetoothOutputStream.isInitialized) {
                bluetoothOutputStream.close()
            }
            if (::bluetoothSocket.isInitialized) {
                bluetoothSocket.close()
            }
        }
        catch(ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    /* Cria uma String do tipo Lx-Sy onde x e y representam o número do led e o estado
       respectivamente */
    private fun preparaComando(led: Int, ligado: Boolean): String {
        val estado = if (ligado) 1 else 0
        return "L${led}-S${estado}"
    }
}