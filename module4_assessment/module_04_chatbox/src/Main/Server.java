/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Kesiah
 */
public class Server {
    
    private static int PORT = 4356;
    private static ArrayList<MultiThread> arrayOfThreads = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        Socket socket = null;
        JOptionPane.showMessageDialog(null, "Server is running!");
        while(true){
            socket = server.accept();
            JOptionPane.showMessageDialog(null, "Client has been accepted!");
            
            MultiThread multi = new MultiThread(socket);
            arrayOfThreads.add(multi);
            multi.start();
        }
        
    }
    
    public ArrayList<MultiThread> getArrayOfThreads(){
        return arrayOfThreads;
    }
    

}
