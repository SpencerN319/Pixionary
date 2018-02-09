#!/bin/bash
wget -O url.txt "https://api.duckduckgo.com/?q=moon&format=json&pretty=1";
cat source.html | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*.jpg" | head -n 1;

#right now this gets an isolated image url
