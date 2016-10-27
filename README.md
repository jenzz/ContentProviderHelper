Content Provider Helper
=======================

... is a frontend to discover/view/query data of android content providers.

[Original](https://github.com/jenzz/ContentProviderHelper) : Copyright (c) 2014-2016 Jens Driller under [MIT license](LICENSE-old-mit-jenzz)
[Improvements](https://github.com/k3b/ContentProviderHelper) : Copyright (c) 2015-2016 k3b under [GNU GENERAL PUBLIC LICENSE Version 3 or later](LICENSE)

Description
-----------

Android content-providers are similar to database-tables. 
They have a name (aka "content:" uri), 
columns and they can be queried similar to Sql.

Android uses 
[https://developer.android.com/guide/topics/providers/content-providers.html content-providers]
as api to manage global accessable data (i.e. Images, Telephone-book, SMS, ...).

ContentProviderHelper is a frontend to query installed content-providers.

You can add and delete your own content URIs manually or search for all available content providers on the device.

App permissions are set generously to provide maximum compatibility.

Requirements: Android-2.2 (api-8) or newer. (tested with android-2.2, 4.2, 4.4)

Program enhancements as merge-request are welcome.

The following content providers are provided by default:

* content://browser/bookmarks
* content://browser/searches
* content://call_log/calls
* content://com.android.calendar/attendees
* content://com.android.calendar/calendar_alerts
* content://com.android.calendar/calendars
* content://com.android.calendar/event_entities
* content://com.android.calendar/events
* content://com.android.calendar/reminders
* content://com.android.contacts/aggregation_exceptions
* content://com.android.contacts/contacts
* content://com.android.contacts/data
* content://com.android.contacts/groups
* content://com.android.contacts/raw_contact_entities
* content://com.android.contacts/raw_contacts
* content://com.android.contacts/settings
* content://com.android.contacts/status_updates
* content://com.android.contacts/syncstate
* content://drm/audio
* content://drm/images
* content://icc/adn
* content://icc/fdn
* content://icc/sdn
* content://media/external/audio/albums
* content://media/external/audio/artists
* content://media/external/audio/genres
* content://media/external/audio/media
* content://media/external/audio/playlists
* content://media/external/images/media
* content://media/external/images/thumbnails
* content://media/external/video/media
* content://media/external/video/thumbnails
* content://media/internal/audio/albums
* content://media/internal/audio/artists
* content://media/internal/audio/genres
* content://media/internal/audio/media
* content://media/internal/audio/playlists
* content://media/internal/images/media
* content://media/internal/images/thumbnails
* content://media/internal/video/media
* content://media/internal/video/thumbnails
* content://mms
* content://mms/inbox
* content://mms/outbox
* content://mms/part
* content://mms/sent
* content://mms-sms/conversations
* content://mms-sms/draft
* content://mms-sms/locked
* content://mms-sms/search
* content://settings/secure
* content://settings/system
* content://sms/conversations
* content://sms/draft
* content://sms/inbox
* content://sms/outbox
* content://sms/sent
* content://telephony/carriers
* content://user_dictionary/words

What's New
----------

### v1.3.0: (to be released in fdroid appstore, soon)

* improvements: restore last used contentprovider+Fields in "on app load"
* updated to gradle-2.14.1/build tools 24.0.2/java8/AndroidStudio-2.2
* changed appID from com.jensdriller.contentproviderhelper to de.k3b.android.contentproviderhelper
* changed original App License from [MIT](https://raw.githubusercontent.com/jenzz/ContentProviderHelper/master/LICENSE) to [GPL-v3+](https://raw.githubusercontent.com/k3b/ContentProviderHelper/master/LICENSE) to allow adding gpl-v3 code.

### v1.2.0: (20150529 not published due to unknown license)

* forked from [jenzz/ContentProviderHelper](https://github.com/jenzz/ContentProviderHelper)
* refactored from eclipse-build to android-studio/gradle build.
* Replaced AppSherloc with AppCompat.
* Removed permission "internet" togehter with capability to automatcally send error reports to the program author
* App now builds for Android-2.2 (api-8) and up

### v1.1.0:

- Choose your theme: Light / Dark / Light (Dark ActionBar)

- Fixed OutOfMemory errors on lower spec devices

- Query content providers with more complex SQL queries

- Save & Share query results as HTML page

### v1.0.0:

- Initial release

License
-------
This project is licensed under the [MIT License](https://raw.githubusercontent.com/jenzz/ContentProviderHelper/master/LICENSE).
