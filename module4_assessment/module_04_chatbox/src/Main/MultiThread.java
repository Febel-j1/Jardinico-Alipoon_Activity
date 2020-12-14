/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.ArrayList;

/**
 *
 * @author Kesiah
 */
public class MultiThread extends Thread {

    //Viriables
    private Socket socket = null;
    private BufferedReader input = null;
    private PrintWriter output = null;
    private ArrayList<MultiThread> arrayOfThreads = new ArrayList<>();

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    //classess
    private Server server = new Server();

    public MultiThread(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        output = new PrintWriter(this.socket.getOutputStream(), true);
        getConnection();
    }

    public void updateArrayOfThreads(ArrayList<MultiThread> arrayOfThreads) {
        this.arrayOfThreads = arrayOfThreads;
    }

    public void SendToAll(String text) {
        for (int i = 0; i < arrayOfThreads.size(); i++) {
            this.arrayOfThreads.get(i).output.println(text);
        }
    }

    public void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatbox", "root", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void StoreLogText(String text) {
        try {
            String sql = "INSERT INTO `text`(`msg_text`) VALUES (?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, text);
            pst.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void getLogText(){
        try {
            String sql = "SELECT `msg_text` FROM `text`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                output.println(rs.getString("msg_text"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        String text = "";
        try {
            while (true) {
                updateArrayOfThreads(server.getArrayOfThreads());
                text = input.readLine();
                if (text.equals("Update")) {
                    getLogText();
                } else {
                    StoreLogText(text);
                    SendToAll(text);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
