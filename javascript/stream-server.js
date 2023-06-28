
if( process.argv.length < 3 ) {
	console.log(
		'Usage: \n' +
Opentracker
Best Online IP Tracker
154.36.128.0
 Click to search!
Summary of IP's Profile DetailsCopy
IP address: 154.36.128.0
City: Saratoga
Region name: California
Country name: United States
Life Expectency: 77.1
Avg income: 30,575 USD
Timezone: America/Los_Angeles
Sub continent: North America
Country code: US
Geo-targeting: true
   ◉ Latitude: 37.751
   ◎ Longitude: -97.822
World currency: USD
EU member: false
org: Hong Kong Megalayer Technology Co.,Limited
isp: Hong Kong Megalayer Technology Co.,Limited
Connection: Cable/DSL
Continent: North America
Population: 278,357,000
IP range tracked: 154.36.0.0 - 154.42.71.255
Surface area: 9,363,520 km sq.
GNP: 8,510,700 mln.
Demographic data: true
Ad (re)targeting: true



The Ultimate IP Address Tracker.
Identify users, collect online details, get IP numbers. View, download and process enriched ip tracker data.

Do you need an IP tracker, to collect user profiles to your website and online assets, based on IP addresses? Need to find out what a visitor from a specific IP address did? Need to track whats working well for your business and what needs to be improved?

This is possible, using our IP tracking technology.

Our reporting system keeps track of every visitor, prospect or customer who visits your online assets, based on IP addresses. Get access to a complete record of activity-per-visit. Improve your business by tracking. All winners are trackers.

What Is An IP Address?
An IP address (Internet Protocol address) is a unique numerical label assigned to a device. It provides the location of the device in a network and a route on how to get there. The internet uses an IP address to send IP packets from a source to a destination. It is a building block that lets the internet function.

Can an IP address identify me?
No, an ip address does not reveal personal information (like a name, social security number or physical address). Millions of devices, like modems and routers keep logs of ip addresses.  Your modem at home, or the 4G antennae you connect to with your phone are logging your ip  addresses. Logs are necessary to maintain the internet. Logs with IP addresses are everywhere!
 
A device keeping track of an ip address.

 A device keeping track of an ip address.

Can I track someones IP-address ?
No, you can’t just track an Ip-address. You first need to have received one. Compare it to receiving a letter. If you receive the letter, then you can figure out where the letter came from by looking at the return address. If you don’t have the letter, then you also won’t have a return address. By the same token, if a letter does not have a destination address, you will not get a letter, and there is nothing to track it back to.

In internet terms this means you need a source address, a destination address and traffic (an email or a browser action) between the two. Normally an online business has a site or app as the destination and someone surfing the web is the source. If you are a business that has a site or an app and you are receiving internet traffic to the site or app the you will be able to see the ip-addresses coming to your site or app. Other places you can also see ip addresses are in the headers of the emails you receive or the log files of routers.

How does an IP tracker work?
Use the IP tracker with an IP address to identify and collect online details based on the IP number. Advanced technology combined with cookies allows you to identify visitors. Enrich, view, download and process IP tracker data with Opentracker.

Opentracker records each unique user and their IP address. Our IP tracer maps where an IP address (and the visitor behind it) originates from, and enriches this data with different sources.

Do you need to find a specific user or visitor?
Your business can locate any user or unique visitor who has been on your website by IP address.

Go back through your historical data to see entire visit or session history of any IP address. 

Can I tag IP addresses?
Yes, Opentracker allows businesses to automatically or manually tag any ip address for future reference, or processing to other destinations.

Can I investigating click-fraud?
Yes, detect Click-fraud and provide proof where needed.

Can I following up on a leads?
Yes, search visitors & clickstreams by IP address – make a record, enter into SalesForce, or any other CRM. Know what your (potential) clients are thinking, and what they are interested in. See how often a potential leads or clients returns, along with their entire history of clicks, downloads, events, and activity.

Profit from invaluable strategic insights. Improve your funnels.
Measure your prospects and customers across complex funnels. Find bottlenecks and fix them. Increase conversions and create scalable ROI.
 
 
Identify multiple customers behind a single IP address
Our first-party cookie tracking technology allows us to identify multiple customers in the same company or organization located behind the same IP address / firewall. See when your product or service offerings are passed on for consideration or discussion within an organization. 

Details about an IP address include:
Referrer, Exit, search term
Browser name & Version
Platform & Devices
Country, Region, City
GPS Longitude & Latitude
Timezone & Language
ISP, Provider or Carrier
Company & Organization
Area, Postal or ZIP code
IP address & Connection type
Display size & Orientation
ABOUT US
Opentracker has more than 10 years experience in tracking, data analytics and statistics innovation. Our hallmark is simple, intuitive, and easy-to-read reporting interfaces, combined with powerful and flexible APIs.
QUICK LINKS
Products video
Features
Pricing
Resources
Articles
Company
Login
Contact
Terms
Privacy
OT4 login
RESOURCES
Getting started
How does user-tracking work?
What is a website tracking system?
Technical Requirements Analytics
Downloadable brochures & leaflets
CONTACT
support@opentracker.net
Opentracker
Torenallee 45 - 7.17
5617 BA Eindhoven
The Netherlands
Copyright © 2023 Opentracker. All rights reserved.



		'node stream-server.js <secret> [<stream-port> <websocket-port>]'
	);
	process.exit();
}

var STREAM_SECRET = process.argv[2],
	STREAM_PORT = process.argv[3] || 8082,
	WEBSOCKET_PORT = process.argv[4] || 8084,
	STREAM_MAGIC_BYTES = 'jsmp'; // Must be 4 bytes

var width = 320,
	height = 240;

// Websocket Server
var socketServer = new (require('ws').Server)({port: WEBSOCKET_PORT});
socketServer.on('connection', function(socket) {
	// Send magic bytes and video size to the newly connected socket
	// struct { char magic[4]; unsigned short width, height;}
	var streamHeader = new Buffer(8);
	streamHeader.write(STREAM_MAGIC_BYTES);
	streamHeader.writeUInt16BE(width, 4);
	streamHeader.writeUInt16BE(height, 6);
	socket.send(streamHeader, {binary:true});

	console.log( 'New WebSocket Connection ('+socketServer.clients.length+' total)' );
	
	socket.on('close', function(code, message){
		console.log( 'Disconnected WebSocket ('+socketServer.clients.length+' total)' );
	});
});

socketServer.broadcast = function(data, opts) {
	for( var i in this.clients ) {
		this.clients[i].send(data, opts);
	}
};


// HTTP Server to accept incomming MPEG Stream
var streamServer = require('http').createServer( function(request, response) {
	var params = request.url.substr(1).split('/');
	width = (params[1] || 320)|0;
	height = (params[2] || 240)|0;

	if( params[0] == STREAM_SECRET ) {
		console.log(
			'Stream Connected: ' + request.socket.remoteAddress + 
			':' + request.socket.remotePort + ' size: ' + width + 'x' + height
		);
		request.on('data', function(data){
			socketServer.broadcast(data, {binary:true});
		});
	}
	else {
		console.log(
			'Failed Stream Connection: '+ request.socket.remoteAddress + 
			request.socket.remotePort + ' - wrong secret.'
		);
		response.end();
	}
}).listen(STREAM_PORT);

console.log('Listening for MPEG Stream on http://127.0.0.1:'+STREAM_PORT+'/<secret>/<width>/<height>');
console.log('Awaiting WebSocket connections on ws://127.0.0.1:'+WEBSOCKET_PORT+'/');
