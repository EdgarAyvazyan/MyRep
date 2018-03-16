package com.mainserver.projects;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.sql.*;
import java.util.Scanner;

public class Phonebook {
   private Scanner scanner = new Scanner(System.in);

    public void run(){
        try {
            String command;
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/phonebook","root",null);
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS contacts (" +
            "id int auto_increment primary key," + "name varchar(50)," + "lastName varchar(50)," + "phoneNumber varchar(50))");
            System.out.println("\n\n***** Welcome to to MyPhonebook\n\n");
            String help_msg = "Press:H for open help panel,A for adding contact,S for searching contact,E for editing contact,Q for exit";
            System.out.println("[Main menu] " + help_msg + "\n");
            boolean msg = false;
            while (true){
                command = scanner.next();
                if (msg){
                    System.out.println(help_msg);
                }

                if (command.trim().length() > 1){
                    msg = true;
//                    continue;
                }
                if (command.equalsIgnoreCase("H")){
                    msg = true;
                }
                else if(command.equalsIgnoreCase("A")){
                    msg = true;
                    System.out.println("Type contact details in format: name,lastName,phoneNumber \n");
                    while (true){
                        String data = scanner.next().trim();
                        String [] tempArray = data.split(",");
                        if (tempArray.length != 3){
                            System.out.println("Error,the insertion format should be in the format:name,lastName,phoneNumber");
                            continue;
                        }
                        conn.createStatement().executeUpdate("insert into contacts(name,lastName,phoneNumber) values('" + tempArray[0] + "','" + tempArray[1] + "','" + tempArray[2] + "')");
                        break;
                    }
                }else if(command.equalsIgnoreCase("S")){
                    msg = true;
                    System.out.println("Type in the name you are searching for :\n");
                    String data = scanner.next().trim();
                    String query = "select * from contacts where name like '%" + data + "%' or lastName like '%" + data + "%' or phoneNumber like '%" + data + "%'";
                    PreparedStatement pst = conn.prepareStatement(query);
//                    pst.clearParameters();
                    ResultSet rs = pst.executeQuery();
                    System.out.println("************Results*************");
                    System.out.println("ID\t\t\tName\t\t\tLastName\t\t\tPhoneNumber\n");
                    while (rs.next()){
                        int id = rs.getInt("id");
                        String name = rs.getString(2);
                        String lastName = rs.getString(3);
                        String phone = rs.getString(4);
                        System.out.printf("%d\t\t\t%s\t\t\t%s\t\t\t%s",id,name,lastName,phone + "\n");
                    }
                }else if (command.equalsIgnoreCase("Q")){
//                    Statement st = conn.createStatement();
//                    st.execute("SHUTDOWN");
                    conn.close();
                    System.out.println("Good Bye user");
                    System.exit(0);
                }else {
                    String errorMsg = "Unknown command ! Try again:";
                    System.out.println(errorMsg);
                    msg = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
