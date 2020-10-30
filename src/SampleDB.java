
import java.sql.*;
import java.util.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class SampleDB {

    /*
    Visualizza tabella amici
     */
    public static void visualTab(Connection c) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM amici");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString("cognome"));
        }
        stmt.close();
    }

    public static boolean esiste(Connection c, String tab) throws SQLException {
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            if (tab.equalsIgnoreCase(rs.getString(3))) {
                return true;
            }
        }

        return false;
    }

    public static int creaTab(Connection c) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        int r = stmt.executeUpdate("CREATE TABLE `amici1` ("
                + "  `id` bigint(20) UNSIGNED NOT NULL auto_increment primary key,"
                + "  `nome` varchar(30) DEFAULT NULL,"
                + "  `cognome` varchar(20) NOT NULL"
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
        return r;
    }

    public static int insertTab(Connection c, String nom, String cog) throws SQLException {
        int r;
        Statement stmt = null;
        stmt = c.createStatement();
        r = stmt.executeUpdate(
                "insert into amici (nome,cognome) values('"
                + nom + "','"
                + cog + "')");

        /*
                String insertTableSQL = "INSERT INTO amici (nome,cognome)"
                        + "values (?,?)";
        PreparedStatement ps = conn.prepareStatement(insertTableSQL);
                ps.setString(1, nom);
                ps.setString(2, cog);
                res = ps.executeUpdate();
         */
        return r;
    }

    // restituiscecome vettore di stringhe l'elenco di campi del recordset
    
    public static String [] rsToVect(ResultSet r,String[] f) throws SQLException{
        String sr[]=new String[f.length];
        for (int i=0;i< f.length;i++)
            sr[i]=r.getString(f[i]);
        return sr;    
    }
            
    
    public static void visAlunni(Connection c) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM alunni");


        while (rs.next()) {
            System.out.println(rs.getString("cognome") + "  " + rs.getString("nome") + "  " + rs.getString("residenza")+"  " + rs.getString("classe"));
        }
        stmt.close();
    }

    public static  List <String []> ricerca01(Connection c,String cl) throws SQLException {
        String [] par={"cognome","nome","classe"};
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]>r=new ArrayList<String[]>();
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM alunni where classe ='"+cl+"'");

        while (rs.next()) 
            r.add(rsToVect(rs,par));
        stmt.close();
        return r;
        
        
        /*provare con 5BI' OR cognome<>'a
        */
    }  
    
       public static  List <String []> ricerca02(Connection c,String cl) throws SQLException {
        String [] par={"cognome","nome","classe"};
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]>r=new ArrayList<String[]>();
        
        String selectClasse = "SELECT * FROM alunni where classe =?";
        PreparedStatement ps = c.prepareStatement(selectClasse);
        ps.setString(1, cl);
        rs = ps.executeQuery();

        while (rs.next()) 
            r.add(rsToVect(rs,par));
        ps.close();
        return r;
    }  
    // visualizza la tabella 
    public static void visual(List<String[]> t){
        for (String []riga :t){
            for (String c:riga)
                System.out.print(c+" ");
            System.out.println();
        }
    }

    public static void main1(String[] arg) {
        Scanner T = new Scanner(System.in);

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/dblorenzo?user=lorenzo&password=lorenzo");

            boolean cont = true;
            while (cont) {
                if (esiste(conn, "amici")) {
                    System.out.println("Tabella esistente");
                } else {
                    System.out.println("Tabella inesistente");
                }

                System.out.print("0 Uscita\n1 visualizza\n2 Aggiungi\n3 Crea Tabella\n");
                String sc = T.nextLine();
                switch (sc.charAt(0)) {
                    case '0':
                        cont = false;
                        break;
                    case '1':
                        visualTab(conn);
                        break;
                    case '2':
                        int res;
                        String nome,
                         cognome;
                        System.out.print("Nome -->");
                        nome = T.nextLine();
                        System.out.print("Cognome -->");
                        cognome = T.nextLine();

                        res = insertTab(conn, nome, cognome);
                        System.out.println("risultato " + res);

                    case '3':

                        creaTab(conn);
                        break;            // inserimento

                }

            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }
    public static void main(String[] arg) {
        Scanner T = new Scanner(System.in);

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
            //        "jdbc:mysql://localhost/dblorenzo?user=lorenzo&password=lorenzo");
        "jdbc:mysql://172.16.1.99/db99999?user=ut99999&password=pw99999");

            boolean cont = true;
            String classe;
            while (cont) {
                if (esiste(conn, "alunni")) {
                    System.out.println("Tabella esistente");
                } else {
                    System.out.println("Tabella inesistente");
                }

                System.out.print("0 Uscita\n1 visualizza\n2 ricerca01\n3 ricerca02\n");
                String sc = T.nextLine();
                switch (sc.charAt(0)) {
                    case '0':
                        cont = false;
                        break;
                    case '1':
                        visAlunni(conn);
                        break;
                    case '2':
                        System.out.print("Cerca classe");
                        classe = T.nextLine();

                         List<String[]> l=ricerca01(conn, classe);
                         visual(l);   
                         break;
                    case '3':
                        System.out.print("Cerca classe__");
                        classe = T.nextLine();

                         l=ricerca02(conn, classe);
                         visual(l);   
                        break;            // inserimento

                }

            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }
}
