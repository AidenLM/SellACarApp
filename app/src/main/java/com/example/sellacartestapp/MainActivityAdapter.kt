package com.example.sellacartestapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellacartestapp.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class MainActivityAdapter (val carList: ArrayList<Car>, private val onItemClickListener: (Car) -> Unit) : RecyclerView.Adapter<MainActivityAdapter.CarHolder>() {

    class CarHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
       val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CarHolder(binding)
    }

    override fun getItemCount(): Int {

        return carList.size
    }

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.binding.brandText.text = carList[position].brand
        holder.binding.modelText.text = carList[position].model
        holder.binding.yearText.text = carList[position].year.toString()
        holder.binding.kilometerText.text = carList[position].kilometer.toString()
        holder.binding.priceText.text = carList[position].price.toString()
        holder.binding.titleText.text = carList[position].comment
        holder.binding.locationText.text = carList[position].location


        Picasso.get()
            .load(carList.get(position).downloadUrl)
            .resize(800, 800)
            .centerCrop()
            .into(holder.binding.recyclerViewImageView)


        holder.itemView.setOnClickListener {
            onItemClickListener(carList[position])
        }
    }


}