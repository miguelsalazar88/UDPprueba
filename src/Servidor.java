import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Servidor extends JFrame {

    //ELementos UDP
    private DatagramSocket datagramSocket; //Instancia del socket
    private byte[] buffer = new byte[256]; //Instancia para el buffer

    //Elementos Ventana
    private JLabel labelServidor = new JLabel("Servidor:"); //Label del JTA
    private JTextArea textoServidor = new JTextArea(); //JTA
    private JScrollPane scrollPaneTexto = new JScrollPane(textoServidor); //Scroll Pane para el JTA

    public Servidor(DatagramSocket datagramSocket) throws IOException {
        //Log
        mensajeLog("Inicializando Servidor\n");

        //UDP
        this.datagramSocket = datagramSocket; //Se asigna el DatagramSocket a la variable

        //Ventana
        this.setSize(500,500); //Tamaño de la ventana
        this.setVisible(true); // Visibilidad de la ventana
        this.setLayout(null); // Sin layout
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //Se termina el programa al cerrar la ventana
        this.setTitle("Ventana Servidor...\n"); //Título de la ventana
        this.initComponents(); //Inicializador de los componentes de la ventana
        mensajeLog("Servidor activo en 127.0.0.1\n");

    }

    //Se inicializan los componrntes de la Ventana
    private void initComponents() {
        this.add(labelServidor); // Label del JTA
        this.labelServidor.setBounds(10,10,70,30); //Tamaño del label
        this.add(scrollPaneTexto); // Scroll Pane del JTA
        this.textoServidor.setEditable(false); //No se puede editar el JTA
        this.scrollPaneTexto.setBounds(10,50,300,300); //Tamaño del JTA

    }

    //Este metodo tiene como proposito recibir los mensajes por parte del servidor y responder
    //con una confirmación que contiene el mensaje
    public void recibirMensaje(){
        while (true){
            try{
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress(); //Direccion IP del paquete
                int puerto = datagramPacket.getPort(); //Recibe el numero del puerto del paquete
                //Se asigna el contenido del mensaje a un String para mostrarlo en el JTA
                String mensajeCliente = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                mensajeLog("Servidor recibe mensaje de Cliente (" +datagramPacket.getAddress()+ "): " + mensajeCliente + "\n");
                //Actualiza el JTA para mostrar el nuevo mensaje recibido
                this.actualizarTextoServidor(mensajeCliente);
                //Confirmación de recepción del mensaje
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, puerto);
                datagramSocket.send(datagramPacket);
            } catch (IOException e){
                e.printStackTrace();
                break;
            }

        }
    }
    public void mensajeLog(String mensajeLog) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/textoResultado.txt",true));
        bw.write(mensajeLog);
        bw.close();
    }

    //Este metodo actualiza el JTA para mostrar el contenido de texto del mensaje
    public void actualizarTextoServidor(String s){
        this.textoServidor.setText(this.textoServidor.getText() + "\n" + "Mensaje del Cliente: " + s);
    }
    //Main
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        Servidor servidor = new Servidor(datagramSocket);
        servidor.recibirMensaje();
    }

}
