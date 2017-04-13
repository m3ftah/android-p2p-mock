package com.example.wifidirect.net.wifi.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.example.wifidirect.net.NetworkInfo;
import com.example.wifidirect.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import com.example.wifidirect.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

public class WifiP2pManager {

    public static final String TAG = "WifiP2pManager";

    public static final String WIFI_P2P_STATE_CHANGED_ACTION =
            "com.example.wifidirect.STATE_CHANGED";

    public static final String WIFI_P2P_PEERS_CHANGED_ACTION =
            "com.example.wifidirect.PEERS_CHANGED";

    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION =
            "com.example.wifidirect.CONNECTION_STATE_CHANGE";

    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION =
            "com.example.wifidirect.THIS_DEVICE_CHANGED";

    public static final int WIFI_P2P_STATE_ENABLED = 2;

    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";

    public static final int ERROR = 0;

    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";

    public static final String EXTRA_NETWORK_INFO = "networkInfo";

    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
    //private final WiDiHandler mWiDiHandler;

    public void setReceiver(BroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    private BroadcastReceiver receiver;

    private WifiP2pServiceInfo mWifiP2pServiceInfo;
    private DnsSdServiceResponseListener mDnsSdServiceResponseListener;
    private DnsSdTxtRecordListener mDnsSdTxtRecordListener;
    private WifiP2pDnsSdServiceRequest mWifiP2pDnsSdServiceRequest;

    public void send_wifi_p2p_state(boolean enabled){
        Intent intent = new Intent();
        intent.setAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        if (enabled){
            intent.putExtra(WifiP2pManager.EXTRA_WIFI_STATE, WifiP2pManager.WIFI_P2P_STATE_ENABLED);
        }
        receiver.onReceive(null,intent);
        
    }

    public Channel initialize(final Context context, final Looper looper,
                              final ChannelListener channelListener) {
        return new Channel();
    }

    public void discoverPeers(final Channel channel, final ActionListener actionListener) {
        //EventBus.getDefault().post(new DiscoverPeersEvent(actionListener));
        Intent intent = new Intent();
        intent.setAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        //intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        //intent.setType("text/plain");
        actionListener.onSuccess();//Response that the discovery has succeeded
        receiver.onReceive(null, intent);//Send a fake peers
    }

    public void stopPeerDiscovery(final Channel channel, final ActionListener actionListener) {
        //EventBus.getDefault().post(new StopDiscoveryEvent(actionListener));
    }

    public void requestPeers(final WifiP2pManager.Channel channel, final WifiP2pManager.PeerListListener peerListListener) {
        Log.d(TAG,"Requesting peers");
        //EventBus.getDefault().post(new RequestPeersEvent(peerListListener));
        List<WifiP2pDevice> deviceList = new ArrayList<>();
        deviceList.add(new WifiP2pDevice("XT1650_31a8","192.168.49.113"));

        WifiP2pDeviceList peers = new WifiP2pDeviceList();

        for (WifiP2pDevice device : deviceList) {
            WifiP2pDevice d = new WifiP2pDevice(device.deviceName,device.deviceAddress);
            peers.update(d);
        }

        peerListListener.onPeersAvailable(peers);
    }
    /**
     * Request device connection info.
     *
     * @param c is the channel created at {@link #initialize}
     * @param listener for callback when connection info is available. Can be null.
     */
    public void requestConnectionInfo(Channel c, ConnectionInfoListener listener) {
        listener.onConnectionInfoAvailable(new WifiP2pInfo("192.168.49.113",true));
        Intent intent = new Intent();
        intent.setAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intent.putExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE, new WifiP2pDevice("TestDeviceMocked"));
        intent.putExtra(WifiP2pManager.EXTRA_NETWORK_INFO, new NetworkInfo(true));

        receiver.onReceive(null,intent);

    }
    public interface ConnectionInfoListener {
        /**
         * The requested connection info is available
         * @param info Wi-Fi p2p connection info
         */
        public void onConnectionInfoAvailable(WifiP2pInfo info);
    }

    public void addLocalService(final Channel channel, final WifiP2pServiceInfo wifiP2pServiceInfo, final ActionListener actionListener) {
        mWifiP2pServiceInfo = wifiP2pServiceInfo;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void clearLocalServices(final Channel channel, final ActionListener actionListener) {
        mWifiP2pServiceInfo = null;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void setDnsSdResponseListeners(
            final Channel channel,
            final DnsSdServiceResponseListener dnsSdServiceResponseListener,
            final DnsSdTxtRecordListener dnsSdTxtRecordListener) {

        mDnsSdServiceResponseListener = dnsSdServiceResponseListener;
        mDnsSdTxtRecordListener = dnsSdTxtRecordListener;
    }

    public void addServiceRequest(final Channel channel,
                                  final WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest,
                                  final ActionListener actionListener) {

        mWifiP2pDnsSdServiceRequest = wifiP2pDnsSdServiceRequest;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void clearServiceRequests(final Channel channel, final ActionListener actionListener) {
        //mDnsSdServiceResponseListener = null;
        mDnsSdTxtRecordListener = null;
        mWifiP2pDnsSdServiceRequest = null;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void discoverServices(final Channel channel, final ActionListener actionListener) {
        /*EventBus.getDefault().post(new DiscoverServicesEvent(mWifiP2pServiceInfo,
                mDnsSdServiceResponseListener, mDnsSdTxtRecordListener, actionListener));*/
    }

    public void connect(final Channel channel, final WifiP2pConfig wifiP2pConfig, final ActionListener actionListener) {
        //EventBus.getDefault().post(new ConnectEvent(wifiP2pConfig, actionListener));
        Log.d("WifiP2pManager","request to connect");
        Intent intent = new Intent();
        intent.setAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intent.putExtra(WifiP2pManager.EXTRA_NETWORK_INFO, new NetworkInfo(true));
        //intent.setType("text/plain");
        actionListener.onSuccess();
        receiver.onReceive(null, intent);//TODO Disconnect event?
    }

    public void cancelConnect(final Channel channel, final ActionListener actionListener) {
        //EventBus.getDefault().post(new CancelConnectEvent(actionListener));//TODO Cancel connect
    }

    public void removeGroup(Channel channel, ActionListener actionListener) {
        // Nothing to do
        actionListener.onSuccess();
    }

    public interface DnsSdTxtRecordListener {
        void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice);
    }

    public class Channel {}

    public interface PeerListListener {
        void onPeersAvailable(WifiP2pDeviceList peers);
    }

    public interface ChannelListener {
        void onChannelDisconnected();
    }

    public interface ActionListener {
        void onSuccess();
        void onFailure(int reason);
    }

    public interface DnsSdServiceResponseListener {
        void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice);
    }
}
