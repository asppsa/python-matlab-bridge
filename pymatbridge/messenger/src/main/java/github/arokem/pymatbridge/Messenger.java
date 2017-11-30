package github.arokem.pymatbridge;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

/**
 * Hello world!
 *
 */
public class Messenger {
  ZContext ctx;
  Socket socket;
  byte[] buffer;

  int BUFFLEN = 20000;

  public Messenger(String socket_addr) {
    ctx = new ZContext();
    socket = ctx.createSocket(ZMQ.REP);
    socket.bind(socket_addr);
    socket.setReceiveTimeOut(20);

    buffer = new byte[BUFFLEN];
  }

  public String listen() {
    int received = socket.recv(buffer, 0, BUFFLEN, 0);

    if (received > 0) {
      return new String(buffer, 0, received, ZMQ.CHARSET);
    }
    else {
      return null;
    }
  }

  public void respond(String data) {
    socket.send(data.getBytes(ZMQ.CHARSET), 0);
  }

  public void exit() {
    socket.close();
    ctx.close();
  }
}
