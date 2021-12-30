package com.syk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Hello world!
 */
public final class App {
    Map<SocketAddress, Integer> dictionary;
    Game game;
    private Selector selector;

    private InetSocketAddress listenAddress;
    private final static int PORT = 9093;

    private int[] logIndex;

    public App(String address, int port) throws IOException {
        game = new Game();
        Thread t = new Thread(game);
        t.start();
        listenAddress = new InetSocketAddress(address, PORT);
        logIndex = new int[4];
        logIndex[0] = 0;
        logIndex[1] = 0;
        logIndex[2] = 0;
        logIndex[3] = 0;
        dictionary = new HashMap<SocketAddress,Integer>();
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            new App("localhost", 9093).startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        while(!game.isEnd()) {
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();

                iterator.remove();

                if(!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                }
            }

        }
        FileOutputStream fileOut = new FileOutputStream("log");
        fileOut.write(game.getLog().getBytes());
        fileOut.close();
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();

        int index = game.addPlayerToPool();
        if(index != -1) {
            dictionary.put(remoteAddr, index);
            logIndex[index] = game.logs.size();
            System.out.println("get connect");
        }
        
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();

        if (numRead == -1) {
            channel.close();
            dictionary.remove(remoteAddr);
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        int index = dictionary.get(remoteAddr);
        String temp = new String(data);

        if(logIndex[index] >= game.logs.size()) {
            return;
        }
        ByteBuffer buff = ByteBuffer.allocate(10240);
        buff.put(game.logs.get(logIndex[index]).getBytes());
        logIndex[index]++;
        buff.flip();
        channel.write(buff);
        buff.clear();
        if (!temp.equals("!")) {
            if(game.players[index] != null) {
                game.players[index].inputInstr(temp);
            }
        }
    }
}
