package com.incadence.coalesce.framework.persistor.performance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.CoalesceFramework;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.testobjects.MissionEntity;
import com.incadencecorp.coalesce.framework.persistance.ServerConn;
import com.incadencecorp.coalesce.framework.persistance.accumulo.AccumuloPersistor;

public class dbStresserMultiThreadReadTest {
	final static Logger _log = Logger.getLogger("TesterLog");
	static ServerConn _serCon;
	static AccumuloPersistor _psPersistor;

	public static void main(String[] args) {
		int ITERATION_LIMIT = 10000;
		int CAPTURE_INTERVAL=10;
		int READ_SIZE = 100;
		myappRunner._coalesceFramework = new CoalesceFramework();

		try {
			if (dbStresserMultiThreadReadTest.OpenConnection() == true) {
				myappRunner._coalesceFramework
						.initialize(dbStresserMultiThreadReadTest._psPersistor);
				try {
					myappRunner.keys = myappRunner._coalesceFramework.getCoalesceEntityKeysForEntityId(".*", ".*", ".*");
				} catch (CoalescePersistorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// timeLogger = new ArrayList<TimeTrack>();
				Thread vol1 = new Thread(new myappRunner(ITERATION_LIMIT, CAPTURE_INTERVAL, READ_SIZE));
				vol1.setName("Thread #1");
				vol1.start();
				Thread vol2 = new Thread(new myappRunner(ITERATION_LIMIT, CAPTURE_INTERVAL, READ_SIZE));
				vol2.setName("Thread #2");
				vol2.start();
				Thread vol3 = new Thread(new myappRunner(ITERATION_LIMIT, CAPTURE_INTERVAL, READ_SIZE));
				vol3.setName("Thread #3");
				vol3.start();
				Thread vol4 = new Thread(new myappRunner(ITERATION_LIMIT, CAPTURE_INTERVAL, READ_SIZE));
				vol4.setName("Thread #4");
				vol4.start();
				/*Thread vol5 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol5.setName("Thread #5");
				vol5.start();
				Thread vol6 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol6.setName("Thread #6");
				vol6.start();
				Thread vol7 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol7.setName("Thread #7");
				vol7.start();
				Thread vol8 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol8.setName("Thread #8");
				vol8.start();
				 Thread vol9 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol9.setName("Thread #9");
				vol9.start();
				Thread vol10 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol10.setName("Thread #10");
				vol10.start();
				Thread vol11 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol11.setName("Thread #11");
				vol11.start();
				Thread vol12 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol12.setName("Thread #12");
				vol12.start();
				Thread vol13 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol13.setName("Thread #13");
				vol13.start();
				Thread vol14 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol14.setName("Thread #14");
				vol14.start();
				Thread vol15 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol15.setName("Thread #15");
				vol15.start();
				Thread vol16 = new Thread(new appRunner(ITERATION_LIMIT, CAPTURE_INTERVAL));
				vol16.setName("Thread #16");
				vol16.start(); */
			}
		} catch (Exception ex) {
			_log.log(java.util.logging.Level.SEVERE, ex.toString());
		}
	}

	public static boolean OpenConnection() throws SQLException {
		_serCon = new ServerConn();
		_serCon.setDatabase("accumulodev");
		_serCon.setUser("root");
		_serCon.setPassword("secret");
		_serCon.setServerName("accumulodev");
		_psPersistor = new AccumuloPersistor();
		_psPersistor.initialize(_serCon);
		return true;
	}
}

class myappRunner implements Runnable {
	private Object mutexXMLLogger = new Object();
	int ITERATION_LIMIT = 10;
	int CAPTURE_METRICS_INTERVAL = 1;
	int LOG_INTERVAL = 100;
	int READ_SIZE = 0;
	ServerConn serCon;

	static CoalesceFramework _coalesceFramework;
	static List<String> keys;

	private String _threadID;

	public String getThreadID() {
		return _threadID;
	}

	public void setThreadID(String string) {
		this._threadID = string;
	}

	int minorVal = 0;
	int majorVal = 0;
	int masterCounter = 0;
	String startSaveTimeStamp = "";
	String completeSaveTimeStamp = "";
	private List<TimeTrack> timeLogger;
	Document dom;

	public myappRunner(int iteration_lim) {
		this.ITERATION_LIMIT = iteration_lim;
	}

