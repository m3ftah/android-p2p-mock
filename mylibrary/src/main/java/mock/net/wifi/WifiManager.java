package mock.net.wifi;

public class WifiManager {

    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION =
        "mock.net.wifi.supplicant.CONNECTION_CHANGE";

    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";

    public boolean isWifiEnabled() {
        return true;
    }
}