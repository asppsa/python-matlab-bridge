package github.arokem.pymatbridge;

import org.zeromq.ZMQ;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MessengerTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public MessengerTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(MessengerTest.class);
  }

  /**
   * The constructor creates an instance; exit closes it
   */
  public void testConstructorExit() {
    Messenger m = new Messenger("tcp://127.0.0.1:5556");
    assertTrue(m instanceof Messenger);
    m.exit();
  }

  /**
   * The listen method returns a string, or null
   */
  public void testListen() throws InterruptedException {
    String addr = "tcp://127.0.0.1:5555";
    Messenger m = new Messenger(addr);

    // Nothing to receive as yet ...
    assertTrue(m.listen() == null);

    // Make a client to talk to the messenger
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.REQ);
    socket.connect(addr);
    String message = "Hello 1 2 3";
    socket.send(message.getBytes(ZMQ.CHARSET), 0);
    Thread.sleep(1000);

    // Check that the message comes through
    String resp = m.listen();
    assertTrue(resp != null);
    assertTrue(resp.equals(message));
  }
}
