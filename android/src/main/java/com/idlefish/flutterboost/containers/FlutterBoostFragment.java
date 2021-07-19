package com.idlefish.flutterboost.containers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.FlutterBoostUtils;
import com.idlefish.flutterboost.Messages;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.android.RenderMode;
import io.flutter.embedding.android.TransparencyMode;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.platform.PlatformPlugin;

import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.ACTIVITY_RESULT_KEY;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_UNIQUE_ID;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_URL;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_URL_PARAM;

public class FlutterBoostFragment extends FlutterFragment implements FlutterViewContainer {
    private static final String TAG = "FlutterBoostFragment";
    private static final boolean DEBUG = false;
    private final String who = UUID.randomUUID().toString();
    private FlutterView flutterView;
    private PlatformPlugin platformPlugin;

    // @Override
    public void detachFromFlutterEngine() {
        /**
         * Override and do nothing.
         *
         * The idea here is to avoid releasing delegate when
         * a new FlutterFragment is attached in Flutter2.0.
         */
        if (DEBUG) Log.d(TAG, "#detachFromFlutterEngine: " + this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (DEBUG) Log.d(TAG, "#onAttach: " + this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FlutterBoost.instance().getPlugin().onContainerCreated(this);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        flutterView = FlutterBoostUtils.findFlutterView(view);
        assert(flutterView != null);
        // Detach FlutterView from engine before |onResume|.
        flutterView.detachFromFlutterEngine();
        if (DEBUG) Log.d(TAG, "#onCreateView: " + flutterView + ", " + this);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        assert(flutterView != null);
        if (hidden) {
            didFragmentHide();
        } else {
            didFragmentShow();
        }
        super.onHiddenChanged(hidden);
        if (DEBUG) Log.d(TAG, "#onHiddenChanged: hidden="  + hidden + ", " + this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        assert(flutterView != null);
        if (isVisibleToUser) {
            didFragmentShow();
        } else {
            didFragmentHide();
        }
        super.setUserVisibleHint(isVisibleToUser);
        if (DEBUG) Log.d(TAG, "#setUserVisibleHint: isVisibleToUser="  + isVisibleToUser + ", " + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            didFragmentShow();
            getFlutterEngine().getLifecycleChannel().appIsResumed();
        }
        if (DEBUG) Log.d(TAG, "#onResume: " + this);
    }

    @Override
    public RenderMode getRenderMode() {
        // Default to |FlutterTextureView|.
        return RenderMode.texture;
    }

    @Override
    public void onPause() {
        super.onPause();
        didFragmentHide();
        getFlutterEngine().getLifecycleChannel().appIsResumed();
        if (DEBUG) Log.d(TAG, "#onPause: " + this);
    }

    @Override
    public void onStop() {
        super.onStop();
        assert(getFlutterEngine() != null);
        getFlutterEngine().getLifecycleChannel().appIsResumed();
        if (DEBUG) Log.d(TAG, "#onStop: " + this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FlutterBoost.instance().getPlugin().onContainerDestroyed(this);
        if (DEBUG) Log.d(TAG, "#onDestroyView: " + this);
    }

    @Override
    public void onDetach() {
        FlutterEngine engine = getFlutterEngine();
        super.onDetach();
        assert(engine != null);
        engine.getLifecycleChannel().appIsResumed();
        if (DEBUG) Log.d(TAG, "#onDetach: " + this);
    }

    @Override
    // This method is called right before the activity's onPause() callback.
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (DEBUG) Log.d(TAG, "#onUserLeaveHint: " + this);
    }

    @Override
    public void onBackPressed() {
        // Intercept the user's press of the back key.
        FlutterBoost.instance().getPlugin().popRoute(null, (Messages.FlutterRouterApi.Reply<Void>) null);
        if (DEBUG) Log.d(TAG, "#onBackPressed: " + this);
    }

    @Override
    public boolean shouldRestoreAndSaveState() {
      if (getArguments().containsKey(ARG_ENABLE_STATE_RESTORATION)) {
        return getArguments().getBoolean(ARG_ENABLE_STATE_RESTORATION);
      }
      // Defaults to |true|.
      return true;
    }

    @Override
    public PlatformPlugin providePlatformPlugin(Activity activity, FlutterEngine flutterEngine) {
        return null;
    }

    @Override
    public boolean shouldDestroyEngineWithHost() {
        // The |FlutterEngine| should outlive this FlutterFragment.
        return false;
    }

    @Override
    public Activity getContextActivity() {
        return getActivity();
    }

    @Override
    public void finishContainer(Map<String, Object> result) {
        if (result != null) {
            Intent intent = new Intent();
            intent.putExtra(ACTIVITY_RESULT_KEY, new HashMap<String, Object>(result));
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
        getActivity().finish();
        if (DEBUG) Log.d(TAG, "#finishContainer: " + this);
    }

    @Override
    public String getUrl() {
        if (!getArguments().containsKey(EXTRA_URL)) {
            throw new RuntimeException("Oops! The fragment url are *MISSED*! You should "
                    + "override the |getUrl|, or set url via CachedEngineFragmentBuilder.");
        }
        return getArguments().getString(EXTRA_URL);
    }

    @Override
    public Map<String, Object> getUrlParams() {
        return (HashMap<String, Object>)getArguments().getSerializable(EXTRA_URL_PARAM);
    }

    @Override
    public String getUniqueId() {
        return getArguments().getString(EXTRA_UNIQUE_ID, this.who);
    }

    @Override
    public String getCachedEngineId() {
      return FlutterBoost.ENGINE_ID;
    }

    private void didFragmentShow() {
        // try to detach prevous container from the engine.
        FlutterViewContainer top = FlutterContainerManager.instance().getTopContainer();
        if (top != null && top != this) top.detachFromEngineIfNeeded();

        FlutterBoost.instance().getPlugin().onContainerAppeared(this);
        performAttach();
        if (DEBUG) Log.d(TAG, "#didFragmentShow: " + this);
    }

    private void didFragmentHide() {
        FlutterBoost.instance().getPlugin().onContainerDisappeared(this);
        // We defer |performDetach| call to new Flutter container's |onResume|;
        // performDetach();
        if (DEBUG) Log.d(TAG, "#didFragmentHide: " + this);
    }

    private void performAttach() {
        if (platformPlugin == null) {
            platformPlugin = new PlatformPlugin(getActivity(), getFlutterEngine().getPlatformChannel());
            // Attach plugins to the activity.
            getFlutterEngine().getActivityControlSurface().attachToActivity(getActivity(), getLifecycle());
            // Attach rendering pipeline.
            flutterView.attachToFlutterEngine(getFlutterEngine());
            if (DEBUG) Log.d(TAG, "#performAttach: " + this);
        }
    }

    private void performDetach() {
        if (platformPlugin != null) {
            // Plugins are no longer attached to the activity.
            getFlutterEngine().getActivityControlSurface().detachFromActivity();
            // Release Flutter's control of UI such as system chrome.
            platformPlugin.destroy();
            platformPlugin = null;
            // Detach rendering pipeline.
            flutterView.detachFromFlutterEngine();
            if (DEBUG) Log.d(TAG, "#performDetach: " + this);
        }
    }

    @Override
    public void detachFromEngineIfNeeded() {
        performDetach();
    }

    public static class CachedEngineFragmentBuilder {
        private final Class<? extends FlutterBoostFragment> fragmentClass;
        private boolean destroyEngineWithFragment = false;
        private RenderMode renderMode = RenderMode.surface;
        private TransparencyMode transparencyMode = TransparencyMode.transparent;
        private boolean shouldAttachEngineToActivity = true;
        private String url = "/";
        private HashMap<String, Object> params;
        private String uniqueId;

        public CachedEngineFragmentBuilder() {
            this(FlutterBoostFragment.class);
        }

        public CachedEngineFragmentBuilder(Class<? extends FlutterBoostFragment> subclass) {
            fragmentClass = subclass;
        }

        public CachedEngineFragmentBuilder url(String url) {
            this.url = url;
            return this;
        }

        public CachedEngineFragmentBuilder urlParams(Map<String, Object> params) {
            this.params = (params instanceof HashMap) ? (HashMap)params : new HashMap<String, Object>(params);
            return this;
        }

        public CachedEngineFragmentBuilder uniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public CachedEngineFragmentBuilder destroyEngineWithFragment(
                boolean destroyEngineWithFragment) {
            this.destroyEngineWithFragment = destroyEngineWithFragment;
            return this;
        }

        public CachedEngineFragmentBuilder renderMode( RenderMode renderMode) {
            this.renderMode = renderMode;
            return this;
        }

        public CachedEngineFragmentBuilder transparencyMode(
                 TransparencyMode transparencyMode) {
            this.transparencyMode = transparencyMode;
            return this;
        }

        public CachedEngineFragmentBuilder shouldAttachEngineToActivity(
                boolean shouldAttachEngineToActivity) {
            this.shouldAttachEngineToActivity = shouldAttachEngineToActivity;
            return this;
        }

        /**
         * Creates a {@link Bundle} of arguments that are assigned to the new {@code FlutterFragment}.
         *
         * <p>Subclasses should override this method to add new properties to the {@link Bundle}.
         * Subclasses must call through to the super method to collect all existing property values.
         */
        protected Bundle createArgs() {
            Bundle args = new Bundle();
            args.putString(ARG_CACHED_ENGINE_ID, FlutterBoost.ENGINE_ID);
            args.putBoolean(ARG_DESTROY_ENGINE_WITH_FRAGMENT, destroyEngineWithFragment);
            args.putString(
                    ARG_FLUTTERVIEW_RENDER_MODE,
                    renderMode != null ? renderMode.name() : RenderMode.surface.name());
            args.putString(
                    ARG_FLUTTERVIEW_TRANSPARENCY_MODE,
                    transparencyMode != null ? transparencyMode.name() : TransparencyMode.transparent.name());
            args.putBoolean(ARG_SHOULD_ATTACH_ENGINE_TO_ACTIVITY, shouldAttachEngineToActivity);
            args.putString(EXTRA_URL, url);
            args.putSerializable(EXTRA_URL_PARAM, params);
            args.putString(EXTRA_UNIQUE_ID, uniqueId != null ? uniqueId : FlutterBoostUtils.createUniqueId(url));
            return args;
        }

        /**
         * Constructs a new {@code FlutterFragment} (or a subclass) that is configured based on
         * properties set on this {@code CachedEngineFragmentBuilder}.
         */
        public <T extends FlutterBoostFragment> T build() {
            try {
                @SuppressWarnings("unchecked")
                T frag = (T) fragmentClass.getDeclaredConstructor().newInstance();
                if (frag == null) {
                    throw new RuntimeException(
                            "The FlutterFragment subclass sent in the constructor ("
                                    + fragmentClass.getCanonicalName()
                                    + ") does not match the expected return type.");
                }

                Bundle args = createArgs();
                frag.setArguments(args);

                return frag;
            } catch (Exception e) {
                throw new RuntimeException(
                        "Could not instantiate FlutterFragment subclass (" + fragmentClass.getName() + ")", e);
            }
        }
    }

}
