package com.dtodorov.androlib.eventdispatcher;

import android.app.Activity;
import android.view.View;

public class ViewEventExtensions {
    public static final String ENABLE_VIEW = "enableView";
    public static final String DISABLE_VIEW = "disableView";
    public static final String HIDE_VIEW = "hideView";
    public static final String SHOW_VIEW = "showView";

    private Activity _activity;

    public ViewEventExtensions(Activity activity)
    {
        _activity = activity;
    }

    public  void register(IEventDispatcher eventDispatcher)
    {
        eventDispatcher.register(ViewEventExtensions.HIDE_VIEW, new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = _activity.findViewById(id.intValue());
                view.setVisibility(View.GONE);
            }
        });

        eventDispatcher.register(ViewEventExtensions.SHOW_VIEW, new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = _activity.findViewById(id.intValue());
                view.setVisibility(View.VISIBLE);
            }
        });

        eventDispatcher.register(ViewEventExtensions.ENABLE_VIEW, new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = _activity.findViewById(id.intValue());
                view.setEnabled(true);
            }
        });

        eventDispatcher.register(ViewEventExtensions.DISABLE_VIEW, new IEventListener() {
            @Override
            public void callback(Object param) {
                Integer id = (Integer) param;
                View view = _activity.findViewById(id.intValue());
                view.setEnabled(false);
            }
        });
    }
}
