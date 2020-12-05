package org.noear.solon.boot.jetty.websocket;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.message.Session;
import org.noear.solon.core.message.Message;
import org.noear.solon.extend.socketd.MessageUtils;
import org.noear.solon.extend.socketd.MessageWrapper;
import org.noear.solon.extend.socketd.SessionBase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class _SocketServerSession extends SessionBase {
    public static Map<org.eclipse.jetty.websocket.api.Session, Session> sessions = new HashMap<>();
    public static Session get(org.eclipse.jetty.websocket.api.Session real) {
        Session tmp = sessions.get(real);
        if (tmp == null) {
            synchronized (real) {
                tmp = sessions.get(real);
                if (tmp == null) {
                    tmp = new _SocketServerSession(real);
                    sessions.put(real, tmp);
                }
            }
        }

        return tmp;
    }

    public static void remove(org.eclipse.jetty.websocket.api.Session real){
        sessions.remove(real);
    }



    org.eclipse.jetty.websocket.api.Session real;
    public _SocketServerSession(org.eclipse.jetty.websocket.api.Session real){
        this.real = real;
    }

    @Override
    public Object real() {
        return real;
    }

    private String _sessionId = Utils.guid();
    @Override
    public String sessionId() {
        return _sessionId;
    }

    @Override
    public MethodType method() {
        return MethodType.WEBSOCKET;
    }

    @Override
    public URI uri() {
        return real.getUpgradeRequest().getRequestURI();
    }

    private String _path;
    @Override
    public String path() {
        if(_path == null) {
            _path = real.getUpgradeRequest().getRequestURI().getPath();
        }

        return _path;
    }

    @Override
    public void send(String message) {
        try {
            if (Solon.global().enableWebSocketD()) {
                sendBytes(MessageUtils.encode(MessageWrapper.wrap(message.getBytes(StandardCharsets.UTF_8))));
            } else {
                real.getRemote().sendString(message);
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void send(byte[] message) {
        if (Solon.global().enableWebSocketD()) {
            sendBytes(MessageUtils.encode(MessageWrapper.wrap(message)));
        } else {
            sendBytes(ByteBuffer.wrap(message));
        }
    }

    @Override
    public void send(Message message) {
        if (Solon.global().enableWebSocketD()) {
            sendBytes(MessageUtils.encode(message));
        } else {
            sendBytes(ByteBuffer.wrap(message.body()));
        }
    }

    private void sendBytes(ByteBuffer buf) {
        try {
            real.getRemote().sendBytes(buf);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }


    private boolean _open = true;
    @Override
    public void close() throws IOException {
        _open = false; //jetty 的 close 不及时
        real.close();
        sessions.remove(real);
    }

    @Override
    public boolean isValid() {
        return _open && real.isOpen();
    }

    @Override
    public boolean isSecure() {
        return real.isSecure();
    }

    @Override
    public InetSocketAddress getRemoteAddress(){
        return real.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress(){
        return real.getLocalAddress();
    }

    private Object attachment;

    @Override
    public void setAttachment(Object obj) {
        attachment = obj;
    }

    @Override
    public <T> T getAttachment() {
        return (T)attachment;
    }

    @Override
    public Collection<Session> getOpenSessions() {
        return new ArrayList<>(sessions.values());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _SocketServerSession that = (_SocketServerSession) o;
        return Objects.equals(real, that.real);
    }

    @Override
    public int hashCode() {
        return Objects.hash(real);
    }
}
