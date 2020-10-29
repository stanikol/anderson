#!/bin/bash
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
  "id": "SGu1Jpq1IO",
  "site": {
	"id": "0006a522ce0f4bbbbaa6b3c38cafaa0f",
	"domain": "fake.tld"
  },
  "device": {
	"id": "440579f4b408831516ebd02f6e1c31b4",
	"geo": {
  	"country": "LT"
	}
  },
  "imp": [
	{
  	"id": "1",
  	"wmin": 50,
  	"wmax": 300,
  	"hmin": 100,
  	"hmax": 300,
  	"h": 250,
  	"w": 300,
  	"bidfloor": 3.12123
	}
  ],
  "user": {
	"geo": {
  	"country": "LT"
	},
	"id": "USARIO1"
  }
}
' \
	'http://127.0.0.1:8080/bid/'
