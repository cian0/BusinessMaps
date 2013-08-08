BusinessMaps
============

XML Parser With Maps Functionality

*Contents*
- Google services library (BusinessMaps/google-play-services_lib)
- Project source code (BusinessMaps/MapsSample)

Program Flow:
- Instantiate map
- Check for connection
- Fetch XML from URL
- Parse XML and store to Business Object Array List
- Parse Array List and create markers for each. 
- Store Marker - Business mapping to a hashmap.
- Add listener when an info window is clicked.
	- When a window is clicked a dialog showing details of location is presented.
	- clicking on Phone number invokes the phone call function
- Plot each markers to the map.

Notes:
* It is assumed that one business only has one location. (A business tag with more than 1 location children tag will not work)
* Error handling on faulty XML format are not yet implemented 
* XML contents are not stored to the database
