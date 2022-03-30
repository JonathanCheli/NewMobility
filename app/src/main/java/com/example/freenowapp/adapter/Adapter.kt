package com.example.freenowapp.adapter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freenowapp.R
import com.example.freenowapp.databinding.ListItemVehiclesBinding
import com.example.freenowapp.model.Poi
import java.util.*


class Adapter(context: Context, private val poiList: List<Poi>, private var clickListener: ItemClickListener)
    : RecyclerView.Adapter<Adapter.VehicleVH>(){

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var binding: ListItemVehiclesBinding
    inner class VehicleVH(itemView: ListItemVehiclesBinding): RecyclerView.ViewHolder(itemView.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter.VehicleVH {

        binding = ListItemVehiclesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleVH(binding)
    }

    override fun onBindViewHolder(holder: Adapter.VehicleVH, position: Int) {

        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_up)

        try {
            val item = poiList[position]
            val addresses: List<Address> = geocoder.getFromLocation(
                item.coordinate.latitude,
                item.coordinate.longitude,
                1
            )

            if (addresses != null) {
                val city: String = addresses[0].locality
                val state: String = addresses[0].adminArea
                val knownName: String = addresses[0].getAddressLine(0)   /*featureName*/
                holder.itemView.apply {
                    "$city $state \n $knownName".also { binding.loc.text = it }
                    binding.vehicleName.text = item.fleetType

                    if (item.fleetType == "TAXI") {
                        Glide.with(binding.vehicleImage.context)
                            .load("")
                            .centerCrop()
                            .placeholder(R.drawable.taxi)
                            .into(binding.vehicleImage)
                    } else {
                        Glide.with(binding.vehicleImage.context)
                            .load("")
                            .centerCrop()
                            .placeholder(R.drawable.pooling)
                            .into(binding.vehicleImage)
                    }

                }
                binding.mainCl.setOnClickListener {
                    clickListener.onCardClick(item)
                }
            }else{
                Log.w("My Current location", "No Address returned!")
            }
        } catch (e : Exception ) {
            e.printStackTrace();
            Log.w("My Current location", "Cannot get Address!");
        }

        
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        // return poiList.size
        if (poiList != null)
            return poiList.size
        else
            return 0;
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
interface ItemClickListener{
    fun onCardClick(poi: Poi)
}