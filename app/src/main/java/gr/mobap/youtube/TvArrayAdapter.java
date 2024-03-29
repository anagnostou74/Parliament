/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gr.mobap.youtube;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.util.List;

import gr.mobap.R;

/**
 * A convenience class to make ListViews easier to use in the demo activities.
 */
public final class TvArrayAdapter extends ArrayAdapter<TvListViewItem> {

  private final LayoutInflater inflater;

  public TvArrayAdapter(Context context, int textViewResourceId, List<TvListViewItem> objects) {
    super(context, textViewResourceId, objects);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @NonNull
  @Override
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    if (view == null) {
      view = inflater.inflate(R.layout.item_youtube, null);
    }

    TextView textView = view.findViewById(R.id.list_item_text);
    textView.setText(getItem(position).getTitle());
    TextView disabledText = view.findViewById(R.id.list_item_disabled_text);
    disabledText.setText(getItem(position).getDisabledText());

    if (isEnabled(position)) {
      disabledText.setVisibility(View.INVISIBLE);
      textView.setTextColor(Color.BLACK);
    } else {
      disabledText.setVisibility(View.VISIBLE);
      textView.setTextColor(Color.GRAY);
    }

    return view;
  }

  @Override
  public boolean areAllItemsEnabled() {
    // have to return true here otherwise disabled items won't show a divider in the list.
    return true;
  }

  @Override
  public boolean isEnabled(int position) {
    return getItem(position).isEnabled();
  }

  public boolean anyDisabled() {
    for (int i = 0; i < getCount(); i++) {
      if (!isEnabled(i)) {
        return true;
      }
    }
    return false;
  }

}
