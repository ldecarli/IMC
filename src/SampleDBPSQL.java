import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
            PreparedStatement pst=conn.prepareStatement(qc);
            
            pst.setString(1,nomeArt);
            //pst.setInt(2,5000);
            
            rs=pst.executeQuery();
*/
public class SampleDBPSQL {

    public static void main(String[] arg) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        String url = "jdbc:postgresql://172.16.1.99/chinook";
        //String nomeArt="Jimmy Page";
        String nomeArt="' or true";
        //String nomeArt="' or ''='";
            
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, "ut99999", "pw99999");
            ResultSet rs;
//Query
            Statement st = conn.createStatement();
            
            String qc="select * from track "
                    + "where composer = ? ";

            String qeff="SELECT  * from track  where composer ='"+nomeArt+"";

            rs = st.executeQuery(qeff);
            // rs = st.executeQuery("SELECT  * from track  where composer ilike '"+nomeArt+"'");

            while (rs.next()) {
                System.out.println( rs.getInt(1)+" "+
                                    rs.getString(2));
                        
            }
            
            
            
            rs.close();
            st.close();
            conn.close();
            System.out.println(qeff);
    }
}
