package com.syk;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import javax.swing.JFrame;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public final class App extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;
    private ArrayList<String> instr;
    private int instrNum;

    private App() {
        super();
        instrNum = 0;
        instr = new ArrayList<String>();
        terminal = new AsciiPanel(40, 60, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new Screen();
        addKeyListener(this);
        repaint();
    }

    public void startClient() throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        SocketChannel client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);
        while (true) {
            if (instrNum < instr.size()) {
                String instruction = instr.get(instrNum);
                instrNum++;
                ByteBuffer buffer = ByteBuffer.allocate(74);
                buffer.put(instruction.getBytes());
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            } else {
                ByteBuffer buffer = ByteBuffer.allocate(74);
                buffer.put(new String("!").getBytes());
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            }
            ByteBuffer recieveBuffer = ByteBuffer.allocate(10240);
            int numRead = -1;
            numRead = client.read(recieveBuffer);
            if (numRead > 0) {
                byte[] data = new byte[numRead];
                System.arraycopy(recieveBuffer.array(), 0, data, 0, numRead);
                screen.totalInstr = new String(data);
                screen.parse();
                this.repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void readFromFileLog(String filePath) throws IOException {
        InputStream fileInput = new FileInputStream(filePath);
        byte[] bytes = new byte[262144];
        int len = 0;
        String logs = "";
        while ((len = fileInput.read(bytes)) != -1) {
            logs = logs + new String(bytes, 0, len);
        }
        fileInput.close();

        String[] log = logs.split("#");
        try {
            for (int i = 0; i < log.length; i++) {
                screen.totalInstr = log[i];
                screen.parse();
                this.repaint();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //todo
        this.instr.add(String.valueOf(e.getKeyChar()));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        App app = new App();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        if (args.length == 0) {
            Runnable client = new Runnable() {
                @Override
                public void run() {
                    try {
                        app.startClient();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(client).start();
        } else {
            try {
                app.readFromFileLog(args[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
