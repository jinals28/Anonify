package com.example.anonifydemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

//    Context context;
//    int images[]={
//
//            R.drawable.image1,
//            R.drawable.image2,
//            R.drawable.image3,
//    };
//
//    public ViewpagerAdapter(Context context){
//        this.context = context;
//    }
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == (LinearLayout) object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);
//
//        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.titleImage);
//        TextView slideHeading = (TextView) view.findViewById(R.id.texttitle);
//        TextView slideDesciption = (TextView) view.findViewById(R.id.textdesciption);
//
//        slidetitleimage.setImageResource(image[position]);
//        slideHeading.setText(Headings[position]);
//        slideDesciption.setText(desciption[position]);
//
//        container.addView(view);
//
//        return view;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//
//        container.removeView((LinearLayout)object);
//    }
}
