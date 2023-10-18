package org.noear.solon.boot.smarthttp.websocket;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.message.Session;
import org.noear.solon.socketd.ProtocolManager;
import org.noear.solon.socketd.SessionBase;
import org.smartboot.http.server.WebSocketRequest;
import org.smartboot.http.server.WebSocketResponse;
import org.smartboot.http.server.impl.WebSocketRequestImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;

public class _SocketServerSession extends SessionBase {
    public static final Map<WebSocketRequest, _SocketServerSession> sessions = new HashMap<>();

    public static _SocketServerSession get(WebSocketRequest req) {
        _SocketServerSession tmp = sessions.get(req);
        if (tmp == null) {
            synchronized (req) {
                tmp = sessions.get(req);
                if (tmp == null) {
                    tmp = new _SocketServerSession(req);
                    sessions.put(req, tmp);
                }
            }
        }

        return tmp;
    }

    public static void remove(WebSocketRequest real) {
        sessions.remove(real);
    }


    private final WebSocketRequest request;
    private final WebSocketResponse real;
    private String _sessionId = Utils.guid();

    public _SocketServerSession(WebSocketRequest request) {
        this.request = request;
        this.real = ((WebSocketRequestImpl) request).getResponse();
    }

    @Override
    public Object real() {
        return request;
    }


    @Override
    public String sessionId() {
        return _sessionId;
    }

    @Override
    public MethodType method() {
        return MethodType.WEBSOCKET;
    }

    private URI _uri;

    @Override
    public URI uri() {
        if (_uri == null) {
            if (Utils.isEmpty(request.getQueryString())) {
                _uri = URI.create(request.getRequestURL());
            } else {
                if (request.getRequestURL().contains("?")) {
                    _uri = URI.create(request.getRequestURL());
                } else {
                    _uri = URI.create(request.getRequestURL() + "?" + request.getQueryString());
                }
            }
        }

        return _uri;
    }

    private String _path;

    @Override
    public String path() {
        if (_path == null) {
            _path = uri().getPath();
        }

        return _path;
    }


    @Override
    public void send(String message) {
        synchronized (this) {
            if (Solon.app().enableWebSocketD()) {
                ByteBuffer buf = ProtocolManager.encode(Message.wrap(message));
                real.sendBinaryMessage(buf.array());
            } else {
                real.sendTextMessage(message);
            }
            real.flush();
        }
    }

    @Override
    public void send(Message message) {
        super.send(message);

        synchronized (this) {
            if (Solon.app().enableWebSocketD()) {
                ByteBuffer buf = ProtocolManager.encode(message);
                real.sendBinaryMessage(buf.array());
            } else {
                if (message.isString()) {
                    real.sendTextMessage(message.bodyAsString());
                } else {
                    byte[] bytes = message.body();
                    real.sendBinaryMessage(bytes);
                }
            }
            real.flush();
        }
    }

    private boolean isOpen = true;

    @Override
    public void close() throws IOException {
        super.close();
        if (real == null) {
            return;
        }

        isOpen = false;
        real.close();
        sessions.remove(request);
    }

    protected void onClose() {
        isOpen = false;
    }

    @Override
    public boolean isValid() {
        if(real == null){
            return false;
        }

        return isOpen;
    }

    @Override
    public boolean isSecure() {
        return request.getRequestURL().startsWith("wss:");
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return request.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return request.getLocalAddress();
    }

    private Object _attachment;

    @Override
    public void setAttachment(Object obj) {
        _attachment = obj;
    }

    @Override
    public <T> T getAttachment() {
        return (T) _attachment;
    }

    @Override
    public Collection<Session> getOpenSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _SocketServerSession that = (_SocketServerSession) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }
}
