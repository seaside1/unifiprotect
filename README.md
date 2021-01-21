![alt text](https://raw.githubusercontent.com/seaside1/unifiprotect/main/img/logocombo.png)
# UniFi Protect  Binding

## About
Integrates UniFi Protect Camera System into OpenHAB. See https://ui.com/why-protect/
This binding utilizes an undocumented json REST API that is present in the NVR. It works very similar
to the 

1) Homebridge solution: https://github.com/hjdhjd/homebridge-unifi-protect 
2) HomeAssistant solution: https://github.com/briis/unifiprotect

The binding is written in java and tailored for OpenHAB.

The Binding has a configurable refresh rate. The refresh rate will update the NVR information. 
For Events (motion detection) starting at ALPHA6 the UniFiProtect Event API over websockets has 
been implemented. Thus motion detection should be instant and without the need of polling.

## OpenHab Version
The binding was originally written for Openhab 2.5.x. The prebuild jar-file for OpenHAB 2.5.x is located 
https://github.com/seaside1/unifiprotect/tree/main/bin and named org.openhab.binding.unifiprotect-2.5.x-ALPHAXX.jar

OpenHAB 3.0.0Mx version is also located under https://github.com/seaside1/unifiprotect/tree/main/bin and name
org.openhab.binding.unifiprotect-3.0.0-ALPHAXX.jar

Future versions of this binding will mainly support **OpenHAB 3.0.0**
I'll build 2.5.x version if there is need.

## Usage
Maturity: ALPHA
OpenHAB Version: 2.5.x & Openhab 3.0.0Mx 

## Example of usage
- Detect Motion and trigger other system notifications, Alexa, Google Home, turn on lights, sound an alarm etc.
- Turn on and off notifications, can be used together with presence detection.
- View general information about Cameras and NVR, for instance check hard-drive health and storage.
- Reboot Cameras
- Turn on / off IR - leds
- Turn on / off Recording from the camera
- Turn on / off HDR mode
- Turn on / off High FPS mode
- Turn of / off status light on camera
- Download a thumbnail of latest motion event
- Download a snapshot from the Camera
- Download an anonymous snapshot (needed if more than you require snapshots more frequent than every 10 seconds)
- Download a Heat map of latest recorded motion event
- Get a score of 0-100 on each motion event, in order to be able to filter out false positives
- Detect if a hardrive in the nvr fails
- Monitor the memory / diskstorage and CPU temperature

## Supported hardware
- UniFi Protect Cloud Key Gen2+ Firmware >= 2.0.18
- UniFi Dream Machine / Pro 
- Any UniFi Protect Camera
- UniFi NVR 
- UniFi Doorbell NOT SUPPORTED (Support can be added at a later stage)

The binding has been rewritten to support UniFi OS only. That means if you have a UCKP (Cloudkey gen2+)
you need to update it to at least firmware 2.0.18. After 2.0.18 UCKP is running UniFiOs just like the
UDMP and UNVR. 
***Please note that the UniFi Network binding currently is not working with firmware >= 2.0.18.***
***If you are using the UniFi binding think twice before upgrading.***

