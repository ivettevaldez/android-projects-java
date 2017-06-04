package com.silvia_valdez.hackathonapp.views.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.views.fragments.SelectMaterialsFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Silvia Valdez on 6/3/17.
 */

public class ProgramRideStepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION_KEY";


    public ProgramRideStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final SelectMaterialsFragment step = new SelectMaterialsFragment();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(R.string.program_ride_activity_title) //can be a CharSequence instead
                .create();
    }

}