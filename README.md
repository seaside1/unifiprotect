![alt text](https://raw.githubusercontent.com/seaside1/unifiprotect/main/img/logocombo.png)
# UniFi Protect  Binding
## Usage
Integrates UniFi Protect Camera System into OpenHAB. See https://ui.com/why-protect/
This binding utilizes an undocumented json REST API that is present in the NVR. It works very similar
to the Homebridge solution: and HomeAssistant solution: but is written in java and tailored for OpenHAB.

## About
Maturity: ALPHA
OpenHAB Version: 2.5.x

##  

Example of usage
- Detect Motion and trigger other system (Notifications, Alexa, Google Home, turn on lights, sound an alarm etc)
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

## Supported hardware
- UniFi Protect Cloud Key Gen2+
- UniFi Dream Machine / Pro NOT SUPPORTED (Support can be added at a later stage)
- Any UniFi Protect Camera
- UniFi NVR NOT SUPPORTED (Same as UDMP)
- UniFi Doorbell NOT SUPPORTED (Support can be added at a later stage)

The reason why UniFi NVR and UniFi Dream Machine/Pro is not supported is because they
are built on UniFi OS and have a slightly different json/rest protocol. For UniFi OS
it is possible to subscribe to an event subscription protocol over web sockets, thus removing
polling the API for events. This could probably be added in the future, but right now
I don't have any UniFi OS hardware to test / develop this.

## Dependencies


## Manual setup
* Log into UniFi Protect and create a user with admin rights that you use
* Manually log into all cameras where you want to use anonmous-snapshots, you have to
enable it yourself

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
| Port                     | Port of the for NVR                                  | Required | 7443    |
| Username                 | The username to access the UniFiProtect              | Required | -       |
| Password                 | The password credential                              | Required | -       |
| Refresh Interval         | Refresh interval in seconds (Polling)                | Required | 10      |
| Thumbnail Width          | Thumbnails will use this width                       | Required | 640     |
| Image Folder             | Images (snapshots etc) will be stored in this folder | Optional | -       |

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
| total-size                   | Number    | Total storage size                                                   | Read        |
| total-space-used             | Number    | Total space used                                                     | Read        |
| hard-drive-0-status          | String    | Hard drive 0 status (present/absent)                                 | Read        |
| hard-drive-0-name            | String    | Hard drive 0 name Manufacturer name                                  | Read        |
| hard-drive-0-size            | Number    | Hard drive 0 size                                                    | Read        |
| hard-drive-0-health          | String    | Hard drive 0 Health                                                  | Read        |
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
| thumbnail                    | Switch    | Store a thumbnail of last motion event in the image folder           | Read/write  |
| thumbnail-img                | Image     | Image to thumbnail last motion event                                 | Read        |
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

items/unifiprotect.items

```
Group    gUniFiProtect        "UniFi Protect"
Group    sUniFiProtect        "Sitemap UniFiProtect"

Group    CKG2PNvr "CKG2+ Nvr" (gUniFiProtect)
String   CKG2PNvrName             "CKG2+ Name"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:name" }
String   CKG2PNvrHost             "CKG2+ Host"                   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host" }
String   CKG2PNvrHosts            "CKG2+ Hosts"                  (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hosts" }
String   CKG2PNvrHostShortName    "CKG2+ Host Short Name"        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host-short-name" }
String   CKG2PNvrVersion          "CKG2+ Version"                (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:version" }
String   CKG2PNvrFirmwareVersion  "CKG2+ FirmwareVersion"        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:firmware-version" }
Number   CKG2PNvrUptime           "CKG2+ Uptime [%d]"            (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:uptime" }
DateTime CKG2PNvrLastUpdatedAt    "CKG2+ LastUpdated [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (CKG2PNvr) {channel="unifiprotect:nvr:NVRID:last-updated-at" }
DateTime CKG2PNvrLastSeen         "CKG2+ LastSeen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:last-seen" }
Switch   CKG2PNvrConnectedToCloud "CKG2+ Cloud Connected [%s]"   (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:is-connected-to-cloud" }
Switch   CKG2PNvrAutomaticBackups "CKG2+ Enabled Automatic Backups [%s]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:enable-automatic-backups" }
Number   CKG2PNvrRetention        "CKG2+ Recording Retention Duration [%d]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:recording-retention-duration" }
Number   CKG2PNvrTotalSize        "CKG2+ Total Size [%d]"        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:total-size" }
Number   CKG2PNvrTotalSpaceUsed   "CKG2+ Total Space Used [%d]"  (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:total-space-used" }
String   CKG2PNvrHd0Status        "CKG2+ Harddrive 0 Status"     (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hard-drive-0-status" }
String   CKG2PNvrHd0Name          "CKG2+ Harddrive 0 Name"       (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hard-drive-0-name" }
String   CKG2PNvrHd0Health        "CKG2+ Harddrive 0 Health"     (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hard-drive-0-health" }
Number   CKG2PNvrHd0Size          "CKG2+ Harddrive 0 Size [%d]"  (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hard-drive-0-size" }
Switch   CKG2PNvrAlerts           "CKG2+ Alerts [%s]"            (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:alerts" }

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
Switch   G3DMyCamThumbnail   "G3 Cam Thumbnail" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:thumbnail",expire="3s,command=OFF" } 
Image   G3DMyCamThumbnailImg    "G3 Cam Thumbnail Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:thumbnail-img" } 
Switch   G3DMyCamHeatmap   "G3 Cam Heatmap" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:heatmap",expire="3s,command=OFF" } 
Image   G3DMyCamHeatmapImg    "G3 Cam Heatmap Img" (G3DMyCam) { channel="unifiprotect:camera:NVRID:MACADDRESS:heatmap-img" } 
```

rules/unifiprotect.rules
This rules example will capture a thumbnail and heatmap once a motion is triggered.
The delay is in order for the NVR to finish registering the event
```
val String LOG = "UniFiProtect"

rule "UniFiProtectMotionDetect"
when 
    Item G3DMyCamMotionDetect changed to ON
then
    logInfo(LOG,"Motion detected by UniFiProtect")
    createTimer(now.plusSeconds(10), [ |    
        G3DMyCamThumbnail.sendCommand(ON)
        G3DMyCamHeatmap.sendCommand(ON) 
    ])
end
```
transform/unifiprotect_ir.map
```
0=Auto
1=On
2=FilterOnly
```

transform/unifiprotect_rec.map
```
0=Never
1=Always
2=Motion
```

Sitemap
```
sitemap unifiprotect label="UniFiProtect Binding" {
	Frame {
		 Group item=sUniFiProtect {
		   Group item=gUniFiProtect
                   Switch item=G3DMyCamMode mappings=[0="Auto",1="On",2="FilterOnly"]
                   Switch item=G3DMyCamRecordingMode mappings=[0="Never",1="Always",2="Motion"]
                 }
	}
}
```

## Manual Install
Get jar-file from repo. Place the jar-file in the openhab-addons folder
https://github.com/seaside1/unifiprotect

## Roadmap

* OpenHAB Version 3 support
* Add support for UniFi OS (Websocket event api)
* Support UniFi Doorbell
* Support for multiple harddrives
