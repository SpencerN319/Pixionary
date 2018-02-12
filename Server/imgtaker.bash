#!/bin/bash
#uses duckduckgo api to download the first image result for the word given as an arg

part1='https://api.duckduckgo.com/?q=';
part2='&format=json&pretty=1';
website=$part1$1$part2;

echo $website;
wget -O url.txt $website;

cat url.txt | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*.jpg" | head -n 1 > link.txt;

wget -i link.txt -O image.jpg;

rm link.txt;
rm url.txt;

