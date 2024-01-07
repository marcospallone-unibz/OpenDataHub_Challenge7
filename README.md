# OpenDataHub_Challenge7

Challenge 7 of OpenDataHub Challenges works on 2 different APIs, fetching entire objects, checking and removing duplicates on them
adding useful information in a single list where we can find all.

This is the config.json, so the configuration file on which data manipulation is based

```JSON
{
  "apiClients": [
    {
      "name": "FirstAPI",
      "type": "OpenDataHubApiDatabase1",
      "url": "https://tourism.opendatahub.com/v1/Accommodation?pagenumber=1&roominfo=1-18%2C18&bokfilter=hgv&msssource=sinfo&availabilitychecklanguage=en&detail=0&removenullvalues=false",
      "keysWhereFindDuplicates": [
        {
          "keyPath": "Items>AccoDetail>de>Zip"
        }
      ]
    },
    {
      "name": "SecondAPI",
      "type": "OpenDataHubApiDatabase2",
      "url": "https://tourism.opendatahub.com/v1/Event?pagenumber=1&removenullvalues=false",
      "keysWhereFindDuplicates": [
        {
          "keyPath": "Items>ContactInfos>de>ZipCode"
        }
      ]
    }
  ],
  "replacementKeys":[
    {
      "value": "CAP"
    }
  ]
}
```

First we read the config.json from ConfigurationReader class that returns all useful information from configuration file. 

Then we fetch object from APIs. So, setting URLs in each apiClient in the config.json, the FirstAPI and SecondAPI classes will fetch object from them.
The two classes that fetch objects from APIs have the same interface plugged in, OpenDataHubApiClient.

The configuration reader and 2 classes are called from DataManager. In this class we store fetched objects, and information from
config.json, so we can work on data. This class have functions to check the existence of duplicates in specified path that we read from config.
If in these path in objects from API 1 and API 2 some duplicates are founded, function to remove duplicates will be activated.

With goIntoAnnidate and goIntoJSONToRemoveDuplicates functions, OpenDataHub_Challenge7 goes into JSON in the specified path and remove the duplicate entry.
This value, with a specified key (replacementKeys in config.json) is inserted in the object that will be returned from this class.

Also number of duplicates and objects in the original data fetched from APIs where duplicates are found, will be inserted in the object
that will be returned from this class, so in the result list at the end we have a complete vision of the process.
If no duplicates are found there is also an adding of values that explain that no duplicates are found, so user have a clear vision of what's happened.

Finally, the result list is returned to Main component that work on this list with a precise goal. In fact, after that this component has called the checkDuplicates
function for each pair of keys where find duplicates, add the result list to its main list to create the final list that will be returned to the user.

# How to use

Set the wanted URLs of two different APIs in the config.json in the url field of each apiClient. Then set path into JSON where you want to find 
duplicates and the replacement key where you want to insert the duplicate value found. Then you have to run the program and the code will give as output a list
with all the useful information!

# Test

To test the program you can try same API class (es. Tourism or Mobility), so you are sure that duplicates will be found with appropriate paths where find duplicates.



