package com.study.demo.communication.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by heyanwei-thinkpad on 2019/2/6.
 */
public class Server1 {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket();
        SocketAddress socketAddress = new InetSocketAddress("",8080);
        int backlog = 2;
        serverSocket.bind(socketAddress,backlog);
        Socket socket ;
        while(true){
//            Thread.sleep(100000000);
            socket = serverSocket.accept();
            System.out.println("new connection is comming... 端口号: "+socket.getLocalPort());
            processConnection(socket);
        }
    }

    private static void processConnection(final Socket socket) throws IOException {
        new Thread(()->{
            InputStream inputStream;
            PrintStream printWriter = null;
            try{
                inputStream =
                        socket.getInputStream();
                printWriter = new PrintStream(System.out);
                byte[] buf = new byte[1024];
                int len;
                while(true ){
                    len = inputStream.read(buf);//实现是SocketInputStream,在read方法中调用 socketRead 时阻塞。
                    System.out.println("len -> "+len);
                    if(len != -1){
                        printWriter.write(buf,0,len);
                    }else{
                        break;
                    }
                }
            }catch (Exception e){

            }
           finally {
                try {
                    printWriter.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
