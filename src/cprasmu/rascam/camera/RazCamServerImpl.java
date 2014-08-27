/*
 * copyright (C) 2013 Christian P Rasmussen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cprasmu.rascam.camera;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cprasmu.rascam.camera.model.Mode;
import cprasmu.rascam.network.HTTPImageServer;
import cprasmu.rascam.network.RDCPMessageSender;
import cprasmu.util.SystemUtilities;

/**
 * {@code RazCamServerImpl} Camera implementation class.
 * @author Christian Rasmussen 
 */
public class RazCamServerImpl {

	public static final String UDP_MULTICAST_ADDRESS = "226.1.1.1";
	public static final Integer UDP_TX_PORT = 15555;
	public static final Integer UDP_RX_PORT = 15556;
	public static final String WEB_SERVER_CONTEXT = "/DCIM/";
	private String filePath = "/DCIM/";
	
	public static final String JSON_DATA_CAMERA_EFFECT 			= "ImageEffect";
	public static final String JSON_DATA_CAMERA_EXPOSURE 		= "ExposureMode";
	public static final String JSON_DATA_CAMERA_METERING 		= "MeteringMode";
	public static final String JSON_DATA_CAMERA_MODE 			= "CameraMode";
	public static final String JSON_DATA_CAMERA_BRIGHTNESS 		= "Brightness";
	public static final String JSON_DATA_CAMERA_SATURATION 		= "Saturation";
	public static final String JSON_DATA_CAMERA_SHARPNESS 		= "Sharpness";
	public static final String JSON_DATA_CAMERA_CONTRAST 		= "Contrast";
	public static final String JSON_DATA_CAMERA_AWB 			= "AWBMode";
	public static final String JSON_DATA_CAMERA_ISO 			= "ISO";
	public static final String JSON_DATA_CAMERA_EV 				= "EV";
	public static final String JSON_DATA_CAMERA_HFLIP 			= "HFlip";
	public static final String JSON_DATA_CAMERA_VFLIP 			= "VFlip";
	public static final String JSON_DATA_CAMERA_VSTAB 			= "VStab";
	public static final String JSON_DATA_CAMERA_ROTATION 		= "Rotation";
	public static final String JSON_DATA_CAMERA_DURATION 		= "Duration";
	public static final String JSON_DATA_CAMERA_TIMELAPSE_DELAY = "TimelapseDelay";
	public static final String JSON_DATA_CAMERA_QUALITY 		= "Quality";
	public static final String JSON_DATA_CAMERA_FPS 			= "FPS";
	public static final String JSON_DATA_CAMERA_VIDEO_RES 		= "VideoResolution";
	public static final String JSON_DATA_CAMERA_STILL_RES 		= "StillResolution";
	public static final String JSON_DATA_CAMERA_SHUTTER_SPEED 	= "ShutterSpeed";
	public static final String JSON_DATA_DCIM_FILE 				= "Filename";
	
	public static final String CAMERA_CMD_STATUS 	= "STATUS";
	public static final String CAMERA_CMD_START 	= "START";
	public static final String CAMERA_CMD_STOP 		= "STOP";
	
	public static final String CAMERA_CMD_PLUS 		= "+";
	public static final String CAMERA_CMD_MINUS 	= "-";
	public static final String CAMERA_CMD_PREVIOUS 	= "PREVIOUS";
	public static final String CAMERA_CMD_NEXT 		= "NEXT";
	public static final String CAMERA_CMD_RESET 	= "RESET";
	public static final String CAMERA_CMD_DELETE 	= "DELETE";
	public static final String CAMERA_CMD_SHUTDOWN 	= "SHUTDOWN";
	public static final String CAMERA_CMD_REBOOT 	= "REBOOT";
	public static final String CAMERA_CMD_QUIT 		= "QUIT";
	public static final String CAMERA_CMD_ENABLE 	= "ENABLE";
	public static final String CAMERA_CMD_DISABLE 	= "DISABLE";

	private static final String DEFAULT_INSTANCE_NAME = "RasCam";

	private final static String TAG = RazCamServerImpl.class.getSimpleName();

	
	private CameraController rcm = new RaspiCameraController();
	private FileSystemModel fsm = new FileSystemModel();
	private RDCPMessageSender rdcmp;
	private String wlanAddress = "";
	private HTTPImageServer webServer;
	private int webServerPort = 9876;
	private String instanceName = DEFAULT_INSTANCE_NAME;

