package com.vivekpanchal.newshub.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.adapters.UserInterestsRecyclerViewAdapter;
import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersInterestsActivity extends AppCompatActivity {

    @BindView(R.id.tv_choice_count_act_user_interests)
    TextView choiceCount;
    @BindView(R.id.tv_selected_choices_act_user_interests)
    TextView selectedChoices;

    @BindView(R.id.btn_next_act_user_interests)
    Button nextButton;

    @BindView(R.id.rv_interest_choices_act_user_interests)
    RecyclerView userInterestChoices;


    private UserInterestsRecyclerViewAdapter adapter;
    private ArrayList<String> userChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_interests);
        ButterKnife.bind(this);

        setUpViews();
        if (savedInstanceState != null) {
            userChoices = savedInstanceState.getStringArrayList(Constants.USER_CHOICES_KEY);
            choiceCount.setText(savedInstanceState.getString(Constants.CHOICE_COUNT_TEXT_VIEW_KEY));
            selectedChoices.setText(savedInstanceState.getString(Constants.USER_INTERESTS_TEXT_VIEW_KEY));
            userInterestChoices.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(Constants.RECYCLER_VIEW_STATE_KEY));
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpViews() {

        // Initializing users choices
        userChoices = new ArrayList<>();
        adapter = new UserInterestsRecyclerViewAdapter(this, Constants.userInterests, userChoices);
        userInterestChoices.setAdapter(adapter);
        userInterestChoices.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setOnItemClickListener(new UserInterestsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (userChoices.contains(Constants.userInterests.get(position).getName())) {
                    userChoices.remove(Constants.userInterests.get(position).getName());
                } else if (userChoices.size() < 3) {
                    userChoices.add(Constants.userInterests.get(position).getName());
                } else {
                    Toast.makeText(UsersInterestsActivity.this, R.string.userIntrestError,
                            Toast.LENGTH_SHORT).show();
                }
                StringBuilder choicesString = new StringBuilder();
                for (String choice : userChoices) {
                    choicesString.append(choice);
                    choicesString.append("   ");
                }
                if (userChoices.size() > 0)
                    selectedChoices.setText(choicesString.toString());
                else
                    selectedChoices.setText(getResources().getString(R.string.splash_screen_no_choices_selected));
                choiceCount.setText(String.valueOf(3 - userChoices.size()) + getString(R.string.numOfChoices));
                adapter.notifyItemChanged(position);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userChoices.size() == 3) {
                    Utility.setUserInterests(UsersInterestsActivity.this, userChoices);
                    Intent newsDisplayActivityIntent = new Intent(UsersInterestsActivity.this,
                            NewsDisplayActivity.class);
                    startActivity(newsDisplayActivityIntent);
                } else {
                    Toast.makeText(UsersInterestsActivity.this, R.string.enter_Intrests, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(Constants.USER_CHOICES_KEY, userChoices);
        outState.putString(Constants.CHOICE_COUNT_TEXT_VIEW_KEY, choiceCount.getText().toString());
        outState.putString(Constants.USER_INTERESTS_TEXT_VIEW_KEY, selectedChoices.getText().toString());
        outState.putParcelable(Constants.RECYCLER_VIEW_STATE_KEY, userInterestChoices.getLayoutManager().onSaveInstanceState());
    }
}
