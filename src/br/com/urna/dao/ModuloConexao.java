package br.com.urna.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Washington Klébio
 */
public class ModuloConexao {
    //Metodo responsavel por estabelecer a conexão com o banco.
    public static Connection conector() {
        
        java.sql.Connection conexao = null;
        // A linha abaixo chama o drive importado para biblioteca.
        String drive = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/urna";
        String user = "root";
        String password = "";
        
        try {
            Class.forName(drive);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
