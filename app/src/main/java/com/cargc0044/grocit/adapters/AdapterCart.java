package com.cargc0044.grocit.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.fragment.CartListFragment;
import com.cargc0044.grocit.util.CustomButtonListener;
import com.cargc0044.grocit.util.TotalPriceCallback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by nguza Yikona on 4/10/2016.
 */
public class AdapterCart extends BaseAdapter {

    //public ArrayList<HashMap<String, String>> listQuantity;
    public ArrayList<Integer> quantity = new ArrayList<Integer>();
    CartListFragment cartListFragment;
    TypedArray images;
    CustomButtonListener customButtonListener;
    //Declare Price Format
    DecimalFormat formatData = new DecimalFormat("#.##");
    private ArrayList<Integer> prodID;
    private ArrayList<String> orderName;
    private ArrayList<Integer> Qty;
    private ArrayList<Double> price;
    private ArrayList<String> image;
    private ArrayList<Double> Total;
    private String[] listViewItems,prices;
    private double Total_Price;
    private TotalPriceCallback TotalCallback;
    private TextView TotalCost;
    private Context context;

    public AdapterCart(Context context, TotalPriceCallback mCallback, ArrayList<Integer> prodID,ArrayList<String> orderName, ArrayList<Double> sub_price, ArrayList<String> image, double Total) {
        //super(context, R.layout.list_row);
        this.context = context;
        this.prodID = prodID;
        this.orderName = orderName;
        this.price = sub_price;
       // this.Qty = quantity;
        this.image = image;
        this.Total_Price = Total;
        TotalCallback = mCallback;


        for(int i =0; i< orderName.size();i++ )
        {
            quantity.add(1);
            //quantity[i]=0;
        }
    }

    public void setCustomButtonListener(CustomButtonListener customButtonListner)
    {
        this.customButtonListener = customButtonListner;
    }

    @Override
    public int getCount() {
        return orderName.size();
    }

    @Override
    public String getItem(int position) {
        return orderName.get(position);
    }


    @Override
    public long getItemId(int position) {
        return prodID.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ListViewHolder listViewHolder;
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            listViewHolder = new ListViewHolder();
            listViewHolder.subTotal = (TextView) convertView.findViewById(R.id.subTotal);
            listViewHolder.orderName = (TextView) convertView.findViewById(R.id.from_name);
            listViewHolder.Price = (TextView) convertView.findViewById(R.id.plist_price_text);
            listViewHolder.ImgThumb = (ImageView) convertView.findViewById(R.id.list_image);
            listViewHolder.minus = (ImageView) convertView.findViewById(R.id.cart_minus_img);
            listViewHolder.add = (ImageView) convertView.findViewById(R.id.cart_plus_img);
            listViewHolder.displayQty = (TextView) convertView.findViewById(R.id.cart_product_quantity_tv);
            TotalCost = (TextView) convertView.findViewById(R.id.TotalPrice);

            convertView.setTag(listViewHolder);


        }
        else
        {
            row=convertView;
            listViewHolder= (ListViewHolder) row.getTag();
        }
       // Total_Price = Double.parseDouble(formatData.format(Total_Price));
        //TotalCost.setText("Hi People");

        listViewHolder.orderName.setText(orderName.get(position));
        Picasso.with(context)
                .load(image.get(position))
                .placeholder(R.drawable.img_banner_placeholder)
                .fit()
                .into(listViewHolder.ImgThumb);

        Double thePrice = price.get(position);

        listViewHolder.Price.setText("Rs. " + thePrice.toString());
        Log.d("Check", quantity.get(position).toString());
        try{

            listViewHolder.displayQty.setText(quantity.get(position).toString());

            double SubPrice = (quantity.get(position)) * thePrice;


            TotalCallback.onTotal(Total_Price);


            //TotalCost.setText("Hi People");
            SubPrice = Double.parseDouble(formatData.format(SubPrice));
            listViewHolder.subTotal.setText(Double.toString(SubPrice));

        }catch(Exception e){
            e.printStackTrace();
        }

        listViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity.set(position, (quantity.get(position) + 1));
                listViewHolder.displayQty.setText(quantity.get(position).toString());
                Double thePrice = price.get(position);
                Double SubPrices = (quantity.get(position)) * thePrice;
                Total_Price = SubPrices;
                TotalCallback.onTotal(Total_Price);

                SubPrices = Double.parseDouble(formatData.format(SubPrices));
                listViewHolder.subTotal.setText(Double.toString(SubPrices));




            }

        });
        //listViewHolder.displayQty.setText("0");
        listViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(quantity.get(position)>0){
                        quantity.set(position,(quantity.get(position) - 1));
                        listViewHolder.displayQty.setText(quantity.get(position).toString());

                        Double thePrice = price.get(position);
                        double SubPricez = (quantity.get(position)) * thePrice;
                        Total_Price = SubPricez;
                        TotalCallback.onTotal(Total_Price);


                        SubPricez = Double.parseDouble(formatData.format(SubPricez));
                        listViewHolder.subTotal.setText(Double.toString(SubPricez));





                    }}

        });


        return convertView;
    }

    private class ListViewHolder {
        public TextView subTotal;
        public TextView orderName;
        public TextView Price;
        public ImageView ImgThumb;
        public ImageView minus;
        public ImageView add;
        public TextView displayQty;

    }


}

