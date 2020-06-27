import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BluetoothDevicesAdapter(val deviceList: MutableList<BluetoothDevice>, val listener: View.OnClickListener):
    RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothDeviceViewHolder>() {
    inner class BluetoothDeviceViewHolder(val deviceNameTv: TextView): RecyclerView.ViewHolder(deviceNameTv) {
        init{
            /* Listener de click de cada célula associado individualmente */
            deviceNameTv.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        /* Infla uma célula e cria um ViewHolder para a nova célula */
        return BluetoothDeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            ) as TextView
        )
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        /* Usa o nome ou o MAC Address para colocar na lista do Adapter */
        holder.deviceNameTv.text = with(deviceList[position]) {name ?: address }

        /* Cada View guarda sua posição na lista para poder usar no listener de click */
        holder.deviceNameTv.tag = position
    }

    fun add(bluetoothDevice: BluetoothDevice) {
        /* Só adiciona se não existe na lista */
        if ( !deviceList.any {it.address == bluetoothDevice.address } ) {
            deviceList.add(bluetoothDevice)
            this.notifyDataSetChanged()
        }
    }

    fun clear() {
        deviceList.clear()
        this.notifyDataSetChanged()
    }
}