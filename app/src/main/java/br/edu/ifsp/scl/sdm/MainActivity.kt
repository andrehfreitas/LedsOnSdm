package br.edu.ifsp.scl.sdm

import BluetoothDevicesAdapter
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.BluetoothConstants.BLUETOOTH_DEVICE_EXTRA
import br.edu.ifsp.scl.sdm.BluetoothConstants.BLUETOOTH_ENABLE_REQUEST_CODE
import br.edu.ifsp.scl.sdm.BluetoothConstants.BLUETOOTH_PERMISSION_REQUEST_CODE
import br.edu.ifsp.scl.sdm.receiver.BluetoothActionReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import splitties.alertdialog.alertDialog
import splitties.alertdialog.cancelButton
import splitties.alertdialog.message
import splitties.alertdialog.okButton
import splitties.toast.toast

class MainActivity : AppCompatActivity() {
    /* 1. Adaptador bt (dispositivo físico) */
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    // Adapter para RecyclerView
    private lateinit var bluetoothDevicesAdapter: BluetoothDevicesAdapter

    private lateinit var bluetoothActionReceiver: BluetoothActionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarTb)
        supportActionBar?.subtitle = getString(R.string.selecione_o_dispositivo)

        /*
        Listener para itens do devicesRv
        */
        val listener = View.OnClickListener {
            bluetoothAdapter?.cancelDiscovery()
            val posicao = it.tag as Int
            val dispositivoescolhido = bluetoothDevicesAdapter.deviceList[posicao]

            val intentLeds = Intent(this, LedsActivity::class.java)
            intentLeds.putExtra(BLUETOOTH_DEVICE_EXTRA, dispositivoescolhido)
            startActivity(intentLeds)
        }

        /*
        Configurando devicesRv
        */
        devicesRv.layoutManager = LinearLayoutManager(this)
        bluetoothDevicesAdapter = BluetoothDevicesAdapter(mutableListOf(), listener)
        devicesRv.adapter = bluetoothDevicesAdapter


        /* 2. Verificando existência de um adaptador bt */
        if (bluetoothAdapter == null) {
            toast(getString(R.string.bluetooth_adapter_needed))
            finish()
        }

        /* 3. Verificando ativação do adaptador bt */
        verificarAtivacaoAdaptadorBluetooth()

        /* 4. Verificando permissões de bt */
        verificarPermissoesBluetooth()

        /*
        Observando eventos bt
        */

        /*
        Busca lista de dispositivos pareados, se lista vazia ou dispositivo
        fora da lista dispara a descoberta
        */
        val pairedBluetoothDevicesSet = bluetoothAdapter?.bondedDevices
        if (pairedBluetoothDevicesSet == null || pairedBluetoothDevicesSet.isEmpty()){
            // DISPARAR A BUSCA DE DISPOSITIVOS PRÓXIMOS
        } else {
            pairedBluetoothDevicesSet.forEach {
                bluetoothDevicesAdapter.add(it)
                visibilidadeViews(false)
            }
        }
    }

    /* 3.1. Requisitando ativação do adaptador bt */
    private fun verificarAtivacaoAdaptadorBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BLUETOOTH_ENABLE_REQUEST_CODE)
        }
    }

    /* 3.2. Tratando retorno de permissões bt */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            grantResults.forEach { resultado ->
                if (resultado != PackageManager.PERMISSION_GRANTED) {
                    alertDialog {
                        message = getString(R.string.bluetooth_permission_needed)
                        okButton {
                            verificarPermissoesBluetooth()
                        }
                        cancelButton {
                            toast(getString(R.string.impossible_to_continue))
                            finish()
                        }
                    }.show()
                }
            }
        }
    }

    /* 4.1. Requisitando permissões bt */
    private fun verificarPermissoesBluetooth() {
        /* Antes do Marshmallow (23), as permissões eram concedidas na instalação */
        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION // Mesma observação feita no AndroidManifest.xml
                    ),
                    BLUETOOTH_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refreshMi -> {
                /*
                Buscar dispositivos bt
                 */
                return true
            }
        }
        return false
    }

    /* Liga/desliga de views para busca de dispositivos bt */
    private fun visibilidadeViews(buscando: Boolean) {
        if (buscando) {
            progressBar.visibility = View.VISIBLE
            devicesRv.visibility = View.GONE
        }
        else {
            progressBar.visibility = View.GONE
            devicesRv.visibility = View.VISIBLE
        }
    }
}
