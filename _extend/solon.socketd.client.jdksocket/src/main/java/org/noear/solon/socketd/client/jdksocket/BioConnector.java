package org.noear.solon.socketd.client.jdksocket;

import org.noear.solon.Utils;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.message.Session;
import org.noear.solon.socketd.ConnectorBase;
import org.noear.solon.socketd.ListenerProxy;
import org.noear.solon.socketd.SocketProps;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;

class BioConnector extends ConnectorBase<Socket> {
    public BioConnector(URI uri, boolean autoReconnect) {
        super(uri, autoReconnect);
    }

    @Override
    public Class<Socket> driveType() {
        return Socket.class;
    }

    @Override
    public Socket open(Session session) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(uri().getHost(), uri().getPort());
            Socket socket = new Socket();

            if (SocketProps.socketTimeout() > 0) {
                socket.setSoTimeout(SocketProps.socketTimeout());
            }

            if (SocketProps.connectTimeout() > 0) {
                socket.connect(socketAddress, SocketProps.connectTimeout());
            } else {
                socket.connect(socketAddress);
            }

            startReceive(session, socket);

            return socket;
        } catch (Exception ex) {
            throw Utils.throwableWrap(ex);
        }
    }

    void startReceive(Session session, Socket socket) {
        Utils.pools.submit(() -> {
            while (true) {
                Message message = BioReceiver.receive(socket);

                if (message != null) {
                    try {
                        ListenerProxy.getGlobal().onMessage(session, message);
                    } catch (Throwable ex) {
                        EventBus.push(ex);
                    }
                } else {
                    break;
                }
            }
        });
    }
}
