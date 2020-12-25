package org.noear.solon.extend.socketd;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.message.Session;
import org.noear.solon.extend.socketd.util.HeaderUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

/**
 * SocketD 上下文
 *
 * @author noear
 * @since 1.0
 * */
public class SocketContext extends ContextEmpty {
    private InetSocketAddress _inetSocketAddress;
    private Session _session;
    private Message _message;
    private boolean _messageIsString;
    private MethodType _method;

    public SocketContext(Session session, Message message) {
        _session = session;
        _message = message;
        _messageIsString = message.isString();
        _method = session.method();
        _inetSocketAddress = session.getRemoteAddress();

        if (Utils.isNotEmpty(message.header())) {
            Map<String,String> headerMap = HeaderUtil.decodeHeaderMap(message.header());
            headerMap().putAll(headerMap);
        }
    }


    @Override
    public Object request() {
        return _session;
    }

    @Override
    public String ip() {
        if (_inetSocketAddress == null)
            return null;
        else
            return _inetSocketAddress.getAddress().toString();
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public String method() {
        return _method.name;
    }

    @Override
    public String protocol() {
        if (_method == MethodType.WEBSOCKET) {
            return "WS";
        } else {
            return "SOCKET";
        }
    }

    @Override
    public URI uri() {
        if (_uri == null) {
            _uri = URI.create(url());
        }

        return _uri;
    }

    private URI _uri;

    @Override
    public String path() {
        return uri().getPath();
    }


    @Override
    public String url() {
        return _message.resourceDescriptor();
    }

    @Override
    public long contentLength() {
        if (_message.body() == null) {
            return 0;
        } else {
            return _message.body().length;
        }
    }

    @Override
    public String contentType() {
        return headerMap().get("Content-Type");
    }

    @Override
    public String queryString() {
        return uri().getQuery();
    }

    @Override
    public InputStream bodyAsStream() throws IOException {
        return new ByteArrayInputStream(_message.body());
    }

    //==============

    @Override
    public Object response() {
        return _session;
    }

    @Override
    public void contentType(String contentType) {
        headerSet("Content-Type", contentType);
    }

    ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();

    @Override
    public OutputStream outputStream() {
        return _outputStream;
    }

    @Override
    public void output(byte[] bytes) {
        try {
            _outputStream.write(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void output(InputStream stream) {
        try {
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = stream.read(buff, 0, 100)) > 0) {
                _outputStream.write(buff, 0, rc);
            }

        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void commit() throws IOException {
        if (_session.isValid()) {
            if (_messageIsString) {
                _session.send(_outputStream.toString());
            } else {
                Message msg = Message.wrapResponse(_message, _outputStream.toByteArray());
                _session.send(msg);
            }
        }
    }

    @Override
    public void close() throws IOException {
        _session.close();
    }
}
