#!/bin/bash
#uses webcrawler to download the first 12  image results for the word given as an arg

part1='http://www.webcrawler.com/serp?qc=images&q=';

website=$part1$1;

echo $website;
wget -O url.txt $website;

cat url.txt | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*.jpg" | head -n 12 > link.txt;

for value in {1..12}
do
	head -n $value link.txt | tail -n 1 > onelink.txt
	echo $url
	wget -i onelink.txt -O $value.jpg
	cat $value.jpg | base64 >$value.txt

done
rm url.txt;
rm link.txt;
rm onelink.txt;