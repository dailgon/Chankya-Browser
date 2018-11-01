// Copyright 2015 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.omnibox.geo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v4.util.ObjectsCompat;
import android.util.Base64;

import org.chromium.base.ApiCompatibilityUtils;
import org.chromium.base.CollectionUtil;
import org.chromium.base.ContextUtils;
import org.chromium.base.Log;
import org.chromium.base.VisibleForTesting;
import org.chromium.base.annotations.CalledByNative;
import org.chromium.base.metrics.RecordHistogram;
import org.chromium.chrome.browser.ContentSettingsType;
import org.chromium.chrome.browser.UrlConstants;
import org.chromium.chrome.browser.omnibox.geo.VisibleNetworks.VisibleCell;
import org.chromium.chrome.browser.omnibox.geo.VisibleNetworks.VisibleWifi;
import org.chromium.chrome.browser.preferences.website.ContentSetting;
import org.chromium.chrome.browser.preferences.website.PermissionInfo;
import org.chromium.chrome.browser.preferences.website.WebsitePreferenceBridge;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.util.UrlUtilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

/**
 * Provides methods for building the X-Geo HTTP header, which provides device location to a server
 * when making an HTTP request.
 *
 * X-Geo header spec: https://goto.google.com/xgeospec.
 */
public class GeolocationHeader {
    private static final String TAG = "GeolocationHeader";

    // Values for the histogram Geolocation.HeaderSentOrNot. Values 1, 5, 6, and 7 are defined in
    // histograms.xml and should not be used in other ways.
    public static final int UMA_LOCATION_DISABLED_FOR_GOOGLE_DOMAIN = 0;
    public static final int UMA_LOCATION_NOT_AVAILABLE = 2;
    public static final int UMA_LOCATION_STALE = 3;
    public static final int UMA_HEADER_SENT = 4;
    public static final int UMA_LOCATION_DISABLED_FOR_CHROME_APP = 5;
    public static final int UMA_MAX = 8;

