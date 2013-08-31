package volkov;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MongoDbTESTS {

	@Test
	public void checkValueTest() {
		MongoDB m = new MongoDB();
		assertTrue(m.checkValue("5423"));
		assertTrue(m.checkValue("0"));
		assertTrue(m.checkValue("-29"));
		assertTrue(m.checkValue("8"));
		assertFalse(m.checkValue("asd"));
		assertFalse(m.checkValue("/-8*fg"));
		assertFalse(m.checkValue("  "));
		assertFalse(m.checkValue(" 456"));
		assertFalse(m.checkValue("2.5"));
		assertFalse(m.checkValue(null));
	}

	@Test
	public void checkDateTest() {
		MongoDB m = new MongoDB();
		assertTrue(m.checkDate("28-02-2013"));
		assertTrue(m.checkDate("31-01-2014"));
		assertTrue(m.checkDate("30-04-2015"));
		assertTrue(m.checkDate("01-02-2013"));
		assertFalse(m.checkDate("35-01-2013"));
		assertFalse(m.checkDate("30-02-2013"));
		assertFalse(m.checkDate("18-09-2020"));
		assertFalse(m.checkDate("08-08-2008"));
	}

	@Test
	public void checkIdTest() {
		MongoDB m = new MongoDB();
		assertTrue(m.checkId("43"));
		assertTrue(m.checkId("-12"));
		assertTrue(m.checkId("2"));
		assertTrue(m.checkId("9"));
		assertFalse(m.checkId("a-d_12"));
		assertFalse(m.checkId(null));
		assertFalse(m.checkId("2.3"));
		assertFalse(m.checkId(" 9"));
	}

	@Test
	public void getArgumentsTest() {
		MongoDB m = new MongoDB();
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.getArguments("/22-03-2013/2", "/");
		verify(behaviour).getArguments("/22-03-2013/2", "/");
		ArrayList<String> tempContainer = new ArrayList<>();
		assertTrue(m.getArguments("/////", "/").containsAll(tempContainer));
		tempContainer.add("12-08-2013");
		assertTrue(m.getArguments("//12-08-2013/", "/").containsAll(tempContainer));
		assertFalse(m.getArguments("/12-09-2013//", "/").containsAll(tempContainer));
		tempContainer.add("4");
		assertTrue(m.getArguments("/12-08-2013/4/", "/").containsAll(tempContainer));
		assertTrue(m.getArguments("///12-08-2013/4", "/").containsAll(tempContainer));
		assertFalse(m.getArguments("/12-08-2013/5/", "/").containsAll(tempContainer));
		assertFalse(m.getArguments("/12-08-2013/4 /", "/").containsAll(tempContainer));
	}

	@Test
	public void findAllTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.findAll();
		verify(behaviour).findAll();
		MongoDB m = new MongoDB();
		assertNotNull(m.findAll());
		assertSame(m.findAll().count(), m.findAll().count());
	}

	@Test
	public void findByDateAndIdTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.findByDateAndId("23-08-2013", "1");
		verify(behaviour).findByDateAndId("23-08-2013", "1");
		MongoDB m = new MongoDB();
		assertSame(1, m.findByDateAndId("23-08-2013", "1").count());
		assertSame(1, m.findByDateAndId("23-08-2013", "2").count());
		assertSame(1, m.findByDateAndId("23-08-2013", "3").count());
		assertSame(1, m.findByDateAndId("24-08-2013", "4").count());
		assertSame(1, m.findByDateAndId("24-08-2013", "5").count());
		assertSame(1, m.findByDateAndId("24-08-2013", "6").count());
		assertSame(1, m.findByDateAndId("24-08-2013", "7").count());
	}

	@Test
	public void findIDTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.findID("1");
		verify(behaviour).findID("1");
		MongoDB m = new MongoDB();
		assertSame(1, m.findID("1").count());
		assertSame(1, m.findID("5").count());
		assertSame(1, m.findID("2").count());
		assertSame(1, m.findID("7").count());
		assertSame(1, m.findID("4").count());
	}

	@Test
	public void findDateTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.findDate("23-08-2013");
		verify(behaviour).findDate("23-08-2013");
		MongoDB m = new MongoDB();
		assertSame(3, m.findDate("23-08-2013").count());
		assertSame(4, m.findDate("24-08-2013").count());
	}

	@Test
	public void postTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.post("8", "25-08-2013");
		verify(behaviour).post("8", "25-08-2013");
		MongoDB m = new MongoDB();
		m.post("8", "25-08-2013");
		assertSame(1, m.findByDateAndId("25-08-2013", "8").count());
		m.post("9", "25-08-2013");
		assertSame(1, m.findByDateAndId("25-08-2013", "9").count());
		m.post("10", "25-08-2013");
		assertSame(1, m.findByDateAndId("25-08-2013", "10").count());
		m.deleteByDate("25-08-2013");
	}

	@Test
	public void deleteByDateTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.deleteByDate("26-08-2013");
		verify(behaviour).deleteByDate("26-08-2013");
		MongoDB m = new MongoDB();
		m.post("8", "26-08-2013");
		m.post("9", "26-08-2013");
		m.post("10", "26-08-2013");
		assertEquals(3, m.findDate("26-08-2013").count());
		m.deleteByDate("26-08-2013");
		assertEquals(0, m.findDate("26-08-2013").count());
	}

	@Test
	public void deleteByDateAndIdTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.deleteByDateAndId("26-08-2013", "8");
		verify(behaviour).deleteByDateAndId("26-08-2013", "8");
		MongoDB m = new MongoDB();
		m.post("8", "26-08-2013");
		m.post("9", "26-08-2013");
		m.post("10", "26-08-2013");
		assertEquals(1, m.findByDateAndId("26-08-2013", "8").count());
		m.deleteByDateAndId("26-08-2013", "8");
		assertEquals(0, m.findByDateAndId("26-08-2013", "8").count());
		assertEquals(1, m.findByDateAndId("26-08-2013", "9").count());
		m.deleteByDateAndId("26-08-2013", "9");
		assertEquals(0, m.findByDateAndId("26-08-2013", "9").count());
		assertEquals(1, m.findByDateAndId("26-08-2013", "10").count());
		m.deleteByDateAndId("26-08-2013", "10");
		assertEquals(0, m.findByDateAndId("26-08-2013", "10").count());
	}

	@Test
	public void putTest() {
		MongoDB behaviour = mock(MongoDB.class);
		behaviour.put("5", "23-08-2013");
		verify(behaviour).put("5", "23-08-2013");
		MongoDB m = new MongoDB();
		m.put("5", "24-08-2013");
		assertEquals("Changed text", m.findByDateAndId("24-08-2013", "5")
				.next().get("note"));
		m.put("6", "24-08-2013");
		assertEquals("Changed text", m.findByDateAndId("24-08-2013", "6")
				.next().get("note"));
		m.put("7", "24-08-2013");
		assertEquals("Changed text", m.findByDateAndId("24-08-2013", "7")
				.next().get("note"));
	}
}