## Dependencies
* OH3 Map transformations ( https://www.openhab.org/addons/transformations/map/ )

## Manual setup
* Log into UniFi Protect and create a user with admin rights that you use
* Manually log into all cameras where you want to use anonymous-snapshots, you have to
enable it yourself.  (See next section for a detailed description)
See https://github.com/briis/unifiprotect for instructions on how to add a user.

A quirk is that there is a bug in firmware 2.0.18 which does not display the local user name
when calling the API. Therefor you need to have the same First Name as user name in order for the binding to pick
up the correct user.

## Step by step setup Anonymous Snapshots (creds @largepills) 
To use the Anonymous Snapshot, you must ensure that each Camera is configured to allow this. This cannot be done in the UniFi Protect Controller, but has to be done on each individual Camera.
1. Login to each of your Cameras by going to http://CAMERA_IP. The Username is ubnt and the Camera Password can be found in Unifi Protect under Settings.
2. If you have never logged in to the Camera before, it might take you through a Setup procedure - just make sure to keep it in Unifi Video mode, so that it is managed by Unifi Protect.
3. Once you are logged in, you will see an option on the Front page for enabling Anonymous Snapshots. Make sure this is checked, and then press the Save Changes button.
4. Repeat step 3 for each of your Cameras.

## Supported Things

* `nvr/bridge` - Running UniFi Protect and handles the attached cameras 
* `camera` -  A UniFi Camera G3/G4 etc

## Discovery
Discovery of cameras is possible. Background auto discovery is turned off and the Controller / NVR needs
to be added manually before you can discover any devices.

## Binding Configuration
The binding has no configuration options, all configuration is done at the Bridge and Thing levels.

## NVR / Bridge Configuration

You need to configure a NVR / Bridge manually before you can setup any cameras.

The following table describes the Bridge configuration parameters:

| Parameter                | Description                                          | Config   | Default |
| ------------------------ | ---------------------------------------------------- |--------- | ------- |
| Hostname                 | Hostname or IP address of the NVR                    | Required | -       |
| Username                 | The username to access the UniFiProtect              | Required | -       |
| Password                 | The password credential                              | Required | -       |
| Refresh Interval         | Refresh interval in seconds                          | Required | 60      |
| Thumbnail Width          | Thumbnails will use this width                       | Required | 640     |
| Image Folder             | Images (snapshots etc) will be stored in this folder | Optional | -       |
| Events Timer Period 	   | The number of seconds to look back for motion events | Optional | 30      |


## Thing Configuration

You must add a NVR / Controller (Bridge) before adding Cameras.

The following table describes the Thing configuration parameters:

| Parameter    | Description                                                  | Config   | Default |
| ------------ | -------------------------------------------------------------|--------- | ------- |
| Name         | The name of The Camera                                       | Required | -       |
| Mac          | The MAC address of the camera                                | Required | 180     |

## NVR Channels

The NVR Channels

| Channel ID                   | Item Type | Description                                                          | Permissions |
|------------------------------|-----------|--------------------------------------------------------------------- | ----------- |
| name                         | String    | NVR Name                                                             | Read        |
| host                         | String    | Hostname / IP                                                        | Read        |
| version                      | String    | NVR Version                                                          | Read        |
| firmware-version             | String    | NVR Firmware version                                                 | Read        |
| uptime                       | Number    | Uptime in milliseconds                                               | Read        |
| last-seen                    | DateTime  | NVR Last Seen At                                                     | Read        |
| last-updated-at              | DateTime  | NVR Last updated at                                                  | Read        |
| is-connected-to-cloud        | Switch    | NVR is Connected to UniFi Cloud                                      | Read        |
| enable-automatic-backups     | Switch    | Automatic backups is enabled or disabled                             | Read        |
| hosts                        | String    | Hosts string (multiple hosts)                                        | Read        |
| host-short-name              | String    | The host short-name                                                  | Read        |
| recording-retention-duration | Number    | Recording retention duration in ms                                   | Read        |
| storage-total-size           | Number    | Total storage size                                                   | Read        |
| storage-type                 | String    | Type of Storage (hdd, usb or similar)                                | Read        |
| storage-used                 | Number    | Used storage space                                                   | Read        |
| storage-available            | String    | Total storage available                                              | Read        |
| device-0-model               | String    | Storage Device 0 model                                               | Read        |
| device-0-size                | Number    | Storage Device 0 size                                                | Read        |
| device-0-healthy             | Switch    | Storage Device 0 Healthy                                             | Read        |
| cpu-average-load             | Number    | CPU Average Load                                                     | Read        |
| cpu-temperature              | Number    | CPU Temperature                                                      | Read        |
| mem-available                | Number    | Available Memory                                                     | Read        |
| mem-free                     | Number    | Free Memory                                                          | Read        |
| mem-total                    | Number    | Tota Memory                                                          | Read        |
| alerts                       | Switch    | Turn on or of notifications                                          | Read/Write  |

### `alerts`

The `alerts` channel allows you to turn on or off alerts sent by the UniFi Protect App.
You need to configure the alerts yourself by logging into the controller.

### Camera Channels

| Channel ID                   | Item Type | Description                                                          | Permissions |
|------------------------------|-----------|--------------------------------------------------------------------- | ----------- |
| name                         | String    | Camera Name                                                          | Read        |
| type                         | String    | Camera Type                                                          | Read        |
| state                        | String    | Connection state                                                     | Read        |
| host                         | String    | Hostname / IP                                                        | Read        |
| is-dark                      | Switch    | If it is considered to be dark                                       | Read        |
| status-light                 | Switch    | Turn on / off status light on the camera                             | Read/Write  |
| reboot                       | Switch    | Reboot a camera                                                      | Read/Write  |
| motion-heatmap               | Image     | An Heatmap of the last motion event                                  | Read        |
| motion-thumbnail             | Image     | A thumbnail image of the last motion event                           | Read        |
| motion-score                 | Number    | A numeric score for last motion event 0-100                          | Read        |
| snapshot                     | Switch    | Capture a snapshot in the image folder                               | Read/Write  |
| snapshot-img                 | Image     | The snapshot image                                                   | Read        |
| a-snapshot                   | Switch    | Capture Anonymous snapshot, stored in the image folder               | Read/Write  |
| a-snapshot-img               | Image     | The anonymous snapshot image                                         | Read        |
| ir-mode                      | Number    | Infra red mode Auto, On, Off                                         | Read/Write  |
| hdr-mode                     | Switch    | Turn HDR on or off                                                   | Read/Write  |
| high-fps-mode                | Switch    | Turn High FPS mode on / off (needs to be supported by camera)        | Read/Write  |
| recording-mode               | Number    | Set recording mode for the camera On/Off/Motion                      | Read/Write  |
| is-motion-detected           | Switch    | Turned on when a motion is detected                                  | Read        |
| is-mic-enabled               | Switch    | If mic is enabled / disabled                                         | Read        |
| is-recording                 | Switch    | If the camera is recording                                           | Read        |
| up-since                     | DateTime  | Camera has been up since                                             | Read        |
| connected-since              | DateTime  | Camera has been connected since                                      | Read        |
| last-seen                    | DateTime  | Camera was last seen                                                 | Read        |
| last-motion                  | DateTime  | When last motion was detected at                                     | Read        |
| mic-volume                   | Number    | The volume of the microphone                                         | Read        |

## Full Example
Make sure you replace the ids in the items below (NVRID and MACADDRESS)

Things
Use paper UI to either discovery or add things
If you want to create Things manually use below example (although this is not encouraged)

items/unifiprotect.items

```
Bridge unifiprotect:nvr:NVRID "UniFi Protect NVR" [ host="...", username="...", password="...", refresh=60 ] {
   Thing camera frontDoorCamera [name="Front door camera", mac="xx:xx:xx:xx:xx:xx"]
}
```

The String NVRID refers to the Thing Id of the bridge. It will be something random if you create it 
using the GUI.

items/unifiprotect.items
```
Group    gUniFiProtect        "UniFi Protect"
Group    sUniFiProtect        "Sitemap UniFiProtect"

//NVR
Group    CKG2PNvr                 "CKG2+ Nvr" (gUniFiProtect)
String   CKG2PNvrName             "CKG2+ Name"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:name" }
String   CKG2PNvrHost             "CKG2+ Host"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host" }
String   CKG2PNvrHosts             "CKG2+ Hosts"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hosts" }
String   CKG2PNvrHostShortName     "CKG2+ Host Short Name"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host-short-name" }
String   CKG2PNvrVersion             "CKG2+ Version"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:version" }
String   CKG2PNvrFirmwareVersion             "CKG2+ FirmwareVersion"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:firmware-version" }
Number   CKG2PNvrUptime             "CKG2+ Uptime [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:uptime" }
DateTime CKG2PNvrLastUpdatedAt      "CKG2+ LastUpdated [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" <timestamp>  (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:last-updated-at" }
DateTime CKG2PNvrLastSeen      "CKG2+ LastSeen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"  <timestamp> (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:last-seen" }
Switch   CKG2PNvrConnectedToCloud "CKG2+ Cloud Connected [%s]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:is-connected-to-cloud" }
Switch   CKG2PNvrAutomaticBackups "CKG2+ Enabled Automatic Backups [%s]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:enable-automatic-backups" }
Number   CKG2PNvrRetention             "CKG2+ Recording Retention Duration [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:recording-retention-duration" }
Number   CKG2PNvrCpuLoad             "CKG2+ CPU Load [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:cpu-average-load" }
Number   CKG2PNvrCpuTemperature             "CKG2+ CPU Temperature [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:cpu-temperature" }
Number   CKG2PNvrMemAvailable             "CKG2+ Memory Available [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-available" }
Number   CKG2PNvrMemFree             "CKG2+ Memory Free [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-total" }
Number   CKG2PNvrMemTotal             "CKG2+ Memory Total [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-free" }
Number   CKG2PNvrStorageUsed             "CKG2+ Storage Used [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-used" }
Number   CKG2PNvrStorageTotalSize             "CKG2+ Storage Total Size [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-total-size" }
Number   CKG2PNvrStorageAvailable             "CKG2+ Storage Available [%d]"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-available" }
String   CKG2PNvrStorageType             "CKG2+ Storage Type"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-type" }
String   CKG2PNvrD0Model    "CKG2+ Device 0 Model"                                    (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-model" }
Switch   CKG2PNvrD0Healthy    "CKG2+ Device 0 Healthy"                                    (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-healthy" }
Number   CKG2PNvrD0Size    "CKG2+ Device 0 Size [%d]"                                    (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-size" }
Switch   CKG2PNvrAlerts           "CKG2+ Alerts [%s]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:alerts" }

//G3 Dome Camera
Group    G3DMyCam                 "G3 Cam"                       (gUniFiProtect)
String   G3DMyCamName             "G3 Cam Name"                   (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:name" }
String   G3DMyCamType             "G3 Cam Type"                   (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:type" }
String   G3DMyCamHost             "G3 Cam Host"                   (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:host" }
String   G3DMyCamState            "G3 Cam State"                   (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:state" }
DateTime G3DMyCamUpSince          "G3 Cam Up Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:up-since" }       
DateTime G3DMyCamLastSeen         "G3 Cam Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:last-seen" }       
DateTime G3DMyCamConnectedSince   "G3 Cam Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:connected-since" }       
DateTime G3DMyCamLastMotion       "G3 Cam Last Motion [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:last-motion" }       
Number   G3DMyCamMicVolume        "G3 Cam Mic Volume [%d]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:mic-volume" }
Switch   G3DMyCamMicEnabled       "G3 Cam Mic Enabled [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:is-mic-enabled" }
Switch   G3DMyCamDark             "G3 Cam is Dark [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:is-dark" }
Switch   G3DMyCamRecording        "G3 Cam is Recording [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:is-recording" }
Switch   G3DMyCamMotionDetect           "G3 Cam Motion Detected [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:is-motion-detected" }
Switch   G3DMyCamStatusLight           "G3 Cam Status Light [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:status-light" }
Switch   G3DMyCamReboot           "G3 Cam Reboot [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:reboot" }
Switch   G3DMyCamHDRMode           "G3 Cam HDR Mode [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:hdr-mode" }
Switch   G3DMyCamHighFPSMode           "G3 Cam High Fps Mode [%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:high-fps-mode" }
Number   G3DMyCamIRMode           "G3 Cam IR Mode [MAP(unifiprotect_ir.map):%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:ir-mode" }
Number   G3DMyCamRecordingMode   "G3 Cam Recording mode [MAP(unifiprotect_rec.map):%s]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:recording-mode" }
Switch   G3DMyCamAnonSnapshot   "G3 Cam AnonSnapshot " (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:a-snapshot",expire="3s,command=OFF" } 
Image   G3DMyCamAnonSnapshotImg    "G3 Cam AnonSnapshot Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:a-snapshot-img" } 
Switch   G3DMyCamSnapshot   "G3 Cam Snapshot " (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:snapshot",expire="3s,command=OFF" } 
Image   G3DMyCamSnapshotImg    "G3 Cam Snapshot Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:snapshot-img" } 
Image   G3DMyMotionThumbnail    "G3 Thumbnail Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:motion-thumbnail" } 
Image   G3DMyMotionHeatmap    "G3 Heatmap Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:motion-heatmap" } 
Number  G3DMyMotionScore      "G3 Score [%d]" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:motion-score" } 
```
transform/unifiprotect_ir.map
```
0=Auto
1=On
2=FilterOnly
NULL=-
-=-
```

transform/unifiprotect_rec.map
```
0=Never
1=Always
2=Motion
NULL=-
-=-
```

Sitemap
```
sitemap unifiprotect label="UniFiProtect Binding" {
	Frame {
		 Group item=sUniFiProtect {
		   Group item=gUniFiProtect
                   Switch item=G3DMyCamIRMode mappings=[0="Auto",1="On",2="FilterOnly"]
                   Switch item=G3DMyCamRecordingMode mappings=[0="Never",1="Always",2="Motion"]
                 }
	}
}
```

## Manual Install
Get jar-file from repo. Place the jar-file in the openhab-addons folder
https://github.com/seaside1/unifiprotect

## Changelog
  ### ALPHA9
 * Fixed Image type for snapshot
  ### ALPHA8
 * Websocket connection is reconnected when closed
 * Fetching thumbnail and heatmap more quickly and in a more stable approach
 * Fixed Name, Status, MAC, Host channels
  ### ALPHA 7
   * OpenHAB Version 3 support
  ### ALPHA 6
   * Websocket Event API Support
  ### ALPHA 5
 * Removed port configuration from the binding. Port 443 and port 80 are used.
 * Renamed and added several attributes for NVR thing
 * Major refactoring for UniFiOS
 * Prepared for event based API
 * Workaround for bug with localusername, firstname needs to be the same as localusername
 * Motion score added
 * Events are now fetched at the same rate as refresh 
 * Changed debug log to not be as verbose
 
## Roadmap
* Support UniFi Doorbell
* Support for multiple harddrives
* Live view of cameras
