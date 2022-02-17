import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Servidor extends JFrame {

    //ELementos UDP
    private DatagramSocket datagramSocket;
    private byte[] buffer = new byte[256];

    //Elementos Ventana
    private JLabel labelServidor = new JLabel("Servidor:");
    private JTextArea textoServidor = new JTextArea();
    private JScrollPane scrollPaneTexto = new JScrollPane(textoServidor);

    public Servidor(DatagramSocket datagramSocket) {
        //UDP
        this.datagramSocket = datagramSocket;

        //Ventana
        this.setSize(500,500);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Ventana Servidor");
        this.initComponents();
    }

    //Se inicializan los componrntes de la Ventana
    private void initComponents() {
        this.add(labelServidor);
        this.labelServidor.setBounds(10,10,70,30);
        this.add(scrollPaneTexto);
        this.textoServidor.setEditable(false);
        this.scrollPaneTexto.setBounds(10,50,300,300);

    }

    public void recibirMensaje(){
        while (true){
            try{
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress();
                int puerto = datagramPacket.getPort();
                String mensajeCliente = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                this.actualizarTextoServidor(mensajeCliente);
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, puerto);
                datagramSocket.send(datagramPacket);
            } catch (IOException e){
                e.printStackTrace();
                break;
            }

        }
    }

    public void actualizarTextoServidor(String s){
        this.textoServidor.setText(this.textoServidor.getText() + "\n" + "Mensaje del Cliente: " + s);
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        Servidor servidor = new Servidor(datagramSocket);
        servidor.recibirMensaje();
    }

}
