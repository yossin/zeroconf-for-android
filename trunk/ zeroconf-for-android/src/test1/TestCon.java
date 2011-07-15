package test1;

import java.io.IOException;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestCon
 */
public class TestCon extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//@Resource(mappedName="java:jdbc/jmdnsDB1", name="jmdnsDB1", authenticationType=AuthenticationType.CONTAINER)
	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Context context = new InitialContext();
			Object o = context.lookup("java:comp/env/jdbc/jmdnsDB1");			
			dataSource = (DataSource) o;
			Connection connection = dataSource.getConnection();
			connection.close();
			response.getWriter().print("Got Connection!");
		}catch (Exception e){
			e.printStackTrace();
			response.getWriter().print("Error!");
			
		}
		
		
	}


}