    @IntDef({UmaPermission.UNKNOWN, UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_YES_LOCATION,
            UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_YES_NO_LOCATION,
            UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_LOCATION,
            UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_NO_LOCATION,
            UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_BLOCKED,
            UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_YES,
            UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_PROMPT,
            UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_BLOCKED,
            UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_YES,
            UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_PROMPT,
            UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_BLOCKED,
            UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_YES_LOCATION,
            UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_YES_NO_LOCATION,
            UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_LOCATION,
            UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_NO_LOCATION,
            UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_BLOCKED,
            UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_YES,
            UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_PROMPT,
            UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_BLOCKED,
            UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_YES,
            UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_PROMPT,
            UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_BLOCKED,
            UmaPermission.GPS_ONLY_APP_YES_DOMAIN_YES_LOCATION,
            UmaPermission.GPS_ONLY_APP_YES_DOMAIN_YES_NO_LOCATION,
            UmaPermission.GPS_ONLY_APP_YES_DOMAIN_PROMPT_LOCATION,
            UmaPermission.GPS_ONLY_APP_YES_DOMAIN_PROMPT_NO_LOCATION,
            UmaPermission.GPS_ONLY_APP_YES_DOMAIN_BLOCKED,
            UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_YES,
            UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_PROMPT,
            UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_BLOCKED,
            UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_YES,
            UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_PROMPT,
            UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_BLOCKED,
            UmaPermission.MASTER_OFF_APP_YES_DOMAIN_YES,
            UmaPermission.MASTER_OFF_APP_YES_DOMAIN_PROMPT,
            UmaPermission.MASTER_OFF_APP_YES_DOMAIN_BLOCKED,
            UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_YES,
            UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_PROMPT,
            UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_BLOCKED,
            UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_YES,
            UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_PROMPT,
            UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_BLOCKED, UmaPermission.UNSUITABLE_URL,
            UmaPermission.NOT_HTTPS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UmaPermission {
        // Values for the histogram Geolocation.Header.PermissionState.
        // These are used to back an UMA histogram and so should be treated as append-only.
        //
        // In order to keep the names of constants from being too long, the following were used:
        // APP_YES (instead of APP_GRANTED) to indicate App permission granted,
        // DOMAIN_YES (instead of DOMAIN_GRANTED) to indicate Domain permission granted.
        int UNKNOWN = 0;
        int HIGH_ACCURACY_APP_YES_DOMAIN_YES_LOCATION = 1;
        int HIGH_ACCURACY_APP_YES_DOMAIN_YES_NO_LOCATION = 2;
        int HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_LOCATION = 3;
        int HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_NO_LOCATION = 4;
        int HIGH_ACCURACY_APP_YES_DOMAIN_BLOCKED = 5;
        int HIGH_ACCURACY_APP_PROMPT_DOMAIN_YES = 6;
        int HIGH_ACCURACY_APP_PROMPT_DOMAIN_PROMPT = 7;
        int HIGH_ACCURACY_APP_PROMPT_DOMAIN_BLOCKED = 8;
        int HIGH_ACCURACY_APP_BLOCKED_DOMAIN_YES = 9;
        int HIGH_ACCURACY_APP_BLOCKED_DOMAIN_PROMPT = 10;
        int HIGH_ACCURACY_APP_BLOCKED_DOMAIN_BLOCKED = 11;
        int BATTERY_SAVING_APP_YES_DOMAIN_YES_LOCATION = 12;
        int BATTERY_SAVING_APP_YES_DOMAIN_YES_NO_LOCATION = 13;
        int BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_LOCATION = 14;
        int BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_NO_LOCATION = 15;
        int BATTERY_SAVING_APP_YES_DOMAIN_BLOCKED = 16;
        int BATTERY_SAVING_APP_PROMPT_DOMAIN_YES = 17;
        int BATTERY_SAVING_APP_PROMPT_DOMAIN_PROMPT = 18;
        int BATTERY_SAVING_APP_PROMPT_DOMAIN_BLOCKED = 19;
        int BATTERY_SAVING_APP_BLOCKED_DOMAIN_YES = 20;
        int BATTERY_SAVING_APP_BLOCKED_DOMAIN_PROMPT = 21;
        int BATTERY_SAVING_APP_BLOCKED_DOMAIN_BLOCKED = 22;
        int GPS_ONLY_APP_YES_DOMAIN_YES_LOCATION = 23;
        int GPS_ONLY_APP_YES_DOMAIN_YES_NO_LOCATION = 24;
        int GPS_ONLY_APP_YES_DOMAIN_PROMPT_LOCATION = 25;
        int GPS_ONLY_APP_YES_DOMAIN_PROMPT_NO_LOCATION = 26;
        int GPS_ONLY_APP_YES_DOMAIN_BLOCKED = 27;
        int GPS_ONLY_APP_PROMPT_DOMAIN_YES = 28;
        int GPS_ONLY_APP_PROMPT_DOMAIN_PROMPT = 29;
        int GPS_ONLY_APP_PROMPT_DOMAIN_BLOCKED = 30;
        int GPS_ONLY_APP_BLOCKED_DOMAIN_YES = 31;
        int GPS_ONLY_APP_BLOCKED_DOMAIN_PROMPT = 32;
        int GPS_ONLY_APP_BLOCKED_DOMAIN_BLOCKED = 33;
        int MASTER_OFF_APP_YES_DOMAIN_YES = 34;
        int MASTER_OFF_APP_YES_DOMAIN_PROMPT = 35;
        int MASTER_OFF_APP_YES_DOMAIN_BLOCKED = 36;
        int MASTER_OFF_APP_PROMPT_DOMAIN_YES = 37;
        int MASTER_OFF_APP_PROMPT_DOMAIN_PROMPT = 38;
        int MASTER_OFF_APP_PROMPT_DOMAIN_BLOCKED = 39;
        int MASTER_OFF_APP_BLOCKED_DOMAIN_YES = 40;
        int MASTER_OFF_APP_BLOCKED_DOMAIN_PROMPT = 41;
        int MASTER_OFF_APP_BLOCKED_DOMAIN_BLOCKED = 42;
        int UNSUITABLE_URL = 43;
        int NOT_HTTPS = 44;
        int NUM_ENTRIES = 45;
    }

    @IntDef({LocationSource.HIGH_ACCURACY, LocationSource.BATTERY_SAVING, LocationSource.GPS_ONLY,
            LocationSource.MASTER_OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LocationSource {
        @VisibleForTesting
        int HIGH_ACCURACY = 0;
        @VisibleForTesting
        int BATTERY_SAVING = 1;
        @VisibleForTesting
        int GPS_ONLY = 2;
        @VisibleForTesting
        int MASTER_OFF = 3;
    }

    @IntDef({Permission.GRANTED, Permission.PROMPT, Permission.BLOCKED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Permission {
        int GRANTED = 0;
        int PROMPT = 1;
        int BLOCKED = 2;
    }

    /** The maximum value for the GeolocationHeader.TimeListening* histograms. */
    public static final int TIME_LISTENING_HISTOGRAM_MAX_MILLIS = 50 * 60 * 1000; // 50 minutes

    /** The maximum value for the GeolocationHeader.LocationAge* histograms. */
    public static final int LOCATION_AGE_HISTOGRAM_MAX_SECONDS = 30 * 24 * 60 * 60; // 30 days

    @IntDef({HeaderState.HEADER_ENABLED, HeaderState.INCOGNITO, HeaderState.UNSUITABLE_URL,
            HeaderState.NOT_HTTPS, HeaderState.LOCATION_PERMISSION_BLOCKED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface HeaderState {
        int HEADER_ENABLED = 0;
        int INCOGNITO = 1;
        int UNSUITABLE_URL = 2;
        int NOT_HTTPS = 3;
        int LOCATION_PERMISSION_BLOCKED = 4;
    }

    /** The maximum age in milliseconds of a location that we'll send in an X-Geo header. */
    private static final int MAX_LOCATION_AGE = 24 * 60 * 60 * 1000;  // 24 hours

    /** The maximum age in milliseconds of a location before we'll request a refresh. */
    private static final int REFRESH_LOCATION_AGE = 5 * 60 * 1000;  // 5 minutes

    /** The X-Geo header prefix, preceding any location descriptors */
    private static final String XGEO_HEADER_PREFIX = "X-Geo:";

    /**
     * The location descriptor separator used in the X-Geo header to separate encoding prefix, and
     * encoded descriptors
     */
    private static final String LOCATION_SEPARATOR = " ";

    /** The location descriptor prefix used in the X-Geo header to specify a proto wire encoding */
    private static final String LOCATION_PROTO_PREFIX = "w";

    /** The time of the first location refresh. Contains Long.MAX_VALUE if not set. */
    private static long sFirstLocationTime = Long.MAX_VALUE;

    /** Present in WiFi SSID that should not be mapped */
    private static final String SSID_NOMAP = "_nomap";

    /** Present in WiFi SSID that opted out */
    private static final String SSID_OPTOUT = "_optout";

    private static int sLocationSourceForTesting;
    private static boolean sUseLocationSourceForTesting;

    private static boolean sAppPermissionGrantedForTesting;
    private static boolean sUseAppPermissionGrantedForTesting;

    /**
     * Requests a location refresh so that a valid location will be available for constructing
     * an X-Geo header in the near future (i.e. within 5 minutes).
     */
    public static void primeLocationForGeoHeader() {
        if (!hasGeolocationPermission()) return;

        if (sFirstLocationTime == Long.MAX_VALUE) {
            sFirstLocationTime = SystemClock.elapsedRealtime();
        }
        GeolocationTracker.refreshLastKnownLocation(
                ContextUtils.getApplicationContext(), REFRESH_LOCATION_AGE);
        VisibleNetworksTracker.refreshVisibleNetworks(ContextUtils.getApplicationContext());
    }

    @HeaderState
    private static int geoHeaderStateForUrl(String url, boolean isIncognito, boolean recordUma) {
        // Only send X-Geo in normal mode.
        if (isIncognito) return HeaderState.INCOGNITO;

        // Only send X-Geo header to Google domains.
        if (!UrlUtilities.nativeIsGoogleSearchUrl(url)) return HeaderState.UNSUITABLE_URL;

        Uri uri = Uri.parse(url);
        if (!UrlConstants.HTTPS_SCHEME.equals(uri.getScheme())) return HeaderState.NOT_HTTPS;

        if (!hasGeolocationPermission()) {
            if (recordUma) recordHistogram(UMA_LOCATION_DISABLED_FOR_CHROME_APP);
            return HeaderState.LOCATION_PERMISSION_BLOCKED;
        }

        // Only send X-Geo header if the user hasn't disabled geolocation for url.
        if (isLocationDisabledForUrl(uri, isIncognito)) {
            if (recordUma) recordHistogram(UMA_LOCATION_DISABLED_FOR_GOOGLE_DOMAIN);
            return HeaderState.LOCATION_PERMISSION_BLOCKED;
        }

        return HeaderState.HEADER_ENABLED;
    }

    /**
     * Returns an X-Geo HTTP header string if:
     *  1. The current mode is not incognito.
     *  2. The url is a google search URL (e.g. www.google.co.uk/search?q=cars), and
     *  3. The user has not disabled sharing location with this url, and
     *  4. There is a valid and recent location available.
     *
     * Returns null otherwise.
     *
     * @param url The URL of the request with which this header will be sent.
     * @param tab The Tab currently being accessed.
     * @return The X-Geo header string or null.
     */
    public static String getGeoHeader(String url, Tab tab) {
        // TODO(lbargu): Refactor and simplify flow.
        boolean isIncognito = tab.isIncognito();
        Location locationToAttach = null;
        VisibleNetworks visibleNetworksToAttach = null;
        long locationAge = Long.MAX_VALUE;
        @HeaderState int headerState = geoHeaderStateForUrl(url, isIncognito, true);
        if (headerState == HeaderState.HEADER_ENABLED) {
            locationToAttach =
                    GeolocationTracker.getLastKnownLocation(ContextUtils.getApplicationContext());
            if (locationToAttach == null) {
                recordHistogram(UMA_LOCATION_NOT_AVAILABLE);
            } else {
                locationAge = GeolocationTracker.getLocationAge(locationToAttach);
                if (locationAge > MAX_LOCATION_AGE) {
                    // Do not attach the location
                    recordHistogram(UMA_LOCATION_STALE);
                    locationToAttach = null;
                } else {
                    recordHistogram(UMA_HEADER_SENT);
                }
            }

            // The header state is enabled, so this means we have app permissions, and the url is
            // allowed to receive location. Before attempting to attach visible networks, check if
            // network-based location is enabled.
            if (isNetworkLocationEnabled() && !isLocationFresh(locationToAttach)) {
                visibleNetworksToAttach = VisibleNetworksTracker.getLastKnownVisibleNetworks(
                        ContextUtils.getApplicationContext());
            }
        }

        @LocationSource int locationSource = getLocationSource();
        @Permission int appPermission = getGeolocationPermission(tab);
        @Permission int domainPermission = getDomainPermission(url, isIncognito);

        // Record the permission state with a histogram.
        recordPermissionHistogram(locationSource, appPermission, domainPermission,
                locationToAttach != null, headerState);

        if (locationSource != LocationSource.MASTER_OFF && appPermission != Permission.BLOCKED
                && domainPermission != Permission.BLOCKED && !isIncognito) {
            // Record the Location Age with a histogram.
            recordLocationAgeHistogram(locationSource, locationAge);
            long duration = sFirstLocationTime == Long.MAX_VALUE
                    ? 0
                    : SystemClock.elapsedRealtime() - sFirstLocationTime;
            // Record the Time Listening with a histogram.
            recordTimeListeningHistogram(locationSource, locationToAttach != null, duration);
        }

        // Proto encoding
        String locationProtoEncoding = encodeProtoLocation(locationToAttach);
        String visibleNetworksProtoEncoding = encodeProtoVisibleNetworks(visibleNetworksToAttach);

        if (locationProtoEncoding == null && visibleNetworksProtoEncoding == null) return null;

        StringBuilder header = new StringBuilder(XGEO_HEADER_PREFIX);
        if (locationProtoEncoding != null) {
            header.append(LOCATION_SEPARATOR).append(LOCATION_PROTO_PREFIX)
                    .append(LOCATION_SEPARATOR).append(locationProtoEncoding);
        }
        if (visibleNetworksProtoEncoding != null) {
            header.append(LOCATION_SEPARATOR).append(LOCATION_PROTO_PREFIX)
                    .append(LOCATION_SEPARATOR).append(visibleNetworksProtoEncoding);
        }
        return header.toString();
    }

    @CalledByNative
    static boolean hasGeolocationPermission() {
        if (sUseAppPermissionGrantedForTesting) return sAppPermissionGrantedForTesting;
        int pid = Process.myPid();
        int uid = Process.myUid();
        if (ApiCompatibilityUtils.checkPermission(ContextUtils.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION, pid, uid)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        // Work around a bug in OnePlus2 devices running Lollipop, where the NETWORK_PROVIDER
        // incorrectly requires FINE_LOCATION permission (it should only require COARSE_LOCATION
        // permission). http://crbug.com/580733
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && ApiCompatibilityUtils.checkPermission(ContextUtils.getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION, pid, uid)
                        != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    /**
     * Returns the app level geolocation permission.
     * This permission can be either granted, blocked or prompt.
     */
    @Permission
    static int getGeolocationPermission(Tab tab) {
        if (sUseAppPermissionGrantedForTesting) {
            return sAppPermissionGrantedForTesting ? Permission.GRANTED : Permission.BLOCKED;
        }
        if (hasGeolocationPermission()) return Permission.GRANTED;
        return tab.getWindowAndroid().canRequestPermission(
                       Manifest.permission.ACCESS_COARSE_LOCATION)
                ? Permission.PROMPT
                : Permission.BLOCKED;
    }

    /**
     * Returns true if the user has disabled sharing their location with url (e.g. via the
     * geolocation infobar).
     */
    static boolean isLocationDisabledForUrl(Uri uri, boolean isIncognito) {
        boolean enabled =
                // TODO(raymes): The call to isPermissionControlledByDSE is only needed if this
                // could be called for an origin that isn't the default search engine. Otherwise
                // remove this line.
                WebsitePreferenceBridge.isPermissionControlledByDSE(
                        ContentSettingsType.CONTENT_SETTINGS_TYPE_GEOLOCATION, uri.toString(),
                        isIncognito)
                && locationContentSettingForUrl(uri, isIncognito) == ContentSetting.ALLOW;
        return !enabled;
    }

    /**
     * Returns the location permission for sharing their location with url (e.g. via the
     * geolocation infobar).
     */
    static ContentSetting locationContentSettingForUrl(Uri uri, boolean isIncognito) {
        PermissionInfo locationSettings = new PermissionInfo(
                PermissionInfo.Type.GEOLOCATION, uri.toString(), null, isIncognito);
        return locationSettings.getContentSetting();
    }

    @VisibleForTesting
    static void setLocationSourceForTesting(int locationSourceForTesting) {
        sLocationSourceForTesting = locationSourceForTesting;
        sUseLocationSourceForTesting = true;
    }

    @VisibleForTesting
    static void setAppPermissionGrantedForTesting(boolean appPermissionGrantedForTesting) {
        sAppPermissionGrantedForTesting = appPermissionGrantedForTesting;
        sUseAppPermissionGrantedForTesting = true;
    }

    /** Records a data point for the Geolocation.HeaderSentOrNot histogram. */
    private static void recordHistogram(int result) {
        RecordHistogram.recordEnumeratedHistogram("Geolocation.HeaderSentOrNot", result, UMA_MAX);
    }

    /** Returns the location source. */
    @LocationSource
    // We should replace our usage of LOCATION_PROVIDERS_ALLOWED when the min API is 19.
    @SuppressWarnings("deprecation")
    private static int getLocationSource() {
        if (sUseLocationSourceForTesting) return sLocationSourceForTesting;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int locationMode;
            try {
                locationMode = Settings.Secure.getInt(
                        ContextUtils.getApplicationContext().getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                Log.e(TAG, "Error getting the LOCATION_MODE");
                return LocationSource.MASTER_OFF;
            }
            if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                return LocationSource.HIGH_ACCURACY;
            } else if (locationMode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY) {
                return LocationSource.GPS_ONLY;
            } else if (locationMode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING) {
                return LocationSource.BATTERY_SAVING;
            } else {
                return LocationSource.MASTER_OFF;
            }
        } else {
            String locationProviders = Settings.Secure.getString(
                    ContextUtils.getApplicationContext().getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders.contains(LocationManager.GPS_PROVIDER)
                    && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                return LocationSource.HIGH_ACCURACY;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                return LocationSource.GPS_ONLY;
            } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                return LocationSource.BATTERY_SAVING;
            } else {
                return LocationSource.MASTER_OFF;
            }
        }
    }

    private static boolean isNetworkLocationEnabled() {
        int locationSource = getLocationSource();
        return locationSource == LocationSource.HIGH_ACCURACY
                || locationSource == LocationSource.BATTERY_SAVING;
    }

    private static boolean isLocationFresh(@Nullable Location location) {
        return location != null
                && GeolocationTracker.getLocationAge(location) <= REFRESH_LOCATION_AGE;
    }

    /**
     * Returns the domain permission as either granted, blocked or prompt.
     * This is based upon the location permission for sharing their location with url (e.g. via the
     * geolocation infobar).
     */
    @Permission
    private static int getDomainPermission(String url, boolean isIncognito) {
        ContentSetting domainPermission = locationContentSettingForUrl(Uri.parse(url), isIncognito);
        switch (domainPermission) {
            case ALLOW:
                return Permission.GRANTED;
            case ASK:
                return Permission.PROMPT;
            default:
                return Permission.BLOCKED;
        }
    }

    /**
     * Returns the enum to use in the Geolocation.Header.PermissionState histogram.
     * Unexpected input values return UmaPermission.UNKNOWN.
     */
    @UmaPermission
    private static int getPermissionHistogramEnum(@LocationSource int locationSource,
            @Permission int appPermission, @Permission int domainPermission,
            boolean locationAttached, @HeaderState int headerState) {
        if (headerState == HeaderState.UNSUITABLE_URL) return UmaPermission.UNSUITABLE_URL;
        if (headerState == HeaderState.NOT_HTTPS) return UmaPermission.NOT_HTTPS;
        if (locationSource == LocationSource.HIGH_ACCURACY) {
            if (appPermission == Permission.GRANTED) {
                if (domainPermission == Permission.GRANTED) {
                    return locationAttached
                            ? UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_YES_LOCATION
                            : UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_YES_NO_LOCATION;
                } else if (domainPermission == Permission.PROMPT) {
                    return locationAttached
                            ? UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_LOCATION
                            : UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_PROMPT_NO_LOCATION;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.HIGH_ACCURACY_APP_YES_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.PROMPT) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.HIGH_ACCURACY_APP_PROMPT_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.BLOCKED) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.HIGH_ACCURACY_APP_BLOCKED_DOMAIN_BLOCKED;
                }
            }
        } else if (locationSource == LocationSource.BATTERY_SAVING) {
            if (appPermission == Permission.GRANTED) {
                if (domainPermission == Permission.GRANTED) {
                    return locationAttached
                            ? UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_YES_LOCATION
                            : UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_YES_NO_LOCATION;
                } else if (domainPermission == Permission.PROMPT) {
                    return locationAttached
                            ? UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_LOCATION
                            : UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_PROMPT_NO_LOCATION;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.BATTERY_SAVING_APP_YES_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.PROMPT) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.BATTERY_SAVING_APP_PROMPT_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.BLOCKED) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.BATTERY_SAVING_APP_BLOCKED_DOMAIN_BLOCKED;
                }
            }
        } else if (locationSource == LocationSource.GPS_ONLY) {
            if (appPermission == Permission.GRANTED) {
                if (domainPermission == Permission.GRANTED) {
                    return locationAttached ? UmaPermission.GPS_ONLY_APP_YES_DOMAIN_YES_LOCATION
                                            : UmaPermission.GPS_ONLY_APP_YES_DOMAIN_YES_NO_LOCATION;
                } else if (domainPermission == Permission.PROMPT) {
                    return locationAttached
                            ? UmaPermission.GPS_ONLY_APP_YES_DOMAIN_PROMPT_LOCATION
                            : UmaPermission.GPS_ONLY_APP_YES_DOMAIN_PROMPT_NO_LOCATION;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.GPS_ONLY_APP_YES_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.PROMPT) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.GPS_ONLY_APP_PROMPT_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.BLOCKED) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.GPS_ONLY_APP_BLOCKED_DOMAIN_BLOCKED;
                }
            }
        } else if (locationSource == LocationSource.MASTER_OFF) {
            if (appPermission == Permission.GRANTED) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.MASTER_OFF_APP_YES_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.MASTER_OFF_APP_YES_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.MASTER_OFF_APP_YES_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.PROMPT) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.MASTER_OFF_APP_PROMPT_DOMAIN_BLOCKED;
                }
            } else if (appPermission == Permission.BLOCKED) {
                if (domainPermission == Permission.GRANTED) {
                    return UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_YES;
                } else if (domainPermission == Permission.PROMPT) {
                    return UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_PROMPT;
                } else if (domainPermission == Permission.BLOCKED) {
                    return UmaPermission.MASTER_OFF_APP_BLOCKED_DOMAIN_BLOCKED;
                }
            }
        }
        return UmaPermission.UNKNOWN;
    }

    /** Records a data point for the Geolocation.Header.PermissionState histogram. */
    private static void recordPermissionHistogram(@LocationSource int locationSource,
            @Permission int appPermission, @Permission int domainPermission,
            boolean locationAttached, @HeaderState int headerState) {
        if (headerState == HeaderState.INCOGNITO) return;
        @UmaPermission
        int result = getPermissionHistogramEnum(
                locationSource, appPermission, domainPermission, locationAttached, headerState);
        RecordHistogram.recordEnumeratedHistogram(
                "Geolocation.Header.PermissionState", result, UmaPermission.NUM_ENTRIES);
    }

    /**
     * Determines the name for a Time Listening Histogram. Returns empty string if the location
     * source is MASTER_OFF as we do not record histograms for that case.
     */
    private static String getTimeListeningHistogramEnum(
            int locationSource, boolean locationAttached) {
        switch (locationSource) {
            case LocationSource.HIGH_ACCURACY:
                return locationAttached
                        ? "Geolocation.Header.TimeListening.HighAccuracy.LocationAttached"
                        : "Geolocation.Header.TimeListening.HighAccuracy.LocationNotAttached";
            case LocationSource.GPS_ONLY:
                return locationAttached
                        ? "Geolocation.Header.TimeListening.GpsOnly.LocationAttached"
                        : "Geolocation.Header.TimeListening.GpsOnly.LocationNotAttached";
            case LocationSource.BATTERY_SAVING:
                return locationAttached
                        ? "Geolocation.Header.TimeListening.BatterySaving.LocationAttached"
                        : "Geolocation.Header.TimeListening.BatterySaving.LocationNotAttached";
            default:
                Log.e(TAG, "Unexpected locationSource: " + locationSource);
                assert false : "Unexpected locationSource: " + locationSource;
                return null;
        }
    }

    /** Records a data point for one of the GeolocationHeader.TimeListening* histograms. */
    private static void recordTimeListeningHistogram(
            int locationSource, boolean locationAttached, long duration) {
        String name = getTimeListeningHistogramEnum(locationSource, locationAttached);
        if (name == null) return;
        RecordHistogram.recordCustomTimesHistogram(
                name, duration, 1, TIME_LISTENING_HISTOGRAM_MAX_MILLIS, TimeUnit.MILLISECONDS, 50);
    }

    /** Records a data point for one of the GeolocationHeader.LocationAge* histograms. */
    private static void recordLocationAgeHistogram(int locationSource, long durationMillis) {
        String name = "";
        if (locationSource == LocationSource.HIGH_ACCURACY) {
            name = "Geolocation.Header.LocationAge.HighAccuracy";
        } else if (locationSource == LocationSource.GPS_ONLY) {
            name = "Geolocation.Header.LocationAge.GpsOnly";
        } else if (locationSource == LocationSource.BATTERY_SAVING) {
            name = "Geolocation.Header.LocationAge.BatterySaving";
        } else {
            Log.e(TAG, "Unexpected locationSource: " + locationSource);
            assert false : "Unexpected locationSource: " + locationSource;
            return;
        }
        long durationSeconds = durationMillis / 1000;
        int duration = durationSeconds >= (long) Integer.MAX_VALUE ? Integer.MAX_VALUE
                                                                   : (int) durationSeconds;
        RecordHistogram.recordCustomCountHistogram(
                name, duration, 1, LOCATION_AGE_HISTOGRAM_MAX_SECONDS, 50);
    }

    /**
     * Encodes location into proto encoding.
     */
    @Nullable
    @VisibleForTesting
    static String encodeProtoLocation(@Nullable Location location) {
        if (location == null) return null;

        // Timestamp in microseconds since the UNIX epoch.
        long timestamp = location.getTime() * 1000;
        // Latitude times 1e7.
        int latitudeE7 = (int) (location.getLatitude() * 10000000);
        // Longitude times 1e7.
        int longitudeE7 = (int) (location.getLongitude() * 10000000);
        // Radius of 68% accuracy in mm.
        int radius = (int) (location.getAccuracy() * 1000);

        // Create a LatLng for the coordinates.
        PartnerLocationDescriptor.LatLng latlng = PartnerLocationDescriptor.LatLng.newBuilder()
                                                          .setLatitudeE7(latitudeE7)
                                                          .setLongitudeE7(longitudeE7)
                                                          .build();

        // Populate a LocationDescriptor with the LatLng.
        PartnerLocationDescriptor.LocationDescriptor locationDescriptor =
                PartnerLocationDescriptor.LocationDescriptor.newBuilder()
                        .setLatlng(latlng)
                        // Include role, producer, timestamp and radius.
                        .setRole(PartnerLocationDescriptor.LocationRole.CURRENT_LOCATION)
                        .setProducer(PartnerLocationDescriptor.LocationProducer.DEVICE_LOCATION)
                        .setTimestamp(timestamp)
                        .setRadius((float) radius)
                        .build();
        return encodeLocationDescriptor(locationDescriptor);
    }

    /**
     * Encodes the given proto location descriptor into a BASE64 URL_SAFE encoding.
     */
    private static String encodeLocationDescriptor(
            PartnerLocationDescriptor.LocationDescriptor locationDescriptor) {
        return Base64.encodeToString(
                locationDescriptor.toByteArray(), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    /**
     * Encodes visible networks in proto encoding.
     */
    @Nullable
    @VisibleForTesting
    static String encodeProtoVisibleNetworks(@Nullable VisibleNetworks visibleNetworks) {
        VisibleNetworks visibleNetworksToEncode = trimVisibleNetworks(visibleNetworks);
        if (visibleNetworksToEncode == null || visibleNetworksToEncode.isEmpty()) {
            // No data to encode.
            return null;
        }
        VisibleWifi connectedWifi = visibleNetworksToEncode.connectedWifi();
        VisibleCell connectedCell = visibleNetworksToEncode.connectedCell();
        Set<VisibleWifi> visibleWifis = visibleNetworksToEncode.allVisibleWifis();
        Set<VisibleCell> visibleCells = visibleNetworksToEncode.allVisibleCells();

        PartnerLocationDescriptor.LocationDescriptor.Builder locationDescriptorBuilder =
                PartnerLocationDescriptor.LocationDescriptor.newBuilder()
                        .setRole(PartnerLocationDescriptor.LocationRole.CURRENT_LOCATION)
                        .setProducer(PartnerLocationDescriptor.LocationProducer.DEVICE_LOCATION);

        if (connectedWifi != null) {
            locationDescriptorBuilder.addVisibleNetwork(connectedWifi.toProto(true));
        }
        if (visibleWifis != null) {
            for (VisibleWifi visibleWifi : visibleWifis) {
                locationDescriptorBuilder.addVisibleNetwork(visibleWifi.toProto(false));
            }
        }
        if (connectedCell != null) {
            locationDescriptorBuilder.addVisibleNetwork(connectedCell.toProto(true));
        }
        if (visibleCells != null) {
            for (VisibleCell visibleCell : visibleCells) {
                locationDescriptorBuilder.addVisibleNetwork(visibleCell.toProto(false));
            }
        }

        return encodeLocationDescriptor(locationDescriptorBuilder.build());
    }

    @Nullable
    @VisibleForTesting
    static VisibleNetworks trimVisibleNetworks(@Nullable VisibleNetworks visibleNetworks) {
        if (visibleNetworks == null || visibleNetworks.isEmpty()) {
            return null;
        }
        // Trim visible networks to only include a limited number of visible not-conntected networks
        // based on flag.
        VisibleCell connectedCell = visibleNetworks.connectedCell();
        VisibleWifi connectedWifi = visibleNetworks.connectedWifi();
        Set<VisibleCell> visibleCells = visibleNetworks.allVisibleCells();
        Set<VisibleWifi> visibleWifis = visibleNetworks.allVisibleWifis();
        VisibleCell extraVisibleCell = null;
        VisibleWifi extraVisibleWifi = null;
        if (shouldExcludeVisibleWifi(connectedWifi)) {
            // Trim the connected wifi.
            connectedWifi = null;
        }
        // Select the extra visible cell.
        if (visibleCells != null) {
            for (VisibleCell candidateCell : visibleCells) {
                if (ObjectsCompat.equals(connectedCell, candidateCell)) {
                    // Do not include this candidate cell, since its already the connected one.
                    continue;
                }
                // Add it and since we only want one, stop iterating over other cells.
                extraVisibleCell = candidateCell;
                break;
            }
        }
        // Select the extra visible wifi.
        if (visibleWifis != null) {
            for (VisibleWifi candidateWifi : visibleWifis) {
                if (shouldExcludeVisibleWifi(candidateWifi)) {
                    // Do not include this candidate wifi.
                    continue;
                }
                if (ObjectsCompat.equals(connectedWifi, candidateWifi)) {
                    // Replace the connected, since the candidate will have level. This is because
                    // the android APIs exposing connected WIFI do not expose level, while the ones
                    // exposing visible wifis expose level.
                    connectedWifi = candidateWifi;
                    // Do not include this candidate wifi, since its already the connected one.
                    continue;
                }
                // Keep the one with stronger level (since it's negative, this is the smaller value)
                if (extraVisibleWifi == null || extraVisibleWifi.level() > candidateWifi.level()) {
                    extraVisibleWifi = candidateWifi;
                }
            }
        }

        if (connectedCell == null && connectedWifi == null && extraVisibleCell == null
                && extraVisibleWifi == null) {
            return null;
        }

        return VisibleNetworks.create(connectedWifi, connectedCell,
                extraVisibleWifi != null ? CollectionUtil.newHashSet(extraVisibleWifi) : null,
                extraVisibleCell != null ? CollectionUtil.newHashSet(extraVisibleCell) : null);
    }

    /**
     * Returns whether the provided {@link VisibleWifi} should be excluded. This can happen if the
     * network is opted out (ssid contains "_nomap" or "_optout").
     */
    private static boolean shouldExcludeVisibleWifi(@Nullable VisibleWifi visibleWifi) {
        if (visibleWifi == null || visibleWifi.bssid() == null) {
            return true;
        }
        String ssid = visibleWifi.ssid();
        if (ssid == null) {
            // No ssid, so the networks is not opted out and should not be excluded.
            return false;
        }
        // Optimization to avoid costly toLowerCase() in most cases.
        if (ssid.indexOf('_') < 0) {
            // No "_nomap" or "_optout".
            return false;
        }
        String ssidLowerCase = ssid.toLowerCase(Locale.ENGLISH);
        return ssidLowerCase.contains(SSID_NOMAP) || ssidLowerCase.contains(SSID_OPTOUT);
    }
}
