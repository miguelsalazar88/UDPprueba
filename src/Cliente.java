import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;


//Clase Cliente: Cuenta con una ventana para enviar mensajes y confirmar recepción por parte
//del servidor.
public class Cliente extends JFrame implements ActionListener {

    //Elementos del Protocolo UDP
    private DatagramSocket datagramSocket; //Instancia de socket para el envío de datos.
    private InetAddress inetAddress; // Instancia de Dirección IP
    private byte[] buffer; // Buffer de envío de datos para el protocolo UDP

    //Elementos de la Ventana
    private JLabel labelCliente = new JLabel("Cliente:"); //Label del JTArea
    private JButton botonEnviar = new JButton("Enviar"); // Boton para enviar mensaje
    private JTextArea textoCliente = new JTextArea(); //JTArea para mostrar el contenido del mensaje
    private JScrollPane scrollPaneTexto = new JScrollPane(textoCliente); // Scroll Pane para el JTA
    private JTextField JTFCliente = new JTextField(); //JTF para escribir el mensaje que se va a enviar.

    //Constructor del Objeto CLiente
    public Cliente(DatagramSocket datagramSocket, InetAddress inetAddress) {

        //UDP
        this.datagramSocket = datagramSocket; //Se asigna el socket a la variable
        this.inetAddress = inetAddress; // Se asigna la dirección IP a la variable

        //Ventana
        this.setSize(500,500); //Tamaño de la ventana
        this.setVisible(true); // Visibilidad de la ventana
        this.setLayout(null); // Sin layout
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //Se termina el programa cuando se cierra la ventana
        this.setTitle("Ventana Cliente"); // Título de la ventana
        this.initComponents(); //Método que inicia los componentes del JFrame

    }

    private void initComponents() {
        this.add(labelCliente); //Label del JTA
        this.labelCliente.setBounds(10,10,70,30); //Tamaño del label
        this.add(scrollPaneTexto); // Scroll pane para el JTA y poder ver todo el texto
        this.textoCliente.setEditable(false); // Se deja el JTA sin opción de editar
        this.scrollPaneTexto.setBounds(10,50,300,300); // Tamaño del JTA
        this.add(JTFCliente); //JTexto Field para escribir el contenido del mensaje
        this.JTFCliente.setBounds(10,360,300,30); // Tamaño del JTF
        this.add(botonEnviar); // Boton para enviar el mensaje
        this.botonEnviar.setBounds(10,400,100,30); //Tamaño del boton enviar
        this.botonEnviar.addActionListener(this); //Se agrega un Actionlistener al boton enviar

    }

    //Este metodo convierte el mensaje a Bytes y lo empaqueta para enviarlo al servidor por el
    //puerto 1234
    public void enviarMensaje(){
            try{

                String mensaje = this.JTFCliente.getText(); //Recibe el mensaje del JTF
                buffer = mensaje.getBytes(); //Se convierte el mensaje a Bytes

                //Se crea una instancia de DatagramPacket para poder encapsular el mensaje, y enviarlo por el puerto
                // 1234
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length, inetAddress, 1234);

                //Se envía el mensaje a través del Socket
                datagramSocket.send(datagramPacket);


                // Se espera la confirmación de la recepción del mensaje por parte del servidor
                datagramSocket.receive(datagramPacket);

                //Recibe el DatagramPacket que contiene la confirmación de recepción del mensaje por parte
                //del servidor
                String mensajeDelServidor = new String(datagramPacket.getData(),0,datagramPacket.getLength());

                //Se actualiza el JTA para mostrar el mensaje recibido
                this.actualizarTextoCliente(mensajeDelServidor);
            } catch (IOException e){
                e.printStackTrace();
            }

    }

    //Este metodo actualiza el JTextArea que contiene la confirmación de los mensajes enviados
    //al servidor
    public void actualizarTextoCliente(String s){
        this.textoCliente.setText(this.textoCliente.getText() + "\n" + "El servidor recibió el mensaje: " + s);
    }

    //Main
    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Cliente cliente = new Cliente(datagramSocket, inetAddress);

    }

    //Método para ejecutar acciones cuando se presiona el botón de enviar el mensaje
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.botonEnviar)){
            this.enviarMensaje(); //Envía el mensaje
            this.JTFCliente.setText(""); //Borra el contenido del JTF
        }
    }
}