	public myappRunner(int iteration_lim, int cap_interval, int read_size) {
		this.ITERATION_LIMIT = iteration_lim;
		this.CAPTURE_METRICS_INTERVAL = cap_interval;
		this.READ_SIZE=read_size;
	}

	@Override
	public void run() {
		String threadID = String.valueOf(Thread.currentThread().getId());
		

		try {
			timeLogger = new ArrayList<TimeTrack>();
			this.setThreadID(String.valueOf(Thread.currentThread().toString()));
			Thread.currentThread();
			outConsoleData(Thread.currentThread().getId(),
					"************* STARTING THREAD # "
							+ Thread.currentThread().getName() + " of "
							+ Thread.activeCount()
							+ " *************", false);
			TimeTrack _timeTrack;
			String startTime = this.getCurrentTime();
			int _iteration_counter = 0;
			this.createDOMDocument();
			for (_iteration_counter = 0; _iteration_counter <= ITERATION_LIMIT; _iteration_counter++) {
				_timeTrack = new TimeTrack();
				masterCounter += 1;
				ArrayList<String> keyset = new ArrayList<String>();
				for (int i=0;i<this.READ_SIZE;i++) {
					keyset.add(keys.get((int) Math.round(Math.random()*keys.size())));
				}
				getEntities( _timeTrack,
						keyset, threadID);				
				if (this.masterCounter % CAPTURE_METRICS_INTERVAL == 0) {
					timeLogger.add(_timeTrack);
					if(this.masterCounter % LOG_INTERVAL == 0) {
						System.out.println("Thread: " + threadID + " Completed: " + this.masterCounter);
					}
				

				_timeTrack = null;
				
					
				} 
				
			}
			synchronized (mutexXMLLogger) {
				this.createDOMTree();
				String stopTime = this.getCurrentTime();
				outConsoleData(Thread.currentThread().getId(), "STARTTIME: "
						+ startTime);
				outConsoleData(Thread.currentThread().getId(), "STOPTIME: "
						+ stopTime);
				this.printToFile("datafile_" + threadID + "_persistance.xml");
			}
		} catch (Exception ex) {
			dbStresserMultiThreadReadTest._log.log(java.util.logging.Level.SEVERE,
					ex.toString());
		}
	}

//	private void outConsoleData(int cntValue, String msg) {
//		System.out.println(msg + getCurrentTime() + "\t" + cntValue);
//	}
//
//	private void outConsoleData(Thread cntValue, String msg) {
//		System.out.println(msg + getCurrentTime() + "\t" + cntValue);
//	}

	private void outConsoleData(long id, String msg) {
		System.out.println(msg + getCurrentTime() + "\t" + id);

	}

	private void outConsoleData(long id, String msg, boolean flagShowTime) {
		if (flagShowTime)
			System.out.println(msg + getCurrentTime() + "\t" + id);
		else
			System.out.println(msg + "\t" + id);

	}

	private void getEntities(TimeTrack _timeTrack,
			List<String>keyset, String threadVal)
			throws CoalescePersistorException {
		
		_timeTrack.setStartTime(getCurrentTime());
		_timeTrack.setStartMSTime(getCurrentTime(false));
		_timeTrack.setIterationVal(String.valueOf(masterCounter));
		_timeTrack.setIterationInterval(String
				.valueOf(CAPTURE_METRICS_INTERVAL));
		if (_timeTrack.getThread() == "" | _timeTrack.getThread() == null)
			_timeTrack.setThread(threadVal);
		String[] keys = (String []) Array.newInstance(String.class, keyset.size());
		keys = keyset.toArray(keys);
		myappRunner._coalesceFramework.getCoalesceEntities(keys);


		_timeTrack.setStopTime(getCurrentTime());
		_timeTrack.setStopMSTime(getCurrentTime(false));
	}

	private String generateEntityVersionNumber(int currentIterationNumber) {
		String entityVersion = "";
			if (currentIterationNumber % 10000 == 0) {
			majorVal += 1;
			minorVal = 0;
			// outConsoleData(masterCounter, "INCREMENT: ");
			entityVersion = String.valueOf(majorVal) + "."
					+ String.valueOf(minorVal);
		} else {
			minorVal += 1;
			entityVersion = String.valueOf(majorVal) + "."
					+ String.valueOf(minorVal);
		}
		return entityVersion;
	}

