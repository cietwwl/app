package udp;

import java.net.SocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class YourHandler extends IoHandlerAdapter {

	// messageSent��Server��Ӧ��Clinet�ɹ��󴥷����¼�
	//
	// @Override
	// public void messageSent(IoSession session, Object message) throws Exception {
	// if (message instanceof IoBuffer) {
	// IoBuffer buffer = (IoBuffer) message;
	// byte[] bb = buffer.array();
	// for (int i = 0; i < bb.length; i++) {
	// System.out.print((char) bb[i]);
	// }
	// }
	// }

	// �׳��쳣�������¼�

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.close(true);
	}

	// Server���յ�UDP���󴥷����¼�

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// System.out.println("messageReceived");
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			byte[] bb = buffer.array();
			System.out.println("�յ����ݳ��ȣ�" + bb.length);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bb.length; i++) {
				sb.append((char) bb[i]);
			}
			System.out.println(sb.toString());

			IoBuffer buffer1 = IoBuffer.wrap("11111".getBytes());// ������Ϣ��Clinet��
			session.write(buffer1);

			// ��������message����ΪIoBuffer����
		}

	}

	// ���ӹرմ������¼�

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed...");
	}

	// �������Ӵ������¼�

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Session created...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		System.out.println(remoteAddress);

	}

	// �Ự����

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("Session idle...");
		IoBuffer buffer1 = IoBuffer.wrap("aaaaa".getBytes());// ������Ϣ��Clinet��
		session.write(buffer1);		
	}

	// �����Ӵ������¼�������sessionCreated���������ڣ�һ�����ӵ�ַ��A����һ������Server�Ὠ��һ��SessionĬ�ϳ�ʱʱ��Ϊ1���ӣ���ʱ��δ�ﵽ��ʱʱ��������ӵ�ַ��A����һ����Server����������sessionOpened�����ӵ�ַ��A����һ����Server��������������ӳ�ʱ����Server��������ʱ��ͬʱ����sessionCreated��sessionOpened�����¼���

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("Session Opened...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		System.out.println(remoteAddress);
	}

}
