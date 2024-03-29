package database;

import assets.Author;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;

@Author("Josuan Leonardo Hulom")
public class UserManagement extends DbConnection{
    public final String table = "userstable";
    
    public final String[] columns = {"userId","firstname","lastname","username","password",
        "birthdate","gender","profileImgPath","userType"};
    
    public final String[] listOfUserType = {"Admin","Seller"};
    
    private final Component component;
    
    public UserManagement(Component component){
        this.component = component;
    }
    
    public String imageNameGenerator(String fname,String date) {
        return date +"_"+ fname + "_wan.jpg";
    } 
    
    public String userIDGenerator(String user_type) throws SQLException{
        String query;
        String id_type;
        if (user_type.equals(listOfUserType[0])) {
            query = "SELECT COUNT(*) AS count FROM " + table + " WHERE "+columns[0]+" LIKE 'superuser%'";
            id_type = "superuser";
        } else if (user_type.equals(listOfUserType[1])) {
            query = "SELECT COUNT(*) AS count FROM " + table + " WHERE "+columns[0]+" LIKE 'user%'";
            id_type = "user";
        } else {
            throw new IllegalArgumentException("Invalid user_type: " + user_type);
        }
        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            if (result.next()) {
                int count = result.getInt("count")+1;
                String userID = String.format(id_type + "%03d.wan", count);
                return userID;
            } else {
                JOptionPane.showMessageDialog(component, "No records found in the table.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }finally {
            prepare.close();
            result.close();
        }
        return null;
    }
    
    public boolean checkCurrentUser(String value) throws SQLException {
        String query = "SELECT " + columns[0] + " FROM " + table +
                       " WHERE " + columns[0] + " = ?";
        try {
            prepare = connection.prepareStatement(query);
            prepare.setString(1, value);
            result = prepare.executeQuery();

            if (result.next()) {
                return true;
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return false;
    }    
    
    public short checkUserCredentials(String u_n, String u_p) throws SQLException {
        String query = "SELECT * FROM " + table;
        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            while (result.next()) {
                String username = result.getString(columns[3]);
                String password = result.getString(columns[4]);

                if (u_n.equals(username) && u_p.equals(password)) {
                    return 1;
                } else if (u_n.equals(username)) {
                    return 2; 
                } else if (u_p.equals(password)) {
                    return 3; 
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(component, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            prepare.close();
            result.close();  
        }
        return 0; 
    }
    
    public short checkUserCredentials(String userID) throws SQLException {
        String query = "SELECT * FROM " + table + " WHERE "+columns[0]+" = ?";

        try {
            prepare = connection.prepareStatement(query);
            prepare.setString(1, userID);
            result = prepare.executeQuery();

            if (result.next()) {
                return 1; 
            } else {
                return 0; 
            }
        }finally {
            prepare.close();
            result.close();
        }
    }
    
    public String getUserId(String u_name,String u_pass) throws SQLException{
        String query = "SELECT "+columns[0]+" FROM "+table+" WHERE "
                + columns[3] + " = ? AND "+columns[4]+" = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, u_name);
            prepare.setString(2, u_pass);
            
            result = prepare.executeQuery();
            
            if(result.next()){
                return result.getString(columns[0]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    }  
    
    public String getImagePath(String _id) throws SQLException{
        String query = "SELECT "+columns[7]+" FROM "+table+" WHERE "
                + columns[0] + " = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, _id);
            result = prepare.executeQuery();
            if(result.next()){
                return result.getString(columns[7]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    }
    
    public String getFName(String _id) throws SQLException{
        String query = "SELECT firstname FROM "+table+" WHERE userId = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, _id);          
            result = prepare.executeQuery();
            if(result.next()){
                return result.getString(columns[1]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    }        
        
    public String getLName(String _id) throws SQLException{
        String query = "SELECT "+columns[2]+" FROM "+table+" WHERE "
                + columns[0] + " = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, _id);
            result = prepare.executeQuery();
            
            if(result.next()){
                return result.getString(columns[2]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    }  
    
    public String getUName(String _id) throws SQLException{
        String query = "SELECT "+columns[3]+" FROM "+table+" WHERE "
                + columns[0] + " = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, _id);
            result = prepare.executeQuery();
            if(result.next()){
                return result.getString(columns[3]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    } 
    
    public String getGender(String _id) throws SQLException{
        String query = "SELECT "+columns[6]+" FROM "+table+" WHERE "
                + columns[0] + " = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, _id);
            result = prepare.executeQuery();
            if(result.next()){
                return result.getString(columns[6]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    }
    
    public String getUserType(String id) throws SQLException{
        String query = "SELECT "+columns[8]+" FROM "+table+
                " WHERE "+columns[0]+" = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, id);
            result = prepare.executeQuery();
            if(result.next()){
                return result.getString(columns[8]);
            }
        }finally{
            prepare.close();
            result.close();  
        }
        return null;        
    } 
    
    public void updateStringData(String newVal,Object id,int column_index) throws SQLException{
        String query = "UPDATE "+table+" SET "+columns[column_index]+" = ? WHERE "+columns[0]+" = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, newVal);
            prepare.setObject(2, id);
            prepare.executeUpdate();
        }finally{
            prepare.close();
        }
    }  
    
    public void addUser(String user_ID, String f_name, String l_name, String u_name, String password, String bdate, String gender, String img_name, String user_type) throws SQLException{
        String query = "INSERT INTO "+table+" ("+String.join(", ", columns)+") VALUES (?,?,?,?,?,?,?,?,?)";
        
        try {
            prepare = connection.prepareStatement(query);
            prepare.setString(1, user_ID);
            prepare.setString(2, f_name);
            prepare.setString(3, l_name); 
            prepare.setString(4, u_name); 
            prepare.setString(5, password); 
            prepare.setString(6, bdate);
            prepare.setString(7, gender);
            prepare.setString(8, img_name);
            prepare.setString(9,user_type);
            
            prepare.executeUpdate();
        }finally{
            prepare.close();
        }     
    }
    
    public void DeleteUser(Object ID) throws SQLException {
        String query = "DELETE FROM "+table+" WHERE "+columns[0]+" = ?";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setObject(1, ID);
            prepare.executeUpdate();
        }finally{
            prepare.close();
        }
    }   
    
    public int getHowManyUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM " + table;
        int userCount = 0;

        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            if (result.next()) {
                userCount = result.getInt(1);
            }
        }finally{
            prepare.close();
            result.close();
        }
        return userCount;
    }
}