	public CameraController getCameraController() {
		return rcm;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public void setInstanceName(String name){
		instanceName = name;	
	}
	
	public RazCamServerImpl() {
		wlanAddress = SystemUtilities.getNetworkAddress();
		
		Map<String,String>users = new HashMap<String,String>();
		//users.put("rascam", "rascam");
		//webServer = new HTTPImageServer(this, wlanAddress, webServerPort, WEB_SERVER_CONTEXT, filePath, "/opt/test.keystore","rascam",instanceName,users);
		webServer = new HTTPImageServer(this, wlanAddress, webServerPort, WEB_SERVER_CONTEXT, filePath, instanceName, users);
		rdcmp = new RDCPMessageSender(this);
		rdcmp.setImagePort(webServerPort);
		rdcmp.start(1000);
	}

	public String getAdvertData() {

		JSONObject obj = new JSONObject();
		obj.put("wlanAddress", wlanAddress);
		obj.put("webServerPort", webServerPort);
		obj.put("instanceName", instanceName);
		obj.put("lastModified", rcm.getLastModified().getTime());

		return obj.toString();

	}



	public JSONObject toJSON() {

		JSONObject obj = new JSONObject();

		obj.put("usage", rcm.getFileSystemUsage());
		obj.put("lastFilename", rcm.getLastFilename());
		obj.put("busy", rcm.isRecording());
		obj.put("CameraModel", rcm.toJSON());

		return obj;
	}

	public synchronized String parseCommand(String jsonData)
			throws IllegalArgumentException {

		String subject = "";
		String value = "";

		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

		jsonData = jsonData.trim();

		try {
			Map json = (Map) parser.parse(jsonData, containerFactory);
			Iterator iter = json.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				// System.out.println(entry.getKey() + "=>" + entry.getValue());
				if (entry.getKey().equals("subject")) {
					subject = (String) entry.getValue();
				}
				if (entry.getKey().equals("value")) {
					value = (String) entry.getValue();
				}

			}
			System.out.println(JSONValue.toJSONString(json));
		} catch (ParseException pe) {
			throw new IllegalArgumentException("Invalid JSON Data");
		}

		if (subject.equals(JSON_DATA_CAMERA_AWB)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextAwbMode();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousAwbMode();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetAwbMode();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_EFFECT)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextImageEffect();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousImageEffect();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetImageEffect();
			}
			
		} else if (subject.equals(JSON_DATA_CAMERA_VIDEO_RES)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextVideoResolution();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousVideoResolution();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetVideoResolution();
			}
			
		} else if (subject.equals(JSON_DATA_CAMERA_STILL_RES)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextStillResolution();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousStillResolution();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetStillResolution();
			}
			
		} else if (subject.equals(JSON_DATA_CAMERA_EXPOSURE)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextExposureMode();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousExposureMode();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetExposureMode();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_METERING)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextMeteringMode();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousMeteringMode();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetMeteringMode();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_EV)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getEv().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getEv().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetEv();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_BRIGHTNESS)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getBrightness().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getBrightness().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetBrightness();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_SATURATION)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getSaturation().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getSaturation().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetSaturation();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_SHARPNESS)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getSharpness().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getSharpness().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetSharpness();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_CONTRAST)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getContrast().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getContrast().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetContrast();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_HFLIP)) {

			rcm.setHFlip(!rcm.isHFlip());

		} else if (subject.equals(JSON_DATA_CAMERA_VFLIP)) {

			rcm.setVFlip(!rcm.isVFlip());

		} else if (subject.equals(JSON_DATA_CAMERA_VSTAB)) {

			rcm.setVStab(!rcm.isVStab());

		} else if (subject.equals(JSON_DATA_CAMERA_TIMELAPSE_DELAY)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getTimelapseDelay().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getTimelapseDelay().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetTimelapseDelay();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_QUALITY)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getQuality().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getQuality().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetQuality();
			}
			
		} else if (subject.equals(JSON_DATA_CAMERA_SHUTTER_SPEED)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getShutterSpeed().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getShutterSpeed().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetShutterSpeed();
			} else if (value.equals(CAMERA_CMD_ENABLE)) {
				rcm.enableShutterSpeed();
			} else if (value.equals(CAMERA_CMD_DISABLE)) {
				rcm.disableShutterSpeed();
			}
			
		} else if (subject.equals(JSON_DATA_CAMERA_ISO)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getIso().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getIso().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetIso();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_ROTATION)) {
			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextRotation();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.nextRotation();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetRotation();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_FPS)) {

			if (value.equals(CAMERA_CMD_PLUS)) {
				rcm.getFps().increment();
			} else if (value.equals(CAMERA_CMD_MINUS)) {
				rcm.getFps().decrement();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetFps();
			}

		} else if (subject.equals(JSON_DATA_CAMERA_MODE)) {

			if (value.equals(CAMERA_CMD_NEXT)) {
				rcm.nextCameraMode();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				rcm.previousCameraMode();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.resetCameraMode();
			}
		} else if (subject.equals(JSON_DATA_DCIM_FILE)) {
			if (value.equals(CAMERA_CMD_NEXT)) {
				//rcm.nextCameraMode();
			} else if (value.equals(CAMERA_CMD_PREVIOUS)) {
				//rcm.previousCameraMode();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				//rcm.resetCameraMode();
			}
			
		} else if (subject.equals(CAMERA_CMD_STATUS)) {

			if (value.equals(CAMERA_CMD_START)) {
				if (!rcm.isRecording()) {
					if (rcm.getCameraMode()
							.equals(Mode.SINGLE_SHOT.displayName)) {
						rcm.singleShotPhoto();

					} else if (rcm.getCameraMode().equals(
							Mode.TIME_LAPSE.displayName)) {
						rcm.timelapsePhoto();

					} else if (rcm.getCameraMode().equals(
							Mode.VIDEO.displayName)) {
						rcm.video();

					} else {
						// // sendMessage("ERROR:" + subject);
					}
				}
			} else if (value.equals(CAMERA_CMD_STOP)) {
				rcm.stop();
			} else if (value.equals(CAMERA_CMD_RESET)) {
				rcm.reset();

			} else if (value.equals(CAMERA_CMD_SHUTDOWN)) {
				rcm.stop();
				SystemUtilities.shutdownOS();
			} else if (value.equals(CAMERA_CMD_REBOOT)) {
				rcm.stop();
				SystemUtilities.rebootOS();
				
			} else if (value.equals(CAMERA_CMD_QUIT)) {
				rcm.stop();
				System.exit(0);

			} else if (value.equals("DIRS")) {

				JSONObject result = new JSONObject();
				JSONArray jsonDirs = new JSONArray();
				File[] dirs = DCIMHelper.dcimDirectory.listFiles();

				Arrays.sort(dirs, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return Long.valueOf(f1.lastModified()).compareTo(
								f2.lastModified());
					}
				});

				for (File dir : dirs) {
					if (dir.isDirectory()) {
						JSONObject folder = new JSONObject();
						folder.put("Folder", dir.getName());
						File[] tmp = dir.listFiles();
						long count = 0;

						for (File t : tmp) {
							if (!t.getName().endsWith(".thm")) {
								count++;
							}
						}
						folder.put("Count", count);
						jsonDirs.add(folder);
					}
				}

				result.put("dirs", jsonDirs);

				return result.toString();

			} else if (value.equals("FILES")) {
				JSONObject obj = new JSONObject();
				obj.put("fileSystemModel",
						fsm.toJSON(DCIMHelper.getCurrentDirectory()));
				return obj.toString();
			} else {

			}

		} else if (subject.equals(CAMERA_CMD_DELETE)) {
			if(!DCIMHelper.getCurrentDirectory().equals( DCIMHelper.dcimDirectory + "/" + value)){
				fsm.delete( DCIMHelper.dcimDirectory + "/" + value);
				System.out.println("DELETED: " + value);
			} else {
				
			}
			
			
		} else if (subject.equals("FOLDER")) {
			JSONObject obj = new JSONObject();
			obj.put("fileSystemModel",
					fsm.toJSON(DCIMHelper.dcimDirectory + "/" + value));
			return obj.toString();
		}
		return this.toJSON().toString();
	}

	public static void main(String[] args) {
		
	//	ImageUtils.createVideoThumbnail(new Object(),"mov.mov");
		
		RazCamServerImpl mss = new RazCamServerImpl();
	}

}
