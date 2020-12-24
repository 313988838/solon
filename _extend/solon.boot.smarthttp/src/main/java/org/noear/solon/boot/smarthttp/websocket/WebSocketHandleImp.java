package org.noear.solon.boot.smarthttp.websocket;

import org.noear.solon.Solon;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.message.Session;
import org.noear.solon.extend.socketd.ListenerProxy;
import org.noear.solon.extend.socketd.ProtocolManager;
import org.smartboot.http.WebSocketRequest;
import org.smartboot.http.WebSocketResponse;
import org.smartboot.http.server.handle.WebSocketDefaultHandle;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class WebSocketHandleImp extends WebSocketDefaultHandle {

    @Override
    public void onHandShark(WebSocketRequest request, WebSocketResponse response) {
        ListenerProxy.getGlobal().onOpen(_SocketServerSession.get(request, response));
    }

    @Override
    public void onClose(WebSocketRequest request, WebSocketResponse response) {
        _SocketServerSession session = _SocketServerSession.get(request, response);
        session.onClose();

        ListenerProxy.getGlobal().onClose(session);

        _SocketServerSession.remove(request);
    }

    @Override
    public void handleTextMessage(WebSocketRequest request, WebSocketResponse response, String data) {
        try {
            Session session = _SocketServerSession.get(request, response);
            Message message = Message.wrap(request.getRequestURI(),null,
                    data.getBytes(StandardCharsets.UTF_8));

            ListenerProxy.getGlobal().onMessage(session, message.isString(true));
        } catch (Throwable ex) {
            EventBus.push(ex);
        }
    }

    @Override
    public void handleBinaryMessage(WebSocketRequest request, WebSocketResponse response, byte[] data) {
        try {
            Session session = _SocketServerSession.get(request, response);
            Message message = null;

            if (Solon.global().enableWebSocketD()) {
                message = ProtocolManager.decode(ByteBuffer.wrap(data));
            } else {
                message = Message.wrap(request.getRequestURI(), null, data);
            }

            ListenerProxy.getGlobal().onMessage(session, message);

        } catch (Throwable ex) {
            EventBus.push(ex);
        }
    }

    @Override
    public void onError(Throwable error) {
//        if (listener != null) {
//            listener.onError(_SocketSession.get(request,response), error);
//        }
    }
}
