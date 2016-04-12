package udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.ExpiringSessionRecycler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class MemoryMonitorTest {

	public static final int PORT = 8088;

	public MemoryMonitorTest() throws IOException {

		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();// ����һ��UDP�Ľ�����
		acceptor.setHandler(new YourHandler());// ���ý������Ĵ������

		Executor threadPool = Executors.newFixedThreadPool(1500);// �����̳߳�
		acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.setSessionRecycler(new ExpiringSessionRecycler(120));
		
		
		DatagramSessionConfig dcfg = acceptor.getSessionConfig();// �������ӵ������ļ�
		dcfg.setReadBufferSize(4096);// ���ý�������ֽ�Ĭ��2048
		dcfg.setReceiveBufferSize(1024);// �������뻺�����Ĵ�С
		dcfg.setSendBufferSize(1024);// ��������������Ĵ�С
		dcfg.setReuseAddress(true);// ����ÿһ�������������ӵĶ˿ڿ�������

		dcfg.setIdleTime(IdleStatus.BOTH_IDLE, 5);
		acceptor.bind(new InetSocketAddress(PORT));// �󶨶˿�
		System.out.println("udp ������");

	}

	public static void main(String[] args) throws IOException {
		new MemoryMonitorTest();
	}
}