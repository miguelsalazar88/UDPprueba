import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente extends JFrame implements ActionListener {

    //Elementos del Protocolo UDP
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    //Elementos de la Ventana
    private JLabel labelCliente = new JLabel("Cliente:");
    private JButton botonEnviar = new JButton("Enviar");
    private JTextArea textoCliente = new JTextArea();
    private JScrollPane scrollPaneTexto = new JScrollPane(textoCliente);
    private JTextField JTFCliente = new JTextField();


    public Cliente(DatagramSocket datagramSocket, InetAddress inetAddress) {

        //UDP
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;

        //Ventana
        this.setSize(500,500);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Ventana Cliente");
        this.initComponents();

    }

    private void initComponents() {
        this.add(labelCliente);
        this.labelCliente.setBounds(10,10,70,30);
        this.add(scrollPaneTexto);
        this.textoCliente.setEditable(false);
        this.scrollPaneTexto.setBounds(10,50,300,300);
        this.add(JTFCliente);
        this.JTFCliente.setBounds(10,360,300,30);
        this.add(botonEnviar);
        this.botonEnviar.setBounds(10,400,100,30);
        this.botonEnviar.addActionListener(this);

    }

    public void enviarMensaje(){
            try{
                String mensaje = this.JTFCliente.getText();
                buffer = mensaje.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length, inetAddress, 1234);
                datagramSocket.send(datagramPacket);
                datagramSocket.receive(datagramPacket);
                String mensajeDelServidor = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                this.actualizarTextoCliente(mensajeDelServidor);
            } catch (IOException e){
                e.printStackTrace();
            }

    }

    public void actualizarTextoCliente(String s){
        this.textoCliente.setText(this.textoCliente.getText() + "\n" + "El servidor recibió el mensaje: " + s);
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Cliente cliente = new Cliente(datagramSocket, inetAddress);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.botonEnviar)){
            this.enviarMensaje();
            this.JTFCliente.setText("");

        }
    }
}
