package volkov;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.DBCursor;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

	private MongoDB db;
	private PrintWriter out;
	private String path;
	private String date;
	private String id;
	private Object[] arguments;
	private DBCursor cursor;

	public MyServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		db = new MongoDB();
		response.setContentType(CONTENT_TYPE);
		out = response.getWriter();
		path = request.getPathInfo();

		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet example</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>GET</h2>");
			arguments = db.getArguments(path, "/").toArray();
			if (arguments.length == 0) {
				cursor = db.findAll();
				while (cursor.hasNext()) {
					out.print("<h3>" + cursor.next() + "</h3>");
				}
			}
			switch (arguments.length) {
			case 1:
				date = (String) arguments[0];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				cursor = db.findDate(date);
				if (cursor.count() != 0) {
					while (cursor.hasNext()) {
						out.print("<h3>" + cursor.next() + "</h3>");
					}
				} else {
					out.print("<h1>The data is not found!</h1>");
				}
				break;

			case 2:
				date = (String) arguments[0];
				id = (String) arguments[1];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				if (!db.checkId(id)) {
					out.print("<h1>The id is not integer value!</h1>");
					break;
				}
				cursor = db.findByDateAndId(date, id);
				if (cursor.count() != 0) {
					while (cursor.hasNext()) {
						out.print("<h3>" + cursor.next() + "</h3>");
					}
				} else {
					out.print("<h1>The data is not found!</h1>");
				}
				break;

			default:
				break;
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		db = new MongoDB();
		response.setContentType(CONTENT_TYPE);
		out = response.getWriter();
		path = request.getPathInfo();
		arguments = db.getArguments(path, "/").toArray();
		
		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet example</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>POST</h2>");
			if (arguments.length == 0) {
				out.print("<h2>In URL not specified date and ID of notice!</h2>");
			}
			switch (arguments.length) {
			case 1:
				out.print("<h1>You must specify URL containing 2 arguments!</h1>");
				break;
			case 2:
				date = (String) arguments[0];
				id = (String) arguments[1];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				if (!db.checkId(id)) {
					out.print("<h1>The id is not integer value!</h1>");
					break;
				}
				cursor = db.findID(id);
				if (cursor.size() != 0) {
					out.print("<h1>The note with this id is already exists!</h1>");
				} else {
					db.post(id, date);
					out.print("<h1>The note was input!</h1>");
				}
				break;
			default:
				out.print("<h1>Error!</h1>");
				break;
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close();
		}
	}

	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		db = new MongoDB();
		response.setContentType(CONTENT_TYPE);
		out = response.getWriter();
		path = request.getPathInfo();
		arguments = db.getArguments(path, "/").toArray();

		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet example</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>PUT");

			if (arguments.length == 0) {
				out.print("<h2>In URL not specified date and ID of notice!</h2>");
			}
			switch (arguments.length) {
			case 1:
				out.print("<h1>You must specify URL containing 2 arguments!</h1>");
				break;
			case 2:
				date = (String) arguments[0];
				id = (String) arguments[1];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				if (!db.checkId(id)) {
					out.print("<h1>The id is not integer value!</h1>");
					break;
				}
				cursor = db.findByDateAndId(date, id);
				if (cursor.size() != 0) {
					db.put(id, date);
					out.print("<h1>Chane complete </h1>");
				} else {
					out.print("<h1>The data for change wasn't found!</h1>");
				}
				break;

			default:
				out.print("<h1>Error!</h1>");
				break;
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close();
		}
	}

	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		db = new MongoDB();
		response.setContentType(CONTENT_TYPE);
		out = response.getWriter();
		path = request.getPathInfo();
		arguments = db.getArguments(path, "/").toArray();

		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet example</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>DELETE</h2>");
			if (arguments.length == 0) {
				out.print("<h2>In URL not specified date and id of notice!</h2>");
			}
			switch (arguments.length) {
			case 1:
				date = (String) arguments[0];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				cursor = db.findDate(date);
				if (cursor.size() != 0) {
					db.deleteByDate(date);
					out.print("<h1>The all notes with this date was deleted!</h1>");
				} else {
					out.print("<h1>The date is not found!</h1>");
					break;
				}

			case 2:
				date = (String) arguments[0];
				id = (String) arguments[1];
				if (!db.checkDate(date)) {
					out.print("<h1>The date is not correct!</h1>");
					break;
				}
				if (!db.checkId(id)) {
					out.print("<h1>The id is not integer value!</h1>");
					break;
				}
				cursor = db.findByDateAndId(date, id);
				if (cursor.size() != 0) {
					db.deleteByDateAndId(date, id);
					out.print("<h1>Delete complete </h1>");
				} else {
					out.print("<h1>The data for delete wasn't found! </h1>");
					break;
				}
			default:
				out.print("<h1>Error!</h1>");
				break;
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close();
		}
	}
}
