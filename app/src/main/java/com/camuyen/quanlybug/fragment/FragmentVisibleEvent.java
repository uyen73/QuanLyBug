package com.camuyen.quanlybug.fragment;

public class FragmentVisibleEvent {
    private String fragmentName;

    public FragmentVisibleEvent(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public FragmentVisibleEvent() {
    }

    public String getFragmentName() {
        return fragmentName;
    }
}
