package volkov;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB {

	private static final int PORT = 27017;
	private static final String HOST = "localhost";
	private static final String DATABASE = "testdb";
	private static final String TABLE = "notes";
	private static final String DATE = "createdDate";
	private static final String ID = "id";
	private static final String NOTE = "note";

	private Mongo mongo;
	private DBCollection dbcollection;
	private BasicDBObject document;
	private DB db;
	private DBCursor cursor;
	private BasicDBObject searchQuery;

	public static void main(String[] args) {
		MongoDB m = new MongoDB();
//		m.deleteByDate("23-08-2013");
//		m.deleteByDate("24-08-2013");
		//System.out.println(m.findByDateAndId("23-08-2013", "1").count());
		m.post("4", "24-08-2013");
		m.post("5", "24-08-2013");
		m.post("6", "24-08-2013");
		m.post("7", "24-08-2013");
		//m.deleteByDateAndId("23-08-2013", "1");
		System.out.println("--------------");
		DBCursor obj =  m.findByDateAndId("23-08-2013", "1"); 
		System.out.println(obj.next().get(NOTE));
	}

	private Mongo getConnection() {
		try {
			mongo = new Mongo(HOST, PORT);
			try {
				mongo.getConnector().getDBPortPool(mongo.getAddress()).get()
						.ensureOpen();
				return mongo;
			} catch (IOException e) {
				throw new MongoDbException("No connect to server!");
			}
		} catch (UnknownHostException e) {
			throw new MongoDbException(
					"IP address of a host could not be determined!");
		} catch (MongoException e) {
			throw new MongoDbException(
					"IP address of a host could not be determined!");
		}
	}

	private DBCollection getCollection() {
		if (DATABASE.length() == 0) {
			throw new MongoDbException("The database name can't be empty!");
		}
		if (TABLE.length() == 0) {
			throw new MongoDbException("The table name can't be empty!");
		}
		db = getConnection().getDB(DATABASE);
		dbcollection = db.getCollection(TABLE);
		return dbcollection;
	}

	public void put(String id, String date) {
		document = new BasicDBObject();
		document.put(ID, id);
		document.put(NOTE, "Changed text");
		document.put(DATE, date);
		searchQuery = new BasicDBObject().append(ID, id);
		getCollection().update(searchQuery, document);
	}

	public void deleteByDate(String date) {
		document = new BasicDBObject();
		document.put(DATE, date);
		getCollection().remove(document);
	}

	public void deleteByDateAndId(String date, String id) {
		document = new BasicDBObject();
		document.put(DATE, date);
		document.put(ID, id);
		getCollection().remove(document);
	}

	public void post(String id, String date) {
		document = new BasicDBObject();
		document.put(ID, id);
		document.put(NOTE, "Some text");
		document.put(DATE, date);
		getCollection().insert(document);
	}

	public DBCursor findDate(String date) {
		document = new BasicDBObject(new BasicDBObject(DATE, date));
		cursor = getCollection().find(document);
		return cursor;
	}

	public DBCursor findID(String id) {
		document = new BasicDBObject(new BasicDBObject(ID, id));
		cursor = getCollection().find(document);
		return cursor;
	}

	public DBCursor findByDateAndId(String date, String id) {
		BasicDBObject object = new BasicDBObject();
		object.put(DATE, date);
		object.put(ID, id);
		cursor = getCollection().find(object);
		return cursor;
	}

	public DBCursor findAll() {
		cursor = getCollection().find();
		return cursor;
	}

	public ArrayList<String> getArguments(String str, String delimiter) {
		String[] split = str.split(delimiter);
		ArrayList<String> container = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (!split[i].isEmpty() && !split[i].contains(" ")) {
				container.add(split[i]);
			}
		}
		return container;
	}
	
	public boolean checkValue(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean checkDate(String argument) {
		ArrayList<String> container = getArguments(argument, "-");
		if (container.size() != 3) {
			return false;
		}

		if (!checkValue(container.get(0)) && !checkValue(container.get(1))
				&& !checkValue(container.get(2))) {
			return false;
		}

		int day = Integer.parseInt(container.get(0));
		int month = Integer.parseInt(container.get(1));
		int year = Integer.parseInt(container.get(2));

		if (month == 2) {
			if (day > 0 && day < 29) {
				if (month > 0 && month < 13) {
					if (year > 2012 && year < 2016) {
						return true;
					}
				}
			}
			return false;
		}

		switch (month % 2) {
		case 0:
			if (day > 0 && day < 31) {
				if (month > 0 && month < 13) {
					if (year > 2012 && year < 2016) {
						return true;
					}
				}
			}
			return false;

		default:
			if (day > 0 && day < 32) {
				if (month > 0 && month < 13) {
					if (year > 2012 && year < 2016) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public boolean checkId(String argument) {
		if (checkValue(argument)) {
			return true;
		}
		return false;
	}
}
