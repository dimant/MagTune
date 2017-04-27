package com.dtodorov.androlib.eventdispatcher;

public interface IViewEventExtensions {
    String ENABLE_VIEW = "enableView";
    String DISABLE_VIEW = "disableView";
    String HIDE_VIEW = "hideView";
    String SHOW_VIEW = "showView";

    void register(IEventDispatcher eventDispatcher);
}
