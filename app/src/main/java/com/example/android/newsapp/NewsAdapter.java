package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class NewsAdapter extends ArrayAdapter<News> {

    //Return a formatted date string from a date object.
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    //Return a formatted timestamp string from a date object.
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Find the current news object at the given position
        News currentNews = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = convertView.findViewById(R.id.layout_list_title);
        // Find the TextView with view ID section_name
        TextView sectionNameView = convertView.findViewById(R.id.layout_list_section);
        // Find the TextView with view ID author_name
        TextView authorNameView = convertView.findViewById(R.id.layout_list_author);

        // Display the title of the current news in that TextView
        titleView.setText(currentNews.getTitle());
        // Display the section name of the current news in that TextView
        sectionNameView.setText(currentNews.getSectionName());
        // Display the author name of the current news in that TextView
        if (currentNews.getAuthorName() != "") {
            authorNameView.setText(currentNews.getAuthorName());

            //Set the author name view as visible
            authorNameView.setVisibility(View.VISIBLE);
        } else {
            //Set the author name view as invisible (we want it to take up layout space)
            authorNameView.setVisibility(View.INVISIBLE);
        }

        // Set the publication date if the data is available, otherwise make the view invisible
        TextView dateView = null;
        if (currentNews.getPublicationDate() != null) {
            dateView = convertView.findViewById(R.id.layout_list_date);
            // Format the Date to a String
            String formattedDate = formatDate(currentNews.getPublicationDate());
            // Display the date of the current earthquake in that TextView
            dateView.setText(formattedDate);

            //Set date & time views as visible
            dateView.setVisibility(View.VISIBLE);
        } else {
            //Set date & time views as invisible
            dateView.setVisibility(View.INVISIBLE);
        }

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }
}