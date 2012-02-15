package gr.forthnet.nms.jndidumper;

/**
 *  Dump the JNDI Tree of JBoss AS 7
 *  
 */

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dump")
public class JNDIDumper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final void listContext(Context ctx, String indent,
			PrintWriter out) {
		try {
			NamingEnumeration list = ctx.listBindings("");
			while (list.hasMore()) {
				Binding item = (Binding) list.next();
				String className = item.getClassName();
				String name = item.getName();
				out.println(indent + className + " " + name);
				Object o = item.getObject();

				if (o instanceof javax.naming.Context) {
					listContext((Context) o, indent + " ", out);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Context ctx = null;
		PrintWriter out = response.getWriter();
		try {
			ctx = (Context) new InitialContext().lookup("/");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		listContext(ctx, "", out);

		out.close();

	}

}