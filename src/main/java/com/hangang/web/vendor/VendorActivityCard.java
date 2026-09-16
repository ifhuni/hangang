package com.hangang.web.vendor;

import com.hangang.web.activity.Activity;

public class VendorActivityCard {

    private final Activity activity;
    private final int pendingCount;
    private final int approvedCount;

    public VendorActivityCard(Activity activity, int pendingCount, int approvedCount) {
        this.activity = activity;
        this.pendingCount = pendingCount;
        this.approvedCount = approvedCount;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public int getApprovedCount() {
        return approvedCount;
    }
}
