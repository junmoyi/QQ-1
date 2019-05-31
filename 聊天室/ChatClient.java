package com.yanqun.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// enum Color{BLACK,WHITE,BLUE}        Color.BLACK

public class ChatClient {
    public static void main(String[] args){
        try {
            //客户端获取一个通道
            SocketChannel socketChannel = SocketChannel.open();

            //切换到非阻塞式
            socketChannel.configureBlocking(false);

            //打开选择器
            Selector selector = Selector.open();

            //  通道.register(选择器,连接状态);               enum  SelectionKey(OP_CONNECT,..)
            socketChannel.register(selector, SelectionKey.OP_CONNECT);//对应于服务端的 OP_ACCEPT

            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

            while (true) {
                selector.select();//一直阻塞（等待），直到 有一个通道处于就绪状态

                //selectionKeys：包含了 所有通道与选择器之间的关系（连接、读、写）
                Set<SelectionKey> selectionKeys = selector.selectedKeys();


                for (SelectionKey selectionKey : selectionKeys) {

                    if (selectionKey.isConnectable()) {//判断是否连接成功

                        //创建一个用于 和 服务端交互的 Channel
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        if (client.isConnectionPending()) {//健壮性
                            client.finishConnect();//建立好了连接，本方法在底层会标记 client.isConnectionPending()为false

                            ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
                            //以下3行：客户端接受键盘敲入的文字  hello world，等价于input.nextLine();
                            InputStreamReader input = new InputStreamReader(System.in);
                            BufferedReader br = new BufferedReader(input);
                            String sendMessage = br.readLine();

                            //将键盘敲入的内容，放入到Buffer中
                            sendBuffer.put(sendMessage.getBytes());//get()

                            //position归零
                                                      sendBuffer.flip();
                            //write()：将sendBuffer发送给服务端
                            client.write(sendBuffer);

                            //以上代码，完成了 客户端向服务端发送消息

//                            ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
//                            executorService.submit(() -> {
//                                while (true) {
//                                    try {
//                                        sendBuffer.clear();
//                                        InputStreamReader reader = new InputStreamReader(System.in);
//                                        BufferedReader bReader = new BufferedReader(reader);
//                                        String message = bReader.readLine();
//
//                                        sendBuffer.put(message.getBytes());
//                                        sendBuffer.flip();
//                                        client.write(sendBuffer);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });

                        }
                            client.register(selector, SelectionKey.OP_READ);//客户端给服务端发送完毕后，立刻给选择器注册一个 读通道，用于 接收服务端的反馈消息
                        } else if (selectionKey.isReadable()) {//判断此时 是否可以从服务端读数据
                            //客户端读取服务端的反馈消息
                            SocketChannel client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            //将服务端的反馈消息 放入readBuffer中
                            int len = client.read(readBuffer);//服务说：ByteBuffer类型的"你也好"

                            if (len > 0) {
                                //将 ByteBuffer类型的"你也好"  转为String类型的“你也好”
                                String receive = new String(readBuffer.array(), 0, len);
                                System.out.println(receive);
                            }

                        }
                    }
                    selectionKeys.clear();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
