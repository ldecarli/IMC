
import java.sql.*;
import java.util.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class JMysq01 {

    /**
     * Sulla connessione c visualizza la tabella tab
     *
     * @param c
     * @param tab
     * @throws SQLException
     */
    public static void visualTab(Connection c, String tab) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;

        String tableSQL = "select * from " + tab;
        stmt = c.createStatement();
        rs = stmt.executeQuery(tableSQL);
        
        ResultSetMetaData md;
        md = rs.getMetaData();
        int nc = md.getColumnCount();
        for (int i = 1; i <= nc; i++) {
            System.out.print(md.getColumnName(i) + " ");
        }
        System.out.println();
        

        while (rs.next()) {
            for (int i = 1; i <= nc; i++) {
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }
        stmt.close();
    }

    /**
     * True se la tabella tab esiste sulla connessione c
     *
     * @param c
     * @param tab
     * @return
     * @throws SQLException
     */
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
        String create = "CREATE TABLE prodotti ("
                + "  cod varchar(10) primary key,"
                + "  descr varchar(30) DEFAULT NULL,"
                + "  prezzo numeric (10,4) NOT NULL,"
                + "  scadenza datetime NOT NULL,"
                + "  qta numeric (10,4),"
                + " um varchar(10)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
        System.out.println(create);
        int r = stmt.executeUpdate(create);
        return r;
    }

    public static int removeTab(Connection c) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String create = "DROP TABLE prodotti";
        int r = stmt.executeUpdate(create);
        return r;
    }

    public static int insertProdotti(Connection c, String cod, String descr, String prezzo, String scadenza, String qta, String um) throws SQLException {
        int r;
        String insertTableSQL = "INSERT INTO prodotti "
                + "values (?,?,?,?,?,?)";
        PreparedStatement ps = c.prepareStatement(insertTableSQL);
        
        ps.setString(1, cod);
        ps.setString(2, descr);
        ps.setString(3, prezzo);
        ps.setString(4, scadenza);
        ps.setString(5, qta);
        ps.setString(6, um);
        r = ps.executeUpdate();
        return r;
    }

    // restituiscecome vettore di stringhe l'elenco di campi del recordset
    public static String[] rsToVect(ResultSet r, String[] f) throws SQLException {
        String sr[] = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            sr[i] = r.getString(f[i]);
        }
        return sr;
    }

    public static void visProdotti(Connection c) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM prodotti");

        while (rs.next()) {
            
            System.out.println(rs.getString(1)+rs.getString("cod") + "  " + rs.getString("descr") + "  "
                    + rs.getString("prezzo") + "  " + rs.getString("qta") + "  " + rs.getString("scadenza"));
        }
        stmt.close();
    }

    public static List<String[]> ricerca01(Connection c, String cl) throws SQLException {
        String[] par = {"cognome", "nome", "classe"};
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]> r = new ArrayList<String[]>();
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM alunni where classe ='" + cl + "'");

        while (rs.next()) {
            r.add(rsToVect(rs, par));
        }
        stmt.close();
        return r;

        /*provare con 5BI' OR cognome<>'a
         */
    }

    public static List<String[]> ricerca02(Connection c, String cl) throws SQLException {
        String[] par = {"cognome", "nome", "classe"};
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]> r = new ArrayList<String[]>();

        String selectClasse = "SELECT * FROM alunni where classe =?";
        PreparedStatement ps = c.prepareStatement(selectClasse);
        ps.setString(1, cl);
        rs = ps.executeQuery();

        while (rs.next()) {
            r.add(rsToVect(rs, par));
        }
        ps.close();
        return r;
    }

    // visualizza la tabella 
    public static void visual(List<String[]> t) {
        for (String[] riga : t) {
            for (String c : riga) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] arg) {
        Scanner T = new Scanner(System.in);

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    //        "jdbc:mysql://casadecarli.ddns.net/dblorenzo?user=lorenzo&password=lorenzo");
                    "jdbc:mysql://casadecarli.ddns.net/lorenzo?user=lorenzo&password=lorenzo2020");

            boolean cont = true;
            String classe;
            while (cont) {
                if (esiste(conn, "prodotti")) {
                    System.out.println("Tabella esistente");
                } else {
                    System.out.println("Tabella inesistente");
                }

                System.out.print("0 Uscita\n1 visualizza\n2 inserisci\n3 ricerca02\n8 Crea Tabella\n9 cancella tabella\n ");
                String sc = T.nextLine();
                switch (sc.charAt(0)) {
                    case '0':
                        cont = false;
                        break;
                    case '1':
                        visualTab(conn, "prodotti");
                        //visProdotti(conn);
                        break;
                    case '2':
                        System.out.print("Inserimento nuovi prodotti");
                        System.out.println("cod descr prezzo  scadenza qta um");
                        String cod,
                         descr,
                         prezzo,
                         scadenza,
                         qta,
                         um;

                        cod = T.nextLine();
                        descr = T.nextLine();
                        prezzo = T.nextLine();
                        scadenza = T.nextLine();
                        qta = T.nextLine();
                        um = T.nextLine();
                        System.out.print("esito -->");
                        System.out.println(insertProdotti(conn, cod, descr, prezzo, scadenza, qta, um));

                        break;

                    case '3':
                        break;
                    case '8':

                        System.out.println(creaTab(conn));
                        break;
                    case '9':

                        System.out.println(removeTab(conn));
                        break;

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
