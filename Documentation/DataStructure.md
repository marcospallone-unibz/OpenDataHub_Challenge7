# API Structure

## Mobility API

### Endpoint

*URL*: [Mobility API](https://mobility.api.opendatahub.com/v2/flat%2Cnode/%2A?limit=1&offset=0&shownull=false&distinct=true)

### Data Structure

```json
{
  "offset": 0,
  "data": [
    {
      "pactive": false,
      "pavailable": true,
      "pcode": "00001",
      "pcoordinate": {
        "x": 13.595222,
        "y": 47.948,
        "srid": 4326
      },
      "pmetadata": {
        "city": "--",
        "state": "ACTIVE",
        "address": "Hauptstraße",
        "capacity": 2,
        "provider": "AP_GEN"
      },
      "pname": "Chargingstation 00001_1",
      "porigin": "ALPERIA",
      "ptype": "EChargingStation",
      "sactive": false,
      "savailable": true,
      "scode": "00001-0",
      "scoordinate": {
        "x": 13.595222,
        "y": 47.948,
        "srid": 4326
      },
      "smetadata": {
        "outlets": [
          {
            "id": "0",
            "maxPower": 22,
            "maxCurrent": 31,
            "minCurrent": 0,
            "hasFixedCable": false,
            "outletTypeCode": "Type2Mennekes"
          }
        ]
      },
      "sname": "Chargingstation 00001_1-1",
      "sorigin": "ALPERIA",
      "stype": "EChargingPlug"
    }
  ],
  "limit": 1
}
```

## Tourism API

### Endpoint

*URL*: [TourismApi](https://tourism.opendatahub.com/v1/ODHActivityPoi?pagenumber=1&pagesize=1&type=255&removenullvalues=false)

### Data Structure

```json
{
  "TotalResults": 22597,
  "TotalPages": 22597,
  "CurrentPage": 1,
  "NextPage": "https://tourism.opendatahub.com/v1/ODHActivityPoi?pagenumber=2&pagesize=1&type=255&removenullvalues=false",
  "Items": [
    {
      "Id": "smgpoi107",
      "Type": "Winter",
      "AgeTo": 0,
      "SmgId": "107",
      "_Meta": {
        "Id": "smgpoi107",
        "Type": "odhactivitypoi",
        "Source": "idm",
        "LastUpdate": "2023-05-24T13:16:16.0865113+00:00",
        "UpdateInfo": {
          "UpdatedBy": "lts.push.import",
          "UpdateSource": "odh.importer"
        }
      },
      "Active": true,
      "Detail": {
        "de": {
          // ... (additional data)},
          "en": {
            // ... (additional data)},
            "it": {
              // ... (additional data)}
            },
            "IsOpen": false,
            "GpsInfo": [
              {
                "Gpstype": "position",
                "Altitude": 1413.0,
                "Latitude": 46.5575176279939,
                "Longitude": 11.7262208461761,
                "AltitudeUnitofMeasure": "m"
              }
            ]
            // ... (additional data)
          }
          ],
          // ... (additional metadata)
        }
```

## Example of Usage

Suppose we want to merge the two results based on coordinates. The configuration file (config.json) should be configured
as follows:

```json
{
  "apiClients": [
    {
      "name": "FirstAPI",
      "type": "OpenDataHubApiDatabase1",
      "url": "https://mobility.api.opendatahub.com/v2/flat%2Cnode/%2A?limit=200&offset=0&shownull=false&distinct=true",
      "mapping": [
        {
          "key": "Latitude",
          "newKeyName": "newLatitude",
          "path": "data>GpsPoints>position"
        },
        {
          "key": "Longitude",
          "newKeyName": "newLongitude",
          "path": "data>GpsPoints>position"
        }
      ]
    },
    {
      "name": "SecondAPI",
      "type": "OpenDataHubApiDatabase2",
      "url": "https://tourism.opendatahub.com/v1/ODHActivityPoi?pagenumber=1&pagesize=1&type=255&removenullvalues=false",
      "mapping": [
        {
          "key": "Latitude",
          "newKeyName": "newLatitude",
          "path": "GpsInfo"
        },
        {
          "key": "Longitude",
          "newKeyName": "newLongitude",
          "path": "GpsInfo"
        }
      ]
    }
  ]
}
```

This is a basic example, and the actual configuration may need to be adjusted based on the actual structure of the API
responses and the desired merging logic. The mapping section is used to specify how to extract the latitude and
longitude from the API responses.

# Output:

Within both datasets, the values corresponding to the "key" in the specified path in config.json will be compared. For
each corresponding pair, an object will be created:

```json
{
  "items": [
    {
      "newLatitude": 14.12345,
      "newLongitude": 46.12345,
      "mobility": {
        /* Previous object matching coords without x & y */
      },
      "tourism": {
        /* Previous object matching coords without latitude & longitude */
      }
    }
  ]
}
```

Note: This is a simplified and fictionalized example based on the provided data structure.
