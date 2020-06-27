package br.edu.ifsp.scl.sdm.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData

class BluetoothActionReceiver: BroadcastReceiver() {
    /* Livedata para quando um dispositivo bt novo for descoberto */
    val foundBluetoothDevice: MutableLiveData<BluetoothDevice> = MutableLiveData()

    /* Livedata para quando processo de descoberta terminar */
    val bluetoothDiscoveryFinished: MutableLiveData<Boolean> = MutableLiveData()
    init {
        bluetoothDiscoveryFinished.value = false // iniciado com valor false
    }

    /* Chamada por callback quando uma action bt de interesse for lançada pelo Android */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (BluetoothDevice.ACTION_FOUND == intent?.action) {
            /* Recupera dispositivo bt encontrado */
            foundBluetoothDevice.value = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        else {
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == intent?.action) {
                bluetoothDiscoveryFinished.value = true
            }
        }
    }
}