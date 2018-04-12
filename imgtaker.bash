#!/bin/bash
#uses webcrawler to download the first image result for the word given as an arg

part1='http://www.webcrawler.com/serp?qc=images&q=';

website=$part1$1;

echo $website;
wget -O url.txt $website;

cat url.txt | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*.jpg" | head -n 10 > link.txt;

for value in {1..10}
do
	head -n $value link.txt | tail -n 1 > onelink.txt
	echo $url
	wget -i onelink.txt  -O $value.jpg;
done
rm url.txt;
rm link.txt;
rm onelink.txt;
