package br.edu.ifsp.scl.sdm

object BluetoothConstants {
    const val BLUETOOTH_ENABLE_REQUEST_CODE = 0
    const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1
    const val BLUETOOTH_DEVICE_EXTRA = "BLUETOOTH_DEVICE_EXTRA"
    /* Depende do dispositivo com o qual você pretende se conectar. Cada um possui um UUID
       diferente. */
    const val BLUETOOTH_SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
}