	private void createDOMDocument() {
		// get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();

		} catch (ParserConfigurationException ex) {
			System.out
					.println("Error while trying to instantiate DocumentBuilder "
							+ ex.toString());
			dbStresserMultiThreadReadTest._log.log(java.util.logging.Level.SEVERE,
					ex.toString());
		}

	}

	private static String getCurrentTime(boolean isNano) {
		String currentTime;
		if (isNano)
			currentTime = String.valueOf(System.nanoTime());
		else
			currentTime = String.valueOf(System.currentTimeMillis());
		return currentTime;
	}

	private String getCurrentTime() {
		Calendar calStamp = Calendar.getInstance();
		java.util.Date nowCurrentDate = calStamp.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(
				nowCurrentDate.getTime());
		return currentTimestamp.toString();
	}

	private void createDOMTree() {
		Element rootEle = dom.createElement("TimeTrack");
		dom.appendChild(rootEle);
		for (TimeTrack b : timeLogger) {
			Element timeTrackElement = createTimeTrackElement(b);
			rootEle.appendChild(timeTrackElement);
		}
	}

	private Element createTimeTrackElement(TimeTrack b) {

		Element timeElement = dom.createElement("TimeTrack");
		timeElement.setAttribute("ENTITYID", b.getEntityID());
		timeElement.setAttribute("COMPLETE_ITERATIONS", b.getIterationVal());
		timeElement.setAttribute("CAPTURE_INTERVAL", b.getIterationInterval());
		timeElement.setAttribute("THREAD", b.getThread());
		// create start time element and start time text node and attach it to
		// timeElement -Start
		Element startEle = dom.createElement("STARTTIME");
		Text startText = dom.createTextNode(b.getStartTime());
		startEle.appendChild(startText);
		timeElement.appendChild(startEle);

		// create stop time element and stop time text node and attach it to
		// timeElement -Stop
		Element stopEle = dom.createElement("STOPTIME");
		Text stopText = dom.createTextNode(b.getStopTime());
		stopEle.appendChild(stopText);
		timeElement.appendChild(stopEle);

		Element startEleMS = dom.createElement("INSERT_START_MS");
		Text startMSText = dom.createTextNode(b.getStartMSTime());
		startEleMS.appendChild(startMSText);
		timeElement.appendChild(startEleMS);

		Element stopEleMS = dom.createElement("INSERT_COMPLETE_MS");
		Text stopMSText = dom.createTextNode(b.getStopMSTime());
		stopEleMS.appendChild(stopMSText);
		timeElement.appendChild(stopEleMS);
		return timeElement;

	}


	private void printToFile(String fileName) throws IOException {
		FileOutputStream fileOutputStream = null;
		try {
			DOMImplementationLS DOMiLS = (DOMImplementationLS) (dom
					.getImplementation()).getFeature("LS", "3.0");
			LSOutput lsoOutput = DOMiLS.createLSOutput();
			fileOutputStream = new FileOutputStream(fileName);
			lsoOutput.setByteStream((OutputStream) fileOutputStream);
			LSSerializer LSS = DOMiLS.createLSSerializer();
			boolean isDoneSerializing = LSS.write(dom, lsoOutput);
			if (isDoneSerializing)
				System.out.println("Persistor Test Data File Available with Results.");
			else
				System.out.println("Data file for Test not available.  Write error.");
		} catch (IOException ex) {
			dbStresserMultiThreadReadTest._log.log(java.util.logging.Level.SEVERE, ex.toString());
		}finally {
			fileOutputStream.close();
		}
	}

	private CoalesceEntity createEntity(String entityVersion)
			throws CoalesceException {
		// Create Test Entity
		CoalesceEntity _entity = new CoalesceEntity();

		_entity = this.createMissionEntity(_entity, entityVersion);

		return _entity;
	}

	private CoalesceEntity createMissionEntity(CoalesceEntity _entity,
			String entityVersion) throws CoalesceException {
		MissionEntity _missionEntity = new MissionEntity();
		_missionEntity.initialize();
		_missionEntity.setName("Mission Entity - Performance Testing");
		_missionEntity.setSource("Coalesce_Mission");
		_missionEntity.setVersion(entityVersion);
		_entity = _missionEntity;
		return _entity;
	}

}
