package volkov;

import java.net.UnknownHostException;
import java.util.ArrayList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDB {

	private static final String DATABASE = "testdb";
	private static final String TABLE = "notes";
	private static final String DATE = "createdDate";
	private static final String ID = "id";
	private static final String NOTE = "note";

	private Mongo mongo;
	private DBCollection table;
	private BasicDBObject document;
	private DB db;
	private DBCursor cursor;
	private BasicDBObject searchQuery;

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

	private Mongo getConnection() {
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return mongo;
	}

	private DBCollection getDataBase() {
		db = getConnection().getDB(DATABASE);
		table = db.getCollection(TABLE);
		return table;
	}

	public void put(String id, String date) {
		document = new BasicDBObject();
		document.put(ID, id);
		document.put(NOTE, "Change text");
		document.put(DATE, date);
		searchQuery = new BasicDBObject().append(ID, id);
		getDataBase().update(searchQuery, document);
	}

	public void deleteByDate(String date) {
		document = new BasicDBObject();
		document.put(DATE, date);
		getDataBase().remove(document);
	}

	public void deleteByDateAndId(String date, String id) {
		document = new BasicDBObject();
		document.put(DATE, date);
		document.put(ID, id);
		getDataBase().remove(document);
	}

	public void post(String id, String date) {
		document = new BasicDBObject();
		document.put(ID, id);
		document.put(NOTE, "Some text");
		document.put(DATE, date);
		getDataBase().insert(document);
	}

	public DBCursor findDate(String date) {
		document = new BasicDBObject(new BasicDBObject(DATE, date));
		cursor = getDataBase().find(document);
		return cursor;
	}

	public DBCursor findID(String id) {
		document = new BasicDBObject(new BasicDBObject(ID, id));
		cursor = getDataBase().find(document);
		return cursor;
	}

	public DBCursor findByDateAndId(String date, String id) {
		BasicDBObject object = new BasicDBObject();
		object.put(DATE, date);
		object.put(ID, id);
		cursor = getDataBase().find(object);
		return cursor;
	}

	public DBCursor findAll() {
		cursor = getDataBase().find();
		return cursor;
	}

	private boolean checkValue(String value) {
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
		if (checkValue(container.get(0))) {
			if (checkValue(container.get(1))) {
				if (checkValue(container.get(2))) {
					int day = Integer.parseInt(container.get(0));
					int month = Integer.parseInt(container.get(1));
					int year = Integer.parseInt(container.get(2));

					if (day > 0 && day < 32) {
						if (month > 0 && month < 13) {
							if (year > 2012 && year < 2020) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean checkId(String argument) {
		if (checkValue(argument)) {
			return true;
		}
		return false;
	}
}
