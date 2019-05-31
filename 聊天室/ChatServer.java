package com.yanqun.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


//final:一旦定义，不可改变    final  int num = 3 ;
public class ChatServer {
    //clientMap:保存所有的客户端, key：客户端的名字,value:客户端连接服务端的Channel
    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8888));//将服务注册到本机Ip:8888

        Selector selector = Selector.open();
        //在服务端的选择器上，注册一个 状态为”OP_ACCEPT“的通道
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();//一直阻塞，直到OP_ACCEPT状态的Channel准备就绪
            // 创建一个用于 和 客户端交互的 Channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //如果selector中有多个通道，则需要 遍历这些通道   找到需要的那个通道
            selectionKeys.forEach(selectionKey -> {//lambda表达式
                final SocketChannel client;
                try {
                    //如果是 状态为OP_ACCEPT的通道（该通道：可以接受 客户端连接）
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();

                        client = server.accept();
                        client.configureBlocking(false);
                        //1.再向选择器中注册一个  OP_READ通道
                        client.register(selector, SelectionKey.OP_READ);
                        //2.将该建立完毕连接的 通道 保存到clientMap中
                        // Math.random(): [0,1)    [0,9000)   [1000,9999]
                        String key = "key" + (int) (Math.random() * 9000 + 1000);//客户端id：    key0.154121     ,value:客户端通道
                        clientMap.put(key, client);

                    } else if (selectionKey.isReadable()) {//如果状态是 OP_READ的通道（可读取数据）
                        client = (SocketChannel) selectionKey.channel();

                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        //将服务端读取到的客户端消息 放入readBuffer中
                        int result = client.read(readBuffer);

                        if (result > 0) {
                            readBuffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String receive = String.valueOf(charset.decode(readBuffer).array());
                            //将读取到的消息 打印...
                            System.out.println(client + ":" + receive);

                            String sendKey = null;
                            /*
                            clientMap{
                                key001,client
                                key002,client
                                key003,client
                                key004,client           client
                            }
                            */
                            //很多client在发下消息，通过for找到 是哪个clint在发
                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                if (client == entry.getValue()) {
                                    sendKey = entry.getKey();//找到 发送消息的client所对应的key
                                    break;
                                }
                            }

                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                SocketChannel eachClient = entry.getValue();

                                ByteBuffer senderBuffer = ByteBuffer.allocate(1024);

                                senderBuffer.put((sendKey + ":" + receive).getBytes());
                                senderBuffer.flip();

                                eachClient.write(senderBuffer);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            selectionKeys.clear();
        }
    }
}

