package com.example.android.bookworm;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Books> {

    public BookAdapter(Activity context, ArrayList<Books> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }
        Books books = getItem(position);

        TextView ratingsView = listItemView.findViewById(R.id.rating_text_view);
        ratingsView.setText(formatDecimal(books.getRating()));

        TextView titleView = listItemView.findViewById(R.id.title_text_view);
        titleView.setText(books.getBookName());

        TextView authorsView = listItemView.findViewById(R.id.author_text_view);
        authorsView.setText("Authors   : " + books.getAuthorName());

        TextView publicationsView = listItemView.findViewById(R.id.publisher_text_view);
        publicationsView.setText("Publisher: " + books.getPublicatiions());

        TextView priceView = listItemView.findViewById(R.id.price_text_view);
        priceView.setText(NumberFormat.getCurrencyInstance().format(books.getPrice()));

        GradientDrawable ratingCircle = (GradientDrawable) ratingsView.getBackground();

        int ratingColor = getRatingColor(books.getRating());

        // Set the color on the magnitude circle
        ratingCircle.setColor(ratingColor);

        return listItemView;
    }

    private String formatDecimal(double Value) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(Value);
    }

    private int getRatingColor(Double rating) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(rating);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
