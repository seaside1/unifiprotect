[![GitHub Release](https://img.shields.io/github/release/seaside1/unifiprotect.svg?style=flat)](https://github.com/seaside1/unifiprotect/releases/latest)

  
![alt text](https://raw.githubusercontent.com/seaside1/unifiprotect/main/img/logocombo.png)

Integrates UniFi Protect Camera System into openHAB. See https://ui.com/why-protect/
This binding utilizes an undocumented json REST API that is present in the NVR. It works very similar
to the Homebridge and HomeAssistant soultion.

The binding is written in java and tailored for openHAB.

The Binding has a configurable refresh rate. The refresh rate will update the NVR information. 
For Events (motion detection) UniFiProtect Event API over websockets has been implemented, 
thus motion detection is instant and without the need of polling.

## openHAB Version

openHAB version 4.x.x is supported.

## Usage

Maturity: Stable

## Example of usage
- Toggle Privacy Zone on or off, for instance turn on when you are home
- Turn recording on or off, for instance turn off recoring when you are home
- Send a notification on motion or ring on doorbell together with a snapshot from the camera
- Detect Motion and trigger other system notifications, Alexa, Google Home, turn on lights, sound an alarm etc.
- Limit detection to only smart motions such as person, vehicle and package.
- Smoke and CO alarm trigger for G4 and later cameras
- Turn on and off notifications, can be used together with presence detection.
- View general information about Cameras and NVR, for instance check hard-drive health and storage.
- Reboot Cameras
- Disable chime on doorbell certain hours
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
- G4 Camera Smart Detections (Person or vehicle)
- G4 Camera Smart Detection thumbnail
- G4 Camera Smart Detection Score
- G4 Camera enable smart detections
- G4 Doorbell set custom LCD message
- G4 Doorbell set predefined message (Leave package and do not disturb)
- G4 Doorbell detect if it is ringing

## Supported hardware

- UniFi Protect Cloud Key Gen2+ Firmware >= 2.0.18
- UniFi Dream Machine / Pro 
- Any UniFi Protect Camera G3, G4 and G5
- UniFi NVR 
- UniFi Doorbell / Pro

The binding has been rewritten to support UniFi OS only. That means if you have a UCKP (Cloudkey gen2+)
you need to update it to at least firmware 2.0.18. After 2.0.18 UCKP is running UniFiOs just like the
UDMP and UNVR. 

<b>Note stay on stable UniFi Protect version. Early Access versions are not supported.</b>

## Dependencies

* OH Map transformations ( https://www.openhab.org/addons/transformations/map/ )

## Manual setup

* Log into UniFi Protect and create a user with admin rights that you use
* Manually log into all cameras where you want to use anonymous-snapshots, 
you have to enable it yourself.  (See next section for a detailed description)
See https://www.home-assistant.io/integrations/unifiprotect/#local-user for instructions on how to add a user.

A quirk is that there is a bug in firmware 2.0.18 which does not display the local user name
when calling the API. Therefor you need to have the same First Name as user name in order for the binding to pick
up the correct user.

## Step by step setup Anonymous Snapshots 

To use the Anonymous Snapshot, you must ensure that each Camera is configured to allow this. 
This cannot be done in the UniFi Protect Controller, but has to be done on each individual Camera.

1. Login to each of your Cameras by going to http://CAMERA_IP. The Username is ubnt and the Camera Password can be found in Unifi Protect under Settings.
2. If you have never logged in to the Camera before, it might take you through a Setup procedure - just make sure to keep it in Unifi Video mode, so that it is managed by Unifi Protect.
3. Once you are logged in, you will see an option on the Front page for enabling Anonymous Snapshots. Make sure this is checked, and then press the Save Changes button.
4. Repeat step 3 for each of your Cameras.
(creds @largepills)

## Supported Things

* `nvr/bridge` - Running UniFi Protect and handles the attached cameras 
* `g3camera` -  A UniFi G3 Camera 
* `g4camera` - A UniFi G4 Camera
* `g4doorbell` - A UniFi G4 Doorbell / Doorbell Pro
* `g5camera` - A UniFi G5 Camera

## Discovery

Discovery of cameras is possible. Background auto discovery is turned off and the Controller / NVR needs to be added manually before you can discover any devices. 
Doing discovery in the UI is recommended since it will reduce the risk of syntax errors.

## Binding Configuration

The binding has no configuration options, all configuration is done at the Bridge and Thing levels.

## NVR / Bridge Configuration

You need to configure a NVR / Bridge manually before you can setup any cameras.

The following table describes the Bridge configuration parameters:

| Parameter                | Description                                                | Config   | Default |
| ------------------------ | ---------------------------------------------------------- |--------- | ------- |
| Hostname                 | Hostname or IP address of the NVR                          | Required | -       |
| Username                 | The username to access the UniFiProtect                    | Required | -       |
| Password                 | The password credential                                    | Required | -       |
| Refresh Interval         | Refresh interval in seconds                                | Required | 60      |
| Thumbnail Width          | Thumbnails will use this width                             | Required | 640     |
| Image Folder             | Images (snapshots etc) will be stored in this folder       | Optional | -       |
| Events Timer Period      | The number of seconds to look back for motion events       | Optional | 30      |
| Watchdog                 | Watchdog for restarting binding if no events are detected  | Optional | True    |

## Thing Configuration

You must add a NVR / Controller (Bridge) before adding Cameras.

The following table describes the Thing configuration parameters for all cameras:

| Parameter              | Description                                                  | Config   | Default |
| ---------------------- | -------------------------------------------------------------|--------- | ------- |
| Name                   | The name of the Camera                                       | Required | -       |
| Mac                    | The MAC address of the camera (uppercase without ':')        | Required | 180     |
| eventDownloadHeatMap   | Enable/disable download of heatmap picture on motion         | Optional | True    |
| eventDownloadThumbnail | Enable/disable download of thumbnail on motion               | Optional | True    |

The following table describes the Thing configuration parameters for G4 Doorbell:

| Parameter         | Description                                                       | Config   | Default |
| ----------------- | ------------------------------------------------------------------|--------- | ------- |
| Name              | The name of the G4 Doorbell                                       | Required | -       |
| Mac               | The MAC address of the G4 Doorbell (uppercase without ':')        | Required | 180     |
| lcdCustomMessage  | A custm LCD preconfigured message                                 | Optional | -       |
| chimeDuration     | If chime is enabled this is the preset duration that vill be used | Optional | -       |

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

### Alerts

The alerts channel allows you to turn on or off alerts sent by the UniFi Protect App.
You need to configure the alerts yourself by logging into the controller.

### G3 Camera Channels

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
| motion-detection             | Switch    | Turn motion detection on or off                                      | Read/Write  |
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
| privacy-zone                 | Switch    | Turn Privacy Filter on / Off                                         | Read/Write  |

### G4 and G5 Camera Channels (Has all the G3 Channels)

| Channel ID                   | Item Type | Description                                                          | Permissions |
|------------------------------|-----------|--------------------------------------------------------------------- | ----------- |
| smart-detect-person          | Switch    | Toggle setting for smartdetection of a person                        | Read/Write  |
| smart-detect-vehicle         | Switch    | Toggle setting for smartdetection of a vehicle                       | Read/Write  |
| smart-detect-package         | Switch    | Toggled setting for smartdetection of package                        | Read/Write  |
| smart-detect-smoke           | Switch    | Toggled setting for smartdetection of smoke / CO                     | Read/Write  |
| smart-detect-motion          | Switch    | Toggled when a smartdetection is triggered                           | Read        |
| smart-detect-thumbnail       | Image     | A thumbnail image of the last smart detection event                  | Read        |
| smart-detect-score           | Number    | A numeric score for last smart motion event 0-100                    | Read        |
| smart-detect-type            | String    | The last smart detection type vehicle or  preson                     | Read        |
| smart-detect-last            | DateTime  | Last Smart Detection Event timestamp                                 | Read        |

### G4 Doorbell Camera Channels (Has all the G3 and G4 Channels)
The LCD message displayed by the doorbell will either be the configured message in the thing configuration or
the set message in the lcd-custom-text channel. 
Use the thing config if you want a static custom message to be displayed. Use the lcd-custom-text channel
if you want to dynamically set the message.

| Channel ID                   | Item Type | Description                                                                     | Permissions |
|------------------------------|-----------|-------------------------------------------------------------------------------- | ----------- |
| ring-thumbnail               | Image     | Last Ring Event Image                                                           | Read/Write  |
| is-ringing                   | Switch    | Toggled when doorbell is pushed (ding-dong)                                     | Read/Write  |
| last-ring                    | DateTime  | Timestamp for last ring event                                                   | Read/Write  |
| lcd-leave-package            | Switch    | Will display the predefined Leave Package at Door message                       | Read/Write  |
| lcd-do-not-disturb           | Switch    | Will display the predefined Do not disturb message                              | Read/Write  |
| lcd-custom-text              | String    | Channel for setting custom lcd messages                                         | Read/Write  |
| status-sounds                | Switch    | Channel for setting toggle status sounds                                        | Read/Write  |
| chime                        | Switch    | Will toggle the Chime on or off. Will used thing configured value for duration. | Read/Write  |


### G5 Camera Channels (Has all the G3 & G4 Channels)

| Channel ID                   | Item Type | Description                                                          | Permissions |
|------------------------------|-----------|--------------------------------------------------------------------- | ----------- |

## Full Example

Make sure you replace the ids in the items below (NVRID and MACADDRESS)
The String NVRID refers to the Thing Id of the bridge. It will be something random if you create it 
using the GUI.

### Things

Use paper UI to either discovery or add things
If you want to create Things manually use below example (although this is not encouraged)

things/unifiprotect.things

```
Bridge unifiprotect:nvr:NVRID "UniFi Protect NVR" [ host="...", username="...", password="...", refresh=60 ] {
   Thing camera frontDoorCamera [name="Front door camera", mac="AABBCCDDEEFFGG"]
}
```

<b>Important, the mac address needs to be in uppercase without ":".</b>

items/unifiprotect.items

```
//NVR
Group    CKG2PNvr                   "CKG2+ Nvr"                                         (gUniFiProtect)
String   CKG2PNvrName               "CKG2+ Name"                                        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:name" }
String   CKG2PNvrHost               "CKG2+ Host"                                        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host" }
String   CKG2PNvrHosts              "CKG2+ Hosts"                                       (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:hosts" }
String   CKG2PNvrHostShortName      "CKG2+ Host Short Name"                             (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:host-short-name" }
String   CKG2PNvrVersion            "CKG2+ Version"                                     (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:version" }
String   CKG2PNvrFirmwareVersion    "CKG2+ FirmwareVersion"                             (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:firmware-version" }
Number   CKG2PNvrUptime             "CKG2+ Uptime [%d]"                                 (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:uptime" }
DateTime CKG2PNvrLastUpdatedAt      "CKG2+ LastUpdated [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:last-updated-at" }
DateTime CKG2PNvrLastSeen           "CKG2+ LastSeen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"    (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:last-seen" }
Switch   CKG2PNvrConnectedToCloud   "CKG2+ Cloud Connected [%s]"                        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:is-connected-to-cloud" }
Switch   CKG2PNvrAutomaticBackups   "CKG2+ Enabled Automatic Backups [%s]"              (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:enable-automatic-backups" }
Number   CKG2PNvrRetention          "CKG2+ Recording Retention Duration [%d]"           (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:recording-retention-duration" }
Number   CKG2PNvrCpuLoad            "CKG2+ CPU Load [%d]"                               (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:cpu-average-load" }
Number   CKG2PNvrCpuTemperature     "CKG2+ CPU Temperature [%d]"                        (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:cpu-temperature" }
Number   CKG2PNvrMemAvailable       "CKG2+ Memory Available [%d]"                       (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-available" }
Number   CKG2PNvrMemFree            "CKG2+ Memory Free [%d]"                            (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-total" }
Number   CKG2PNvrMemTotal           "CKG2+ Memory Total [%d]"                           (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:mem-free" }
Number   CKG2PNvrStorageUsed        "CKG2+ Storage Used [%d]"                           (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-used" }
Number   CKG2PNvrStorageTotalSize   "CKG2+ Storage Total Size [%d]"                     (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-total-size" }
Number   CKG2PNvrStorageAvailable   "CKG2+ Storage Available [%d]"                      (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-available" }
String   CKG2PNvrStorageType        "CKG2+ Storage Type"                                (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:storage-type" }
String   CKG2PNvrD0Model            "CKG2+ Device 0 Model"                              (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-model" }
Switch   CKG2PNvrD0Healthy          "CKG2+ Device 0 Healthy"                            (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-healthy" }
Number   CKG2PNvrD0Size             "CKG2+ Device 0 Size [%d]"                          (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:device-0-size" }
Switch   CKG2PNvrAlerts             "CKG2+ Alerts [%s]"                                 (CKG2PNvr) { channel="unifiprotect:nvr:NVRID:alerts" }

//G3 Camera
Group    G3MyCam                 "G3 Cam"                                                 (gUniFiProtect)
String   G3MyCamName             "G3 Cam Name"                                            (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:name" }
String   G3MyCamType             "G3 Cam Type"                                            (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:type" }
String   G3MyCamHost             "G3 Cam Host"                                            (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:host" }
String   G3MyCamState            "G3 Cam State"                                           (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:state" }
DateTime G3MyCamUpSince          "G3 Cam Up Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"        (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:up-since" }       
DateTime G3MyCamLastSeen         "G3 Cam Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"       (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:last-seen" }       
DateTime G3MyCamConnectedSince   "G3 Cam Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:connected-since" }       
DateTime G3MyCamLastMotion       "G3 Cam Last Motion [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"     (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:last-motion" }       
Number   G3MyCamMicVolume        "G3 Cam Mic Volume [%d]"                                 (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:mic-volume" }
Switch   G3MyCamMicEnabled       "G3 Cam Mic Enabled [%s]"                                (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:is-mic-enabled" }
Switch   G3MyCamDark             "G3 Cam is Dark [%s]"                                    (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:is-dark" }
Switch   G3MyCamRecording        "G3 Cam is Recording [%s]"                               (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:is-recording" }
Switch   G3MyCamMotionDetect     "G3 Cam Motion Detected [%s]"                            (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:is-motion-detected" }
Switch   G3MyCamStatusLight      "G3 Cam Status Light [%s]"                               (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:status-light" }
Switch   G3MyCamReboot           "G3 Cam Reboot [%s]"                                     (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:reboot" }
Switch   G3MyCamHDRMode          "G3 Cam HDR Mode [%s]"                                   (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:hdr-mode" }
Switch   G3MyCamHighFPSMode      "G3 Cam High Fps Mode [%s]"                              (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:high-fps-mode" }
Number   G3MyCamIRMode           "G3 Cam IR Mode [MAP(unifiprotect_ir.map):%s]"           (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:ir-mode" }
Number   G3MyCamRecordingMode    "G3 Cam Recording mode [MAP(unifiprotect_rec.map):%s]"   (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:recording-mode" }
Switch   G3MyCamAnonSnapshot     "G3 Cam AnonSnapshot "                                   (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:a-snapshot",expire="3s,command=OFF" } 
Image    G3MyCamAnonSnapshotImg  "G3 Cam AnonSnapshot Img"                                (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:a-snapshot-img" } 
Switch   G3MyCamSnapshot         "G3 Cam Snapshot "                                       (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:snapshot",expire="3s,command=OFF" } 
Image    G3MyCamSnapshotImg      "G3 Cam Snapshot Img"                                    (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:snapshot-img" } 
Image    G3MyMotionThumbnail     "G3 Thumbnail Img"                                       (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:motion-thumbnail" } 
Image    G3MyMotionHeatmap       "G3 Heatmap Img"                                         (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:motion-heatmap" } 
Number   G3MyMotionScore         "G3 Score [%d]"                                          (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:motion-score" } 
Switch   G3MyCamPrivacyZone      "G3 Cam Privacy Zone [%s]"                               (G3MyCam) { channel="unifiprotect:g3camera:NVRID:MACADDRESS:privacy-zone" }

//G4 Camera
Group    G4MyCam                 "G4 Cam"                                                 (gUniFiProtect)
String   G4MyCamName             "G4 Cam Name"                                            (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:name" }
String   G4MyCamType             "G4 Cam Type"                                            (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:type" }
String   G4MyCamHost             "G4 Cam Host"                                            (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:host" }
String   G4MyCamState            "G4 Cam State"                                           (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:state" }
DateTime G4MyCamUpSince          "G4 Cam Up Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"        (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:up-since" }       
DateTime G4MyCamLastSeen         "G4 Cam Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"       (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:last-seen" }       
DateTime G4MyCamConnectedSince   "G4 Cam Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:connected-since" }       
DateTime G4MyCamLastMotion       "G4 Cam Last Motion [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"     (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:last-motion" }       
Number   G4MyCamMicVolume        "G4 Cam Mic Volume [%d]"                                 (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:mic-volume" }
Switch   G4MyCamMicEnabled       "G4 Cam Mic Enabled [%s]"                                (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:is-mic-enabled" }
Switch   G4MyCamDark             "G4 Cam is Dark [%s]"                                    (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:is-dark" }
Switch   G4MyCamRecording        "G4 Cam is Recording [%s]"                               (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:is-recording" }
Switch   G4MyCamMotionDetect     "G4 Cam Motion Detected [%s]"                            (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:is-motion-detected" }
Switch   G4MyCamStatusLight      "G4 Cam Status Light [%s]"                               (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:status-light" }
Switch   G4MyCamReboot           "G4 Cam Reboot [%s]"                                     (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:reboot" }
Switch   G4MyCamHDRMode          "G4 Cam HDR Mode [%s]"                                   (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:hdr-mode" }
Switch   G4MyCamHighFPSMode      "G4 Cam High Fps Mode [%s]"                              (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:high-fps-mode" }
Number   G4MyCamIRMode           "G4 Cam IR Mode [MAP(unifiprotect_ir.map):%s]"           (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:ir-mode" }
Number   G4MyCamRecordingMode    "G4 Cam Recording mode [MAP(unifiprotect_rec.map):%s]"   (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:recording-mode" }
Switch   G4MyCamAnonSnapshot     "G4 Cam AnonSnapshot "                                   (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:a-snapshot",expire="3s,command=OFF" } 
Image    G4MyCamAnonSnapshotImg  "G4 Cam AnonSnapshot Img"                                (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:a-snapshot-img" } 
Switch   G4MyCamSnapshot         "G4 Cam Snapshot "                                       (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:snapshot",expire="3s,command=OFF" } 
Image    G4MyCamSnapshotImg      "G4 Cam Snapshot Img"                                    (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:snapshot-img" } 
Image    G4DMyMotionThumbnail    "G4 Thumbnail Img"                                       (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:motion-thumbnail" } 
Image    G4DMyMotionHeatmap      "G4 Heatmap Img"                                         (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:motion-heatmap" } 
Number   G4DMyMotionScore        "G4 Score [%d]"                                          (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:motion-score" } 
Switch   G4SmartDetectPerson     "G4 SmartDetect Person"                                  (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-person" }
Switch   G4SmartDetectVehicle    "G4 SmartDetect Vehicle"                                 (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-vehicle" }
Switch   G4SmartDetectMotion     "G4 SmartDetect Motion"                                  (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-motion" }
Image    G4SmartDetectThumbnail  "G4 SmartDetect Thumbnail Img"                           (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-thumbnail" } 
Number   G4SmartDetectScore      "G4 Score [%d]"                                          (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-score" } 
String   G4SmartDetectType       "G4 SmartDetect Type"                                    (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-type" }
DateTime G4SmartDetectLast       "G4 Last SmartDetect [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"    (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:smart-detect-last" }
Switch   G4MyCamPrivacyZone      "G4 Cam Privacy Zone [%s]"                               (G4MyCam) { channel="unifiprotect:g4camera:NVRID:MACADDRESS:privacy-zone" }

//G4 Doorbell
Group    G4DB                     "G4DB"                                                   (gUniFiProtect)
String   G4DBName                 "G4DB Name"                                              (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:name" }
String   G4DBType                 "G4DB Type"                                              (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:type" }
String   G4DBHost                 "G4DB Host"                                              (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:host" }
String   G4DBState                "G4DB State"                                             (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:state" }
DateTime G4DBUpSince              "G4DB Up Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"          (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:up-since" }       
DateTime G4DBLastSeen             "G4DB Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"         (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:last-seen" }       
DateTime G4DBConnectedSince       "G4DB Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"   (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:connected-since" }       
DateTime G4DBLastMotion           "G4DB Last Motion [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"       (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:last-motion" }       
Number   G4DBMicVolume            "G4DB Mic Volume [%d]" (                                 (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:mic-volume" }       
Switch   G4DBMicEnabled           "G4DB Mic Enabled [%s]"                                  (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:is-mic-enabled" }       
Switch   G4DBDark                 "G4DB is Dark [%s]"                                      (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:is-dark" }       
Switch   G4DBRecording            "G4DB is Recording [%s]"                                 (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:is-recording" }              
Switch   G4DBMotionDetect         "G4DB Motion Detected [%s]"                              (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:is-motion-detected" }       
Switch   G4DBStatusLight          "G4DB Status Light [%s]"                                 (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:status-light" }       
Switch   G4DBReboot               "G4DB Reboot [%s]"                                       (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:reboot" }       
Switch   G4DBHDRMode              "G4DB HDR Mode [%s]"                                     (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:hdr-mode" }
Switch   G4DBHighFPSMode          "G4DB High Fps Mode [%s]"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:high-fps-mode" }       
Number   G4DBIRMode               "G4DB IR Mode [MAP(unifiprotect_ir.map):%s]"             (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:ir-mode" }       
Number   G4DBRecordingMode        "G4DB Recording mode [MAP(unifiprotect_rec.map):%s]"     (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:recording-mode" }       
Switch   G4DBAnonSnapshot         "G4DB AnonSnapshot "                                     (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:a-snapshot",expire="3s,command=OFF" } 
Image    G4DBAnonSnapshotImg      "G4DB AnonSnapshot Img"                                  (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:a-snapshot-img" } 
Switch   G4DBSnapshot             "G4DB Snapshot "                                         (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:snapshot",expire="3s,command=OFF" } 
Image    G4DBSnapshotImg          "G4DB Snapshot Img"                                      (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:snapshot-img" } 
Image    G4DBMotionThumbnail      "G4DB Thumbnail Img"                                     (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:motion-thumbnail" } 
Image    G4DBMotionHeatmap        "G4DB Heatmap Img"                                       (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:motion-heatmap" } 
Number   G4DBMotionScore          "G4DB Score [%d]"                                        (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:motion-score" } 
Switch   G4DBLcdLeavePackage      "G4DB LCD Leave Package At Door"                         (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:lcd-leave-package",expire="5s,command=OFF" } 
Switch   G4DBLcdDoNotDisturb      "G4DB LCD Do not disturb"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:lcd-do-not-disturb",expire="5s,command=OFF" } 
Switch   G4DBCustomMessage        "G4DB LCD Custom Message"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:lcd-custom",expire="5s,command=OFF" } 
String   G4DBCustomMessageText    "G4DB LCD Custom Message Text"                           (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:lcd-custom-text" } 
Switch   G4DBIsRinging            "G4DB is Ringing"                                        (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:is-ringing" } 
DateTime G4DBLastRing             "G4DB Last Ring [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"         (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:last-ring" }       
Image    G4DBRingThumbnail        "G4DB Ring Thumbnail Img"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:ring-thumbnail" } 
Switch   G4DBSetCustomMessage     "G4DB Set Custom message"                                (G4DB)
Switch   G4DBSmartDetectPerson    "G4DB SmartDetect Person"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-person" }
Switch   G4DBSmartDetectVehicle   "G4DB SmartDetect Vehicle"                               (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-vehicle" }
Switch   G4DBSmartDetectMotion    "G4DB SmartDetect Motion"                                (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-motion" }
Image    G4DBSmartDetectThumbnail "G4DB SmartDetect Thumbnail Img"                         (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-thumbnail" } 
Number   G4DBSmartDetectScore     "G4DB Score [%d]"                                        (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-score" } 
String   G4DBSmartDetectType      "G4DB SmartDetect Type"                                  (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-type" }
DateTime G4DBSmartDetectLast      "G4DB Last SmartDetect [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"  (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:smart-detect-last" }
Switch   G4DBDPrivacyZone         "G4DB Privacy Zone [%s]"                                 (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:privacy-zone" }
Switch   G4DBDChime               "G4DB Chime [%s]"                                        (G4DB) { channel="unifiprotect:g4doorbell:NVRID:MACADDRESS:chime" }

//G5 Camera
Group    G5MyCam                 "G5 Cam"                                                 (gUniFiProtect)
String   G5MyCamName             "G5 Cam Name"                                            (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:name" }
String   G5MyCamType             "G5 Cam Type"                                            (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:type" }
String   G5MyCamHost             "G5 Cam Host"                                            (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:host" }
String   G5MyCamState            "G5 Cam State"                                           (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:state" }
DateTime G5MyCamUpSince          "G5 Cam Up Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"        (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:up-since" }       
DateTime G5MyCamLastSeen         "G5 Cam Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"       (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:last-seen" }       
DateTime G5MyCamConnectedSince   "G5 Cam Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:connected-since" }       
DateTime G5MyCamLastMotion       "G5 Cam Last Motion [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"     (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:last-motion" }       
Number   G5MyCamMicVolume        "G5 Cam Mic Volume [%d]"                                 (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:mic-volume" }
Switch   G5MyCamMicEnabled       "G5 Cam Mic Enabled [%s]"                                (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:is-mic-enabled" }
Switch   G5MyCamDark             "G5 Cam is Dark [%s]"                                    (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:is-dark" }
Switch   G5MyCamRecording        "G5 Cam is Recording [%s]"                               (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:is-recording" }
Switch   G5MyCamMotionDetect     "G5 Cam Motion Detected [%s]"                            (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:is-motion-detected" }
Switch   G5MyCamStatusLight      "G5 Cam Status Light [%s]"                               (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:status-light" }
Switch   G5MyCamReboot           "G5 Cam Reboot [%s]"                                     (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:reboot" }
Switch   G5MyCamHDRMode          "G5 Cam HDR Mode [%s]"                                   (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:hdr-mode" }
Switch   G5MyCamHighFPSMode      "G5 Cam High Fps Mode [%s]"                              (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:high-fps-mode" }
Number   G5MyCamIRMode           "G5 Cam IR Mode [MAP(unifiprotect_ir.map):%s]"           (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:ir-mode" }
Number   G5MyCamRecordingMode    "G5 Cam Recording mode [MAP(unifiprotect_rec.map):%s]"   (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:recording-mode" }
Switch   G5MyCamAnonSnapshot     "G5 Cam AnonSnapshot "                                   (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:a-snapshot",expire="3s,command=OFF" } 
Image    G5MyCamAnonSnapshotImg  "G5 Cam AnonSnapshot Img"                                (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:a-snapshot-img" } 
Switch   G5MyCamSnapshot         "G5 Cam Snapshot "                                       (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:snapshot",expire="3s,command=OFF" } 
Image    G5MyCamSnapshotImg      "G5 Cam Snapshot Img"                                    (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:snapshot-img" } 
Image    G5DMyMotionThumbnail    "G5 Thumbnail Img"                                       (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:motion-thumbnail" } 
Image    G5DMyMotionHeatmap      "G5 Heatmap Img"                                         (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:motion-heatmap" } 
Number   G5DMyMotionScore        "G5 Score [%d]"                                          (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:motion-score" } 
Switch   G5SmartDetectPerson     "G5 SmartDetect Person"                                  (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-person" }
Switch   G5SmartDetectVehicle    "G5 SmartDetect Vehicle"                                 (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-vehicle" }
Switch   G5SmartDetectMotion     "G5 SmartDetect Motion"                                  (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-motion" }
Image    G5SmartDetectThumbnail  "G5 SmartDetect Thumbnail Img"                           (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-thumbnail" } 
Number   G5SmartDetectScore      "G5 Score [%d]"                                          (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-score" } 
String   G5SmartDetectType       "G5 SmartDetect Type"                                    (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-type" }
DateTime G5SmartDetectLast       "G5 Last SmartDetect [%1$tY.%1$tm.%1$td %1$tH:%1$tM]"    (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:smart-detect-last" }
Switch   G5MyCamPrivacyZone      "G5 Cam Privacy Zone [%s]"                               (G5MyCam) { channel="unifiprotect:g5camera:NVRID:MACADDRESS:privacy-zone" }

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
https://github.com/seaside1/unifiprotect/releases/

## Changelog
  ### 1.1
  * Added Privazy Zone Support, toggle privacy zone from any camera on or off
  * Added chime channel for G4DB/PRO to easily enable / disable chime. Using chimeDuration configuration.
  
  ### 1.0
  * Add enable / disable of thumbnail download
    
  ### BETA12
  * Added watchdog to restart binding if no events are detected for 15 minutes
    
  ### BETA11
  * Fix build for openHAB 4.x.x

  ### BETA10
  * Support for openHAB 4.x.x
    
  ### BETA9
  
  * Basic support for G5-cameras
  * Support for G4 Doorbell Pro
  * Smoke / CO smart detections for G4/G5 cameras
  * Package smart detection for G4/G5 cameras
  * Enable/disable status sounds G4 Doorbell / Pro
  * Motion Detection / Enable & Disable
  
  ### BETA8

  * If debug is enabled, bootstrap.json will be printed to a temp-file

  ### BETA7

  * Removed warnings
  * Fixed Doorbell custom message, removed the switch to trigger the message
  * Fixed heatmap and thumbnail retrival without refresh of entire system

  ### BETA6

  * Login issue for 2.5.8 fixed

  ### BETA5

  * Support for UniFi Protect 2.1.1
  * Reworked event api actions
  * Motion detections more robust
  * Ring notification is faster and more robust
  * Removed not needed calls for refreshing Protect

  ### BETA4

  * Prepare for UniFi Protect 2.1.1
  * Fixed npe that caused websocket to close
  * Writing bootrap to tempfile if debug is on

  ### BETA3

  * New build system
  * Fixed npe warning

  ### BETA2

  * Update for OpenHab 3.2.x and fixed bug were recoring mode was renamed in UniFi Protect 1.20.0

  ### ALPHA15

  * Storage info updated again

  ### ALPHA14

  * Fixed bug for UniFi Protect version 1.19.0 beta 10 where storage info is no longer working.

  ### ALPHA13

  * Fixed motion detection channel bug

  ### ALPHA12

  * Fixed bug in heatmap dl
  * Removed some logging

  ### ALPHA11

  * Added G3 Camera
  * Added G4 Camera
  * Added G4 Doorbell
  * Added Smart Detection Events
  * Added G4 Doorbell features
  * Rework event detction (motion, smartevents, ringing)

  ### ALPHA10

  * Removed info logging, changed to debug
  * Eventlistener will now be retarted in case the NVR is rebooted
  * Anon snapshots will be refreshed right away

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
